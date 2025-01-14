package com.ups.post_service.services;

import com.ups.post_service.auth.UserContextHolder;
import com.ups.post_service.entities.Post;
import com.ups.post_service.entities.PostLike;
import com.ups.post_service.event.PostLikedEvent;
import com.ups.post_service.exceptions.BadRequestException;
import com.ups.post_service.exceptions.ResourceNotFoundException;
import com.ups.post_service.repositories.PostLikeRepository;
import com.ups.post_service.repositories.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private KafkaTemplate<Long , PostLikedEvent> kafkaTemplate;


    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("attempting to like the post id:{}" , postId);

        Post post  = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post with id: " + postId + " not found"));

        boolean alreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId , userId);
        if(alreadyLiked) throw new BadRequestException("already liked post");

        PostLike postLike = PostLike.builder()
                .postId(postId)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .build();

        postLikeRepository.save(postLike);
        log.info("liked successfully id:{}" , postId);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .likedByUserId(userId)
                .creatorId(post.getUserId())
                .postId(postId)
                .build();

        kafkaTemplate.send("post-liked-topic" , postId, postLikedEvent);
    }



    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("attempting to unlike the post id:{}" , postId);
        boolean exists = postRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("post with id: " + postId + " not found");

        boolean alreadyLiked = postLikeRepository.existsByPostIdAndUserId(postId , userId);
        if(!alreadyLiked) throw new BadRequestException("cannot unlike a post which is not liked");


        postLikeRepository.deleteByPostIdAndUserId(postId , userId);

        log.info("unliked successfully id:{}" , postId);
    }
}
