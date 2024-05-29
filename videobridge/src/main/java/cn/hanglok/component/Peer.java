package cn.hanglok.component;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onvoid.webrtc.*;
import dev.onvoid.webrtc.media.MediaStream;
import dev.onvoid.webrtc.media.video.VideoDesktopSource;
import dev.onvoid.webrtc.media.video.VideoTrack;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen
 * @version 1.0
 * @className Peer
 * @description TODO
 * @date 2024/5/23
 */
@Component
@Slf4j
@Data
public class Peer {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PeerConnectionFactory factory = new PeerConnectionFactory();
    private final RTCConfiguration config = new RTCConfiguration() {{
        iceServers = new ArrayList<>() {{
            add(new RTCIceServer() {{
                urls = List.of("stun:10.8.6.34:3478");
                username = "hanglok";
                password = "hanglok";
            }});
        }};
    }};

    private WebSocketSession session;

    private List<RTCIceCandidate> candidateList = new ArrayList<>();

    private final PeerConnectionObserver observer = new PeerConnectionObserver() {
        @Override
        public void onSignalingChange(RTCSignalingState state) {
            log.info("Signaling Change: {}", state);
        }

        @Override
        public void onIceConnectionChange(RTCIceConnectionState state) {
            log.info("ICE Connection State Change: {}", state);
        }

        @Override
        public void onAddStream(MediaStream stream) {
            log.info("Stream added: {}", stream);
            // 在此处处理接收到的视频流，例如在界面上显示
        }

        @Override
        public void onTrack(RTCRtpTransceiver transceiver) {
            PeerConnectionObserver.super.onTrack(transceiver);
            log.error("on track");
        }

        @Override
        public void onIceCandidate(RTCIceCandidate candidate) {
            log.info("Local ICE Candidate: {}", candidate);
            candidateList.add(candidate);
        }

        @Override
        public void onAddTrack(RTCRtpReceiver receiver, MediaStream[] mediaStreams) {
            log.info("Track added: {}", receiver.getTrack());
        }

        @Override
        public void onDataChannel(RTCDataChannel dataChannel) {
            PeerConnectionObserver.super.onDataChannel(dataChannel);
            dataChannel.registerObserver(new RTCDataChannelObserver() {
                @Override
                public void onBufferedAmountChange(long previousAmount) {

                }

                @Override
                public void onStateChange() {

                }

                @Override
                public void onMessage(RTCDataChannelBuffer buffer) {
                }
            });
            log.info("On Data Channel");
            try {
                sendStringData("test");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

    public void sendStringData(String message) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
        RTCDataChannelBuffer dataBuffer = new RTCDataChannelBuffer(buffer, false);
        dataChannel.send(dataBuffer);
        log.info("Sent: {}", message);
    }

    private final RTCPeerConnection peerConnection = factory.createPeerConnection(config, observer);

    private RTCDataChannel dataChannel = peerConnection.createDataChannel("dataChannel", new RTCDataChannelInit());

    private RTCSessionDescription offer;

    public void createOffer() {

        RTCOfferOptions opt = new RTCOfferOptions();
        peerConnection.createOffer(opt, new CreateSessionDescriptionObserver() {
            @Override
            public void onSuccess(RTCSessionDescription description) {
                log.info("Success created offer");
                offer = description;
                peerConnection.setLocalDescription(description, new SetSessionDescriptionObserver() {
                    @Override
                    public void onSuccess() {
                        log.info("Success set local sdp");
                    }

                    @Override
                    public void onFailure(String error) {
                        log.error("Set local sdp error: {}", error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                log.error("Error created offer: {}", error);
            }
        });
    }

    public void createVideo() {
        VideoDesktopSource source = new VideoDesktopSource();
        VideoTrack videoTrack = factory.createVideoTrack("localvideotrack", source);
        source.start();
        peerConnection.addTrack(videoTrack, List.of("stream_id"));
    }

    public void channelRegisterObserver() {
        dataChannel.registerObserver(new RTCDataChannelObserver() {
            @Override
            public void onBufferedAmountChange(long previousAmount) {
                log.info("buffered amount change: {}", previousAmount);
            }

            @Override
            public void onStateChange() {
                log.info("state change");
            }

            @Override
            public void onMessage(RTCDataChannelBuffer buffer) {
                log.info("on message: {}", buffer);
            }
        });
    }

    public void receiveAnswer(String sdp) throws IOException {
        RTCSessionDescription description = new RTCSessionDescription(RTCSdpType.ANSWER, sdp);
        peerConnection.setRemoteDescription(description, new SetSessionDescriptionObserver() {
            @Override
            public void onSuccess() {
                log.info("Success set remote sdp");
            }

            @Override
            public void onFailure(String error) {
                log.error("Set remote sdp error: {}", error);
            }
        });
        for (RTCIceCandidate candidate : candidateList) {
            sendIceCandidate(candidate);
        }
    }

    public void addIceCandidate(JSONObject candidate) {
        RTCIceCandidate iceCandidate = new RTCIceCandidate(
                String.valueOf(candidate.get("sdpMid")),
                Integer.parseInt(candidate.get("sdpMLineIndex").toString()),
                String.valueOf(candidate.get("sdp"))
        );
        log.info("Add ice candidate: {}", iceCandidate);
        peerConnection.addIceCandidate(iceCandidate);
    }

    public void sendIceCandidate(RTCIceCandidate candidate) throws IOException {
        session.sendMessage(new TextMessage(new JSONObject() {{
            put("type", "ICE-CANDIDATE");
            put("candidate", candidate);
        }}.toString()));
    }

    public Peer() {
        createOffer();
        createVideo();
        channelRegisterObserver();
    }
}
