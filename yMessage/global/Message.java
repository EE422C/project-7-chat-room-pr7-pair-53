package global;


import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Message implements Serializable{

    private User from;
    private User to;
    private String body;
    private boolean adminmode;
    transient private Thread myThread;
    private Timestamp time;

    public Message(){
        this.body = "";
        this.adminmode = true;
        this.time = new Timestamp(System.currentTimeMillis());
        this.myThread = new Thread();
    }

    public Message(User from, User to, String body, boolean adminmode){
        this.from = from;
        this.to = to;
        this.body = body;
        this.adminmode = adminmode;
        this.time = new Timestamp(System.currentTimeMillis());
        this.myThread = new Thread();

    }

    public void setBody(String body){
        this.body = body;
    }

    public void setMode(boolean adminmode){
        this.adminmode = adminmode;
    }

    public String getBody(){
        return body;
    }

    public String adminMessage(){
        if (adminmode){
            body = "User " + from.fullName() + " has joined the chat with user " + to.fullName() + " at " + printTime() +".\n";
            if (from.isBirthday()){
                body += "It is " + from.fullName() + "'s birthday today! Happy birthday!";
            }
            return body;
        }
        else
            return null;
    }

    public Timestamp getTime() {
        return time;
    }

    public String printTime(){
        return time.toString();
    }
}


