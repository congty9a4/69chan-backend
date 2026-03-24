package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.annotation.TrackExecutionTime;
import com.congty9a4.backend.entity.Relationship;
import com.congty9a4.backend.entity.enums.Relation;
import com.congty9a4.backend.entity.enums.RelationEntity;
import com.congty9a4.backend.repository.jpa.RelationshipRepository;
import com.congty9a4.backend.service.RelationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RelationServiceImpl implements RelationService {

    RelationshipRepository relRepo;

    @TrackExecutionTime
    @Transactional
    @Override
    public void follow(String userId, String targetUserId) {
        if (isFollowing(userId, targetUserId)) {
            return; // Already following, no action needed
        }
        var newFollower = userToUser(userId, targetUserId, Relation.FOLLOWER);
        relRepo.save(newFollower);
    }

    @TrackExecutionTime
    @Override
    @Transactional
    public void unfollow(String userId, String targetUserId) {
        relRepo.unfollow(userId, targetUserId);
    }

    public boolean isFollowing(String userId, String targetUserId) {
        return relRepo.checkIfAlreadyFollowed(userId, targetUserId);
    }

    private Relationship userToUser(String userId, String targetUserId, Relation relation) {
        return Relationship.builder()
                .objectName(RelationEntity.user.name())
                .objectId(userId)
                .subjectName(RelationEntity.user.name())
                .subjectId(targetUserId)
                .relation(relation.toString().toLowerCase())
                .build();
    }

}
