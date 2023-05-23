package com.example.LibManager.models;

import com.example.LibManager.repositories.AuthorRepository;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    @NotBlank(message = "Tên sách không được để trống")
    @Size(min = 3, max = 300)
    private String bookName;

    @Column(name = "bookPrice")
    @NotNull
    @Min(0)
    private double bookPrice;

    @Column(name = "authorID")
    @NotNull
    @NotBlank(message = "Tên tác giả không được để trống")
    @Size(min = 3, max = 300)
    private String authorID;

    @Column(name = "releasedDay")
    @NotNull
    @NotBlank(message = "Vui lòng iền ngày phát hành")
    @Size(min = 3, max = 300)
    private String releasedDay;

    @Column(name = "plCompanyID")
    @NotNull
    @NotBlank(message = "Nhà xuất bản không được để trống")
    @Size(min = 3, max = 300)
    private String plCompanyID;

    @Column(name = "pageNumber")
    @NotNull
    @Min(0)
    private int pageNumber;

    @Lob
    @Column(name = "imagePath", length = 1000)
    private String imagePath;

    @Column(name = "categoryID")
    private String categoryID;

    @OneToMany(mappedBy = "book",fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Borrow_Book> borrow_books;

}
