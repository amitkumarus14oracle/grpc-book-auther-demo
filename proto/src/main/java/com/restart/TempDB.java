package com.restart;

import com.restart.Schema;
import com.restart.Schema.Author;
import com.restart.Schema.Book;

import java.util.ArrayList;
import java.util.List;

public class TempDB {
    public static List<Author> getAuthorsFromTempDB() {
        return new ArrayList<Author>() {
            {
                add(Author.newBuilder().setAuthorId(1).setFirstName("Amit").setLastName("Kumar").setGender("Male").setBookId(1).build());
                add(Author.newBuilder().setAuthorId(2).setFirstName("Sapna").setLastName("Rani").setGender("Female").setBookId(2).build());
                add(Author.newBuilder().setAuthorId(3).setFirstName("Sumit").setLastName("Raj").setGender("Male").setBookId(3).build());
                add(Author.newBuilder().setAuthorId(4).setFirstName("Amrendra").setLastName("Raj").setGender("Male").setBookId(4).build());
                add(Author.newBuilder().setAuthorId(5).setFirstName("Amita").setLastName("Kumar").setGender("Female").setBookId(5).build());
                add(Author.newBuilder().setAuthorId(6).setFirstName("Lovely").setLastName("Shraddha").setGender("Female").setBookId(6).build());
            }
        };
    }

    public static List<Book> getBooksFromTempDB() {
        return new ArrayList<Book>() {
            {
                add(Book.newBuilder().setBookId(1).setPrice(100.0f).setTitle("One").setAuthorId(1).setPages(1).build());
                add(Book.newBuilder().setBookId(2).setPrice(200.0f).setTitle("Two").setAuthorId(2).setPages(2).build());
                add(Book.newBuilder().setBookId(3).setPrice(300.0f).setTitle("Three").setAuthorId(3).setPages(3).build());
                add(Book.newBuilder().setBookId(4).setPrice(400.0f).setTitle("Four").setAuthorId(4).setPages(4).build());
                add(Book.newBuilder().setBookId(5).setPrice(500.0f).setTitle("Five").setAuthorId(5).setPages(5).build());
                add(Book.newBuilder().setBookId(6).setPrice(600.0f).setTitle("Six").setAuthorId(6).setPages(6).build());
            }
        };
    }
}
