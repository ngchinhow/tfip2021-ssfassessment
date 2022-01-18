package com.tfip2021.module2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.json.JsonObject;

public class Book {
    private String key;
    private String title;
    private String description;
    private List<String> excerpts;
    private String coverUrl;
    private String webpageUrl;
    private boolean cached;
    
    public Book() { }

    public Book(String key, String title, String webpageUrl) {
        this.key = key;
        this.title = title;
        this.webpageUrl = webpageUrl;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getExcerpts() { return excerpts; }
    public void setExcerpts(List<String> excerpts) { this.excerpts = excerpts; }
    
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public String getWebpageUrl() { return webpageUrl; }
    public void setWebpageUrl(String webpageUrl) { this.webpageUrl = webpageUrl; }

    public boolean getCached() { return cached; }
    public void setCached(boolean cached) { this.cached = cached; }

    public void buildFromJson(JsonObject o) {
        this.setKey(o.getString("key"));
        this.setTitle(o.getString("title"));
        if (o.containsKey("description")) {
            Object descriptionObject = o.get("description");
            if (descriptionObject instanceof JsonObject) {
                JsonObject descriptionJsonObject = (JsonObject) descriptionObject;
                this.setDescription(descriptionJsonObject.
                    getString("value", "No Descripton Available")
                );
            } else if (descriptionObject instanceof String) {
                this.setDescription((String) descriptionObject);
            }
        } else {
            this.setDescription("No Description Available");
        }
            
        if (o.containsKey("excerpts")) {
            this.setExcerpts(
                o.getJsonArray("excerpts").stream().map(v -> {
                        JsonObject excerpt = (JsonObject) v;
                        return excerpt.getString("excerpt", "No Excerpt Available");
                    }).
                    collect(Collectors.toList())
            );
        } else {
            this.setExcerpts(new ArrayList<String>() {
                {
                    add("No Excerpt Available");
                }
            });
        }
            
        
        String coverUrlFormat = "https://covers.openlibrary.org/b/id/%s-M.jpg";
        if (o.containsKey("covers")) {
            this.setCoverUrl(String.format(
                coverUrlFormat,
                o.getJsonArray("covers").getInt(0)
            ));
        }
            
    }
}
