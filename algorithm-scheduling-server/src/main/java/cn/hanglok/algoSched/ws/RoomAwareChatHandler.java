package cn.hanglok.algoSched.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

/**
 * @author Allen
 * @version 1.0
 * @className RoomAwareChatHandler
 * @description TODO
 * @date 2024/4/12
 */
@Slf4j
public class RoomAwareChatHandler extends TextWebSocketHandler {

    private final RoomService roomService = new RoomService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String username = (String) session.getAttributes().get("username");
        String roomName = (String) session.getAttributes().get("roomName");
        roomService.join(roomName, username, session);
        broadcast(new AlgoMessage() {{
            setMessageId(String.valueOf(UUID.randomUUID()));
            setRoom(roomName);
            setFrom(username);
            setTo("");
            setContent(username + " has joined the room.");
            setTimestamp(System.currentTimeMillis());
        }});
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String username = (String) session.getAttributes().get("username");
        String roomName = (String) session.getAttributes().get("roomName");
        roomService.leave(roomName, username);
        broadcast(new AlgoMessage() {{
            setMessageId(String.valueOf(UUID.randomUUID()));
            setRoom(roomName);
            setFrom(username);
            setTo("");
            setContent(username + " has left the room.");
            setTimestamp(System.currentTimeMillis());
        }});
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        log.debug("session: {}, message: {}", session, message.getPayload());
        String payload = message.getPayload();
        try {
            AlgoMessage data = objectMapper.readValue(payload, AlgoMessage.class);
            roomService.sendMessage(data);
        } catch (Exception ignored) {
        }
    }

    private void broadcast(AlgoMessage message) {
        roomService.sendMessage(message);
    }
}
