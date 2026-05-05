package com.congty9a4.backend.dto.req;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ResendEmailRequest {
    private String from;
    private List<String> to;
    private String subject;
    private String html;
    private String text;
}
