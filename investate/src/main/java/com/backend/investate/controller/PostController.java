package com.backend.investate.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.investate.enums.PropertyType;
import com.backend.investate.model.NotificationsForBroker;
import com.backend.investate.model.Post;
import com.backend.investate.model.Profile;
import com.backend.investate.services.NotificationServiceForClients;
import com.backend.investate.services.PostService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@CrossOrigin(origins = "*") // Adjust the origin as necessary
@RequestMapping("/api/posts")
public class PostController {
    /**
     * @author E Praveen Kumar
     */
    @Autowired
    private PostService postService;
    

    // for users
    @PostMapping("/create-post")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return ResponseEntity.status(201).body(createdPost);
    }

    // for clients
    @GetMapping("/getall-posts-for-sale") // Updated endpoint to get all posts for sale
    public ResponseEntity<List<Post>> getAllPostsForSale() {
        List<Post> posts = postService.getAllPostsForSale();
        return ResponseEntity.ok(posts);
    }

    // for clients
    @GetMapping("/getall-posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // for clients
    @PutMapping("/mark-sold/{postId}")
    public ResponseEntity<Post> markPostAsSold(@PathVariable Long postId) {
        return postService.markAsSold(postId)
                .map(post -> ResponseEntity.ok(post))
                .orElse(ResponseEntity.notFound().build());
    }

    // for clients
    @GetMapping("/getall-posts-sold")
    public ResponseEntity<List<Post>> getAllSoldPosts() {
        List<Post> soldPosts = postService.getAllSoldPosts();
        return ResponseEntity.ok(soldPosts);
    }

    


    @PutMapping("update-post/{postId}")
    public ResponseEntity<String> updatePostDetails(@PathVariable Long postId, @RequestBody Post postDetails) {
        try {
            postService.updatePost(postId, postDetails);
            return ResponseEntity.ok("Post details updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Post update failed: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<Post> searchPosts(@RequestParam String keyword) {
        return postService.searchPostsByKeyword(keyword);
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok("Post deleted successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body("Post not found.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to delete post: " + e.getMessage());
        }
    }

    
     // Endpoint for uploading an image
    @PostMapping("/{postId}/uploadImage")
    public ResponseEntity<Post> uploadImage(@PathVariable Long postId, @RequestParam("image") MultipartFile file) {
        try {
            Post post = postService.findPostById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            // Convert the file to byte array
            post.setImage(file.getBytes()+""); // Directly setting the image as byte[]
            postService.updatePost(postId, post); // Update the post

            return ResponseEntity.ok(post);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   
    @GetMapping("/search-by-keyword/{keyword}")
    public List<Post> getPostsByKeyword(@PathVariable String keyword) {
        return postService.getPostsByKeyword(keyword);
    }
    @GetMapping("/get-broker/{brokerName}")
    public List<Post> getPostsByBroker(@PathVariable String brokerName) {
        return postService.getPostsByBrokerName(brokerName);
    }


    @GetMapping("/get-post-by-property-type")
    public ResponseEntity<List<Post>> getPostsByPropertyType(@RequestParam PropertyType propertyType) {
        List<Post> posts = postService.findPostsByPropertyType(propertyType);
        return ResponseEntity.ok(posts);
    }


    


    
}
