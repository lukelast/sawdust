package com.googlecode.sawdust.server.db;

import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.h2.tools.Server;

import com.googlecode.sawdust.shared.LogEntry;

import javax.inject.Inject;
import javax.inject.Named;

public final class DataAccessH2Impl implements DataAccessService {
    private static final Logger LOG = Logger.getLogger(DataAccessH2Impl.class.getSimpleName());

    private final Connection conn;

    @Inject
    DataAccessH2Impl(@Named("h2") String dbPath) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        conn = DriverManager.getConnection("jdbc:h2:" + dbPath, "sa", "");
        try {
            final Statement stm = conn.createStatement();
            boolean result = stm.execute("CREATE TABLE logs"
                    + "("
                    + "name varchar(255),"
                    + "time INT8," +
                    "properties varchar(8000)," +
                    "PRIMARY KEY(name, time)"
                    + ")");
            LOG.info("Created logs table");
        } catch (Exception ex) {
        }
        //ResultSet tables = conn.getMetaData().getTables(conn.getCatalog(), null, null, null);
        //while(tables.next())

    }

    @Override
    public void launchExternalAdminInterface() {
        Thread th = new Thread() {
            @Override
            public void run() {
                try {
                    Server.startWebServer(conn);
                } catch (SQLException ex) {
                    Logger.getLogger(DataAccessH2Impl.class.getName())
                            .log(Level.WARNING, "Exception launching H2 console", ex);
                }
            }
        };
        th.setPriority(Thread.MIN_PRIORITY);
        th.setDaemon(true);
        th.start();
    }

    @Override
    public void saveLogEntries(String logName, Iterator<LogEntry> logProvider)
            throws SQLException {
        final StringBuilder sb = new StringBuilder(512);

        while (logProvider.hasNext()) {
            LogEntry log = logProvider.next();


            sb.setLength(0);
            sb.append("INSERT INTO logs (name, time, properties ) VALUES (");
            sb.append('\'').append(logName).append("',").
                    append(log.getTimestamp()).append(',')
                    .append('\'');

            for (Map.Entry<String, String> prop : log.getProperties()) {
                sb.append(prop.getKey()).append(':').append(prop.getValue()).append(';');
            }

            sb.append("');");

            final Statement stm = conn.createStatement();
            boolean result = stm.execute(sb.toString());
            System.out.println(result);
        }
    }

    @Override
    public Collection<LogEntry> getLogEntries(String logName, long timeStart, long timeEnd)
            throws Exception {
        final StringBuilder sb = new StringBuilder(256);
        sb.append("SELECT * FROM logs WHERE name = '")
                .append(logName).append("';");

        final Statement stm = conn.createStatement();
        ResultSet result = stm.executeQuery(sb.toString());

        ImmutableList.Builder<LogEntry> logs = ImmutableList.builder();
        while (result.next()) {
            logs.add(new LogEntry(result.getString("name"), result.getLong("time"),
                    ImmutableMap.of("props", result.getString("properties"))));

        }

        return logs.build();
    }
}