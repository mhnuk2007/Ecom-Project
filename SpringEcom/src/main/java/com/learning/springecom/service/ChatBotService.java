package com.learning.springecom.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatBotService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private VectorStore vectorStore;  // Changed from PgVectorStore to VectorStore interface

    @Autowired
    private ChatClient chatClient;

    public String ChatBotResponse(String userQuery) {
        try {
            System.out.println("[CHATBOT] Received query: " + userQuery);

            String promptStringTemplate = Files.readString(
                    resourceLoader.getResource("classpath:prompts/chatbot-rag-prompt.st")
                            .getFile()
                            .toPath()
            );

            // Fetch semantic context with enhanced logging
            String context = fetchSemanticContext(userQuery);

            if (context.isEmpty()) {
                System.out.println("[CHATBOT] No relevant context found for query: " + userQuery);
                // Try with lower similarity threshold
                context = fetchSemanticContextWithLowerThreshold(userQuery);
            }

            Map<String, Object> variables = new HashMap<>();
            variables.put("userQuery", userQuery);
            variables.put("context", context);

            PromptTemplate promptTemplate = PromptTemplate.builder()
                    .template(promptStringTemplate)
                    .variables(variables)
                    .build();

            String response = chatClient.prompt(promptTemplate.create()).call().content();
            System.out.println("[CHATBOT] Generated response: " + response);

            return response;

        } catch (IOException e) {
            System.err.println("[CHATBOT] Failed to load prompt template: " + e.getMessage());
            e.printStackTrace();
            return "Bot failed to load template: " + e.getMessage();
        } catch (Exception e) {
            System.err.println("[CHATBOT] Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return "Bot encountered an error: " + e.getMessage();
        }
    }

    private String fetchSemanticContext(String userQuery) {
        System.out.println("[CHATBOT] Searching vector store for: " + userQuery);

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuery)
                        .topK(5)
                        .similarityThreshold(0.7f)
                        .build()
        );

        System.out.println("[CHATBOT] Found " + documents.size() + " relevant documents");

        StringBuilder context = new StringBuilder();
        for (Document document : documents) {
            System.out.println("[CHATBOT] Document ID: " + document.getId());
            System.out.println("[CHATBOT] Document Metadata: " + document.getMetadata());
            System.out.println("[CHATBOT] Document Content Preview: " +
                    document.getFormattedContent().substring(0, Math.min(200, document.getFormattedContent().length())));

            context.append(document.getFormattedContent()).append("\n\n");
        }

        System.out.println("[CHATBOT] Total context length: " + context.length() + " characters");
        return context.toString();
    }

    private String fetchSemanticContextWithLowerThreshold(String userQuery) {
        System.out.println("[CHATBOT] Trying with lower similarity threshold for: " + userQuery);

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userQuery)
                        .topK(10)  // Increased topK
                        .similarityThreshold(0.3f)  // Lower threshold
                        .build()
        );

        System.out.println("[CHATBOT] Found " + documents.size() + " documents with lower threshold");

        StringBuilder context = new StringBuilder();
        for (Document document : documents) {
            context.append(document.getFormattedContent()).append("\n\n");
        }

        return context.toString();
    }

    // Optional: Method to test vector store content
    public void debugVectorStoreContent(String query) {
        System.out.println("\n=== VECTOR STORE DEBUG ===");
        System.out.println("Query: " + query);

        // Try different similarity thresholds
        float[] thresholds = {0.9f, 0.7f, 0.5f, 0.3f, 0.1f};

        for (float threshold : thresholds) {
            List<Document> documents = vectorStore.similaritySearch(
                    SearchRequest.builder()
                            .query(query)
                            .topK(3)
                            .similarityThreshold(threshold)
                            .build()
            );

            System.out.println("\nThreshold " + threshold + ": Found " + documents.size() + " documents");
            for (Document doc : documents) {
                System.out.println("  - Type: " + doc.getMetadata().get("type"));
                System.out.println("    ID: " + doc.getId());
                if (doc.getMetadata().containsKey("orderId")) {
                    System.out.println("    Order ID: " + doc.getMetadata().get("orderId"));
                }
                if (doc.getMetadata().containsKey("productId")) {
                    System.out.println("    Product ID: " + doc.getMetadata().get("productId"));
                }
            }
        }
        System.out.println("=========================\n");
    }
}