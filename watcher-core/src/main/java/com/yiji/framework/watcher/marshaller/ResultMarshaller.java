/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * qzhanbo@yiji.com 2015-05-20 01:04 创建
 *
 */
package com.yiji.framework.watcher.marshaller;

import static com.yiji.framework.watcher.Constants.NULL;

import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Joiner;
import com.yiji.framework.watcher.Marshaller;
import com.yiji.framework.watcher.model.Result;

/**
 * @author qiubo@yiji.com
 */
public class ResultMarshaller implements Marshaller {
	
	@Override
	public String marshall(Result result) {
		if (result == null) {
			result = new Result();
			result.setSuccess(false);
			result.setMessage("marshall object is null");
		}
		if (result.responseJson()) {
			return toJson(result);
		} else {
			if (result.isSuccess()) {
				Object data = result.getData();
				return convertObjToStr(data);
			} else {
				return result.getMessage();
			}
		}
	}
	
	private String convertObjToStr(Object result) {
		if (result == null) {
			return NULL.toString();
		}
		if (result instanceof Collection) {
			Collection collection = (Collection) result;
			return Joiner.on("\n").join(collection);
		} else if (result instanceof Map) {
			Map map = (Map) result;
			return Joiner.on("\n").withKeyValueSeparator("=").join(map);
		} else {
			return result.toString();
		}
	}
	
	private String toJson(Result result) {
		SerializeWriter out = null;
		try {
			if (result.isPrettyFormat()) {
				out = new SerializeWriter(SerializerFeature.PrettyFormat);
			} else {
				out = new SerializeWriter();
			}
			JSONSerializer serializer = getJsonSerializer(out);
			serializer.write(result);
			return out.toString();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	private JSONSerializer getJsonSerializer(SerializeWriter out) {
		JSONSerializer serializer = new JSONSerializer(out);
		serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
		serializer.setDateFormat("yyyy-MM-dd HH:mm:ss");
		serializer.config(SerializerFeature.QuoteFieldNames, true);
		serializer.config(SerializerFeature.DisableCircularReferenceDetect, true);
		return serializer;
	}
}
