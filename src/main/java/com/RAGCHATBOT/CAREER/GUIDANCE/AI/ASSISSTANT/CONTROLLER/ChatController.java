package com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.CONTROLLER;

import com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.SERVICE.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat")
    public String chat(@RequestBody String userInput) {
        return chatService.getResponse(userInput);
    }
}