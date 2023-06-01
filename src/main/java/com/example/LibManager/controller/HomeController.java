package com.example.LibManager.controller;

import com.example.LibManager.models.Book;
import com.example.LibManager.models.BookSearchDTO;
import com.example.LibManager.repositories.BookRepository;
import com.example.LibManager.repositories.CategoryRepository;
import com.example.LibManager.services.VNCharacterUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping(path = "main")
@RequiredArgsConstructor
public class HomeController {

    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

    @GetMapping("")
    public String homePage(ModelMap modelMap) {
        modelMap.addAttribute("categories", categoryRepository.findAll());
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
        return "homePage";
    }

    @PostMapping("/search")
    public String search(ModelMap modelMap,
                         @ModelAttribute("bookDTO") BookSearchDTO bookDTO) {

        // to remove accent and space
        String keyword = VNCharacterUtils.removeSpace(VNCharacterUtils.removeAccent(bookDTO.getBookName().trim())).toUpperCase();

        Iterable<Book> books = bookRepository.findAll();
        ArrayList<Book> foundBooks = new ArrayList<Book>();
        for (Book book : books){
            String rootName = VNCharacterUtils.removeSpace(VNCharacterUtils.removeAccent(book.getBookName()));
            if(rootName.toUpperCase().contains(keyword)) {
                foundBooks.add(book);
            }
        }
        if(foundBooks.isEmpty()) {
            modelMap.addAttribute("found", false);
            return "searchPage";
        }
        modelMap.addAttribute("found", true);
        modelMap.addAttribute("books", foundBooks);
        return "searchPage";
    }
}
