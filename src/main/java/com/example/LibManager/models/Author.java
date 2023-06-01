package com.example.LibManager.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;

@Entity
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(generator = "authorGenerator")
    @GenericGenerator(name = "authorGenerator", strategy = "com.example.LibManager.Generator.AuthorIDGenerator")
    @Column(name = "authorID")
    private String authorID;

    @Column(name = "authorName")
    private String authorName;

    @OneToMany(mappedBy = "author")
    @JsonManagedReference
    private Set<Book> books;

    public Author() {
    }

    public Author(String authorID, String authorName) {
        this.authorID = authorID;
        this.authorName = authorName;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
