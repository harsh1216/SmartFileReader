package com.scm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.Document;
import com.scm.entities.User;

import java.util.List;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
	List<Document> findByUser(User user);
}
