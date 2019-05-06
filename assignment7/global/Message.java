package global;


import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {

    private String from;
    private String to;
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

    public Message(String from, String to, String body, int adminmode) {
        this.from = from;
        this.to = to;
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
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getMode() {
        return adminmode;
    }

    public String getFrom() {
        return from;
    }


    public String printTime() {
        return time.toString();
    }

    public String toInfoString() {
        String tostring = "λ";
        tostring += "~" + adminmode + "~" + from + "~" + to + "~" + body;
        return tostring;
    }

    public Message parseString(String tostring) {

        if (tostring.substring(0, 1).equals("λ")) {
            String[] separated = tostring.split("~");

            if (separated[1].equals("1")) {
                return new Message(separated[2], separated[3], "", Integer.parseInt(separated[1]));
        } else
        if (separated[1].equals("0")) {
            return new Message(separated[2], separated[3], separated[4], Integer.parseInt(separated[1]));
        }else {
                return null;
            }
        } else {
            return null;
        }
    }

}
