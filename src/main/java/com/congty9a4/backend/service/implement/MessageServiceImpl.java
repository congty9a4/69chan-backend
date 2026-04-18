package com.congty9a4.backend.service.implement;

import com.congty9a4.backend.dto.req.message.MessageCreateRequest;
import com.congty9a4.backend.dto.req.message.MessageUpdateRequest;
import com.congty9a4.backend.dto.resp.MessageResponse;
import com.congty9a4.backend.entity.Message;
import com.congty9a4.backend.mapper.MessageMapper;
import com.congty9a4.backend.repository.mongo.MessageRepository;
import com.congty9a4.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class MessageServiceImpl implements MessageService {

    MessageRepository messageRepository;

    MessageMapper messageMapper;

    @Override
    @Async("taskExecutor")
    public CompletableFuture<MessageResponse> save(MessageCreateRequest request) {
        Message message = Message.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .content(request.getContent())
                .build();
        Message saved = messageRepository.save(message);
        return CompletableFuture.completedFuture(messageMapper.toMessageResponse(saved));
    }

    @Override
    public MessageResponse getById(Long id) {
        return messageRepository.findById(id)
                .map(messageMapper::toMessageResponse)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
    }

    @Override
    public List<MessageResponse> listByUser(String userId) {
        return messageRepository.findBySenderIdOrReceiverId(userId, userId)
                .stream().map(messageMapper::toMessageResponse).collect(Collectors.toList());
    }

    @Override
    public MessageResponse update(Long id, MessageUpdateRequest request) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        message.setContent(request.getContent());
        Message saved = messageRepository.save(message);
        return messageMapper.toMessageResponse(saved);
    }

    @Override
    public void delete(Long id) {
        messageRepository.deleteById(id);
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSenderId(),
                message.getReceiverId(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
