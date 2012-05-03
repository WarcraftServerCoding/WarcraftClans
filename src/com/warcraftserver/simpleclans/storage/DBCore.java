package com.warcraftserver.simpleclans.storage;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author phaed
 */
public interface DBCore
{
    /**
     * @return connection
     */
    public Connection getConnection();

    /**
     * @return whether connection can be established
     */
    public Boolean checkConnection();

    /**
     * Close connection
     */
    public void close();

    /**
     * Execute a select statement
     * @param query
     * @return
     */
    public ResultSet select(String query);

    /**
     * Execute an insert statement
     * @param query
     */
    public void insert(String query);

    /**
     * Execute an update statement
     * @param query
     */
    public void update(String query);

    /**
     * Execute a delete statement
     * @param query
     */
    public void delete(String query);

    /**
     * Execute a statement
     * @param query
     * @return
     */
    public Boolean execute(String query);

    /**
     * Check whether a table exists
     * @param table
     * @return
     */
    public Boolean existsTable(String table);

}
