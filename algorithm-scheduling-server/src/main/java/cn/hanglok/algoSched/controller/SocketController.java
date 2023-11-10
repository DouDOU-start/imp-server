package cn.hanglok.algoSched.controller;

import cn.hanglok.algoSched.socket.AlgorithmServerHandler;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Allen
 * @version 1.0
 * @className SocketController
 * @description TODO
 * @date 2023/9/18
 */
@RestController
@RequestMapping("/socket")
public class SocketController {
    @GetMapping
    public Object get() {
        return AlgorithmServerHandler.ctxMap;
    }

        @GetMapping("/execute/{id}")
    public String execute(@PathVariable(value = "id") String id) {
        AlgorithmServerHandler.Ctx ctx = AlgorithmServerHandler.ctxMap.get(id);
        ctx.getCtx().writeAndFlush(
                Unpooled.copiedBuffer(
                        new JSONObject() {{
                            put("test", 1);
                        }}.toString(),
                CharsetUtil.UTF_8)
        );
        return "success";
    }
}
