package cn.hanglok.algoSched.config;

import cn.hanglok.algoSched.ws.AuthHandshakeInterceptor;
import cn.hanglok.algoSched.ws.RoomAwareChatHandler;
import cn.hanglok.algoSched.ws.UserAndRoomHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
/**
 * @author Allen
 * @version 1.0
 * @className WebSocketConfig
 * @description TODO
 * @date 2024/4/12
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(roomAwareChatHandler(), "/ws")
                .addInterceptors(
                        new AuthHandshakeInterceptor(),
                        new UserAndRoomHandshakeInterceptor()
                )
                .setAllowedOrigins("*"); // 可以根据需要设置允许的源
    }

    public WebSocketHandler roomAwareChatHandler() {
        return new RoomAwareChatHandler();
    }
}
