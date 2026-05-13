package com.library.library_app.repository;

import com.library.library_app.model.BorrowRecord;
import com.library.library_app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByUser(User user);
    List<BorrowRecord> findByUserAndStatus(User user, String status);
}