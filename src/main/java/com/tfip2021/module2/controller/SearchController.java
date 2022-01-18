package com.tfip2021.module2.controller;

import java.util.List;

import com.tfip2021.module2.model.Book;
import com.tfip2021.module2.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = { "/search" })
public class SearchController {

    @Autowired
    private BookService service;

    @GetMapping(produces = { "text/html" })
    public String getCurrentWeather(
        @RequestParam MultiValueMap<String, String> params,
        Model model
    ) {
        String query = params.getFirst("q");
        List<Book> books = service.search(query);
        model.addAttribute("query", query);
        model.addAttribute("books", books);
        return "search";
    }
}
