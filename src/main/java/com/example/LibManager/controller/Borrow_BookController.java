package com.example.LibManager.controller;

import com.example.LibManager.models.*;
import com.example.LibManager.repositories.*;
import com.example.LibManager.services.LengthOfMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping(path = "bb")
@RequiredArgsConstructor
public class Borrow_BookController {

    public static double sum = 0;

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

    public int size(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection<?>) iterable).size();
        }
        return 0;
    }

    @RequestMapping(path = "/addBB/{bookID}", method = RequestMethod.POST)
    public String addBB(ModelMap modelMap,
                      @PathVariable String bookID,
                      @ModelAttribute("customer") Customer customer,
                      BindingResult bindingResult) {

        Customer foundCustomer = new Customer();

        if (customerRepository.findByCustomerName(customer.getCustomerName()).isPresent()) {
            foundCustomer = customerRepository.findByCustomerName(customer.getCustomerName()).get();
        }else {
            return "redirect:/bb/showBorrowForm/" + bookID;
        }

        Book borrowBook = bookRepository.findById(bookID).get();
        Borrow borrow = borrowRepository.findByCustomer(foundCustomer).get();

        // thời gian mượn sách không quá 7 ngày
        LocalDate returnDay = null;
        int lom = LengthOfMonth.LOM(LocalDate.now().getYear(), LocalDate.now().getMonth().getValue());

        if(LocalDate.now().getDayOfMonth() >= 25) {
            returnDay = LocalDate.of(LocalDate.now().getYear(),
                    LocalDate.now().getMonthValue() + 1,
                    (LocalDate.now().getDayOfMonth() + 7) - lom);
        }

        Borrow_Book bb = new Borrow_Book(borrow, borrowBook, LocalDate.now(), returnDay, borrowBook.getBookPrice() * 10 / 100,"Đang mượn");

        Set<Borrow_Book> bbs = new HashSet<Borrow_Book>();
        bbs.add(bb);

        borrowBook.setBorrow_books(bbs);
        borrow.setBorrow_books(bbs);

        bbRepository.save(bb);
        //Iterable<Borrow_Book> bbs = bBRepository.findAll();
        for (Borrow_Book b: bbs) {
            if(LocalDate.now().equals(bb.getBorrowDay())) {
                sum += bb.getBorrowFee();
            }
        }
        modelMap.addAttribute("sum", sum);
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bbs", bbRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
        return "manageBook";
    }

    @RequestMapping(path = "/showBorrowForm/{bookID}", method = RequestMethod.GET)
    public String showBorrowForm(ModelMap modelMap, @PathVariable String bookID) {
        Book book = bookRepository.findById(bookID).get();
        modelMap.addAttribute("author", authorRepository.findById(book.getAuthor().getAuthorID()).get());
        modelMap.addAttribute("plc", plCompanyRepository.findById(book.getPlCompany().getPlCompanyID()).get());
        modelMap.addAttribute("book", book);
        modelMap.addAttribute("customer", new Customer());
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
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
        modelMap.addAttribute("sum", sum);
        modelMap.addAttribute("books", bookRepository.findAll());
        modelMap.addAttribute("bbs", bbRepository.findAll());
        modelMap.addAttribute("bookDTO", new BookSearchDTO());
        return "manageBook";
    }
}
