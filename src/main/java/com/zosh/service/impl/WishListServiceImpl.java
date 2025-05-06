package com.zosh.service.impl;

import com.zosh.modal.Product;
import com.zosh.modal.User;
import com.zosh.modal.Wishlist;
import com.zosh.repository.WishListRepository;
import com.zosh.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    @Override
    public Wishlist createWishList(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishListRepository.save(wishlist);
    }

    @Override
    public Wishlist getWishListByUserId(User user) {
        Wishlist wishlist = wishListRepository.findByUserId(user.getId());
        if(wishlist==null){
            wishlist=createWishList(user);
        }
        return wishlist;
    }

    @Override
    public Wishlist addProductToWishList(User user, Product product) {
        Wishlist wishlist =getWishListByUserId(user);
        if(wishlist.getProducts().contains(product)){
            wishlist.getProducts().remove(product);
        }else wishlist.getProducts().add(product);
        return wishListRepository.save(wishlist);
    }
}
