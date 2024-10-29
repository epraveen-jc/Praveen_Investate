package com.backend.investate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.investate.model.Post;
/**
 * @author E Praveen Kumar
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByIsForSaleTrueAndIsSoldFalseOrderByCreatedAtDesc(); // Fetch unsold posts for sale
    List<Post> findByIsSoldTrueOrderByCreatedAtDesc(); // Fetch sold posts
}
