package com.congty9a4.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "userchans", indexes = {
        @Index(name = "idx_userchan_email", columnList = "email", unique = true)
})
public class Userchan {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false)
    String username;

    @NotBlank
    @Column(name = "password", nullable = false)
    String password;

    @NotBlank
    @Email
    @Column(nullable = false)
    String email;

    @Builder.Default
    @Column(name = "is_active", columnDefinition = "boolean default true")
    boolean isActive = true;

    @Builder.Default
    @Column(name = "is_verified", columnDefinition = "boolean default false", nullable = false)
    boolean isVerified = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Profile profile;

    @Column(name = "fts_document", columnDefinition = "tsvector", insertable = false)
    @Basic(fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    private String ftsDocument;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    public Infochan toInfochan() {
        Infochan newInfo = Infochan.builder()
                .username(this.username)
                .userId(this.id.toString())
                .build();

        if (profile != null) {
            newInfo.setKeyName(profile.getKeyName());
            newInfo.setProfilePicture(profile.getAvatarUrl());
        }

        return newInfo;
    }
}
