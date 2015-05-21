package com.yiji.framework.watcher

import com.alibaba.fastjson.JSON
import com.yiji.framework.watcher.model.Request
import com.yiji.framework.watcher.model.Result
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
        Result result = monitorService.watch(monitorRequest);
        then:
        assert result.success == status
        if (result.success) {
            String data = JSON.toJSONString(result.data)
            assert data.contains(value) == true
        }
        where:
        action                  | status | value
        "busyJavaThread"        | false  | ""
        "classload"             | true   | "totalLoadedClassCount"
        "cpu"                   | true   | "processors"
        "cpuinfo"               | true   | "CacheSize"
        "datasource"            | false  | ""
        "df"                    | true   | "Used"
        "dubboRegistryStatus"   | false  | ""
        "dubboServerStatus"     | false  | "isBound"
        "dubboThreadPoolStatus" | false  | "queueRemainingCapacity"
        "fileDescriptor"        | true   | "maxFileDescriptorCount"
        "gc"                    | true   | "totalTime"
        "healthCheck"           | true   | "healthy"
        "iostat"                | true   | "Reads"
        "jstack"                | true   | "stackTrace"
        "jvmMem"                | true   | "nonHeapMemoryUsage"
        "netinfo"               | true   | "SecondaryDns"
        "netstat"               | true   | "tcpInboundTotal"
        "osVersion"             | true   | "OS data model"
        "pid"                   | true   | Long.toString(Utils.pid)
        "procExe"               | true   | "inputArguments"
        "swap"                  | true   | "PageOut"
        "sysEnv"                | true   | "USER"
        "sysProp"               | true   | "java.runtime.name"
        "test"                  | true   | "xxx.spi.TestWatcherMetrics1"
        "testshell"             | true   | "hello"
        "thread"                | true   | "totalStartedThreadCount"
        "ulimit"                | true   | "Memory"
        "uptime"                | true   | "uptime"
        "webContainer"          | true   | ""
        "metricRegistry"        | true   | "requests"
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

    def "get all extension name"() {
        given:
        def names = DefaultWatcherService.INSTANCE.set();
        expect:
        !names.isEmpty();
    }

    @IgnoreIf({ os.windows })
    def "jstack return text"() {
        given:
        def monitorRequest = new Request();

        when:
        monitorRequest.action = action;
        monitorRequest.responseType = Constants.ResponseType.TEXT
        Result result = monitorService.watch(monitorRequest);
        then:
        assert result.success == status
        if (result.success) {
            assert result.data.contains(value) == true
        }
        where:
        action   | status | value
        "jstack" | true   | "main"
    }

}