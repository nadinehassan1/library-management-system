package com.library.library_app.service;

import com.library.library_app.model.Book;
import com.library.library_app.model.BorrowRecord;
import com.library.library_app.model.User;
import com.library.library_app.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowService {

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Autowired
    private BookService bookService;

    public BorrowRecord borrowBook(User user, Long bookId) {
        Book book = bookService.getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available");
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookService.saveBook(book);

        BorrowRecord record = new BorrowRecord();
        record.setUser(user);
        record.setBook(book);
        return borrowRecordRepository.save(record);
    }

    public BorrowRecord returnBook(Long recordId) {
        BorrowRecord record = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found"));
        record.setStatus("RETURNED");
        record.setReturnDate(LocalDateTime.now());

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookService.saveBook(book);

        return borrowRecordRepository.save(record);
    }

    public List<BorrowRecord> getUserHistory(User user) {
        return borrowRecordRepository.findByUser(user);
    }

    public List<BorrowRecord> getAllRecords() {
        return borrowRecordRepository.findAll();
    }
}