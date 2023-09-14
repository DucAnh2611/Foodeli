package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body;

public class UpdateProductImageBody {

    private int pid, uid, sid, iid;
    private String image;

    public UpdateProductImageBody(
            int pid, int uid, int sid, int iid,
            String image
    ) {
        this.pid = pid;
        this.uid = uid;
        this.sid = sid;
        this.iid = iid;
        this.image = image;
    }

}
