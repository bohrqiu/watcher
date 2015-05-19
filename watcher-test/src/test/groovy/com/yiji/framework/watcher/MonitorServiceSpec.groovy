package com.yiji.framework.watcher

import com.alibaba.dubbo.common.json.JSON
import com.yiji.framework.watcher.model.Request
import spock.lang.IgnoreIf
import spock.lang.Shared
import spock.lang.Specification

class MonitorServiceSpec extends Specification {
    @Shared
    def monitorService = DefaultWatcherService.INSTANCE


    @IgnoreIf({ os.windows })
    def "os level monitor"() {
        given:
        def monitorRequest = new Request();

        when:
        monitorRequest.action = action;
        String result=monitorService.watchAndMarshall(monitorRequest);
        print(result)
        def object = JSON.parse(result);
        then:
        null != object.get("data").get(key)
        where:
        action    | key
        "swap"    | "Used"
        "netinfo" | "PrimaryDns"
        "netstat" | "tcpInboundTotal"

    }

    def "access not exist monitor metrics"() {
        given:
        def monitorRequest = new Request();
        when:
        monitorRequest.action = "xxx"
        def obj = monitorService.watchAndMarshall(monitorRequest)
        then:

        obj.contains("\"success\":false");
    }

}