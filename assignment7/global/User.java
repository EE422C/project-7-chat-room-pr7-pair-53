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
    private GregorianCalendar birthdate;

    public User (){
        this.firstname = "NEW";
        this.lastname = "USER";
        birthdate = new GregorianCalendar(1900, 1, 1);

    }

    public User (String firstname, String lastname, String username){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        birthdate = new GregorianCalendar(1900, 1, 1);
    }

    public User (String firstname, String lastname, String username, int year, int month, int day){
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        birthdate = new GregorianCalendar(year, month, day);
    }

    public User (String firstname, String lastname,  int year, int month, int day){
        this.firstname = firstname;
        this.lastname = lastname;
        birthdate = new GregorianCalendar(year, month, day);
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


    public GregorianCalendar getBirthdate(){
        return birthdate;
    }

    public void setBirthdate(int year, int month, int day){
        birthdate = new GregorianCalendar(year, month, day);
    }

    public boolean isBirthday(){

        if (birthdate.equals(new GregorianCalendar(1900, 1,1))){
            return false;
        }
        else{
            GregorianCalendar today = new GregorianCalendar(); // Today's date
            GregorianCalendar birthday = new GregorianCalendar(today.get(YEAR), birthdate.get(MONTH),
                    birthdate.get(DATE));
            return (birthday.equals(today));
        }


    }

    public Message welcomeMessage(){
            String body = "User " + fullName() + " has joined the chat.";
            if (isBirthday()){
                body += "It is " + fullName() + "'s birthday today! Happy birthday!";
            }
            Message admin = new Message();
            admin.setBody(body);
            return admin;
        }


}
