package com.example.cv_analyzer.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChatController {

    private final ChatModel chatModel;

    public ChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message") String message) {
        UserMessage userMessage = new UserMessage(message);

        ChatResponse response = chatModel.call(new Prompt(List.of(userMessage),
                OpenAiChatOptions
                        .builder()
                        .withFunction("getMyCv")
                        .withFunction("getJobsOffers")
                        .build()));
        return response.getResult().getOutput().getContent();
    }
}
