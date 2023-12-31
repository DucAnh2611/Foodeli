package com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Body;

import java.util.ArrayList;

public class CreateReviewBody {

    private int uid, pid, oid, rate;
    private String title, desc;
    private ArrayList<String> images;

    public CreateReviewBody(int uid, int pid, int oid, int rate, String title, String desc, ArrayList<String> images){
        this.uid = uid;
        this.pid = pid;
        this.oid = oid;
        this.rate = rate;
        this.title = title;
        this.desc = desc;
        this.images = images;
    }

}
