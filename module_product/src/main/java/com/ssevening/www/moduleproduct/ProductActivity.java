package com.ssevening.www.moduleproduct;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ssevening.www.base.MyServices;
import com.ssevening.www.service.shopcart.IShopcartService;

import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private EditText et_massage;
    private TextView tv_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        et_massage = (EditText) findViewById(R.id.et_massage);
        tv_loading = (TextView) findViewById(R.id.tv_loading);
        Intent intent = getServiceIntent(this, IShopcartService.class);
        bindService(intent, shopcartServiceConnection, Context.BIND_AUTO_CREATE);

        IShopcartService iShopcartService = MyServices.get(IShopcartService.class);
        if (iShopcartService != null) {
            try {
                Toast.makeText(this, iShopcartService.getCartCount(), Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public Intent getServiceIntent(Context context, final Class<?> service_interface) {
        Intent intent = new Intent(service_interface.getName().intern());
        ComponentName componentName = null;
        final List<ResolveInfo> matches = context.getPackageManager().queryIntentServices(intent, 0);
        if (matches != null && !matches.isEmpty()) {
            ResolveInfo resolveInfo = matches.get(0);
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name.intern());
        }
        if (componentName != null) {
            intent.setComponent(componentName);
        }
        return intent;
    }

    private ServiceConnection shopcartServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            tv_loading.setText("购物车数据加载中....");
            final IShopcartService footBathService = IShopcartService.Stub.asInterface(service);
            new AsyncTask<String, Integer, String>() {
                @Override
                protected String doInBackground(String... params) {
                    try {
                        return footBathService.getCartCount() + "\r\n\r\n" + footBathService.addToCart("1234567890");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        return "RemoteException";
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    et_massage.setText(s);
                    tv_loading.setText("购物车数据和产品添到到购物车成功");
                }
            }.execute();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("aidl", "onServiceDisconnected");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(shopcartServiceConnection);
    }


}
