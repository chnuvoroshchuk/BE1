package com.demo.organizing.chatgpt;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenAiApiClient {
    WebClient webClient = WebClient.builder().baseUrl("https://api.openai.com/v1")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + "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").build();

    public String postToOpenAiApi(String requestBodyAsJson) {
        return webClient.post().uri("/completions").contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(CompletionRequest.defaultWith(requestBodyAsJson)))
                .retrieve().bodyToMono(CompletionResponse.class)
                .blockOptional()
                .flatMap(CompletionResponse::firstAnswer).orElse("");
    }
}
