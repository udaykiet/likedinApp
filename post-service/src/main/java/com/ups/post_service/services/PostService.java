package com.ups.post_service.services;

import com.ups.post_service.auth.UserContextHolder;
import com.ups.post_service.clients.ConnectionsClient;
import com.ups.post_service.dto.PersonDto;
import com.ups.post_service.dto.PostCreateRequestDto;
import com.ups.post_service.dto.PostDto;
import com.ups.post_service.entities.Post;
import com.ups.post_service.event.PostCreatedEvent;
import com.ups.post_service.exceptions.ResourceNotFoundException;
import com.ups.post_service.repositories.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ConnectionsClient connectionsClient;

    @Autowired
    private KafkaTemplate<Long , PostCreatedEvent> kafkaTemplate;


    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postCreateRequestDto , Post.class);
        post.setUserId(userId);
        Post savedPost = postRepository.save(post);

        //send notification to all the connections ... about the post

        //creating the event which needs to  send
        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .userId(userId)
                .content(savedPost.getContent())
                .build();


        kafkaTemplate.send("post-created-topic" , postCreatedEvent);



        return modelMapper.map(savedPost , PostDto.class);

    }

    public PostDto getPostById(Long id) {

        Long userId = UserContextHolder.getCurrentUserId();
        log.info("userId is : {}" , userId);

//        List<PersonDto> allFirstDegreeConnections = connectionsClient.getAllFirstDegreeConnections();
//        log.info(allFirstDegreeConnections.toString());

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post with not found with id: " + id));
        return modelMapper.map(post , PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        return posts
                .stream()
                .map(post -> modelMapper.map(post , PostDto.class))
                .collect(Collectors.toList());
    }
}
