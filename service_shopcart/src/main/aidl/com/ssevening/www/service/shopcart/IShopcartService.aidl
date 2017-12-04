// IShopcartService.aidl
package com.ssevening.www.service.shopcart;

// Declare any non-default types here with import statements

interface IShopcartService {
       // 获取购物车数据
       String getCartCount();

        // 产品添加到购物车
       String addToCart(String productId);
}
