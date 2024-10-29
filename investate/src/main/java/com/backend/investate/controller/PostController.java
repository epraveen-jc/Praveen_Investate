package com.backend.investate.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.investate.model.Notification;
import com.backend.investate.model.Post;
import com.backend.investate.model.Profile;
import com.backend.investate.services.NotificationService;
import com.backend.investate.services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private NotificationService notificationService;
    //for users
    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        Post createdPost = postService.createPost(post);
        return ResponseEntity.status(201).body(createdPost);
    }
    //for clients
    @GetMapping("/for-sale") // Updated endpoint to get all posts for sale
    public ResponseEntity<List<Post>> getAllPostsForSale() {
        List<Post> posts = postService.getAllPostsForSale();
        return ResponseEntity.ok(posts);
    }
    // for clients
    @GetMapping("/getall")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    //for clients
    @PutMapping("/{postId}/mark-sold")
    public ResponseEntity<Post> markPostAsSold(@PathVariable Long postId) {
        return postService.markAsSold(postId)
            .map(post -> ResponseEntity.ok(post))
            .orElse(ResponseEntity.notFound().build());
    }
//for clients
    @GetMapping("/sold")
    public ResponseEntity<List<Post>> getAllSoldPosts() {
        List<Post> soldPosts = postService.getAllSoldPosts();
        return ResponseEntity.ok(soldPosts);
    }

   

    // New endpoint to handle "Deal" action from the client & in frontend we should send clientprofile json
    @PostMapping("/{postId}/deal")
    public ResponseEntity<String> dealPost(@PathVariable Long postId, @RequestBody Profile clientProfile) {
        Post post = postService.findPostById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));
        
        notificationService.createNotification(postId, clientProfile.getName(), post.getAgentName());
        System.out.println(post.getAgentName());
        return ResponseEntity.ok("Notification sent to the agent.");
    }

    @GetMapping("/{agentName}/notifications")
    public ResponseEntity<List<Notification>> getAgentNotifications(@PathVariable String agentName) {
        List<Notification> notifications = notificationService.getNotificationsForAgent(agentName);
        return ResponseEntity.ok(notifications);
    }
}
