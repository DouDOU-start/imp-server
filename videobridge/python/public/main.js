// 连接到信令服务器 (服务器环境真实 ip地址)
const signalingServerUrl = 'wss://192.168.0.168:3000/wss'
signalingSocket = new WebSocket(signalingServerUrl)

signalingSocket.onopen = async () => {
    start()
}

signalingSocket.onmessage = async (message) => {
    const data = JSON.parse(message.data)

    switch (data.type) {
        case 'answer':
            pc.setRemoteDescription(data);
            break
        default:
            break
    }
};

var pc = null;

async function negotiate() {
    pc.addTransceiver('video', { direction: 'recvonly' });
    pc.addTransceiver('audio', { direction: 'recvonly' });
    offer = await pc.createOffer()
    await pc.setLocalDescription(offer)
    await signalingSocket.send(JSON.stringify({
        sdp: offer.sdp,
        type: offer.type,
    }))
    new Promise((resolve) => {
        if (pc.iceGatheringState === 'complete') {
            resolve();
        } else {
            const checkState = () => {
                if (pc.iceGatheringState === 'complete') {
                    pc.removeEventListener('icegatheringstatechange', checkState);
                    resolve();
                }
            };
            pc.addEventListener('icegatheringstatechange', checkState);
        }
    });
}

function start() {
    var config = {
        sdpSemantics: 'unified-plan'
    };

    config.iceServers = [
        {
            urls: 'turn:10.8.6.34:3478',
            username: 'hanglok',
            credential: 'hanglok'
        }
    ];

    pc = new RTCPeerConnection(config);

    // connect audio / video
    pc.addEventListener('track', (evt) => {
        if (evt.track.kind == 'video') {
            document.getElementById('video').srcObject = evt.streams[0];
        }
    });

    negotiate();

}

function stop() {
    document.getElementById('stop').style.display = 'none';

    // close peer connection
    setTimeout(() => {
        pc.close();
    }, 500);

    document.getElementById('start').style.display = 'inline-block';
}
