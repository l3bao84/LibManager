package com.example.LibManager.models;

import com.example.LibManager.repositories.AuthorRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Entity
@Table(name = "book")
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

    @Column(name = "bookImg")
    @NotNull
    @NotBlank(message = "Đường dẫn ảnh không được để trống")
    @Size(min = 3, max = 300)
    private String bookImg;

    @Column(name = "categoryID")
    private String categoryID;

    public Book(){}


    public Book(String bookID, String bookName, double bookPrice, String authorID, String releasedDay, String plCompanyID, int pageNumber, String bookImg, String categoryID) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookPrice = bookPrice;
        this.authorID = authorID;
        this.releasedDay = releasedDay;
        this.plCompanyID = plCompanyID;
        this.pageNumber = pageNumber;
        this.bookImg = bookImg;
        this.categoryID = categoryID;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getReleasedDay() {
        return releasedDay;
    }

    public void setReleasedDay(String releasedDay) {
        this.releasedDay = releasedDay;
    }

    public String getPlCompanyID() {
        return plCompanyID;
    }

    public void setPlCompanyID(String plCompanyID) {
        this.plCompanyID = plCompanyID;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getBookImg() {
        return bookImg;
    }

    public void setBookImg(String bookImg) {
        this.bookImg = bookImg;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }
}
