package com.ssevening.www.module.shopcart;

import android.os.IBinder;
import android.os.RemoteException;

import com.ssevening.www.service.shopcart.IShopcartService;

/**
 * Created by Pan on 2017/12/13.
 */

public class ShopcartServiceImpl implements IShopcartService {

    @Override
    public String getCartCount() throws RemoteException {
        return "ShopcartServiceImpl getCartCount";
    }

    @Override
    public String addToCart(String productId) throws RemoteException {
        return "addToCart " + productId;
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
