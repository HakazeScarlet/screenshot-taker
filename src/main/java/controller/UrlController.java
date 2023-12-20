package controller;

import io.javalin.http.Handler;
import parser.HtmlParser;

import java.util.HashSet;
import java.util.Set;

public class UrlController {

    private final HtmlParser htmlParser;

    public UrlController(HtmlParser htmlParser) {
        this.htmlParser = htmlParser;
    }

    public Handler getLinks() {
        return context -> {
            Set<String> url = Set.of(context.bodyAsClass(UrlBody.class).getUrl());
            int deep = Integer.parseInt(context.pathParam("deep"));
            Set<String> urls = htmlParser.parse(url, deep, new HashSet<>());
            context.json(urls);
        };
    }
}
