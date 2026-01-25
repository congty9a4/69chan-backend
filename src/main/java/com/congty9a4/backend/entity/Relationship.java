package com.congty9a4.backend.entity;

import com.congty9a4.backend.entity.enums.FriendStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "relationships", indexes = {
    @Index(name = "idx_relation_object", columnList = "objectName, objectId, relation"),
    @Index(name = "idx_subject", columnList = "subjectName, subjectId")})
public class Relationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    // source subject such as: "user", "group", "post", etc.)
    String objectName;

    String objectId;

    // "viewer", "member", "owner", etc.
    String relation;

    // target subject such as: "user", "group", "post", etc.)
    String subjectName;

    String subjectId;

    // 'member' -> group ; 'follower' -> user
    String subjectRelation;

}
