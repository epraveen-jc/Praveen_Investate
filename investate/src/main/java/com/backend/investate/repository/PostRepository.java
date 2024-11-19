package com.backend.investate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.investate.enums.PropertyType;
import com.backend.investate.model.Post;
/**
 * @author E Praveen Kumar
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> findByIsForSaleTrueAndIsSoldFalseOrderByCreatedAtDesc(); // Fetch unsold posts for sale
    List<Post> findByIsSoldTrueOrderByCreatedAtDesc(); // Fetch sold posts
    // Find posts by broker name
    List<Post> findByBrokerName(String brokerName);

    // Find posts containing a specific keyword
    List<Post> findByKeyWordsContaining(String keyword);

     // New method to find posts by property type
    List<Post> findByPropertyType(PropertyType propertyType);

    
   

    
}
