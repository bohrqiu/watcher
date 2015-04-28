package com.yiji.framework.watcher.adaptor.telnet;

/**
 * Created by SegeonTang on 2015/4/27.
 */
public interface TelnetService {
    /**
     * 初始化telnet服务，需要完成如下工作：
     * 1. 读取服务配置，包括服务端口，最大连接数，是否启动验证，工作线程池的线程数等
     * 2. 获取注册的telnet命令
     * 3. 初始化工作线程池
     * 4. 打开监听端口
     * @throws Exception
     */
    void init() throws Exception;


    /**
     * 停止telnet服务，需要完成如下工作：
     * 1. 拒绝新的客户端请求
     * 2. 处理线程池中尚未完成的任务
     * 3. 释放资源
     */
    void stop();
}
