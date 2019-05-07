/*
 * CHATROOM User.java
 * EE422C Project 7 submission by
 * Guy Sexton
 * gwm639
 * 16190
 * Dylan Wolford
 * ddw2379
 * 16190
 * Slip days used: 0
 * Spring 2019
 */




package global;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Date;

public class User implements Serializable {

    private String firstname;
    private String lastname;
    private String username;

    public User (){
        this.firstname = "NEW";
        this.lastname = "USER";
       this.username = "USERNAME_";

    }

    public User (String firstname, String lastname, String username){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
    }



    public String getUsername(){
        return username;
    }



    public Message welcomeMessage(){
            String body = "User " + getUsername() + " has joined the server.";
            Message admin = new Message();
            admin.setBody(body);
            admin.setMode(0);
            return admin;
        }


}
