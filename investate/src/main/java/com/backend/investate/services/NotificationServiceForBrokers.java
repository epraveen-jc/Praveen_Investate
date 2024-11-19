// src/main/java/com/backend/investate/service/NotificationServiceForBrokers.java
package com.backend.investate.services;

import com.backend.investate.model.NotificationsForBroker;
import com.backend.investate.model.Post;
import com.backend.investate.repository.NotificationRepositoryForBrokers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class NotificationServiceForBrokers {

    @Autowired
    private NotificationRepositoryForBrokers notificationRepositoryForBrokers;
    @Autowired
    private PostService postService;

    @Autowired
    private ProfileService profileService;
    public NotificationsForBroker createNotification(Long postId, String clientName, String brokerName,
            String clientPhoneNumber) {
        NotificationsForBroker notification = new NotificationsForBroker();
        notification.setPostId(postId);
        notification.setClientName(clientName);
        notification.setBrokerName(brokerName);
        notification.setClientPhoneNumber(clientPhoneNumber);

        Optional<Post> post = postService.findPostById(postId);
        if (post != null) {
            String type = brokerName;
            if(profileService.findByName(brokerName).getProfileType().toString().equalsIgnoreCase("broker")){
                type = "Broker "+brokerName;
            }else{
                type = "Owner "+ brokerName;
            }
            notification.setMessage(
                    "Dear "+type+",\n" +
                            "We are pleased to inform you that client " + clientName
                            + " has shown interest in one of your posts.\n" +
                            "Post Details:\n" +
                            "Post ID: " + postId + "\n" +
                            "Title: " + post.get().getTitle() + "\n" +
                            "Property Type: " + post.get().getPropertyType() + "\n" +
                            "Location: \n" +
                            "   - State: " + post.get().getState() + "\n"+
                            "   - District: " + post.get().getDistrict() + "\n" +
                            "   - Colony/Street: " + post.get().getStreetOrColony() + "\n" +
                            "Price per Sq Ft: " + post.get().getPricePerSqrFeet() + "\n" +
                            "Total Area: " + post.get().getTotalSqrFeet() + " sq ft\n" +
                            "Total Price: " + post.get().getTotalPrice() + "\n" +
                            "Thank you for using our service!\n" +
                            "Best Regards,\n" +
                            "Investate Team\n");

        } else {
            notification.setMessage("Post Not Found To Send Notification");
        }

        return notificationRepositoryForBrokers.save(notification);
    }

    public List<NotificationsForBroker> getNotificationsForBroker(String brokerName) {
        System.out.println(notificationRepositoryForBrokers.findByBrokerNameOrderByCreatedDateDesc(brokerName));
        return notificationRepositoryForBrokers.findByBrokerNameOrderByCreatedDateDesc(brokerName);
    }

    public void deleteNotification(Long notificationId) {
        notificationRepositoryForBrokers.deleteById(notificationId);
    }

    public void acceptDeal(Long notificationId) {
        NotificationsForBroker notification = notificationRepositoryForBrokers.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException("Notification not found"));
        // Additional logic to handle the deal acceptance (e.g., notify client) can be
        // added here.
        notificationRepositoryForBrokers.save(notification);
    }
}
