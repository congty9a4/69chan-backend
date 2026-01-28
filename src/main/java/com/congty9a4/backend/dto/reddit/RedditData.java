package com.congty9a4.backend.dto.reddit;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedditData {
    String after;
    String before;
    List<RedditChild> children;
}

