package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return
     */
    List<ShoppingCart> showShoppingCart();

    /**
     * 根据用户id清空购物车
     */
    void clean(Long id);

    /**
     * 查询购物车菜品信息
     * @param shoppingCartDTO
     * @return
     */
    ShoppingCart selectDishOrSetmeal(ShoppingCartDTO shoppingCartDTO);

    /**
     * 更新购物车数据
     * @param shoppingCart
     */
    void update(ShoppingCart shoppingCart);

    /**
     * 删除购物车商品数据
     * @param id
     */
    void deleteById(Long id);
}
