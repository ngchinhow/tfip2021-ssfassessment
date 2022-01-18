package com.tfip2021.module2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import static com.tfip2021.module2.model.Constants.OPENLIBRARY_BASE_URI;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;


import com.tfip2021.module2.model.Book;

@Service
public class BookService {

    @Autowired
    private BookRepository repo;

    public Book get(String olid) {
        
        final Book book = new Book();
        if (repo.hasCachedBook(olid)) {
            // restore from cache
            System.out.println("Did not refresh");
            book.buildFromJson(getFromRedis(olid));
            book.setCached(true);
        } else {
            // get from API
            System.out.println("Did refresh");
            book.buildFromJson(getFromAPI(olid));
            book.setCached(false);
        }
        return book;
    }

    public JsonObject getFromRedis(String olid) {
        JsonReader reader = Json.createReader(
            new StringReader(repo.getCachedBook(olid))
        );
        return reader.readObject();
    }

    public JsonObject getFromAPI (String olid) {
        String url = UriComponentsBuilder.
            fromUriString(OPENLIBRARY_BASE_URI).
            pathSegment("works", olid + ".json").
            toUriString();

        JsonObject workObject = makeGetRequest(url);
        repo.setCachedBook(olid, workObject.toString());
        return workObject;
    }

    public List<Book> search(String q) {
        final String defaultFields = "key,title";
        final int defaultLimit = 20;
        System.out.println(q);
        String cleanQuery = q.trim().replaceAll(" ", "+");

        // build url
        String url = UriComponentsBuilder.
            fromUriString(OPENLIBRARY_BASE_URI).
            pathSegment("search.json").
            queryParam("title", cleanQuery).
            queryParam("fields",  defaultFields).
            queryParam("limit", defaultLimit).
            toUriString();
        System.out.println(url);
        
        // make request
        JsonArray booksJson = makeGetRequest(url).getJsonArray("docs");
        return booksJson.stream().map(v -> {
            JsonObject bookResult = (JsonObject) v;
            String key = bookResult.getString("key");
            return new Book(
                key.replace("/works/", ""),
                bookResult.getString("title"),
                OPENLIBRARY_BASE_URI + key
            );
        }).collect(Collectors.toList());
    }

    public JsonObject makeGetRequest(String url) {
        RequestEntity<Void> req = RequestEntity.
            get(url).
            build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
        return reader.readObject();
    }
}
