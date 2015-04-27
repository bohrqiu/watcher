package com.yiji.framework.watcher;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author qzhanbo@yiji.com
 */
public class DefaultMonitorServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMonitorServiceTest.class);

    MonitorService monitorService = DefaultMonitorService.INSTANCE;

    @Test
    public void testMonitor() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("classload");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("cpu");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("fileDescriptor");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("gc");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("mem");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("pid");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("startTime");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("sysEnv");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("sysProp");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("thread");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("uptime");
        System.out.println(monitorService.monitor(monitorRequest));

        monitorRequest.setAction("jstack");
        System.out.println(monitorService.monitor(monitorRequest));

    }

    @Test
    public void testJstack() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setResponseType(ResponseType.TEXT);
        monitorRequest.setAction("jstack");
        System.out.println(monitorService.monitor(monitorRequest));
    }

    @Test
    public void testSysEnv() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("sysEnv");
        monitorRequest.addParam("key", "PATH");
        System.out.println(monitorService.monitor(monitorRequest));
    }

    @Test
    public void testNetinfo() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("netinfo");
        System.out.println(monitorService.monitor(monitorRequest));
    }

    @Test
    public void testNetStat() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("netStat");
        System.out.println(monitorService.monitor(monitorRequest));

    }

    @Test
    public void testSwap() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("swap");
        System.out.println(monitorService.monitor(monitorRequest));

    }

    @Test
    public void testOSCPU() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("cpuinfo");
        System.out.println(monitorService.monitor(monitorRequest));
    }

    @Test
    public void testCmd() throws Exception {

        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("cmd");
        System.out.println(monitorService.monitor(monitorRequest));
    }

    @Test
    public void testResourceLimit() throws Exception {

        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("resourceLimit");
        System.out.println(monitorService.monitor(monitorRequest));
    }

    @Test
    public void testNotExists() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("cpuinfo");
        monitorRequest.responseJson().prettyFormat();
        System.out.println(monitorService.monitor(monitorRequest));
    }

    @Test
    public void testAdd() throws Exception {
        MonitorMetrics monitorMetrics = new MonitorMetrics() {
            @Override
            public Object monitor(Map<String, Object> params) {
                return null;
            }

            @Override
            public String name() {
                return "gc";
            }

            @Override
            public String desc() {
                return "test";
            }
        };
        monitorService.addMonitorMetrics(monitorMetrics);
    }

    @Test
    public void testNames() throws Exception {
        Set<String> names = monitorService.names();
        names.stream().forEach(s -> {
            MonitorRequest monitorRequest = new MonitorRequest();
            monitorRequest.setAction(s);
            monitorRequest.responseJson();
            logger.info("{}->\n{}", s, monitorService.monitor(monitorRequest));
        });
    }

    @Test
    public void testName() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("procExe");
        monitorRequest.responseJson().prettyFormat();
        System.out.println(monitorService.monitor(monitorRequest));

    }
    @Test
    public void testName1() throws Exception {
        MonitorRequest monitorRequest = new MonitorRequest();
        monitorRequest.setAction("webContainer");
        monitorRequest.responseJson().prettyFormat();
        System.out.println(monitorService.monitor(monitorRequest));

    }
}