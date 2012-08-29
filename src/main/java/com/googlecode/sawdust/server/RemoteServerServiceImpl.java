package com.googlecode.sawdust.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.sawdust.client.RemoteServerService;
import com.googlecode.sawdust.shared.FieldVerifier;
import com.googlecode.sawdust.shared.LogEntry;

import java.io.FileReader;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RemoteServerServiceImpl extends RemoteServiceServlet implements
		RemoteServerService {

	public String greetServer(String input) throws IllegalArgumentException {

		// Verify that the input is valid.
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back
			// to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script
		// vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private static String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public ImmutableList<LogEntry> getLogs(ImmutableList<String> logNames,
			long timeStart, long timeEnd) {

		try {
			TomcatLogParser log = new TomcatLogParser(new FileReader(
					"/Users/llast/code/workspace/sawdust/catalina.out"));

			return ImmutableList.copyOf(log.parse());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ImmutableList.of(
				new LogEntry("log", 1, ImmutableMap.of("col1", "row1col1",
						"col2", "row1col2")),
				new LogEntry("log", 22, ImmutableMap.of("col1", "row2col1",
						"col2", "row2col2")),
				new LogEntry("log", 333, ImmutableMap.of("col1", "row3col1",
						"col2", "row3col2", "col3", "row3col3")));
	}
}