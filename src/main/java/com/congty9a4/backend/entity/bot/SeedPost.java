package com.congty9a4.backend.entity.bot;


import com.congty9a4.backend.entity.enums.ExternalSource;
import com.congty9a4.backend.entity.post.Post;
import jakarta.validation.constraints.Max;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// posts crawled from external service to seed content

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("seed_posts")
public class SeedPost extends Post {

    // source platform of the original post, e.g., "REDDIT", "TWITTER"
    @Field("original_source")
    ExternalSource originalSource;

    @Field
    @Indexed(unique = true)
    @Max(50)
    String originalId;

    @Field
    @Max(500)
    @Indexed(unique = true)
    String originalUrl;

}
