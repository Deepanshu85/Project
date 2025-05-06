package com.zosh.repository;

import com.zosh.modal.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository  extends JpaRepository<Wishlist,Long> {

    Wishlist findByUserId(Long userId);
}
