package com.chatserver.chatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManagerFactory;

import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ChatserverApplication {

	private static EntityManagerFactory ENTITY_MANAGE_FACTORY;

	public static void main(String[] args) {
		ENTITY_MANAGE_FACTORY = createEntityManagerFactory();
		SpringApplication.run(ChatserverApplication.class, args);
	}

	public static EntityManagerFactory createEntityManagerFactory(){
		Map<String, String> env = System.getenv();
		Map<String, Object> configOverrides = new HashMap<String, Object>();
		configOverrides.put("javax.persistence.jdbc.user", env.get("CHATROOM_DB_USER"));
		configOverrides.put("javax.persistence.jdbc.password", env.get("CHATROOM_DB_PASSWORD"));
		return Persistence.createEntityManagerFactory("chatserver", configOverrides);
	}

	public static EntityManagerFactory getEntityManagerFactory(){
		return ENTITY_MANAGE_FACTORY;
	}

	public static void closeEntityManagerFactory(){
		ENTITY_MANAGE_FACTORY.close();
	}

}
