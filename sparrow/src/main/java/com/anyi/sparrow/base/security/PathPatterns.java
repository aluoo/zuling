package com.anyi.sparrow.base.security;

import java.util.HashSet;
import java.util.Set;

public class PathPatterns {
    private Set<String> urls = new HashSet<>();

    public PathPatterns add(String url) {
        urls.add(url);
        return this;
    }

    public PathPatterns() {
    }

    public String[] toArray() {
        return urls.toArray(new String[urls.size()]);
    }

    public static PathPatterns create() {
        return new PathPatterns();
    }

    public static PathPatterns createDefault() {
        PathPatterns p = new PathPatterns();
        p.add("/swagger-ui.html");
        p.add("/doc.html");
        p.add("/swagger-resources/**");
        p.add("/v2/**");
        p.add("/webjars/**");
        p.add("/**/favicon.ico");
        return p;
    }
}
