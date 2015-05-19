/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-04 19:04 创建
 *
 */
package com.yiji.framework.watcher.metrics.os;

import java.util.Map;

import org.hyperic.sigar.ResourceLimit;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.cmd.SigarCommandBase;
import org.hyperic.sigar.jmx.SigarInvokerJMX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * @author qiubo@yiji.com
 */
public class WatcherUlimit extends SigarCommandBase {
	private static final Logger logger = LoggerFactory.getLogger(WatcherUlimit.class);
	
	private static Map<String, String> map = Maps.newHashMap();
	private static Map<String, String> result = Maps.newHashMap();
	static {
		map.put("Core", "core file size");
		map.put("Data", "data seg size");
		map.put("FileSize", "file size");
		map.put("PipeSize", "pipe size");
		map.put("Memory", "max memory size");
		map.put("OpenFiles", "open files");
		map.put("Stack", "stack size");
		map.put("Cpu", "cpu time");
		map.put("Processes", "max user processes");
		map.put("VirtualMemory", "virtual memory");
	}
	private SigarInvokerJMX invoker;
	
	private String mode;
	
	public WatcherUlimit() {
	}
	
	public String getUsageShort() {
		return "Display system resource limits";
	}
	
	protected boolean validateArgs(String[] args) {
		return true;
	}
	
	private static String format(long val) {
		return val == ResourceLimit.INFINITY() ? "unlimited" : String.valueOf(val);
	}
	
	private String getValue(String attr) throws SigarException {
		Long val = (Long) this.invoker.invoke(attr + this.mode);
		return format(val.longValue());
	}
	
	public void output(String[] args) throws SigarException {
		this.mode = "Cur";
		this.invoker = SigarInvokerJMX.getInstance(this.proxy, "Type=ResourceLimit");
		
		for (int i = 0; i < args.length; ++i) {
			String arg = args[i];
			if (arg.equals("-H")) {
				this.mode = "Max";
			} else {
				if (!arg.equals("-S")) {
					throw new SigarException("Unknown argument: " + arg);
				}
				
				this.mode = "Cur";
			}
		}
		
		this.println("core file size......." + this.getValue("Core"));
		this.println("data seg size........" + this.getValue("Data"));
		this.println("file size............" + this.getValue("FileSize"));
		this.println("pipe size............" + this.getValue("PipeSize"));
		this.println("max memory size......" + this.getValue("Memory"));
		this.println("open files..........." + this.getValue("OpenFiles"));
		this.println("stack size..........." + this.getValue("Stack"));
		this.println("cpu time............." + this.getValue("Cpu"));
		this.println("max user processes..." + this.getValue("Processes"));
		this.println("virtual memory......." + this.getValue("VirtualMemory"));
	}
	
	public Map<String, String> getUlimitInfo() throws SigarException {
		if (!result.isEmpty()) {
			return result;
		}
		this.mode = "Cur";
		this.invoker = SigarInvokerJMX.getInstance(this.proxy, "Type=ResourceLimit");
		
		for (String s : map.keySet()) {
			String val = null;
			try {
				val = this.getValue(s);
			} catch (SigarException e) {
				logger.error("获取ulimit[{}]信息失败", s, e);
				val = "error";
			}
			result.put(s, val);
		}
		return result;
		
		//this.println("core file size......." + this.getValue("Core"));
		//this.println("data seg size........" + this.getValue("Data"));
		//this.println("file size............" + this.getValue("FileSize"));
		//this.println("pipe size............" + this.getValue("PipeSize"));
		//this.println("max memory size......" + this.getValue("Memory"));
		//this.println("open files..........." + this.getValue("OpenFiles"));
		//this.println("stack size..........." + this.getValue("Stack"));
		//this.println("cpu time............." + this.getValue("Cpu"));
		//this.println("max user processes..." + this.getValue("Processes"));
		//this.println("virtual memory......." + this.getValue("VirtualMemory"));
	}
	
	public Map<String, String> getComment() {
		return map;
	}
	
}
