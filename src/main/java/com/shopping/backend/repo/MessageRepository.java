package com.shopping.backend.repo;

import com.shopping.backend.model.Message;
import com.shopping.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    /** Method 1: Find conversation using User objects */
    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender = :user1 AND m.receiver = :user2)
           OR (m.sender = :user2 AND m.receiver = :user1)
        ORDER BY m.sentAt ASC
    """)
    List<Message> findConversationByUsers(
            @Param("user1") User user1,
            @Param("user2") User user2
    );

    /** Method 2: Find conversation using email strings */
    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender.email = :email1 AND m.receiver.email = :email2)
           OR (m.sender.email = :email2 AND m.receiver.email = :email1)
        ORDER BY m.sentAt ASC
    """)
    List<Message> findConversationByEmails(
            @Param("email1") String email1,
            @Param("email2") String email2
    );
}
