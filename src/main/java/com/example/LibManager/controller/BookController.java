package com.example.LibManager.controller;

import com.example.LibManager.models.*;
import com.example.LibManager.repositories.*;
import com.example.LibManager.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;

@Controller
@RequestMapping(path = "books")
@RequiredArgsConstructor
public class BookController {

    private final PlCompanyRepository plCompanyRepository;

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    private final BookRepository bookRepository;

    private final Borrow_BookRepository bBRepository;

    private final StorageService service;


    @GetMapping("/detailBook/{bookID}")
    public String getDetailBook(ModelMap modelMap, @PathVariable String bookID) {
        Book book = bookRepository.findById(bookID).get();
        modelMap.addAttribute("categories", categoryRepository.findAll());
        modelMap.addAttribute("book", book);
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
        return "detailBook";
    }

    @RequestMapping(path = "/manageBook", method = RequestMethod.GET)
    public String manageBook(ModelMap modelMap) {

        Iterable<Borrow_Book> bbs = bBRepository.findAll();
        modelMap.addAttribute("sum", Borrow_BookController.sum);
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bbs", bbs);
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
        return "manageBook";
    }

    @PostMapping("/deleteBook/{bookID}")
    public String deleteBook(@PathVariable String bookID, ModelMap modelMap) {
        try{
            Iterable<Borrow_Book> bbs = bBRepository.findAll();
            for (Borrow_Book bb: bbs) {
                if(bb.getBorrowBookKey().getBookID().equalsIgnoreCase(bookID)) {
                    bBRepository.delete(bb);
                }
            }
            bookRepository.deleteById(bookID);
            //Iterable<Borrow_Book> bbs = bBRepository.findAll();
            modelMap.addAttribute("sum", Borrow_BookController.sum);
            modelMap.addAttribute("books", bookRepository.findAll());
            modelMap.addAttribute("bbs", bBRepository.findAll());
            modelMap.addAttribute("bookDTO", new BookSearchDTO());
            return "manageBook";
        }catch (Exception ex) {
            modelMap.addAttribute("error", ex.toString());
            return "manageBook";
        }
    }

