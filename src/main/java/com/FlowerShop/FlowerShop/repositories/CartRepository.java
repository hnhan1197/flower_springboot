package com.FlowerShop.FlowerShop.repositories;

import com.FlowerShop.FlowerShop.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findAllByUserIdAndDeletedFalse(int id);

    List<Cart> findAllByUserId(int id);
    Cart findOneById(int id);
}