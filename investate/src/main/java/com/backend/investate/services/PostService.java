package com.backend.investate.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.backend.investate.enums.PropertyType;
import com.backend.investate.model.Post;
import com.backend.investate.repository.PostRepository;

/**
 * @author E Praveen Kumar
 */
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
 

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // Newly added method to find post by ID
    public Optional<Post> findPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getAllPostsForSale() {
        return postRepository.findByIsForSaleTrueAndIsSoldFalseOrderByCreatedAtDesc();
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll(); // Assuming you're using JPA or similar
    }

    public Optional<Post> markAsSold(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setIsSold(true); // Mark the post as sold
            post.setIsForSale(false); // Optionally, mark as not for sale
            postRepository.save(post);
            return Optional.of(post);
        }
        return Optional.empty(); // Return empty if post not found
    }

    public List<Post> getAllSoldPosts() {
        return postRepository.findByIsSoldTrueOrderByCreatedAtDesc();
    }

    public List<Post> searchPostsByKeyword(String keyword) {
        // Fetch all posts
        List<Post> allPosts = postRepository.findAll();
        List<Post> filteredPosts = new ArrayList<>();
        
        
        
        return filteredPosts;
    }
    public Post getPostById(Long id) {
        Optional<Post> postOptional = postRepository.findById(id);
        return postOptional.orElse(null); 
    }
     // Find posts by broker name
     public List<Post> getPostsByBrokerName(String brokerName) {
        return postRepository.findByBrokerName(brokerName);
    }

    // Find posts by keywords
    public List<Post> getPostsByKeyword(String keyword) {
        return postRepository.findByKeyWordsContaining(keyword);
    }

    
    public void updatePost(Long postId, Post postDetails) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setBrokerName(postDetails.getBrokerName());
        post.setPhoneNumber(postDetails.getPhoneNumber());
        post.setTitle(postDetails.getTitle());
        post.setImage(postDetails.getImage());
        post.setState(postDetails.getState());
        post.setDistrict(postDetails.getDistrict());
        post.setTotalPrice(postDetails.getTotalPrice());
        post.setGeolocation(postDetails.getGeolocation());
        post.setDescription(postDetails.getDescription());
        post.setPricePerSqrFeet(postDetails.getPricePerSqrFeet());
        post.setTotalSqrFeet(postDetails.getTotalSqrFeet());
        post.setIsForSale(postDetails.getIsForSale());
        post.setIsSold(postDetails.getIsSold());
        post.setPropertyType(postDetails.getPropertyType());
        post.setKeyWords(postDetails.getKeyWords());
        post.setStreetOrColony(postDetails.getStreetOrColony());
        postRepository.save(post);
        
    }




     public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found."));
        postRepository.delete(post);
    }



     public List<Post> findPostsByPropertyType(PropertyType propertyType) {
        return postRepository.findByPropertyType(propertyType);
    }
    
    
}
