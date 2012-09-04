package com.googlecode.sawdust.server.db;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.googlecode.sawdust.shared.LogEntry;

public final class DataAccessFakeData implements DataAccessService
{
    @Override
    public void saveLogEntries( String logName, Iterator<LogEntry> logProvider ) throws Exception
    {
        // Ignore.
    }

    @Override
    public void launchExternalAdminInterface()
    {
    }

    @Override
    public Collection<LogEntry> getLogEntries( String logName, long timeStart, long timeEnd )
                                                                                             throws Exception
    {
        return ImmutableList.of( new LogEntry( logName, timeStart, ImmutableMap.of( "col1",
                                                                                    "row1col1",
                                                                                    "col2",
                                                                                    "row1col2" ) ),
                                 new LogEntry( logName, timeStart + 1, ImmutableMap.of( "col1",
                                                                                        "row2col1",
                                                                                        "col2",
                                                                                        "row2col2" ) ),
                                 new LogEntry( logName, timeEnd, ImmutableMap.of( "col1",
                                                                                  "row3col1",
                                                                                  "col2",
                                                                                  "row3col2",
                                                                                  "col3",
                                                                                  "row3col3" ) ) );
    }
}