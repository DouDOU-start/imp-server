package cn.hanglok.algoSched.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Allen
 * @version 1.0
 * @className AlgoMessage
 * @description TODO
 * @date 2024/4/15
 */
@Slf4j
@Data
public class AlgoMessage {
    private String messageId;
    private String room;
    private String from;
    private String to;
    private String content;
    private long timestamp;

    public String toJsonString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }
        return null;
    }
}
