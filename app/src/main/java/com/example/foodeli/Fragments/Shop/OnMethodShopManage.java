package com.example.foodeli.Fragments.Shop;

import com.example.foodeli.MySqlSetUp.Schemas.User.User;
import com.example.foodeli.MySqlSetUp.Schemas.UserShop.Response.GetAllUserInShop;

public interface OnMethodShopManage {
    void onEdit(int position, Object obj);
    void onDelete(int position, int id);
}
