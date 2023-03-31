package com.example.LibManager.controller;

import com.example.LibManager.models.Author;
import com.example.LibManager.models.Book;
import com.example.LibManager.models.PlCompany;
import com.example.LibManager.repositories.AuthorRepository;
import com.example.LibManager.repositories.BookRepository;
import com.example.LibManager.repositories.CategoryRepository;
import com.example.LibManager.repositories.PlCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "books")
public class BookController {

    @Autowired
    private PlCompanyRepository plCompanyRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(path = "/detailBook/{bookID}", method = RequestMethod.GET)
    public String getDetailBook(ModelMap modelMap, @PathVariable String bookID) {
        Book book = bookRepository.findById(bookID).get();
        modelMap.addAttribute("author", authorRepository.findById(book.getAuthorID()).get());
        modelMap.addAttribute("plc", plCompanyRepository.findById(book.getPlCompanyID()).get());
        modelMap.addAttribute("categories", categoryRepository.findAll());
        modelMap.addAttribute("book", book);
        return "detailBook";
    }

    @RequestMapping(path = "/manageBook", method = RequestMethod.GET)
    public String manageBook(ModelMap modelMap) {
        modelMap.addAttribute("books", bookRepository.findAll());
        return "manageBook";
    }

    @RequestMapping(path = "/deleteBook/{bookID}", method = RequestMethod.POST)
    public String deleteBook(@PathVariable String bookID, ModelMap modelMap) {
        try{
            bookRepository.deleteById(bookID);
            return "redirect:/books/manageBook";
        }catch (Exception ex) {
            modelMap.addAttribute("error", ex.toString());
            return "redirect:/books/manageBook";
        }
    }

    @RequestMapping(path = "/insertBook", method = RequestMethod.GET)
    public String insertBook(ModelMap modelMap) {
        modelMap.addAttribute("book", new Book());
        modelMap.addAttribute("categories", categoryRepository.findAll());
        return "insertBook";
    }

    public String getAuthorIDByName(String name) {
        String Id = "";
        Iterable<Author> authors = authorRepository.findAll();
        for (Author author:authors) {
            if(author.getAuthorName().equalsIgnoreCase(name)) {
                Id += author.getAuthorID();
            }
        }
        return Id;
    }

    public String getPlCompanyIDByName(String name) {
        String Id = "";
        Iterable<PlCompany> plCompanies = plCompanyRepository.findAll();
        for (PlCompany plCompany:plCompanies) {
            if(plCompany.getPlCompanyName().equalsIgnoreCase(name)) {
                Id += plCompany.getPlCompanyID();
            }
        }
        return Id;
    }

    public String getAuthorNameByID(String id) {
        String name = "";
        Iterable<Author> authors = authorRepository.findAll();
        for (Author author:authors) {
            if(author.getAuthorID().equalsIgnoreCase(id)) {
                name += author.getAuthorName();
            }
        }
        return name;
    }

    public String getPlCompanyNameByID(String id) {
        String name = "";
        Iterable<PlCompany> plCompanies = plCompanyRepository.findAll();
        for (PlCompany plCompany : plCompanies) {
            if(plCompany.getPlCompanyID().equalsIgnoreCase(id)) {
                name += plCompany.getPlCompanyName();
            }
        }
        return name;
    }

    @RequestMapping(path = "/insertBook", method = RequestMethod.POST)
    public String insertBook(ModelMap modelMap,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult) {
        if(bindingResult.hasErrors() == true) {
            modelMap.addAttribute("books", bookRepository.findAll());
            return "redirect:/books/manageBook";
        }else {
            try {
                if (getAuthorIDByName(book.getAuthorID()) == "") {
                    Author author = new Author();
                    author.setAuthorName(book.getAuthorID());
                    authorRepository.save(author);
                    book.setAuthorID(author.getAuthorID());
                } else {
                    book.setAuthorID(getAuthorIDByName(book.getAuthorID()));
                }
                if (getPlCompanyIDByName(book.getPlCompanyID()) == "") {
                    PlCompany plCompany = new PlCompany();
                    plCompany.setPlCompanyName(book.getPlCompanyID());
                    plCompanyRepository.save(plCompany);
                    book.setPlCompanyID(plCompany.getPlCompanyID());
                    bookRepository.save(book);
                } else {
                    book.setPlCompanyID(getPlCompanyIDByName(book.getPlCompanyID()));
                }
                bookRepository.save(book);
                return "redirect:/books/manageBook";
            } catch (Exception ex) {
                modelMap.addAttribute("error", ex.toString());
                return "insertBook";
            }
        }
    }

    @RequestMapping(path = "/updateBookForm/{bookID}", method = RequestMethod.GET)
    public String updateBook(ModelMap modelMap, @PathVariable String bookID) {
        modelMap.addAttribute("categories", categoryRepository.findAll());
        Book book = bookRepository.findById(bookID).get();
        book.setAuthorID(getAuthorNameByID(book.getAuthorID()));
        book.setPlCompanyID(getPlCompanyNameByID(book.getPlCompanyID()));
        modelMap.addAttribute("book", book);
        return "updateBook";
    }

    @RequestMapping(path = "/updateBook/{bookID}", method = RequestMethod.POST)
    public String updateBook(ModelMap modelMap,
                             @PathVariable String bookID,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult) {
        boolean hasfault = false;
        if(bindingResult.hasErrors()) {
            hasfault = true;
            return "updateBookForm";
        }else {
            try {
                if(bookRepository.findById(bookID).isPresent()) {
                    Book foundBook = bookRepository.findById(bookID).get();

                    foundBook.setBookName(book.getBookName());
                    foundBook.setBookPrice(book.getBookPrice());
                    foundBook.setPageNumber(book.getPageNumber());
                    foundBook.setReleasedDay(book.getReleasedDay());
                    foundBook.setBookImg(book.getBookImg());
                    foundBook.setCategoryID(book.getCategoryID());
                    foundBook.setAuthorID(getAuthorIDByName(book.getAuthorID()));
                    foundBook.setPlCompanyID(getPlCompanyIDByName(book.getPlCompanyID()));
                    bookRepository.save(foundBook);
                    modelMap.addAttribute("books", bookRepository.findAll());
                    modelMap.addAttribute("hasfault", hasfault);
                    System.out.println(hasfault);
                    return "redirect:/books/manageBook";
                }
            }catch (Exception ex) {
                modelMap.addAttribute("error", ex.toString());
                return "updateBookForm";
            }
        }
        modelMap.addAttribute("books", bookRepository.findAll());
        return "redirect:/books/manageBook";
    }

    @RequestMapping(path = "/getBooksByCategoryID/{categoryID}", method = RequestMethod.GET)
    public String getBooksByCategoryID(ModelMap modelMap, @PathVariable String categoryID) {
        modelMap.addAttribute("category", categoryRepository.findById(categoryID).get());
        modelMap.addAttribute("books", bookRepository.findByCategoryID(categoryID));
        return "bookOnCat";
    }
}
