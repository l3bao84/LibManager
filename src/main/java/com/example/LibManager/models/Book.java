package com.example.LibManager.models;

import com.example.LibManager.repositories.AuthorRepository;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Book {

    @Id
    @GeneratedValue(generator = "bookGenerator")
    @GenericGenerator(name = "bookGenerator", strategy = "com.example.LibManager.Generator.BookIDGenerator")
    @Column(name = "bookID")
    private String bookID;

    @Column(name = "bookName")
    @NotEmpty(message = "Tên sách không được để trống")
    @Size(min = 3, max = 300)
    private String bookName;

    @Column(name = "bookPrice")
    @NotNull
    @Min(0)
    private double bookPrice;

    @Column(name = "releasedDay")
    @NotEmpty(message = "Vui lòng điền ngày phát hành")
    @Size(min = 3, max = 300)
    private String releasedDay;

    @Column(name = "pageNumber")
    @Min(0)
    private int pageNumber;

    @Lob
    @Column(name = "imagePath", length = 1000)
    private String imagePath;

    @OneToMany(mappedBy = "book",fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Borrow_Book> borrow_books;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorID", nullable = false, referencedColumnName = "authorID")
    @JsonManagedReference
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plCompanyID", nullable = false, referencedColumnName = "plCompanyID")
    @JsonManagedReference
    private PlCompany plCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryID", nullable = false, referencedColumnName = "categoryID")
    @JsonManagedReference
    private Category category;
}
