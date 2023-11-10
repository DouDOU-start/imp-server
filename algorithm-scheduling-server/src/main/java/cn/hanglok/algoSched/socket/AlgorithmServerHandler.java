package cn.hanglok.algoSched.socket;

import com.alibaba.fastjson2.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Allen
 * @version 1.0
 * @className AlgorithmServerHandler
 * @description TODO
 * @date 2023/9/18
 */
@Slf4j
public class AlgorithmServerHandler extends ChannelInboundHandlerAdapter {

    public static Map<String, Ctx> ctxMap = new ConcurrentHashMap<>();

    @Data
    public static class Ctx {
        private String id;
        private ChannelHandlerContext ctx;
        private String algorithm;

        public Ctx(String id, ChannelHandlerContext ctx, String algorithm) {
            this.id = id;
            this.ctx = ctx;
            this.algorithm = algorithm;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;

        byteBuf.toString(CharsetUtil.UTF_8);

        Map<String, Object> msgMap = JSON.parseObject(byteBuf.toString(CharsetUtil.UTF_8), HashMap.class.getGenericSuperclass());

        String msgType = (String) msgMap.getOrDefault("msgType", "");

        switch (msgType) {
            case "register":
                Ctx ctx11 = JSON.parseObject(msgMap.get("msgContent").toString(), Ctx.class);
                ctx11.setCtx(ctx);
                ctxMap.put(String.valueOf(ctx.channel().id()), ctx11);
                break;
            case "res":
                log.info("收到客户端" + ctx.channel().remoteAddress() + "发送的消息" + msgMap.get("msgContent").toString());
                break;
            default:
                break;
        }


        log.info("收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ctxMap.remove(String.valueOf(ctx.channel().id()));
    }

}
