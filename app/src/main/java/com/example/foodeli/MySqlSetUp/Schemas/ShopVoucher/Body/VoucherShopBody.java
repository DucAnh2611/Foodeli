package com.example.foodeli.MySqlSetUp.Schemas.ShopVoucher.Body;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class VoucherShopBody {

    private int uid, sid, expired, limit;
    private String text, title, desc, code, target;
    private float min, max, discount;

    public VoucherShopBody(
        int uid,
        int sid,
        int expired,
        int limit,
        String text,
        String title,
        String desc,
        String code,
        String target,
        float min,
        float max,
        float discount
    ) {
        this.uid = uid;
        this.sid = sid;
        this.expired = expired;
        this.limit = limit;
        this.text = text;
        this.title = title;
        this.desc = desc;
        this.code = code;
        this.target = target;
        this.min = min;
        this.max = max;
        this.discount = discount;
    }
    public ArrayList<String> checkIfValid() {
        ArrayList<String> err = new ArrayList<>();
        try {
            // Get the class of the current object
            Class<?> clazz = this.getClass();

            // Get all declared fields of the class
            Field[] fields = clazz.getDeclaredFields();

            // Loop through each field
            for (Field field : fields) {
                // Make the field accessible (required if it's a private field)
                field.setAccessible(true);

                // Get the value of the field
                Object value = field.get(this);

                // Check if the value is null or empty
                if (value == null || (value instanceof String && ((String) value).isEmpty()) && !field.getName().equals("text")) {
                    err.add("Field " + field.getName() + " is null or empty.");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return err;
    }

    @Override
    public String toString() {
        return "VoucherShopBody{" +
                "uid=" + uid +
                ", sid=" + sid +
                ", expired=" + expired +
                ", limit=" + limit +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", code='" + code + '\'' +
                ", target='" + target + '\'' +
                ", min=" + min +
                ", max=" + max +
                ", discount=" + discount +
                '}';
    }
}
