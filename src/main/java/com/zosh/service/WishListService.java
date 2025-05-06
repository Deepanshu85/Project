package com.zosh.service;

import com.zosh.modal.Product;
import com.zosh.modal.User;
import com.zosh.modal.Wishlist;

public interface WishListService {

    Wishlist createWishList(User user);
    Wishlist getWishListByUserId(User user);
        Wishlist addProductToWishList(User user, Product product);
}
