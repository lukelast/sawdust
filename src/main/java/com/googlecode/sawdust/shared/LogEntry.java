package com.googlecode.sawdust.shared;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public final class LogEntry {

	private final String logName;
	private final long timestamp;
	private final ImmutableMap<String, String> properties;

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
}