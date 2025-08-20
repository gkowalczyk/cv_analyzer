package com.example.cv_analyzer.controller;

import com.example.cv_analyzer.domain.JobOffer;
import com.example.cv_analyzer.service.ChatService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(value = "message") String message, HttpSession httpSession) {
        try {
            String result = chatService.chat(message);
            httpSession.setAttribute("chatResponse", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error" + e.getMessage());
        }
    }

    @PostMapping("/chat/save")
    public ResponseEntity<List<JobOffer>> saveToDb(HttpSession session) {
        String result = (String) session.getAttribute("chatResponse");
        return ResponseEntity.ok(chatService.saveDataToDb(result));
    }
}
