package com.library.library_app.controller;

import com.library.library_app.model.Book;
import com.library.library_app.service.BookService;
import com.library.library_app.service.BorrowService;
import com.library.library_app.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String adminHome(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("records", borrowService.getAllRecords());
        model.addAttribute("users", userRepository.findAll());
        return "admin/home";
    }

    @GetMapping("/books/add")
    public String addBookPage(Model model) {
        model.addAttribute("book", new Book());
        return "admin/add-book";
    }

    @PostMapping("/books/add")
    public String addBook(@Valid @ModelAttribute("book") Book book,
                          BindingResult result) {
        if (result.hasErrors()) {
            return "admin/add-book";
        }
        book.setAvailableCopies(book.getTotalCopies());
        bookService.saveBook(book);
        return "redirect:/admin/home";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/home";
    }
}