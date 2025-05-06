package com.zosh.controller;

import com.zosh.exception.ProductException;
import com.zosh.modal.Product;
import com.zosh.modal.User;
import com.zosh.modal.Wishlist;
import com.zosh.service.ProductService;
import com.zosh.service.UserService;
import com.zosh.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishListService wishListService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<Wishlist> getWishlistByUserId(
            @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserByJwtToken(jwt);
        Wishlist wishlist = wishListService.getWishListByUserId(user);
        return ResponseEntity.ok(wishlist);
    }
    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Wishlist> addProductToWishlist(
            @PathVariable Long productId, @RequestHeader("Authorization") String jwt)
            throws Exception {
        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwt);
        Wishlist updatedWishlist = wishListService.addProductToWishList(user,product);
        return ResponseEntity.ok(updatedWishlist);
    }

}
