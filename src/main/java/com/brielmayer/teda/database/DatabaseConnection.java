package com.brielmayer.teda.database;

import com.brielmayer.teda.exception.TedaException;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseConnection {

    private static final List<String> PATTERNS = Arrays.asList(".*://(\\w*):(\\d++).*", ".*@(\\w*):(\\d++).*");

    private final String url;
    private final String user;
    private final String password;
    private String scheme;
    private String host;
    private int port;
    private String databaseName;

    public DatabaseConnection(final String url, final String user, final String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        parseUrl();
    }

    private void parseUrl() {
        final String cleanURI = url.substring(5);

        final URI uri = URI.create(cleanURI);
        this.scheme = uri.getScheme();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.databaseName = uri.getPath();

        if (this.host == null) {
            for (final String pattern : PATTERNS) {
                if (parseUrlByPattern(pattern)) {
                    break;
                }
            }
        }
        if (this.host == null) {
            throw TedaException.builder()
                    .appendMessage("Failed to parse JDBC url %s", url)
                    .build();
        }
    }

    private boolean parseUrlByPattern(final String pattern) {
        final Pattern p = Pattern.compile(pattern);

        final Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group(1);
            port = Integer.parseInt(matcher.group(2));
            return true;
        }
        return false;
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
