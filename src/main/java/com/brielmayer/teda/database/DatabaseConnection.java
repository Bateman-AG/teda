package com.brielmayer.teda.database;

import lombok.Getter;

import java.net.URI;

@Getter
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
}
