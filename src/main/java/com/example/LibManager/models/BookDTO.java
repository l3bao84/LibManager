package com.example.LibManager.models;


import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookDTO {

    private String bookID;

    @NotEmpty(message = "Tên sách không được để trống")
    @Size(min = 3, max = 300)
    private String bookName;

    @NotNull
    @Min(0)
    private double bookPrice;

    @NotEmpty(message = "Tên tác giả không được để trống")
    @Size(min = 3, max = 300)
    private String authorID;

    @NotEmpty(message = "Vui lòng điền ngày phát hành")
    @Size(min = 3, max = 300)
    private String releasedDay;

    @NotEmpty(message = "Nhà xuất bản không được để trống")
    @Size(min = 3, max = 300)
    private String plCompanyID;

    @Min(0)
    private int pageNumber;

    @Lob
    private String imagePath;


    private String categoryID;
}
