package com.bird.maru.common.config.WebSocket;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private MessageType type;

    private String content;

    private String sender;

}
