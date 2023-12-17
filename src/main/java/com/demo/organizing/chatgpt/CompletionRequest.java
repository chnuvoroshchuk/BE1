package com.demo.organizing.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CompletionRequest {
    protected String model;
    protected String prompt;
    protected double temperature;
    @JsonProperty("max_tokens")
    protected int maxTokens;

    protected static CompletionRequest defaultWith(String prompt) {
        return new CompletionRequest("text-davinci-003", prompt, 0.1, 1000);
    }
}
