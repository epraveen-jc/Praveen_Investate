package com.backend.investate.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
