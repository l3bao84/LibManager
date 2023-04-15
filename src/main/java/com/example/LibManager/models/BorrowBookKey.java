package com.example.LibManager.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class BorrowBookKey implements Serializable {

    @Column(name = "bookID")
    private String bookID;

    @Column(name = "borrowID")
    private String borrowID;

}
