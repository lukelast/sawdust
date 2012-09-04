package com.googlecode.sawdust.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.h2.tools.Server;

import com.googlecode.sawdust.shared.LogEntry;

public final class DataAccessH2Impl implements DataAccessService
{

    private final Connection conn;

    public DataAccessH2Impl() throws ClassNotFoundException, SQLException
    {
        Class.forName( "org.h2.Driver" );
        conn = DriverManager.getConnection( "jdbc:h2:logs", "sa", "" );
    }

    @Override
    public void launchExternalAdminInterface()
    {
        Thread th = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Server.startWebServer( conn );
                }
                catch ( SQLException ex )
                {
                    Logger.getLogger( DataAccessH2Impl.class.getName() )
                        .log( Level.WARNING, "Exception launching H2 console", ex );
                }
            }
        };
        th.setPriority( Thread.MIN_PRIORITY );
        th.setDaemon( true );
        th.start();
    }

    @Override
    public void saveLogEntries( String logName, Iterator<LogEntry> logProvider )
                                                                                throws SQLException
    {
        final Statement stm = conn.createStatement();
        boolean result = stm.execute( "CREATE TABLE logs"
                                      + "("
                                      + "name varchar(255),"
                                      + "time Long,"
                                      + ")" );
        System.out.println( result );
    }

    @Override
    public Collection<LogEntry> getLogEntries( String logName, long timeStart, long timeEnd )
                                                                                             throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }
}