package com.example.foodeli.Activities.Auth;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationField {

    private String email;
    private String phone;
    private String bothEmailAndPhone;
    private String password;
    private String fullname;
    private static final String EMAIL_REGEX = "^\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b$";
    private static final String PHONE_REGEX = "^\\d{10}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";;

    public ValidationField(String email, String phone, String password, String fullname) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fullname = fullname;
    }

    public boolean isCorrectFormat() {
        return verifyEmail() && verifyPhone() && verifyFullname() && verifyPassword();
    }

    public boolean verifyEmail() {
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(this.email);
        return matcher.matches() && !this.email.isEmpty();
    }
    public boolean verifyPhone() {
        Pattern pattern = Pattern.compile(PHONE_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(this.phone);
        return matcher.matches() && !this.phone.isEmpty();
    }

    public boolean verifyBothEmailAndPhone() {
        return verifyEmail() || verifyEmail();
    }
    public boolean verifyFullname() {
        return !this.fullname.isEmpty();
    }
    public boolean verifyPassword() {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(this.password);
        return matcher.matches() && !this.password.isEmpty();
    }


}
