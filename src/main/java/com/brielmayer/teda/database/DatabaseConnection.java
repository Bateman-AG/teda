package com.brielmayer.teda.database;

import java.net.URI;

public class DatabaseConnection {
    private final String url;
    private final String user;
    private final String password;
    private String scheme;
    private String host;
    private int port;
    private String databaseName;

    public DatabaseConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        parseUrl();
    }

    private void parseUrl() {
        String cleanURI = url.substring(5);

        URI uri = URI.create(cleanURI);
        this.scheme = uri.getScheme();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.databaseName = uri.getPath();
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getScheme() {
        return scheme;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
