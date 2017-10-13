package com.example.kimgo.kimgouweleeuw_pset6;

/**
 * Created by kimgo on 13-10-2017.
 */

public class Book {
    public String title;
    public String author;
    public String publisher;
    public String publishedDate;
    public String description;
    public int rank;

    // Default constructor for firebase
    public Book() {}

    public Book(String aTitle, String anAuthor, String aPublisher, String aPublishedDate, String aDescription) {
        this.title = aTitle;
        this.author = anAuthor;
        this.publisher = aPublisher;
        this.publishedDate = aPublishedDate;
        this.description = aDescription;
        this.rank = 0;
    }
}
