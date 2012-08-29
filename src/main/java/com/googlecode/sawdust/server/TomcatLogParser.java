package com.googlecode.sawdust.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.googlecode.sawdust.shared.LogEntry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public final class TomcatLogParser {

	private enum SplitToken {
		SPACE(' '), NEW_LINE('\n'), TAB('\t');

		private final char split;

		private SplitToken(char character) {
			this.split = character;
		}

		public int getSplitChar() {
			return this.split;
		}
	}

	public TomcatLogParser(Reader reader) {
		this.reader = new BufferedReader(reader, 1024 * 128);
	}

	private final Reader reader;
	private final StringBuilder sb = new StringBuilder(128);
	private String nextToken = "";
	private SplitToken lastSplit;

	public Collection<LogEntry> parse() throws IOException, ParseException {
		Builder<LogEntry> list = ImmutableList.builder();
		while (true) {
			final LogEntry parsedEntry = this.parseNextLogEntry();
			if (parsedEntry == null)
				break;
			else
				list.add(parsedEntry);
		}
		return list.build();
	}

	/**
	 * 4 digit year, month-day, hour:minute:second.millisecond GMT
	 */
	private static final String PARSE_FORMAT = "yyyy MM-dd HH:mm:ss.SSS z";
	private static final DateFormat PARSER = new SimpleDateFormat(PARSE_FORMAT);
	private final int YEAR = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
			.get(Calendar.YEAR);

	/**
	 * 
	 * @param date
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	private long parseTime(String date, String time) throws ParseException {
		final StringBuilder sb = new StringBuilder(32);
		sb.append(YEAR);
		sb.append(' ').append(date).append(' ').append(time).append(' ')
				.append("GMT");
		return PARSER.parse(sb.toString()).getTime();
	}

	public LogEntry parseNextLogEntry() throws IOException, ParseException {

		while (true) {
			reader.mark(32);
			if (reader.read() == -1)
				return null;
			reader.reset();
			final Map<String, String> props = new TreeMap<String, String>();

			this.parseDate(props);
			System.out.println(props);

			final long logTime = parseTime(props.remove("date"),
					props.remove("time"));
			System.out.println(new Date(logTime).toGMTString());

			return new LogEntry("log", logTime, props);
		}

	}

	private void parseDate(Map<String, String> properties) throws IOException {
		properties.put("date",
				parseNextTokenUntil(SplitToken.SPACE, this.reader, this.sb)
						.toString());
		this.parseTime(properties);
	}

	private void parseTime(Map<String, String> properties) throws IOException {
		properties.put("time",
				parseNextTokenUntil(SplitToken.SPACE, this.reader, this.sb)
						.toString());
		parseUser(properties);
	}

	private void parseUser(Map<String, String> props) throws IOException {
		parseNextTokenUntil(SplitToken.SPACE, this.reader, this.sb);
		props.put("user", this.sb.substring(1, sb.length() - 1));
		parseIpAddr(props, reader, sb);
	}

	private static void parseIpAddr(Map<String, String> props, Reader reader,
			StringBuilder sb) throws IOException {
		parseNextTokenUntil(SplitToken.SPACE, reader, sb);
		if (1 < sb.length())
			props.put("ip", sb.toString());
		parsePath(props, reader, sb);
	}

	private static void parsePath(Map<String, String> props, Reader reader,
			StringBuilder sb) throws IOException {
		parseNextTokenUntil(SplitToken.SPACE, reader, sb);
		if (1 < sb.length())
			props.put("path", sb.toString());
		parseTag(props, reader, sb);
	}

	private static void parseTag(Map<String, String> props, Reader reader,
			StringBuilder sb) throws IOException {
		parseNextTokenUntil(SplitToken.SPACE, reader, sb);
		props.put("tag", sb.toString());
		parseLogLevel(props, reader, sb);
	}

	private static void parseLogLevel(Map<String, String> props, Reader reader,
			StringBuilder sb) throws IOException {
		parseNextTokenUntil(SplitToken.SPACE, reader, sb);
		props.put("level", sb.toString());
		parseMessage(props, reader, sb);
	}

	private static void parseMessage(Map<String, String> props, Reader reader,
			StringBuilder sb) throws IOException {
		sb.setLength(0); // Reset.
		while (true) {
			final int nextChar = reader.read();
			// -1 means end of stream.
			if (nextChar < 0) {
				break;
			}
			if (nextChar == SplitToken.NEW_LINE.getSplitChar()) {
				reader.mark(1);
				final char lookahead = (char) reader.read();
				reader.reset();
				if (Character.isDigit(lookahead)) {
					break;
				}
			}
			sb.append((char) nextChar);
		}
		props.put("message", sb.toString());
	}

	private static StringBuilder parseNextTokenUntil(SplitToken end,
			Reader source, StringBuilder sb) throws IOException {
		sb.setLength(0); // Reset.
		while (true) {
			final int nextChar = source.read();
			// -1 means end of stream.
			if (nextChar < 0 || nextChar == end.getSplitChar()) {
				return sb;
			}
			sb.append((char) nextChar);
		}
	}

}