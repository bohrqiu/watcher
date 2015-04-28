package com.yiji.framework.watcher.adaptor.telnet;

import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.telnet.TelnetHandler;
import com.alibaba.dubbo.remoting.telnet.support.Help;
import com.alibaba.dubbo.remoting.telnet.support.TelnetUtils;
import com.google.common.collect.Lists;
import com.yiji.framework.watcher.DefaultMonitorService;
import com.yiji.framework.watcher.MonitorMetrics;
import com.yiji.framework.watcher.MonitorRequest;
import com.yiji.framework.watcher.MonitorService;
import com.yjf.common.util.StringUtils;

import java.util.List;

/**
 * Created by SegeonTang on 2015/4/28.
 */
@Help(parameter = "[-h] metricName [key1=val1,...]", summary = "查看watcher提供的指标", detail = "查看watcher提供的指标，命令格式如下 key1=value1,key2=value2,...")
public class WatcherTelnetHandler implements TelnetHandler{
    private MonitorService monitorService = DefaultMonitorService.INSTANCE;
    private String helpInfo;

    public WatcherTelnetHandler() {
        String[] header = {"metricName", "description"};
        List<List<String>> table = Lists.newLinkedList();
        for (MonitorMetrics monitorMetrics : monitorService.monitorMetricses()) {
            List<String> row = Lists.newLinkedList();
            row.add(monitorMetrics.name());
            row.add(monitorMetrics.desc());
            table.add(row);
        }
        helpInfo = TelnetUtils.toTable(header, table);
    }

    @Override
    public String telnet(Channel channel, String message) throws RemotingException {
        if (message.equals("-h")) {
            return helpInfo;
        } else {
            try {
                MonitorRequest request = RequestParser.parse(message);
                request.setPrettyFormat(false);
                return monitorService.monitor(request);
            } catch (IllegalArgumentException e) {
                return "命令格式错误，正确的格式：metricName key1=value1,key2=value2,...";
            }
        }
    }
}
