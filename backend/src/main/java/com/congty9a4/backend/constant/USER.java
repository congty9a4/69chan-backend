package com.congty9a4.backend.constant;

import com.congty9a4.backend.entity.Userchan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class USER {
    public class GUEST {
        public static final String EMAIL = "guest@69chan.com";
        public static final String USERNAME = "guest";
        public static final String PASSWORD = "guest123";

        public static Userchan toUserchan() {
            return Userchan.builder()
                    .password(new BCryptPasswordEncoder().encode(PASSWORD))
                    .email(EMAIL)
                    .username(USERNAME).build();
        }
    }
}
