package com.example.LibManager.controller;

import com.example.LibManager.models.Book;
import com.example.LibManager.models.BookSearchDTO;
import com.example.LibManager.models.Borrow;
import com.example.LibManager.models.Customer;
import com.example.LibManager.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(path = "customer")
public class CustomerController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PlCompanyRepository plCompanyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    public int size(Iterable<?> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection<?>) iterable).size();
        }
        return 0;
    }

    public String genID() {
        Iterable<Borrow> borrows = borrowRepository.findAll();
        ArrayList<String> IDs = new ArrayList<String>();
        for (Borrow b:borrows) {
            IDs.add(b.getBorrowID().replaceAll("BR", ""));
        }
        return "BR" + (String.format("%03d", IDs.stream().mapToLong(Long::parseLong).max().orElse(0L) + 1));
    }

    @RequestMapping(value = "/addCustomer/{bookID}", method = RequestMethod.POST)
    public String addCustomer(ModelMap modelMap,
                              @Valid @ModelAttribute("customer") Customer customer,
                              BindingResult bindingResult,
                              @PathVariable String bookID) {
        if(bindingResult.hasErrors()) {
            Book book = bookRepository.findById(bookID).get();
            modelMap.addAttribute("author", authorRepository.findById(book.getAuthor().getAuthorID()).get());
            modelMap.addAttribute("plc", plCompanyRepository.findById(book.getPlCompany().getPlCompanyID()).get());
            modelMap.addAttribute("book", book);
            modelMap.addAttribute("customer", new Customer());
            modelMap.addAttribute("bookDTO", new BookSearchDTO());
            return "borrowForm";
        }else {
            Borrow borrow = new Borrow("None");
            borrow.setBorrowID(genID());

            Set<Borrow> borrows = new HashSet<Borrow>();
            borrows.add(borrow);

            borrow.setCustomer(customer);
            customer.setBorrows(borrows);

            customerRepository.save(customer);

            Book book = bookRepository.findById(bookID).get();
            modelMap.addAttribute("author", authorRepository.findById(book.getAuthor().getAuthorID()).get());
            modelMap.addAttribute("plc", plCompanyRepository.findById(book.getPlCompany().getPlCompanyID()).get());
            modelMap.addAttribute("book", book);
            modelMap.addAttribute("customer", customer);
            modelMap.addAttribute("bookDTO", new BookSearchDTO());
            return "borrowForm";
        }
    }
}
