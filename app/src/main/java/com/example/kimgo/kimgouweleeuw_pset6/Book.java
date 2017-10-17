/*
 * Book class. In this class all functions for the book
 * object can be found: the constructor and the functions
 * to access the values of the variables.
 */

package com.example.kimgo.kimgouweleeuw_pset6;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String publisher;
    private String publishedDate;
    private String description = "";
    private float rating;


    /* Default constructor for Firebase. */
    public Book() {}


    /* Constructor for Book object. */
    public Book(String aTitle, String anAuthor, String aPublisher, String aPublishedDate,
                String aDescription) {
        this.title = aTitle;
        this.author = anAuthor;
        this.publisher = aPublisher;
        this.publishedDate = aPublishedDate;
        this.description = aDescription;
        this.rating = 0;
    }


    public Book(String aTitle, String anAuthor, String aPublisher, String aPublishedDate) {
        this.title = aTitle;
        this.author = anAuthor;
        this.publisher = aPublisher;
        this.publishedDate = aPublishedDate;
        this.rating = 0;
    }


    /* Get the title of the book. */
    String getTitle() { return title; }


    /* Get the author of the book. */
    String getAuthor() { return author; }


    /* Get the publisher of the book. */
    String getPublisher() { return publisher; }


    /* Get the publication date of the book. */
    String getPublishedDate() { return publishedDate; }


    /* Get the description of the book. */
    String getDescription() { return description; }


    /* Get the rating of the book. */
    float getRating() { return rating; }


    /* Set the rating of the book. */
    void setRating(float newRating) { rating = newRating; }

}
