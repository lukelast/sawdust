package com.googlecode.sawdust.server.db;

import java.util.Collection;
import java.util.Iterator;

import com.googlecode.sawdust.shared.LogEntry;

public interface DataAccessService
{
    void saveLogEntries( String logName, Iterator<LogEntry> logProvider ) throws Exception;
    
    Collection<LogEntry> getLogEntries(String logName, long timeStart, long timeEnd) throws Exception;

    void launchExternalAdminInterface();
}