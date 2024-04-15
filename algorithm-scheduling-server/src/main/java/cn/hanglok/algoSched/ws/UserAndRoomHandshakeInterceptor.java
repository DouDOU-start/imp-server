package cn.hanglok.algoSched.ws;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Allen
 * @version 1.0
 * @className UserHandshakeInterceptor
 * @description TODO
 * @date 2024/4/12
 */
public class UserAndRoomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        String query = request.getURI().getQuery();

        Map<String, String> queryParams = parseQuery(query);

        String username = queryParams.get("username");
        String roomName = queryParams.get("roomName");

        attributes.put("username", username);
        attributes.put("roomName", roomName);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private Map<String, String> parseQuery(String query) {
        Map<String, String> queryParams = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                queryParams.put(entry[0], entry[1]);
            }
        }
        return queryParams;
    }
}