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
import com.chinatelecom.xjdh.bean.SubstationBean;
import com.chinatelecom.xjdh.tool.GetRequest_Interface;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.URLs;
import com.hss01248.dialog.StyledDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChooseStationActivity extends BaseActivity {
    private RecyclerView rv;
    private RelativeLayout rl_head_return;
    private String isMany;
    private String cityCode;
    private String titleShow;
    private List<SubstationBean.Data> substationBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_station);
        setTitle("选择局站");
        rv = (RecyclerView) findViewById(R.id.rv);
        rl_head_return = (RelativeLayout) findViewById(R.id.rl_head_return);
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rl_head_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isMany = getIntent().getExtras().getString("isMany");
        cityCode = getIntent().getExtras().getString("cityCode");
        titleShow = getIntent().getExtras().getString("titleShow");
        request();

    }


    private class MyAdapter extends  RecyclerView.Adapter<MyHolder> {
        private final List<String> list;

        public MyAdapter() {
            list = new ArrayList<String>();
            for (SubstationBean.Data substation:substationBeanList) {
                list.add(substation.getName());
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
                    if(isMany.equals("1")){
                        Intent intent = new Intent(getApplicationContext(),ChooseRoomActivity.class);
                        intent.putExtra("stationcode",substationBeanList.get(position).getId());
                        startActivity(intent);
                    }else if(isMany.equals("2")){
                        Intent intent = new Intent(getApplicationContext(),ChooseManyRoomActivity.class);
                        intent.putExtra("stationcode",substationBeanList.get(position).getId());
                        intent.putExtra("titleShow",titleShow + "->"+substationBeanList.get(position).getName());
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getApplicationContext(),ChooseVdioeRoomActivity.class);
                        intent.putExtra("stationcode",substationBeanList.get(position).getId());
                        startActivity(intent);
                    }
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
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();
        StyledDialog.buildLoading("数据加载中").show();
        final GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        Call<SubstationBean> call = request.getSubstationCall("Api/Get_Substation_List/"+cityCode, PreferenceUtils.getPrefString(mContext, PreferenceConstants.ACCESSTOKEN,""));
        call.enqueue(new retrofit2.Callback<SubstationBean>() {
            @Override
            public void onResponse(Call<SubstationBean> call, Response<SubstationBean> response) {
                if(response.body().getRet() == 0){
                    substationBeanList = new ArrayList<>();
                    substationBeanList = response.body().getData();
                    MyAdapter adapter = new MyAdapter();
                    rv.setAdapter(adapter);
                    StyledDialog.dismissLoading();
                }
            }

            @Override
            public void onFailure(Call<SubstationBean> call, Throwable t) {
                System.out.println("连接失败");
            }
        });
    }
}