package com.yiji.framework.watcher.adaptor.telnet;

import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.telnet.TelnetHandler;
import com.alibaba.dubbo.remoting.telnet.support.Help;

/**
 * Created by SegeonTang on 2015/4/28.
 */
@Help(parameter = "", summary = "", detail = "")
public class WatcherTelnetHandler implements TelnetHandler{
    @Override
    public String telnet(Channel channel, String message) throws RemotingException {
        return null;
    }
}
