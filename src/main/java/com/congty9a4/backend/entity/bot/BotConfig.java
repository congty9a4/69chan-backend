package com.congty9a4.backend.entity.bot;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Document("botConfig")
public class BotConfig {
    @Id
    String id;

    Set<String> sourceSubreddits;

    @Builder.Default
    int activeStartHour = 8;

    @Builder.Default
    int activeEndHour = 22;

    @Builder.Default
    int postsPerDay = 2;

    @Builder.Default
    boolean isActive = true;

}
