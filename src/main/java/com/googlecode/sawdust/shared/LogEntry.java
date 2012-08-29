package com.googlecode.sawdust.shared;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Map;

public final class LogEntry implements IsSerializable {

	private String logName;
	private long timestamp;
	private ImmutableMap<String, String> properties;

	/**
	 * Only for GWT serialization, do not use.
	 */
	public LogEntry() {
	}

	public LogEntry(String logName, long timestamp,
			Map<String, String> properties) {
		this.logName = logName;
		this.timestamp = timestamp;
		this.properties = ImmutableMap.copyOf(properties);
	}

	public String getLogName() {
		return logName;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getProperty(String key) {
		return this.properties.get(key);
	}

	public ImmutableSet<String> getKeys() {
		return this.properties.keySet();
	}
}