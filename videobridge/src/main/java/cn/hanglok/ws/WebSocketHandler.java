package cn.hanglok.ws;

import cn.hanglok.component.Peer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.onvoid.webrtc.RTCSessionDescription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

/**
 * @author Allen
 * @version 1.0
 * @className WebSocketHandler
 * @description TODO
 * @date 2024/5/17
 */
@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    Peer peer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        peer.setSession(session);
        RTCSessionDescription offer = peer.getOffer();
        log.debug("offer: {}", offer);
        session.sendMessage(new TextMessage(new JSONObject() {{
            put("type", "offer");
            put("sdp", offer.sdp);
        }}.toString()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map map = objectMapper.readValue(payload, Map.class);
        switch (map.get("type").toString()) {
            case "answer":
                peer.receiveAnswer(map.get("sdp").toString());
                break;
            case "candidate":
                peer.addIceCandidate(JSONObject.parseObject(JSON.toJSONString(map.get("candidate"))));
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("Disconnected: " + session.getId());
    }
}
