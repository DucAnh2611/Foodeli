package com.example.foodeli.MySqlSetUp.Schemas.ShopProduct.Body;

public class UpdateProductCategoryBody {
    private int pid, uid, o_cid, n_cid, sid;
    private String image;

    public UpdateProductCategoryBody(
            int pid, int uid, int o_cid, int n_cid, int sid
    ) {
        this.pid = pid;
        this.uid = uid;
        this.o_cid = o_cid;
        this.n_cid = n_cid;
        this.sid = sid;
    }
}
