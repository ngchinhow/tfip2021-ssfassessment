package com.tfip2021.module2.controller;

import com.tfip2021.module2.model.Book;
import com.tfip2021.module2.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = { "book" })
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping(path = { "/{work_id}" })
    public String getBook(
        @PathVariable(name="work_id", required=true) String olid,
        Model model
    ) {
        final Book book = service.get(olid);
        model.addAttribute("book", book);
        return "book";
    }
}
