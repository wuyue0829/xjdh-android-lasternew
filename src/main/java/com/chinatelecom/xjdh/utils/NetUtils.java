package com.chinatelecom.xjdh.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/6.
 */

public class NetUtils {

    public  static  void getDatas(Context context,final Map data, int method, String url, Response.Listener<String> succeed, Response.ErrorListener error){
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(method,url,succeed,error){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return data;
            }

        };
        requestQueue.add(stringRequest);
    }
}
