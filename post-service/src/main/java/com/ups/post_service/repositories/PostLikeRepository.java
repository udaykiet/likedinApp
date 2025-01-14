package com.ups.post_service.repositories;

import com.ups.post_service.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike , Long> {

    boolean existsByPostIdAndUserId(Long postId , Long userId);

    @Transactional
    void deleteByPostIdAndUserId(Long postId, Long userId);

}
