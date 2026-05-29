package com.congty9a4.backend.constant;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public final class LOCALE {
    public static OffsetDateTime now() {
        return OffsetDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }
}
