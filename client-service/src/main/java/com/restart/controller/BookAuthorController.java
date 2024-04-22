package com.restart.controller;

import com.google.protobuf.Descriptors;
import com.restart.service.BookAuthorClientService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class BookAuthorController {
    @Autowired
    private BookAuthorClientService bookAuthorService;

    @GetMapping("/author/{id}")
    public Map<Descriptors.FieldDescriptor, Object> getAuthor(@PathVariable("id") String authorId) {
        return bookAuthorService.getAuthor(Integer.parseInt(authorId));
    }

    @GetMapping("/book/{authorId}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthor(@PathVariable("authorId") String authorId) throws InterruptedException {
        return bookAuthorService.getBookByAuthor(Integer.parseInt(authorId));
    }

    @GetMapping("/book/expensive")
    public Map<String, Map<Descriptors.FieldDescriptor,Object>> getExpensiveBook() throws InterruptedException {
        return bookAuthorService.getExpensiveBook();
    }

    @GetMapping("/book/author/{gender}")
    public List<Map<Descriptors.FieldDescriptor,Object>> getBookByAuthorGender(@PathVariable String gender) throws InterruptedException {
        return bookAuthorService.getBooksByAuthorGender(gender);
    }
}
