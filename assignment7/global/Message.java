package global;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Message implements Serializable{

    private User from;
    private ArrayList<User> to;
    private String body;
    private int mode;
    transient private Thread myThread;
    private Timestamp time;

    public Message(){
        this.body = "";
        this.mode = 0;
        this.time = new Timestamp(System.currentTimeMillis());
        this.myThread = new Thread();
    }

    public Message(User from, ArrayList<User> to, String body, int adminmode){
        this.from = from;
        this.to = new ArrayList<>(to);
        this.body = body;
        this.mode = adminmode;
        this.time = new Timestamp(System.currentTimeMillis());
        this.myThread = new Thread();

    }

    public void setBody(String body){
        this.body = body;
    }

    public void setMode(int adminmode){
        this.mode = adminmode;
    }

    public String getBody(){
        return body;
    }

    /*public String adminMessage(){
        if (mode){
            String tolist = "";
            for (int i=0; i<to.size(); i++){
                if (!to.get(i).fullName().equals(from.fullName())){
                    tolist += to.get(i).fullName() + ", ";
                }
            }
            tolist.substring(0,tolist.length()-2);
            body = "User " + from.fullName() + " has joined the chat with user(s) " + tolist + " at " + printTime() +".\n";
            if (from.isBirthday()){
                body += "It is " + from.fullName() + "'s birthday today! Happy birthday!";
            }
            adminmode = false;
            return body;
        }
        else
            return null;
    }*/

    public Timestamp getTime() {
        return time;
    }

    public String printTime(){
        return time.toString();
    }
}


