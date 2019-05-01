package global;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import java.util.GregorianCalendar;

public class User {

    private String firstname;
    private String lastname;
    GregorianCalendar birthdate;

    public User (){
        this.firstname = "NEW";
        this.lastname = "USER";
        birthdate = new GregorianCalendar(1900, 1, 1);

    }

    public User (String firstname, String lastname){
        this.firstname = firstname;
        this.lastname = lastname;
        birthdate = new GregorianCalendar(1900, 1, 1);
    }

    public User (String firstname, String lastname, int year, int month, int day){
        this.firstname = firstname;
        this.lastname = lastname;
        birthdate = new GregorianCalendar(year, month, day);
    }

    public String fullName(){
        return firstname + " " + lastname;
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


}
