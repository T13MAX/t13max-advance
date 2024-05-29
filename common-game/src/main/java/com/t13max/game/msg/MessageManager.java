package com.t13max.game.msg;

import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import com.t13max.game.exception.CommonException;
import com.t13max.game.manager.ManagerBase;
import com.t13max.game.session.BattleSession;
import com.t13max.game.session.ISession;
import com.t13max.util.Log;
import com.t13max.util.PackageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: t13max
 * @since: 20:30 2024/5/28
 */
public class MessageManager extends ManagerBase {

    private final Map<Integer, IMessage> messageMap = new HashMap<>();
    private final Map<Integer, Method> parserMap = new HashMap<>();

    @Override
    public void init() {

        try {
            Set<Class<?>> classSet = PackageUtil.scan("com.t13max");
            //创建实例
            for (Class<?> clazz : classSet) {
                // 只需要加载TemplateHelper注解数据
                if (!Message.class.isAssignableFrom(clazz) || Modifier.isAbstract(clazz.getModifiers())) {
                    continue;
                }

                // 创建实例
                Object inst = clazz.getDeclaredConstructor().newInstance();
                Message annotation = clazz.getAnnotation(Message.class);
                if (annotation == null) {
                    continue;
                }

                IMessage message = (IMessage) inst;
                messageMap.put(annotation.value(), message);

                Method[] declaredMethods = IMessage.class.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equals("doMessage")) {
                        Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                        if (parameterTypes.length > 2) {
                            Class<?> parameterType = parameterTypes[2];
                            Method method = parameterType.getMethod("getDefaultInstance");
                            parserMap.put(annotation.value(), method);
                        }
                    }
                }
            }

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new CommonException(e);
        }

    }

    /**
     * 获取当前实例对象
     *
     * @Author t13max
     * @Date 16:44 2024/5/23
     */
    public static MessageManager inst() {
        return ManagerBase.inst(MessageManager.class);
    }

    public IMessage getMessage(int msgId) {
        return messageMap.get(msgId);
    }

    public Method getParseMethod(int msgId) {
        return parserMap.get(msgId);
    }

    public void doMessage(ISession session, int msgId, byte[] data) {
        IMessage message = this.getMessage(msgId);
        if (message == null) {
            Log.common.error("msg不存在, msgId={}", msgId);
            return;
        }
        Method parseMethod = this.getParseMethod(msgId);
        if (parseMethod == null) {
            Log.common.error("parseMethod不存在, msgId={}", msgId);
            return;
        }

        try {

            MessageLite messageLite = (MessageLite) parseMethod.invoke(data);
            message.doMessage(session, msgId, messageLite);
        } catch (Exception e) {
            //后续添加异常处理
            Log.common.error("doMessage error, msgId={}, error={}", msgId, e.getMessage());
        }

    }

    public void sendMessage(ISession session, int msgId, int resCode, MessageLite messageLite) {
        Channel channel = session.getChannel();
        if (!channel.isActive()) {
            Log.common.error("sendMessage failed, channel inactive, uuid={}, msgId={}, message={}", session.getUuid(), msgId, messageLite, getClass().getSimpleName());
            return;
        }
        ByteBuf byteBuf = wrapBuffers(msgId, resCode, messageLite == null ? null : messageLite.toByteArray());
        channel.writeAndFlush(byteBuf);
        Log.common.info("sendMessage, uuid={}, msgId={}, message={}", session.getUuid(), msgId, messageLite, getClass().getSimpleName());

    }

    public void sendMessage(ISession session, int msgId, MessageLite messageLite) {
        sendMessage(session, msgId, 0, messageLite);
    }

    public ByteBuf wrapBuffers(int msgId, int resCode, byte[] data) {
        int len = MessageConst.CLIENT_HEADER_LENGTH;
        if (null != data) len += data.length;
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer(len);
        buf.writeInt(len);
        buf.writeInt(msgId);
        buf.writeInt(resCode);
        if (null != data) {
            buf.writeBytes(data);
        }
        return buf;
    }
}
