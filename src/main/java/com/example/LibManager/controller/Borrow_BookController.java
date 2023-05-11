package com.example.LibManager.controller;

import com.example.LibManager.models.*;
import com.example.LibManager.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(path = "bb")
public class Borrow_BookController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private Borrow_BookRepository bbRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PlCompanyRepository plCompanyRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    @RequestMapping(path = "/addBB/{bookID}", method = RequestMethod.POST)
    public String addBB(ModelMap modelMap,
                      @PathVariable String bookID,
                      @ModelAttribute("customer") Customer customer,
                      BindingResult bindingResult) {
        Customer foundCustomer = customerRepository.findByCustomerName(customer.getCustomerName()).get();

        Book borrowBook = bookRepository.findById(bookID).get();
        Borrow borrow = borrowRepository.findByCustomer(foundCustomer).get();

        // thời gian mượn sách không quá 7 ngày
        LocalDate returnDay = LocalDate.of(LocalDate.now().getYear(),
                                           LocalDate.now().getMonth(),
                                           LocalDate.now().getDayOfMonth() + 7);

        Borrow_Book bb = new Borrow_Book(borrow, borrowBook, LocalDate.now(), returnDay, "Đang mượn");

        Set<Borrow_Book> bbs = new HashSet<Borrow_Book>();
        bbs.add(bb);

        borrowBook.setBorrow_books(bbs);
        borrow.setBorrow_books(bbs);

        bbRepository.save(bb);
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bbs", bbRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "manageBook";
    }

    @RequestMapping(path = "/showBorrowForm/{bookID}", method = RequestMethod.GET)
    public String showBorrowForm(ModelMap modelMap, @PathVariable String bookID) {
        Book book = bookRepository.findById(bookID).get();
        modelMap.addAttribute("author", authorRepository.findById(book.getAuthorID()).get());
        modelMap.addAttribute("plc", plCompanyRepository.findById(book.getPlCompanyID()).get());
        modelMap.addAttribute("book", book);
        modelMap.addAttribute("customer", new Customer());
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "borrowForm";
    }

    @RequestMapping(path = "/return/{borrowID}", method = RequestMethod.POST)
    public String returnBook(ModelMap modelMap,
                             @PathVariable String borrowID) {

        Iterable<Borrow_Book> bbs = bbRepository.findAll();
        for (Borrow_Book borrow_book : bbs){
            if(borrow_book.getBorrowBookKey().getBorrowID().equalsIgnoreCase(borrowID) && borrow_book.getStatus().equalsIgnoreCase("Đang mượn")) {
                bbRepository.delete(borrow_book);
                break;
            }
        }
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bbs", bbRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookDTO());
        return "manageBook";
    }
}
