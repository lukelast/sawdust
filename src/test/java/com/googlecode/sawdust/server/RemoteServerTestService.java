package com.googlecode.sawdust.server;

import com.googlecode.sawdust.server.db.DataAccessFakeData;


/**
 * Allows the GWT unit tests to create a server RPC implementation with a fake data source.
 */
public class RemoteServerTestService extends RemoteServerServiceImpl {
    public RemoteServerTestService()
    {
        super(new DataAccessFakeData());
    }
}
