package global;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Message implements Serializable {

    private User from;
    private User to;
    private String body;
    private int adminmode;
    transient private Thread myThread;
    private Timestamp time;

    public Message() {
        this.body = "";
        this.adminmode = 0;
        this.time = new Timestamp(System.currentTimeMillis());
        this.myThread = new Thread();
    }

    public Message(User from, User to, String body, int adminmode) {
        this.from = from;
        this.to = to;
        this.body = body;
        this.adminmode = adminmode;
        this.time = new Timestamp(System.currentTimeMillis());
        this.myThread = new Thread();

    }

    public Message(String from, String to, String body, int adminmode) {
        this.from = new User();
        this.to = new User();
        this.from.setUsername(from);
        this.to.setUsername(to);
        this.body = body;
        this.adminmode = adminmode;
        this.time = new Timestamp(System.currentTimeMillis());
        this.myThread = new Thread();
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setMode(int adminmode) {
        this.adminmode = adminmode;
    }

    public String getBody() {
        return body;
    }

    public String getTo() {
        return to.getUsername();
    }

    public int getMode() {
        return adminmode;
    }

    public String getFrom() {
        return from.getUsername();
    }


    public Timestamp getTime() {
        return time;
    }

    public String printTime() {
        return time.toString();
    }

    public String toInfoString() {
        String tostring = "λ";
        tostring += "~" + adminmode + "~" + from.getUsername() + "~" + to.getUsername() + "~" + body;
        return tostring;
    }

    public Message parseString(String tostring) {

        //Message message = new Message();
        if (tostring.substring(0, 1).equals("λ")) {
            String[] separated = tostring.split("~");

            if (separated[0].equals("0")) {
                return new Message(separated[1], separated[2], separated[3], Integer.parseInt(separated[0]));
            } else if (separated[1].equals("1")) {
                return new Message(separated[2], separated[3], "", Integer.parseInt(separated[1]));
            } else {
                return null;
            }
        } else {
            return null;
        }
        else
            return null;
    }

}
