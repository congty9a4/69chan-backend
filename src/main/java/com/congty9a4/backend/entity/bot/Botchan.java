package com.congty9a4.backend.entity.bot;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.OffsetDateTime;

import static lombok.AccessLevel.*;

// Botchans will act as real users to seed content for feeds
@Getter
@Setter
@SuperBuilder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Table(name = "botchans")
public class Botchan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String displayName;

    @Column(name = "bot_config_id", nullable = false)
    String botConfigId;

    @URL
    @Column(name = "avatar_url")
    String avatarUrl;

    @Builder.Default
    @Column(name = "is_active", columnDefinition = "boolean default true")
    boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "bot_type", nullable = false)
    BotType botType;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic", nullable = false)
    BotTopic botTopic;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt;
}




