package com.googlecode.sawdust.client;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.sawdust.shared.LogEntry;

/**
 * The asynchronous counterpart of {@link RemoteServerService}.
 */
public interface RemoteServerServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getLogs(ImmutableList<String> logNames, long timeStart, long timeEnd,
			AsyncCallback<ImmutableList<LogEntry>> callback);

	void importLogs(String path, String logName, AsyncCallback<Void> callback);
}