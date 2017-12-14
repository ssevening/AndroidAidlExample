package com.ssevening.www.module.shopcart;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.ssevening.www.service.shopcart.IShopcartService;


/**
 * Created by Pan on 2017/12/1.
 */

public class ShopcartService extends Service implements IShopcartService {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IShopcartService.Stub() {


            @Override
            public String getCartCount() throws RemoteException {
                return getCartCountImpl() + " service";
            }

            @Override
            public String addToCart(String productId) throws RemoteException {
                return addToCartImpl(productId) + "service";
            }
        };
    }

    @Override
    public String getCartCount() throws RemoteException {
        return getCartCountImpl() + " forName";
    }

    @Override
    public String addToCart(String productId) throws RemoteException {
        return addToCartImpl(productId) + "forName";
    }

    private String addToCartImpl(String productId) {
        return "This is add To Cart Result:" + productId + "\r\n\r\n"
                + HttpUtils.getHtmlContent("http://www.baidu.com", "utf-8");
    }

    private String getCartCountImpl() {
        return "10";
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
