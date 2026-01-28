package com.congty9a4.backend.dto.reddit;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedditChild {
    String kind;
    RedditPost data;
}

