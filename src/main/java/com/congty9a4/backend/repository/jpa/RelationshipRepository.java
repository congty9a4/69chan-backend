package com.congty9a4.backend.repository.jpa;

import com.congty9a4.backend.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Integer> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Relationship r WHERE r.objectId = :userId AND r.relation = 'follower' AND r.subjectId = :targetUserId")
    boolean checkIfAlreadyFollowed(@Param("userId") String userId, @Param("targetUserId") String targetUserId);

    @Modifying
    @Query("DELETE FROM Relationship r WHERE r.objectId = :userId AND r.relation = 'follower' AND r.subjectId = :targetUserId")
    void unfollow(@Param("userId") String userId, @Param("targetUserId") String targetUserId);

}
