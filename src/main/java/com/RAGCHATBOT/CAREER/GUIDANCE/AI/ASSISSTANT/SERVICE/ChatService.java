package com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.SERVICE;

import com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.ENTITY.Career;
import com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.REPOSITORY.CareerRepository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private CareerRepository careerRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    // 🔥 MEMORY (simple)
    private String userName = "";

    // =========================
    // 🔹 MAIN METHOD
    // =========================
    public String getResponse(String userInput) {

        String input = userInput.toLowerCase();

        // 🔥 MEMORY ACTIONS ONLY
        if (input.contains("my name is")) {
            return handleName(userInput);
        }

        if (input.contains("what is my name")) {
            return getUserName();
        }

        // 🔹 NORMAL AI FLOW (RAG)
        String context = buildContext();

        String prompt = """
You are an expert career guidance assistant.

Give answers in a CLEAN and PROFESSIONAL FORMAT.

STRICT FORMAT:

📌 Title

🎯 Skills Required:
• Skill 1
• Skill 2
• Skill 3

🛣 Roadmap:
1. Step 1
2. Step 2
3. Step 3

📚 Resources:
• Resource 1
• Resource 2

RULES:
- Use bullet points
- Use emojis
- Use short clear lines
- DO NOT write long paragraphs
- Make it visually clean for UI

Context:
%s

Question:
%s
""".formatted(context, userInput);

        return callAI(prompt);
    }

    // =========================
    // 🔥 MEMORY METHODS
    // =========================

    private String handleName(String input) {

        userName = input.substring(input.toLowerCase().indexOf("my name is") + 11).trim();

        return "Nice to meet you, " + userName + "!";
    }

    private String getUserName() {

        if (!userName.isEmpty()) {
            return "Your name is " + userName;
        } else {
            return "I don't know your name yet.";
        }
    }

    // =========================
    // 🔥 RAG CONTEXT (DB + TXT)
    // =========================

    private String buildContext() {

        StringBuilder context = new StringBuilder();

        List<Career> careers = careerRepository.findAll();

        for (Career c : careers) {

            context.append("Role: ").append(c.getRole()).append("\n")
                    .append("Skills: ").append(c.getSkillsRequired()).append("\n")
                    .append("Roadmap: ").append(c.getRoadmap()).append("\n")
                    .append("Resources: ").append(c.getResources()).append("\n\n");
        }

        context.append("General Information:\n");
        context.append(readTextFile());

        return context.toString();
    }

    // =========================
    // 🔹 READ TXT FILE
    // =========================

    private String readTextFile() {
        try {
            return new String(
                    java.nio.file.Files.readAllBytes(
                            java.nio.file.Paths.get("info.txt")
                    )
            );
        } catch (Exception e) {
            return "";
        }
    }

    // =========================
    // 🔹 CALL AI API
    // =========================

    private String callAI(String prompt) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String safePrompt = prompt.replace("\"", "\\\"");

            String body = """
            {
              "model": "meta-llama/llama-3-8b-instruct",
              "messages": [
                {"role": "user", "content": "%s"}
              ],
              "max_tokens": 300
            }
            """.formatted(safePrompt);

            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(apiUrl, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}