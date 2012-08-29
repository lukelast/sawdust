package com.googlecode.sawdust.client;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.sawdust.shared.LogEntry;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("rpc")
public interface RemoteServerService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;

	ImmutableList<LogEntry> getLogs(ImmutableList<String> logNames,
			long timeStart, long timeEnd);
}