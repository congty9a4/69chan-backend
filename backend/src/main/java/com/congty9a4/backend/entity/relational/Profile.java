package com.congty9a4.backend.entity.relational;

import com.congty9a4.backend.entity.relational.Userchan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "profiles",
    indexes = {
        @Index(name = "idx_profile_user", columnList = "user_id", unique = true)
    }
)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Userchan user;

    @Size(max = 255)
    @Column(name = "full_name")
    private String fullName;

    @Size(max = 500)
    String bio;

    @URL
    @Column(name = "avatar_url")
    String avatarUrl;

    @URL
    @Column(name = "cover_photo_url")
    String coverPhotoUrl;

    @Size(max = 255)
    @Column(name = "location")
    String location;

    @URL
    @Column(name = "website")
    String websiteUrl;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid birthday format, should be YYYY-MM-DD")
    @Column(name = "birth_date")
    LocalDate birthday;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

}
