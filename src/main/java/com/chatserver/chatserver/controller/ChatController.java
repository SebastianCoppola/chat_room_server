package com.chatserver.chatserver.controller;

import com.chatserver.chatserver.ChatserverApplication;
import com.chatserver.chatserver.model.Message;
import com.chatserver.chatserver.model.Status;
import com.chatserver.chatserver.model.User;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NonUniqueResultException;
 import javax.persistence.TypedQuery;
import java.util.List;

@Controller
public class ChatController {

    @MessageMapping("/login")
    @SendTo("/chatroom")
    private Message login(@Payload User user){
        User newUser = addUser(user.getName());
        Message message = new Message();
        message.setUser(newUser);
        message.setMessage(newUser.getName() + " has joined the Chatroom.");
        message.setStatus(Status.JOIN);
        message.setUsers(getAllUsers());
        return message;
    }

    @MessageMapping("/logout")
    @SendTo("/chatroom")
    private Message logout(@Payload User user){
        User deletedUser = deleteUser(user.getId());
        Message message = new Message();
        message.setUser(deletedUser);
        message.setMessage(deletedUser.getName() + " left the Chatroom.");
        message.setStatus(Status.LEAVE);
        message.setUsers(getAllUsers());
        return message;
    }

    @MessageMapping("/message")
    @SendTo("/chatroom")
    private Message receivePublicMessage(@Payload Message message){
        return message;
    }

    /*CREATE USER*/
    public static User addUser(String name){
        EntityManager em = ChatserverApplication.getEntityManagerFactory()
                .createEntityManager();
        EntityTransaction et = null;
        User user = new User();
        try{
            et = em.getTransaction();
            et.begin();
            user.setName(name);
            em.persist(user);
            em.getTransaction().commit();
        }catch(Exception ex){
            if(et != null){
                et.rollback();
            }
            ex.printStackTrace();
        }finally{
            em.close();
            return user;
        }
    }

    /*GET USER*/
    public static void getUser(int id){
        EntityManager em = ChatserverApplication.getEntityManagerFactory()
                .createEntityManager();
        String query = "SELECT u FROM User u WHERE u.id = :id";
        TypedQuery<User> tq = em.createQuery(query,User.class);
        tq.setParameter("id", id);
        User user = null;
        try{
            user = tq.getSingleResult();
            System.out.println(user.getName());
        }catch(NonUniqueResultException ex){
            ex.printStackTrace();
        }finally{
            em.close();
        }
    }

    /*GET ALL USERS*/
    public static List<User> getAllUsers(){
        EntityManager em = ChatserverApplication.getEntityManagerFactory()
                .createEntityManager();
        String query = "SELECT u FROM User u";
        TypedQuery<User> tq = em.createQuery(query,User.class);
        List<User> list = null;
        try{
            list = tq.getResultList();
        }catch(NonUniqueResultException ex){
            ex.printStackTrace();
        }finally{
            em.close();
            return list;
        }
    }

    /*DELETE USER*/
    public static User deleteUser(int id){
        EntityManager em = ChatserverApplication.getEntityManagerFactory()
                .createEntityManager();
        EntityTransaction et = null;
        User user = new User();
        try{
            et = em.getTransaction();
            et.begin();
            user = em.find(User.class, id);
            em.remove(user);
            em.getTransaction().commit();
        }catch(Exception ex){
            if(et != null){
                et.rollback();
            }
            ex.printStackTrace();
        }finally{
            em.close();
            return user;
        }
    }

}
