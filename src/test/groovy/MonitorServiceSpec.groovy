package com.yiji.framework.watcher

import com.alibaba.dubbo.common.json.JSON
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification

import static MonitorServiceSpec.isWindows

//此处要注意，需要静态引入
class MonitorServiceSpec extends Specification {
    @Shared
    def monitorService = DefaultMonitorService.INSTANCE

    static def isWindows() {
        System.getProperty("os.name").startsWith("Windows");
    }

    @IgnoreIf({ isWindows() })
    def "os level monitor"() {
        given:
        def monitorRequest = new MonitorRequest();

        when:
        monitorRequest.action = action;
        def object = JSON.parse(monitorService.monitor(monitorRequest));
        then:
        null != object.get(key)
        where:
        action    | key
        "swap"    | "Used"
        "netinfo" | "PrimaryDns"
        "netstat" | "tcpInboundTotal"

    }
}