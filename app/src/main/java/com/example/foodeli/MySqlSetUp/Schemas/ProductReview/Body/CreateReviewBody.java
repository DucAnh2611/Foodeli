package com.example.foodeli.MySqlSetUp.Schemas.ProductReview.Body;

public class CreateReviewBody {

    private int uid, rate, pid;
    private String title, desc;

    public CreateReviewBody(int uid, int rate, int pid, String title, String desc) {
        this.uid = uid;
        this.rate = rate;
        this.pid = pid;
        this.title = title;
        this.desc = desc;
    }

}
