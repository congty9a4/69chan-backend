package com.congty9a4.backend.listener;

import com.congty9a4.backend.entity.Message;
import com.congty9a4.backend.util.SnowflakeIdGenerator;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class MessageMongoListener extends AbstractMongoEventListener<Message> {

    private final SnowflakeIdGenerator snowflakeIdGenerator;

    public MessageMongoListener(SnowflakeIdGenerator snowflakeIdGenerator) {
        this.snowflakeIdGenerator = snowflakeIdGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Message> event) {
        Message message = event.getSource();
        if (message.getId() == null) {
            message.setId(snowflakeIdGenerator.nextId());
        }
    }
}

