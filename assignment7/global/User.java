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



    public String fullName(){
        return firstname + " " + lastname;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String user){
        username = user;
    }




    public Message welcomeMessage(){
            String body = "User " + getUsername() + " has joined the server.";
            Message admin = new Message();
            admin.setBody(body);
            //admin.setFrom(getUsername());
            admin.setMode(0);
            return admin;
        }


}
