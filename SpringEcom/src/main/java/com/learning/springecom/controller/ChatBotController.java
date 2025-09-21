package com.learning.springecom.controller;

import com.learning.springecom.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatBotController {
@Autowired
private ChatBotService chatBotService;
    @GetMapping("/ask")
    public ResponseEntity<String> askBot(@RequestParam String message){
        return new ResponseEntity<> (chatBotService.ChatBotResponse(message), HttpStatus.OK);

    }
}
