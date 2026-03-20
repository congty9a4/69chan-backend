package com.congty9a4.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "relationships",
    indexes = {
        @Index(name = "idx_subject_object", columnList = "objectId, relation, subjectId"),
        @Index(name = "idx_object_subject", columnList = "subjectId, relation, objectId")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_relationship_object_subject_relation",
            columnNames = {"objectId", "subjectId", "relation"}
        )
    }
    )
// object -> relation -> subject
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

    /**
     * The reciprocal relationship type from the subject's perspective.
     * Examples: 'member' for group relationships, 'follower' for user relationships.
     * This field helps establish bidirectional relationship semantics.
     */
    String subjectRelation;

}
