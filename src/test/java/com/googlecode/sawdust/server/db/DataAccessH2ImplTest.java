package com.googlecode.sawdust.server.db;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.googlecode.sawdust.shared.LogEntry;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class DataAccessH2ImplTest {

    public static final String LOG_NAME = "log-name";

    private static final Collection<LogEntry> createLogs() {
        return ImmutableList.of(new LogEntry(LOG_NAME, 1,
                ImmutableMap.of("key1", "val1", "key2", "val2")),
                new LogEntry(LOG_NAME, 2,
                        ImmutableMap.of("key1b", "val1b", "key2b", "val2b")));
    }

    @Test
    public void testSaveGetLogs() throws Exception {
        DataAccessH2Impl h2 = new DataAccessH2Impl("mem:logs");

        Collection<LogEntry> logs = createLogs();

        h2.saveLogEntries(LOG_NAME, logs.iterator());



        Assert.assertEquals(logs, h2.getLogEntries(LOG_NAME,Long.MIN_VALUE,Long.MAX_VALUE));
    }
}
