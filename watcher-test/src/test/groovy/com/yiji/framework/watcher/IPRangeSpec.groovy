package com.yiji.framework.watcher

import com.yiji.framework.watcher.http.adaptor.web.util.IPAddress
import com.yiji.framework.watcher.http.adaptor.web.util.IPRange

class IPRangeSpec extends spock.lang.Specification {

    def "ip in ip range "() {
        expect:
        new IPRange(ipRange).isIPAddressInRange(new IPAddress(ip)) == result;
        where:
        ipRange                     | ip              | result
        "127.0.0.1"                 | "127.0.0.1"     | true
        "127.0.0.1"                 | "127.0.0.2"     | false

        "192.168.0.0/255.255.255.0" | "192.168.0.0"   | true
        "192.168.0.0/255.255.255.0" | "192.168.0.255" | true
        "192.168.0.0/255.255.255.0" | "192.168.1.0"   | false
        "192.168.0.0/255.255.0.0"   | "192.168.1.0"   | true
    }

}  