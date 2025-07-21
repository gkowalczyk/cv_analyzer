package com.example.cv_analyzer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatModel chatModel;
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "message") String message) {
        UserMessage userMessage = new UserMessage(message);

        ChatResponse response = chatModel.call(new Prompt(List.of(userMessage),
                OpenAiChatOptions
                        .builder()
                        .withFunction("getMyCv")
                        .withFunction("getOffersAdapter")
                        .build()));
        return ResponseEntity.ok(response.getResult().getOutput().getContent());
    }
}
