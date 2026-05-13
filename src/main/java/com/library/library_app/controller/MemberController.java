package com.library.library_app.controller;

import com.library.library_app.model.User;
import com.library.library_app.service.BookService;
import com.library.library_app.service.BorrowService;
import com.library.library_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private UserService userService;

    private User getLoggedInUser(Authentication auth) {
        return userService.findByEmail(auth.getName()).orElseThrow();
    }

    @GetMapping("/home")
    public String memberHome(Model model, Authentication auth) {
        User user = getLoggedInUser(auth);
        model.addAttribute("user", user);
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("history", borrowService.getUserHistory(user));
        return "member/home";
    }

    @GetMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId, Authentication auth) {
        User user = getLoggedInUser(auth);
        borrowService.borrowBook(user, bookId);
        return "redirect:/member/home";
    }

    @GetMapping("/return/{recordId}")
    public String returnBook(@PathVariable Long recordId) {
        borrowService.returnBook(recordId);
        return "redirect:/member/home";
    }
}