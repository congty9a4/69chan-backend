package com.congty9a4.backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.congty9a4.backend.config.security.JwtService;
import com.congty9a4.backend.exception.error.AppException;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtService jwtService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-69chan").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Chỉ kiểm tra Token khi User bắt đầu CONNECT
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authHeader = accessor.getFirstNativeHeader("Authorization");

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        try {
                            // Gọi hàm validate của để check Hết hạn + Blacklist Redis + Loại Token
                            // Chuyền "true" vào để báo rằng đây bắt buộc phải là Access Token
                            jwtService.validateToken(token, true);

                            // Nếu qua được ải trên (không văng Exception), thì lấy ID ra dùng
                            String userId = jwtService.extractUserId(token);

                            // CẤP PHÉP VÀO TRONG
                            accessor.setUser(() -> userId);
                            log.info("User {} đã kết nối WebSocket an toàn!", userId);

                        } catch (AppException e) {
                            log.error("Truy cập WebSocket bị từ chối: {}", e.getMessage());
                            throw new IllegalArgumentException("Token không hợp lệ!");
                        } catch (Exception e) {
                            log.error("Lỗi xác thực WebSocket: {}", e.getMessage());
                            throw new IllegalArgumentException("Từ chối kết nối!");
                        }
                    } else {
                        log.error("Thiếu Token khi kết nối WebSocket!");
                        throw new IllegalArgumentException("Vui lòng cung cấp Token!");
                    }
                }
                return message;
            }
        });
    }
}