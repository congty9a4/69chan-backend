package com.congty9a4.backend.entity;

import com.congty9a4.backend.annotation.profileTagName.ProfileTagName;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private Userchan user;

    // name for tagging
    @Size(min = 5, max = 255)
    @ProfileTagName
    @Column(name = "fullname", unique = true)
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

    @Column(name = "birth_date")
    LocalDate birthday;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    String phone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;


    private LocalDate fun(String date){
        return LocalDate.parse(date);
    }
}
