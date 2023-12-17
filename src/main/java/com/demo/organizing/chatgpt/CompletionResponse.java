package com.demo.organizing.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CompletionResponse {
    private Usage usage;
    private List<Choice> choices;

    public Optional<String> firstAnswer() {
        if (choices == null || choices.isEmpty())
            return Optional.empty();
        return Optional.of(choices.get(0).text);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Usage {
        private int totalTokens;
        private int promptTokens;
        private int completionTokens;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private String text;
    }
}
