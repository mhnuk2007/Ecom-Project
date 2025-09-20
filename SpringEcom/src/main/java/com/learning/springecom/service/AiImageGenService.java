package com.learning.springecom.service;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.net.URL;

@Service
public class AiImageGenService {

    @Autowired
    private ImageModel imageModel;

    public byte[] generateImage(String imagePrompt) {
        OpenAiImageOptions options = OpenAiImageOptions.builder()
                .N(1)
                .width(1024)
                .height(1024)
                .quality("standard")
                .responseFormat("url")
                .model("dall-e-3")
                .build();

        ImageResponse response = imageModel.call(new ImagePrompt(imagePrompt, options));
        String imageUrl = response.getResult().getOutput().getUrl();
        try {
            return new URL(imageUrl).openStream().readAllBytes();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
