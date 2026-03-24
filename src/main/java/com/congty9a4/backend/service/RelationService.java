package com.congty9a4.backend.service;



public interface RelationService {

    void follow(String currentUserId, String targetUserId);

    void unfollow(String currentUserId, String targetUserId);

    boolean isFollowing(String userId, String targetUserId);
}
