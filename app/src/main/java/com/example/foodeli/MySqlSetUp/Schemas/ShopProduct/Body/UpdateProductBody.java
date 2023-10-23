package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class UpdateProductBody extends ProductPostBody {

    private ArrayList<ImageState> images;
    private ArrayList<CategoryState> categories;

    public UpdateProductBody(
            int uid,
            int sid,
            float price,
            int stock,
            int from,
            int to,
            String name,
            String unit,
            String shortDesc,
            String longDesc,
            ArrayList<ImageState> images,
            ArrayList<CategoryState> categories
    ) {
        super(uid, sid, price, stock, name, unit, shortDesc, longDesc, from, to);

        this.images = images;
        this.categories = categories;
    }

    public static class ImageState {
        private int iid;
        private String base64;
        private int state;

        public ImageState(int iid, String base64, int state) {
            this.iid = iid;
            this.base64 = base64;
            this.state = state;
        }

        public int getIid() {
            return iid;
        }

        public void setIid(int iid) {
            this.iid = iid;
        }

        public String getBase64() {
            return base64;
        }

        public void setBase64(String base64) {
            this.base64 = base64;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }

    public static class CategoryState {
        private int cid;
        private int state;

        public CategoryState(int cid, int state) {
            this.cid = cid;
            this.state = state;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
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
                if (value == null || (value instanceof String && ((String) value).isEmpty())) {
                    err.add("Field " + field.getName() + " is null or empty.");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return err;
    }

    public ArrayList<ImageState> getImages() {
        return images;
    }

    public ArrayList<CategoryState> getCategories() {
        return categories;
    }
}

