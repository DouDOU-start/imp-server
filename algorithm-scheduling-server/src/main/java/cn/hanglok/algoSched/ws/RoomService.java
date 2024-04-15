package cn.hanglok.algoSched.ws;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Allen
 * @version 1.0
 * @className RoomService
 * @description TODO
 * @date 2024/4/12
 */
public class RoomService {

    private final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, String> sessionRoomMap = new ConcurrentHashMap<>();

    public void join(String roomName, String username, WebSocketSession session) {
        rooms.computeIfAbsent(roomName, k -> new ConcurrentHashMap<>()).put(username, session);
        sessionRoomMap.put(session, roomName);
    }

    public void leave(String roomName, String username) {
        Map<String, WebSocketSession> room = rooms.get(roomName);
        if (room != null) {
            WebSocketSession session = room.remove(username);
            if (session != null) {
                sessionRoomMap.remove(session);
            }
            if (room.isEmpty()) {
                rooms.remove(roomName);
            }
        }
    }

    public Map<String, WebSocketSession> getRoom(String roomName) {
        return rooms.get(roomName);
    }

    /**
     * 根据成员会话查询roomMap
     * @param session 成员会话
     * @return roomMap
     */
    public Map<String, WebSocketSession> getRoomBySession(WebSocketSession session) {
        String roomName = sessionRoomMap.get(session);
        return roomName == null ? null : rooms.get(roomName);
    }
    
    public void sendMessage(AlgoMessage message) {
        Map<String, WebSocketSession> room = rooms.get(message.getRoom());
        try {
            if (null != room) {
                if ("".equals(message.getTo())) {
                    for (WebSocketSession session:room.values()) {
                        if (! session.equals(room.get(message.getFrom()))) {
                            session.sendMessage(new TextMessage(message.toJsonString()));
                        }
                    }
                } else {
                    WebSocketSession session = room.get(message.getTo());
                    session.sendMessage(new TextMessage(message.toJsonString()));
                }
            }
        } catch (Exception ignored) {

        }

    }

}