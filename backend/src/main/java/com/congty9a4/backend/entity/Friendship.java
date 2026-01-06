package com.congty9a4.backend.entity;

import com.congty9a4.backend.entity.enums.FriendStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "send_from", nullable = false)
    Userchan sendFrom;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "send_to", nullable = false)
    Userchan sendTo;

    @NotNull
    @Enumerated
    @Builder.Default
    @Column(name = "status", nullable = false)
    FriendStatus status = FriendStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

}
