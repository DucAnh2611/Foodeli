package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body;

import java.util.ArrayList;

public class CreateProductBody extends ProductPostBody{

    private ArrayList<String> images;
    private ArrayList<CategoryId> categories;

    public CreateProductBody(
            int uid,
            int sid,
            float price,
            float stock,
            String name,
            String unit,
            String shortDesc,
            String longDesc,
            ArrayList<String> images,
            ArrayList<CategoryId> categories
    ) {
        super(uid, sid, price,stock, name, unit, shortDesc, longDesc);

        this.images = images;
        this.categories = categories;
    }

    public class CategoryId {

        private int id;
        public CategoryId(int id) {this.id = id;}

    }

}
