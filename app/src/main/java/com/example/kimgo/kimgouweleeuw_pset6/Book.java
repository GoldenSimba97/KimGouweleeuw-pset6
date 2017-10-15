package com.example.kimgo.kimgouweleeuw_pset6;

import java.io.Serializable;

/**
 * Created by kimgo on 13-10-2017.
 */

public class Book implements Serializable {
    public String title;
    public String author;
    public String publisher;
    public String publishedDate;
    public String description;
    public Integer rank;

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

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public String getPublisher() { return publisher; }

    public String getPublishedDate() { return publishedDate; }

    public String getDescription() { return description; }

    public Integer getRank() { return rank; }

    public void setRank(int newRank) { rank = newRank; }
}
