package com.example.atividade02;

import java.io.Serializable;

public class ModelBook implements Serializable {
    int id;
    String title;
    String isbn;
    String author;
    String bookMaker;

    public ModelBook(int id, String title, String isbn, String author, String bookMaker) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.bookMaker = bookMaker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookMaker() {
        return bookMaker;
    }

    public void setBookMaker(String bookMaker) {
        this.bookMaker = bookMaker;
    }

    @Override
    public String toString() {
        return "BOOK {" +
                "ID: " + id +
                "Title: '" + title + '\'' +
                "ISBN: " + isbn +
                "Author: '" + author + '\'' +
                "BookMaker: '" + bookMaker + '\'' +
                '}';
    }
}
