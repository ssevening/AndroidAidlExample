package com.ssevening.www.module.shopcart;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.ssevening.www.service.shopcart.IShopcartService;


/**
 * Created by Pan on 2017/12/1.
 */

public class ShopcartService extends Service {

    private Binder mBinder = new IShopcartService.Stub() {


        @Override
        public String getCartCount() throws RemoteException {
            return "10";
        }

        @Override
        public String addToCart(String productId) throws RemoteException {
            return "This is add To Cart Result:" + productId + "\r\n\r\n"
                    + HttpUtils.getHtmlContent("http://www.baidu.com", "utf-8");
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
