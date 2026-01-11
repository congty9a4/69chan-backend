package com.congty9a4.backend.script;

import com.congty9a4.backend.constant.USER;
import com.congty9a4.backend.repository.jpa.UserRepository;
import com.congty9a4.backend.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class InitApp implements ApplicationRunner {


    private final UserService userService;
    private final UserRepository userRepository;

    public InitApp(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "org.postgresql.Driver"
    )
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        userRepository.findByEmail(USER.GUEST.EMAIL)
                        .orElseGet( () -> userRepository.save(USER.GUEST.toUserchan()));

    }
}
