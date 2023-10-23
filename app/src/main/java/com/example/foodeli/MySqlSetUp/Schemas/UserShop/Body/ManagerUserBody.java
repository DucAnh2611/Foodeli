package com.example.foodeli.MySqlSetUp.Schemas.UserShop.Body;

public class ManagerUserBody {

    private int uid_add, uid, sid, pid;

    public ManagerUserBody(int uid_add, int uid, int sid, int pid) {
        this.uid_add = uid_add;
        this.uid = uid;
        this.sid = sid;
        this.pid = pid;
    }

}
