package com.googlecode.sawdust.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.collect.ImmutableList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.sawdust.client.RemoteServerService;
import com.googlecode.sawdust.server.db.DataAccessFakeData;
import com.googlecode.sawdust.server.db.DataAccessService;
import com.googlecode.sawdust.shared.FieldVerifier;
import com.googlecode.sawdust.shared.LogEntry;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings( "serial" )
@Singleton
public class RemoteServerServiceImpl extends RemoteServiceServlet implements RemoteServerService
{
    private static final Logger LOG = Logger.getLogger( RemoteServerServiceImpl.class.getName() );
    private final DataAccessService dataService;

    @Inject
    RemoteServerServiceImpl( DataAccessService dataService )
    {
        this.dataService = dataService;
    }

    @Override
    public String greetServer( String input ) throws IllegalArgumentException
    {
        // Verify that the input is valid.
        if ( !FieldVerifier.isValidName( input ) )
        {
            // If the input is not valid, throw an IllegalArgumentException back
            // to
            // the client.
            throw new IllegalArgumentException( "Name must be at least 4 characters long" );
        }

        String serverInfo = getServletContext().getServerInfo();
        String userAgent = getThreadLocalRequest().getHeader( "User-Agent" );

        // Escape data from the client to avoid cross-site script
        // vulnerabilities.
        input = escapeHtml( input );
        userAgent = escapeHtml( userAgent );

        return "Hello, " +
               input +
               "!<br><br>I am running " +
               serverInfo +
               ".<br><br>It looks like you are using:<br>" +
               userAgent;
    }

    /**
     * Escape an html string. Escaping data received from the client helps to
     * prevent cross-site script vulnerabilities.
     * @param html the html string to escape
     * @return the escaped string
     */
    private static String escapeHtml( String html )
    {
        if ( html == null )
        {
            return null;
        }
        return html.replaceAll( "&", "&amp;" ).replaceAll( "<", "&lt;" ).replaceAll( ">", "&gt;" );
    }

    @Override
    public ImmutableList<LogEntry> getLogs( ImmutableList<String> logNames, long timeStart,
                                            long timeEnd )
    {
        try
        {
            return ImmutableList.copyOf( this.dataService.getLogEntries( "log", timeStart, timeEnd ) );
        }
        catch ( Exception ex )
        {
            LOG.log( Level.WARNING, "", ex );
            return ImmutableList.of();
        }
        //TomcatLogParser log = new TomcatLogParser(new FileReader("/Users/llast/code/workspace/sawdust/catalina.out"));
        //return ImmutableList.copyOf(log.parse());
    }

    @Override
    public void importLogs( String path, String logName )
    {

        System.out.println( path );
        System.out.println( logName );
    }

    @Override
    public void launchDbConsole() {
        this.dataService.launchExternalAdminInterface();
    }
}