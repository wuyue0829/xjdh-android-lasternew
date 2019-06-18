package com.chinatelecom.xjdh.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.CityBean;
import com.chinatelecom.xjdh.tool.GetRequest_Interface;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.URLs;
import com.hss01248.dialog.StyledDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ChooseAreaActivity extends BaseActivity {
    private RecyclerView rv;
    private RelativeLayout rl_head_return;
    private String isMany;
    private List<CityBean.Data> cityBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        setTitle("选择地区");
        rv = (RecyclerView) findViewById(R.id.rv);
        rl_head_return = (RelativeLayout) findViewById(R.id.rl_head_return);
        isMany = getIntent().getExtras().getString("isMany");
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rl_head_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();

    }


    private void initData(){
        request();
    }


    private class MyAdapter extends  RecyclerView.Adapter<MyHolder> {
        private final List<String> list;

        public MyAdapter() {
            list = new ArrayList<String>();
            for (CityBean.Data city:cityBeanList) {
                list.add(city.getName());
            }
        }

        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_choose, parent, false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        public void onBindViewHolder(MyHolder holder, final int position) {

            String item = list.get(position);
            holder.textView.setText(item);
            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),ChooseStationActivity.class);
                    intent.putExtra("cityCode",cityBeanList.get(position).getCitycode());
                    intent.putExtra("isMany",isMany);
                    intent.putExtra("titleShow",cityBeanList.get(position).getName());
                    startActivity(intent);
                }
            });
        }

        public int getItemCount() {
            return list.size();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout rl_item;
        public MyHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
            rl_item = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        }
    }


    public void request() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.URL_API_HOST) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        StyledDialog.buildLoading("数据加载中").show();
        final GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        Call<CityBean> call = request.getCall(PreferenceUtils.getPrefString(mContext, PreferenceConstants.ACCESSTOKEN,""));
        call.enqueue(new Callback<CityBean>() {
            @Override
            public void onResponse(Call<CityBean> call, retrofit2.Response<CityBean> response) {
                if(null != response.body()){
                    if(response.body().getRet() == 0){
                        cityBeanList = new ArrayList<>();
                        cityBeanList = response.body().getData();
                        MyAdapter adapter = new MyAdapter();
                        rv.setAdapter(adapter);
                        StyledDialog.dismissLoading();
                    }
                }
            }

            @Override
            public void onFailure(Call<CityBean> call, Throwable t) {

            }
        });
    }
}
