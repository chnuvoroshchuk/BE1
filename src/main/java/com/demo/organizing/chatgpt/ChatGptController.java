package com.demo.organizing.chatgpt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatGptController {
    private final OpenAiApiClient openAiApiClient;

    @GetMapping("/api/chat")
    public ResponseEntity<String> getAnswer(@RequestParam String prompt) {
        return ResponseEntity.ok().body(openAiApiClient.postToOpenAiApi(prompt));
    }
}
