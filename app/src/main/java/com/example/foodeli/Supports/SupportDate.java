package com.example.foodeli.Supports;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class SupportDate {

    public SupportDate() {}

    public String GetCurrentDateVN() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }
    public String GetCurrentDateLA() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            TimeZone timeZone = TimeZone.getTimeZone("GMT-7");
            dateFormat.setTimeZone(timeZone);

            Date currentDate = new Date();

            return dateFormat.format(currentDate);
        }
        return "";
    }


    public String ConvertLAtoVN(String dateTimeString) {
        DateTimeFormatter formatter = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime gmtDateTime = LocalDateTime.parse(dateTimeString, formatter);

            ZoneId gmtZoneId = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(0));

            ZoneId systemZoneId = ZoneId.ofOffset("GMT", ZoneOffset.ofHours(7));

            ZonedDateTime gmtZonedDateTime = ZonedDateTime.of(gmtDateTime, gmtZoneId);
            ZonedDateTime systemZonedDateTime = gmtZonedDateTime.withZoneSameInstant(systemZoneId);

            String systemDateTimeString = systemZonedDateTime.format(formatter);

            return systemDateTimeString;
        }

        return "";
    }
    public long CalculateSecondBetween(String dateStr) {
//        String dateStr = "2022-01-01"; // Replace with your desired date in "YYYY-MM-DD" format

        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();

            LocalDate givenDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);

            LocalTime currentTime = LocalTime.now();
            ZonedDateTime currentDateTime = ZonedDateTime.of(currentDate, currentTime, ZoneId.systemDefault());

            ZonedDateTime givenDateTime = ZonedDateTime.of(givenDate, currentTime, ZoneId.systemDefault());

            long secondsBetween = Duration.between(currentDateTime, givenDateTime).getSeconds();

            return secondsBetween;
        }
        return 0;
    }

    public String TimeAfterSecond(String inputDateTime, int secondsToAdd) {

        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(inputDateTime, formatter);

            LocalDateTime updatedDateTime = dateTime.plusSeconds(secondsToAdd);

            String outputDateTime = updatedDateTime.format(formatter);

            return outputDateTime;
        }

        return "";
    }

    public boolean Is16(int year, int month, int day) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate userBirthDate = LocalDate.of(year, month, day);
            LocalDate currentDate = null;

            currentDate = LocalDate.now();

            // Calculate the age difference between current date and user's birth date
            Period ageDifference = Period.between(userBirthDate, currentDate);

            return ageDifference.getYears() >= 16;
        }

        return false;
    }
}
