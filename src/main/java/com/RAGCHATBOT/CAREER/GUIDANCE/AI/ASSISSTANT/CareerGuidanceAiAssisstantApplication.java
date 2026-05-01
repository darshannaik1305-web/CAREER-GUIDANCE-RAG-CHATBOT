package com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT;

import com.RAGCHATBOT.CAREER.GUIDANCE.AI.ASSISSTANT.SERVICE.ChatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CareerGuidanceAiAssisstantApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerGuidanceAiAssisstantApplication.class, args);
	}
	//@Bean
	//CommandLineRunner run(ChatService chatService) {
	//	return args -> {
	//		chatService.generateAndStoreEmbeddings();
	//	};
	//}

}
