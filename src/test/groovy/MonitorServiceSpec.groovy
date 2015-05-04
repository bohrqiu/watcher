package com.yiji.framework.watcher

import com.alibaba.dubbo.common.json.JSON
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification

class MonitorServiceSpec extends Specification {
    @Shared
    def monitorService = DefaultMonitorService.INSTANCE


    @IgnoreIf({ os.windows })
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

    def "access not exist monitor metrics"() {
        given:
        def monitorRequest = new MonitorRequest();
        when:
        monitorRequest.action = "xxx"
        def obj = monitorService.monitor(monitorRequest)
        then:
        obj.contains("\"success\":false");
    }

}