    @GetMapping("/insertBook")
    public String insertBook(ModelMap modelMap) {
        modelMap.addAttribute("book", new BookDTO());
        modelMap.addAttribute("categories", categoryRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
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

    @GetMapping("/{bookID}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String bookID) throws IOException {
        byte[] imageData = service.downloadImageFromFileSystem(bookID);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

    @PostMapping("/insertBook")
    public String insertBook(ModelMap modelMap,
                             @Valid @ModelAttribute("book") BookDTO bookDTO,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file) {
        if(bindingResult.hasErrors()) {
            modelMap.addAttribute("categories", categoryRepository.findAll());
            modelMap.addAttribute("bookDTO", new BookSearchDTO());
            return "insertBook";
        }else {
            Book book = new Book();
            try {
                book.setBookName(bookDTO.getBookName());
                book.setBookPrice(bookDTO.getBookPrice());
                book.setPageNumber(bookDTO.getPageNumber());
                book.setReleasedDay(bookDTO.getReleasedDay());
                book.setImagePath(service.uploadFileToFileSystem(file));
                book.setCategory(categoryRepository.findById(bookDTO.getCategoryID()).get());
                if (getAuthorIDByName(bookDTO.getAuthorID()) == "") {
                    Author author = new Author();
                    author.setAuthorName(bookDTO.getAuthorID());
                    authorRepository.save(author);
                    book.setAuthor(author);
                } else {
                    book.setAuthor(authorRepository.findByAuthorName(bookDTO.getAuthorID()).get());
                }
                if (getPlCompanyIDByName(bookDTO.getPlCompanyID()) == "") {
                    PlCompany plCompany = new PlCompany();
                    plCompany.setPlCompanyName(bookDTO.getPlCompanyID());
                    plCompanyRepository.save(plCompany);
                    book.setPlCompany(plCompany);
                    bookRepository.save(book);
                } else {
                    //.setPlCompanyID(getPlCompanyIDByName(book.getPlCompanyID()));
                    book.setPlCompany(plCompanyRepository.findByPlCompanyName(bookDTO.getPlCompanyID()).get());
                }
                //book.setImagePath(service.uploadFileToFileSystem(file));
                bookRepository.save(book);
                modelMap.addAttribute("sum", Borrow_BookController.sum);
                modelMap.addAttribute("books", bookRepository.findAll());
                modelMap.addAttribute("bbs", bBRepository.findAll());
                modelMap.addAttribute("bookDTO", new BookSearchDTO());
                return "manageBook";
            } catch (Exception ex) {
                modelMap.addAttribute("error", ex.toString());
                return "insertBook";
            }
        }
    }


    @GetMapping("/updateBookForm/{bookID}")
    public String updateBook(ModelMap modelMap, @PathVariable String bookID) {
        modelMap.addAttribute("categories", categoryRepository.findAll());
        Book book = bookRepository.findById(bookID).get();
        BookDTO bookDTO = BookDTO.builder()
                .bookID(book.getBookID())
                .bookName(book.getBookName())
                .bookPrice(book.getBookPrice())
                .pageNumber(book.getPageNumber())
                .authorID(book.getAuthor().getAuthorName())
                .plCompanyID(book.getPlCompany().getPlCompanyName())
                .releasedDay(book.getReleasedDay())
                .categoryID(book.getCategory().getCategoryID())
                .build();
        modelMap.addAttribute("book", bookDTO);
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
        return "updateBook";
    }


    @PostMapping("/updateBook/{bookID}")
    public String updateBook(ModelMap modelMap,
                             @PathVariable String bookID,
                             @Valid @ModelAttribute("book") BookDTO bookDTO,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file) {
        if(bindingResult.hasErrors()) {
            modelMap.addAttribute("categories", categoryRepository.findAll());
            Book fBook = bookRepository.findById(bookID).get();
            //fBook.setAuthorID(getAuthorNameByID(fBook.getAuthorID()));
            //fBook.setPlCompanyID(getPlCompanyNameByID(fBook.getPlCompanyID()));
            modelMap.addAttribute("book", fBook);
            modelMap.addAttribute("bookDTO", new BookSearchDTO());
            return "updateBook";
        }else {
            try {
                if(bookRepository.findById(bookID).isPresent()) {
                    Book foundBook = bookRepository.findById(bookID).get();

                    foundBook.setBookName(bookDTO.getBookName());
                    foundBook.setBookPrice(bookDTO.getBookPrice());
                    foundBook.setPageNumber(bookDTO.getPageNumber());
                    foundBook.setReleasedDay(bookDTO.getReleasedDay());
                    foundBook.setImagePath(service.uploadFileToFileSystem(file));
                    foundBook.setCategory(categoryRepository.findById(bookDTO.getCategoryID()).get());
                    foundBook.setAuthor(authorRepository.findByAuthorName(bookDTO.getAuthorID()).get());
                    foundBook.setPlCompany(plCompanyRepository.findByPlCompanyName(bookDTO.getPlCompanyID()).get());
                    bookRepository.save(foundBook);
                    modelMap.addAttribute("sum", Borrow_BookController.sum);
                    modelMap.addAttribute("books", bookRepository.findAll());
                    modelMap.addAttribute("bookDTO", new BookSearchDTO());
                    return "manageBook";
                }
            }catch (Exception ex) {
                modelMap.addAttribute("error", ex.toString());
                return "updateBookForm";
            }
        }
        modelMap.addAttribute("sum", Borrow_BookController.sum);
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bbs", bBRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
        return "manageBook";
    }

    @GetMapping("/getBooksByCategoryID/{categoryID}")
    public String getBooksByCategoryID(ModelMap modelMap, @PathVariable String categoryID) {
        ArrayList<Book> books = (ArrayList<Book>) bookRepository.findByCategory(categoryRepository.findById(categoryID).get());
        if(!books.isEmpty()) {
            modelMap.addAttribute("category", categoryRepository.findById(categoryID).get());
            modelMap.addAttribute("books", books);
            modelMap.addAttribute("hasData", true);
            modelMap.addAttribute("bookDTO", new BookSearchDTO());
            return "bookOnCat";
        }
        modelMap.addAttribute("category", categoryRepository.findById(categoryID).get());
        modelMap.addAttribute("books", books);
        modelMap.addAttribute("hasData", false);
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
        return "bookOnCat";
    }

}
