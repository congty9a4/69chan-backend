package com.congty9a4.backend.service;


import java.util.Set;

public interface RelationService {

    void follow(String currentUserId, String targetUserId);

    void unfollow(String currentUserId, String targetUserId);

    boolean isFollowing(String userId, String targetUserId);

    Set<String> retrieveFollowees(String userId);
}
