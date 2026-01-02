package com.congty9a4.backend.entity.relational;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "userchans",
    indexes = {
        @Index(name = "idx_userchan_email", columnList = "email", unique = true)
    }
)
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

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
    @Column(name = "phone")
    String phoneNumber;

    @Column(name = "is_active", columnDefinition = "boolean default true")
    boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

}
