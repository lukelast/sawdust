package com.googlecode.sawdust.server;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.googlecode.sawdust.server.db.DataAccessH2Impl;
import com.googlecode.sawdust.server.db.DataAccessService;

public final class MyGuiceServletConfig extends GuiceServletContextListener
{
    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector( new ServletModule()
        {
            @Override
            protected void configureServlets()
            {
                serve( "/sawdust/rpc" ).with( RemoteServerServiceImpl.class );
            }
        }, new AbstractModule()
        {
            @Override
            protected void configure()
            {
                bind( DataAccessService.class ).to( DataAccessH2Impl.class ).in( Singleton.class );
            }
        } );
    }
}