package com.chatserver.chatserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManagerFactory;

import javax.persistence.Persistence;

@SpringBootApplication
public class ChatserverApplication {

	private static EntityManagerFactory ENTITY_MANAGE_FACTORY = Persistence
			.createEntityManagerFactory("chatserver");


	public static void main(String[] args) {
		System.out.println();
		SpringApplication.run(ChatserverApplication.class, args);
	}

	public static EntityManagerFactory getEntityManagerFactory(){
		return ENTITY_MANAGE_FACTORY;
	}

	public static void closeEntityManagerFactory(){
		ENTITY_MANAGE_FACTORY.close();
	}

}
