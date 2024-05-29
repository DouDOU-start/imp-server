import asyncio
import json
import logging
import os
import platform
import ssl

import websockets
from aiohttp import web
from aiortc import RTCPeerConnection, RTCSessionDescription
from aiortc.contrib.media import MediaPlayer, MediaRelay
from aiortc.rtcrtpsender import RTCRtpSender

ROOT = os.path.dirname(__file__)

relay = None
webcam = None


def create_local_tracks(play_from, decode):
    global relay, webcam

    if play_from:
        player = MediaPlayer(play_from, decode=decode)
        return player.audio, player.video
    else:
        options = {"framerate": "30", "video_size": "1920x1080"}
        if relay is None:
            if platform.system() == "Darwin":
                webcam = MediaPlayer(
                    "default:none", format="avfoundation", options=options
                )
            elif platform.system() == "Windows":
                webcam = MediaPlayer(
                    "video=Integrated Camera", format="dshow", options=options
                )
            else:
                webcam = MediaPlayer("/dev/video0", format="v4l2", options=options)
            relay = MediaRelay()
        return None, relay.subscribe(webcam.video)


def force_codec(pc, sender, forced_codec):
    kind = forced_codec.split("/")[0]
    codecs = RTCRtpSender.getCapabilities(kind).codecs
    transceiver = next(t for t in pc.getTransceivers() if t.sender == sender)
    transceiver.setCodecPreferences(
        [codec for codec in codecs if codec.mimeType == forced_codec]
    )


async def index(request):
    content = open(os.path.join(ROOT, "index.html"), "r").read()
    return web.Response(content_type="text/html", text=content)


async def javascript(request):
    content = open(os.path.join(ROOT, "client.js"), "r").read()
    return web.Response(content_type="application/javascript", text=content)


async def revived_offer(sdp):
    offer = RTCSessionDescription(sdp=sdp, type="offer")

    pc = RTCPeerConnection()
    pcs.add(pc)

    @pc.on("connectionstatechange")
    async def on_connectionstatechange():
        logging.info("Connection state is %s" % pc.connectionState)
        if pc.connectionState == "failed":
            await pc.close()
            pcs.discard(pc)

    # open media source
    audio, video = create_local_tracks(
        False, decode=not True
    )

    if video:
        video_sender = pc.addTrack(video)
        force_codec(pc, video_sender, 'video/H264')

    await pc.setRemoteDescription(offer)

    answer = await pc.createAnswer()
    await pc.setLocalDescription(answer)

    return json.dumps({"sdp": pc.localDescription.sdp, "type": pc.localDescription.type})


pcs = set()


async def on_shutdown(app):
    # close peer connections
    coros = [pc.close() for pc in pcs]
    await asyncio.gather(*coros)
    pcs.clear()


# 保存当前连接的客户端
connected_clients = set()


async def process_message(message, websocket):
    if message['type'] == 'offer':
        logging.info(f"Received offer: {message['sdp']}")
        answer = await revived_offer(message['sdp'])
        await websocket.send(answer)


async def handler(websocket, path):
    # 将新连接的客户端添加到集合中
    connected_clients.add(websocket)
    try:
        async for message in websocket:
            data = json.loads(message)
            await process_message(data, websocket)
    except websockets.exceptions.ConnectionClosed:
        logging.info(f"Client disconnected: {websocket.remote_address}")
    finally:
        connected_clients.remove(websocket)


class SignalingServer:
    def __init__(self, host='0.0.0.0', port=8765):
        self.host = host
        self.port = port
        self.connected_clients = {}
        self.forwarding_client = None
        self.start()

    def start(self):
        asyncio.set_event_loop(asyncio.new_event_loop())  # 创建新的事件循环
        loop = asyncio.get_event_loop()
        ssl_context = ssl.SSLContext(ssl.PROTOCOL_TLS_SERVER)
        ssl_context.load_cert_chain(certfile="cert/cert.pem", keyfile="cert/key.pem")
        server = websockets.serve(handler, self.host, self.port, ssl=ssl_context)
        print(f"Signaling server started on {self.host}:{self.port}")
        loop.run_until_complete(server)
        loop.run_forever()


if __name__ == "__main__":
    verbose = False

    if verbose:
        logging.basicConfig(level=logging.DEBUG)
    else:
        logging.basicConfig(level=logging.INFO)

    SignalingServer()
