package com.example.LibManager.controller;

import com.example.LibManager.models.Book;
import com.example.LibManager.models.Borrow;
import com.example.LibManager.models.Customer;
import com.example.LibManager.repositories.*;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import org.hibernate.*;
import org.hibernate.cache.spi.CacheTransactionSynchronization;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.hibernate.engine.jdbc.spi.JdbcCoordinator;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.SelectionQuery;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaInsertSelect;
import org.hibernate.query.spi.QueryImplementor;
import org.hibernate.query.sql.spi.NativeQueryImplementor;
import org.hibernate.resource.jdbc.spi.JdbcSessionContext;
import org.hibernate.resource.transaction.spi.TransactionCoordinator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Stream;

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
        if(size(borrows) == 0) {
            return "BR" +(String.format("%03d", 1));
        }
        return "BR" +(String.format("%03d", size(borrows) + 1));
    }

    @RequestMapping(value = "/addCustomer/{bookID}", method = RequestMethod.POST)
    public String addCustomer(ModelMap modelMap,
                              @ModelAttribute("customer") Customer customer,
                              BindingResult bindingResult,
                              @PathVariable String bookID) {
        if(bindingResult.hasErrors() == true) {
            Book book = bookRepository.findById(bookID).get();
            modelMap.addAttribute("author", authorRepository.findById(book.getAuthorID()).get());
            modelMap.addAttribute("plc", plCompanyRepository.findById(book.getPlCompanyID()).get());
            modelMap.addAttribute("book", book);
            modelMap.addAttribute("customer", new Customer());
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
            modelMap.addAttribute("author", authorRepository.findById(book.getAuthorID()).get());
            modelMap.addAttribute("plc", plCompanyRepository.findById(book.getPlCompanyID()).get());
            modelMap.addAttribute("book", book);
            modelMap.addAttribute("customer", customer);
            return "borrowForm";
        }
    }
}
