/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2015-04-29 11:21 创建
 *
 */
package com.yiji.framework.watcher;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;

/**
 * @author qiubo@yiji.com
 */
public class Utils {
	
	public static final String[] EMPTY_STRINGS = {};
	
	private static Long pid = null;
	
	public static String trim(String str) {
		if (str == null) {
			return null;
		} else {
			return str.trim();
		}
	}
	
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}
	
	public static Object getParam(Map<String, Object> params, String key, Object defaultValue) {
		if (params == null) {
			return null;
		}
		Object v = params.get(key);
		if (v == null) {
			v = defaultValue;
		}
		return v;
	}
	
	public static String[] split(String str, char separatorChar) {
		if (str == null) {
			return null;
		}
		int length = str.length();
		
		if (length == 0) {
			return EMPTY_STRINGS;
		}
		
		List list = new ArrayList();
		int i = 0;
		int start = 0;
		boolean match = false;
		
		while (i < length) {
			if (str.charAt(i) == separatorChar) {
				if (match) {
					list.add(str.substring(start, i));
					match = false;
				}
				start = ++i;
				continue;
			}
			match = true;
			i++;
		}
		if (match) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
	
	public static Long getPid() {
		if (pid == null) {
			final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
			final int index = jvmName.indexOf('@');
			
			if (index < 1) {
				return -1l;
			}
			try {
				pid = Long.parseLong(jvmName.substring(0, index));
				return pid;
			} catch (NumberFormatException e) {
				// ignore
				return -1l;
			}
		} else {
			return pid;
		}
	}
	
	public static void unzip(String sourceFile, String toDir) throws Exception {
		ZipFile zipFile = new ZipFile(sourceFile);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			File entryDestination = new File(toDir, entry.getName());
			entryDestination.getParentFile().mkdirs();
			if (entry.isDirectory())
				entryDestination.mkdirs();
			else {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = zipFile.getInputStream(entry);
					out = new FileOutputStream(entryDestination);
					ByteStreams.copy(in, out);
				} finally {
					Closeables.close(in, true);
					Closeables.close(out, true);
				}
				
			}
		}
		Closeables.close(zipFile, true);
	}
	
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = Utils.class.getClassLoader();
			if (cl == null) {
				// getClassLoader() returning null indicates the bootstrap ClassLoader
				try {
					cl = ClassLoader.getSystemClassLoader();
				} catch (Throwable ex) {
					// Cannot access system ClassLoader - oh well, maybe the caller can live with null...
				}
			}
		}
		return cl;
	}
	
	public static Set<URL> findResourceFromClassPath(String path) throws Exception {
		Set<URL> result = new LinkedHashSet<>(8);
		Enumeration<URL> urlEnumeration = Utils.getDefaultClassLoader().getResources(path);
		if (urlEnumeration.hasMoreElements()) {
			URL url = urlEnumeration.nextElement();
			if (isJarResource(url)) {
				result.addAll(findPathMatchingJarResources(url));
			} else {
				result.addAll(findPathMatchingResources(url));
			}
		}
		return result;
	}
	
	private static Set<URL> findPathMatchingResources(URL rootUrl) throws IOException {
		Set<URL> result = new LinkedHashSet<>(8);
		doFindPathMatchingResources(rootUrl, result);
		return result;
	}
	
	private static void doFindPathMatchingResources(URL rootUrl, Set<URL> result) throws IOException {
		File rootDir = new File(rootUrl.getFile());
		File[] dirContents = rootDir.listFiles();
		if (dirContents == null) {
			return;
		}
		for (File content : dirContents) {
			if (content.isDirectory()) {
				URL dirUrl = new URL("file:" + content.getAbsolutePath() + File.separator);
				result.add(dirUrl);
				doFindPathMatchingResources(dirUrl, result);
			} else {
				result.add(new URL("file:" + content.getAbsolutePath()));
			}
		}
	}
	
	private static Set<URL> findPathMatchingJarResources(URL rootUrl) throws IOException {
		//在jar包中的相对路径
		URLConnection con = rootUrl.openConnection();
		JarURLConnection jarCon = (JarURLConnection) con;
		JarFile jarFile = jarCon.getJarFile();
		JarEntry jarEntry = jarCon.getJarEntry();
		String rootEntryPath = (jarEntry != null ? jarEntry.getName() : "");
		
		if (!"".equals(rootEntryPath) && !rootEntryPath.endsWith("/")) {
			rootEntryPath = rootEntryPath + "/";
		}
		
		Set<URL> result = new LinkedHashSet<>(8);
		for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
			JarEntry entry = entries.nextElement();
			String entryPath = entry.getName();
			if (entryPath.startsWith(rootEntryPath)) {
				if (entryPath.startsWith("/")) {
					entryPath = entryPath.substring(1);
				}
				URL url = new URL(rootUrl, entryPath);
				result.add(getCleanedUrl(url, url.toString()));
			}
		}
		return result;
	}
	
	private static URL getCleanedUrl(URL originalUrl, String originalPath) {
		try {
			return new URL(StringUtils.cleanPath(originalPath));
		} catch (MalformedURLException ex) {
			// Cleaned URL path cannot be converted to URL
			// -> take original URL.
			return originalUrl;
		}
	}
	
	public static String cleanPath(String path) {
		if (path == null) {
			return null;
		} else {
			String pathToUse = replace(path, "\\", "/");
			int prefixIndex = pathToUse.indexOf(":");
			String prefix = "";
			if (prefixIndex != -1) {
				prefix = pathToUse.substring(0, prefixIndex + 1);
				pathToUse = pathToUse.substring(prefixIndex + 1);
			}
			
			if (pathToUse.startsWith("/")) {
				prefix = prefix + "/";
				pathToUse = pathToUse.substring(1);
			}
			
			String[] pathArray = delimitedListToStringArray(pathToUse, "/");
			LinkedList pathElements = new LinkedList();
			int tops = 0;
			
			int i;
			for (i = pathArray.length - 1; i >= 0; --i) {
				String element = pathArray[i];
				if (!".".equals(element)) {
					if ("..".equals(element)) {
						++tops;
					} else if (tops > 0) {
						--tops;
					} else {
						pathElements.add(0, element);
					}
				}
			}
			
			for (i = 0; i < tops; ++i) {
				pathElements.add(0, "..");
			}
			
			return prefix + collectionToDelimitedString(pathElements, "/");
		}
	}
	
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
			StringBuffer sbuf = new StringBuffer();
			int pos = 0;
			int index = inString.indexOf(oldPattern);
			
			for (int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
				sbuf.append(inString.substring(pos, index));
				sbuf.append(newPattern);
				pos = index + patLen;
			}
			
			sbuf.append(inString.substring(pos));
			return sbuf.toString();
		} else {
			return inString;
		}
	}
	
	public static boolean hasLength(CharSequence str) {
		return str != null && str.length() > 0;
	}
	
	public static String collectionToDelimitedString(Collection coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}
	
	public static String collectionToDelimitedString(Collection coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		} else {
			StringBuffer sb = new StringBuffer();
			Iterator it = coll.iterator();
			
			while (it.hasNext()) {
				sb.append(prefix).append(it.next()).append(suffix);
				if (it.hasNext()) {
					sb.append(delim);
				}
			}
			
			return sb.toString();
		}
	}
	
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		return delimitedListToStringArray(str, delimiter, (String) null);
	}
	
	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		} else if (delimiter == null) {
			return new String[] { str };
		} else {
			ArrayList result = new ArrayList();
			int pos;
			if ("".equals(delimiter)) {
				for (pos = 0; pos < str.length(); ++pos) {
					result.add(deleteAny(str.substring(pos, pos + 1), charsToDelete));
				}
			} else {
				pos = 0;
				
				int var6;
				for (boolean delPos = false; (var6 = str.indexOf(delimiter, pos)) != -1; pos = var6 + delimiter.length()) {
					result.add(deleteAny(str.substring(pos, var6), charsToDelete));
				}
				
				if (str.length() > 0 && pos <= str.length()) {
					result.add(deleteAny(str.substring(pos), charsToDelete));
				}
			}
			
			return toStringArray((Collection) result);
		}
	}
	
	public static String[] toStringArray(Collection collection) {
		return collection == null ? null : (String[]) ((String[]) collection.toArray(new String[collection.size()]));
	}
	
	public static String deleteAny(String inString, String charsToDelete) {
		if (hasLength(inString) && hasLength(charsToDelete)) {
			StringBuffer out = new StringBuffer();
			
			for (int i = 0; i < inString.length(); ++i) {
				char c = inString.charAt(i);
				if (charsToDelete.indexOf(c) == -1) {
					out.append(c);
				}
			}
			
			return out.toString();
		} else {
			return inString;
		}
	}
	
	public static String collectionToCommaDelimitedString(Collection coll) {
		return collectionToDelimitedString(coll, ",");
	}
	
	public static boolean isJarResource(URL url) throws IOException {
		String protocol = url.getProtocol();
		return "jar".equals(protocol) || "zip".equals(protocol) || "vfszip".equals(protocol) || "wsjar".equals(protocol)
				|| "code-source".equals(protocol) && url.getPath().contains("!/");
	}
	
}
