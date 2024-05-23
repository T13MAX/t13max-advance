package com.t13max.game.run;

import com.t13max.game.Const;
import com.t13max.game.config.BaseConfig;
import com.t13max.game.exception.CommonException;
import com.t13max.game.manager.ManagerBase;
import com.t13max.game.server.BaseServer;
import com.t13max.util.Log;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.locks.LockSupport;

/**
 * @author: t13max
 * @since: 17:29 2024/5/23
 */
public class Application {

    private static BaseConfig config;

    private static String instanceName;

    public static void run(Class<?> clazz, String[] args) {

        //初始化所有manager
        ManagerBase.initAllManagers();

        //加载配置
        loadConfig(clazz);

        initInstance();

        //启动监听服
        initServer(clazz);

        //添加停服钩子 manager shutdown
        addShutdownHook(ManagerBase::shutdown);

        //阻塞主线程
        if (config.isPark()) LockSupport.park();
    }

    private static void initServer(Class<?> clazz) throws RuntimeException {

        NettyServer annotation = clazz.getAnnotation(NettyServer.class);

        if (annotation == null) {
            autoLogger().info("不启动监听...");
            return;
        }

        Class<? extends BaseServer> serverClazz = annotation.serverClazz();
        if (serverClazz == null) {
            autoLogger().info("不启动监听...");
            return;
        }

        Constructor<? extends BaseServer> constructor;

        try {
            constructor = serverClazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new CommonException("无法启动监听, 没有空参构造");
        }

        BaseServer baseServer;
        try {
            baseServer = constructor.newInstance();
        } catch (Exception e) {
            throw new CommonException("无法启动监听, 创建server对象失败");
        }

        try {
            //开启监听
            baseServer.startTcpServer();
            //停服
            addShutdownHook(baseServer::shutdown);
        } catch (InterruptedException e) {
            throw new CommonException("监听服被打断");
        }
    }

    /**
     * 实例初始化
     *
     * @Author t13max
     * @Date 19:02 2024/5/23
     */
    private static void initInstance() {
        String instanceId = config.getInstanceId();

        String[] split = instanceId.split("-");
        instanceName = split[0];
    }

    /**
     * 获取配置文件
     *
     * @Author t13max
     * @Date 17:58 2024/5/23
     */
    public static <T extends BaseConfig> T config() {
        return (T) config;
    }

    /**
     * 添加停服钩子
     *
     * @Author t13max
     * @Date 18:32 2024/5/23
     */
    public static void addShutdownHook(Runnable runnable) {
        Runtime.getRuntime().addShutdownHook(new Thread(runnable));
    }

    /**
     * 加载配置文件
     *
     * @Author t13max
     * @Date 17:58 2024/5/23
     */
    private static void loadConfig(Class<?> clazz) {

        ServerConfig annotation = clazz.getAnnotation(ServerConfig.class);

        Class<? extends BaseConfig> configClazz = BaseConfig.class;
        if (annotation != null) {
            configClazz = annotation.configClazz();
        }

        Yaml yaml = new Yaml();

        config = yaml.loadAs(Application.class.getClassLoader().getResourceAsStream(Const.CONFIG_NAME), configClazz);

        //校验配置
        if (!configCheck()) {
            throw new CommonException("配置文件校验异常");
        }
    }

    private static boolean configCheck() {
        return config.check();
    }

    /**
     * 获取自适应log
     *
     * @Author t13max
     * @Date 19:04 2024/5/23
     */
    public static Logger autoLogger() {

        switch (instanceName) {
            case "scene" -> {
                return Log.scene;
            }
            case "game" -> {
                return Log.game;
            }
            case "battle" -> {
                return Log.battle;
            }
            default -> {
                return Log.def;
            }
        }
    }

}
