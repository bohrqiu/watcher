package com.yiji.framework.watcher.adaptor.telnet;

import com.yiji.framework.watcher.DefaultMonitorService;
import com.yiji.framework.watcher.MonitorRequest;
import com.yiji.framework.watcher.MonitorService;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * Created by SegeonTang on 2015/4/27.
 */
public class MinaTelnetHandler extends IoHandlerAdapter{
    private static MonitorService monitorService = DefaultMonitorService.INSTANCE;

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String messageStr = (String) message;
        try {
            MonitorRequest request = RequestParser.parse(messageStr);
            String ret = monitorService.monitor(request);
            session.write(ret);
        } catch (IllegalArgumentException e) {
            session.write("参数有误，请检查后重试！");
        }
    }
}
