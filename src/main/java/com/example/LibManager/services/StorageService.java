package com.example.LibManager.services;

import com.example.LibManager.models.Book;
import com.example.LibManager.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final BookRepository bookRepository;
    private final String FOLDER_PATH = "C:\\Users\\HELLO\\OneDrive\\Desktop\\LibManager\\LibManager\\src\\main\\resources\\images\\";

    public String generateNewFileName(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return UUID.randomUUID().toString() + "." + extension;
    }

    public String uploadFileToFileSystem(MultipartFile file) throws IOException {
        String filePath = FOLDER_PATH + generateNewFileName(file);
        file.transferTo(new File(filePath));
        return filePath;
    }

    public byte[] downloadImageFromFileSystem(String bookID) throws IOException {
        Optional<Book> dbImageData = bookRepository.findById(bookID);
        String filePath = dbImageData.get().getImagePath();
        byte[] images = Files.readAllBytes(new File(filePath).toPath());
        return images;
    }
}
