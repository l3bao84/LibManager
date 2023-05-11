package com.example.LibManager.controller;

import com.example.LibManager.models.Book;
import com.example.LibManager.models.BookDTO;
import com.example.LibManager.repositories.BookRepository;
import com.example.LibManager.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping(path = "main")
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String homePage(ModelMap modelMap) {
        modelMap.addAttribute("categories", categoryRepository.findAll());
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "homePage";
    }

    @PostMapping("/search")
    public String search(ModelMap modelMap,@ModelAttribute("bookDTO") BookDTO bookDTO) {
        String keyword = bookDTO.getBookName().trim();
        Iterable<Book> books = bookRepository.findAll();
        ArrayList<Book> foundBooks = new ArrayList<Book>();
        for (Book book : books){
            if(book.getBookName().contains(keyword)) {
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
