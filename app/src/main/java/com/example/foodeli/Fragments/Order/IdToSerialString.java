package com.example.foodeli.Fragments.Order;

public class IdToSerialString {

    public String serial = "FDL";

    public String convertIdToSerialString(int number) {
        String base36String = Integer.toString(number, 36);
        String paddedString = String.format("%4s", base36String.toUpperCase()).replace(' ', '0'); // Pads with leading zeros

        return serial + paddedString;
    }
}
