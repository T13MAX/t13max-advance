package com.t13max.game.server.codec;

import com.t13max.common.exception.CommonException;
import com.t13max.common.ioc.IocManager;
import com.t13max.common.ioc.annotaion.Request;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author t13max
 * @since 17:55 2024/11/4
 */
public class HttpServerHandler extends io.netty.channel.SimpleChannelInboundHandler<FullHttpRequest> {

    private final static Map<Method, Object> controllerMap = new HashMap<>();
    private final static Map<String, Method> methodMap = new HashMap<>();

    public HttpServerHandler() {
        Set<String> beanNames = IocManager.inst().getBeanNames();
        for (String beanName : beanNames) {
            Object bean = IocManager.inst().getBean(beanName);
            Method[] declaredMethods = bean.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                Request request = method.getAnnotation(Request.class);
                if (request == null) {
                    continue;
                }
                String name = request.value();
                if (methodMap.containsKey(name)) {
                    throw new CommonException("Request的name重复");
                }
                methodMap.put(name, method);
                controllerMap.put(method, bean);
            }
        }
    }

    @Override
    protected void channelRead0(io.netty.channel.ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
        Map<String, String> param = parseParam(req);
        String uri = req.uri();
        Method method = methodMap.get(uri);
        FullHttpResponse response;
        if (method == null) {
            response = handleNotFound();
            ctx.writeAndFlush(response);
            return;
        }
        Object controller = controllerMap.get(method);
        if (controller == null) {
            response = handleNotFound();
            ctx.writeAndFlush(response);
            return;
        }
        String result = (String) method.invoke(controller, param);
        response = createJsonResponse(result);
        // 确保 Connection header 正确处理
        if (HttpUtil.isKeepAlive(req)) {
            response.headers().set("Connection", "keep-alive");
        }
        ctx.writeAndFlush(response);
    }

    private Map<String, String> parseParam(FullHttpRequest req) throws IOException {
        Map<String, String> result;
        if (req.method().name().equals("POST")) {
            ByteBuf jsonBuf = req.content();
            String jsonStr = jsonBuf.toString(CharsetUtil.UTF_8);
            // 使用 Jackson 解析 JSON
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(jsonStr, Map.class);
        } else {
            result = new HashMap<>();
            String uri = req.uri();
            // 解析查询参数
            QueryStringDecoder decoder = new QueryStringDecoder(uri);
            Map<String, List<String>> parameters = decoder.parameters();
            for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
                result.put(entry.getKey(), entry.getValue().get(0));
            }
        }
        return result;
    }

    private FullHttpResponse handleNotFound() {
        String jsonContent = "{\"error\": \"404 - Not Found\"}";
        return createJsonResponse(jsonContent);
    }

    private FullHttpResponse createJsonResponse(String jsonContent) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, OK, Unpooled.copiedBuffer(jsonContent, CharsetUtil.UTF_8));
        response.headers().set("Content-Type", "application/json; charset=UTF-8");
        return response;
    }

    @Override
    public void exceptionCaught(io.netty.channel.ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
