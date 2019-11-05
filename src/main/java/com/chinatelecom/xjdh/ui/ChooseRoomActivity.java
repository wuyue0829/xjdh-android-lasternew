package com.chinatelecom.xjdh.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.RoomBean;
import com.chinatelecom.xjdh.linphone.LinphoneUtils;
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

public class ChooseRoomActivity extends BaseActivity {
    private RecyclerView rv;
    private RelativeLayout rl_head_return;
    private List<RoomBean.Data> roomBeanList;
    private String stationcode;
    private String isJian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);
        setTitle("选择机房");
        rv = (RecyclerView) findViewById(R.id.rv);
        stationcode = getIntent().getExtras().getString("stationcode");
        isJian = getIntent().getExtras().getString("isJian");
        rl_head_return = (RelativeLayout) findViewById(R.id.rl_head_return);
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rl_head_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        request();
    }


    private class MyAdapter extends  RecyclerView.Adapter<MyHolder> {
        private final List<String> list;

        public MyAdapter() {
            list = new ArrayList<String>();
            for (RoomBean.Data substation:roomBeanList) {
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
                    if(null != isJian && isJian.length()>0){
                        //静音监听时
                        show1(roomBeanList.get(position).getPhone_number(),list.get(position));
                    }else{
                        LinphoneUtils.call(mContext,mEngine,roomBeanList.get(position).getPhone_number(),false,roomBeanList.get(position).getName(),false);

                    }

                }
            });
        }

        public int getItemCount() {
            return list.size();
        }
    }

    private void show1(final String phoneNumber, final String name) {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_normal1, null);
        bottomDialog.setContentView(contentView);
        contentView.findViewById(R.id.tv_yuyin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinphoneUtils.call(mContext,mEngine,phoneNumber,false,name,true);
                bottomDialog.dismiss();
            }
        });

        contentView.findViewById(R.id.tv_shipin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinphoneUtils.call(mContext,mEngine,phoneNumber,true,name,true);
                bottomDialog.dismiss();
            }
        });
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
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
        Call<RoomBean> call = request.getRoomCall("Api/Get_Room_List/"+stationcode, PreferenceUtils.getPrefString(mContext, PreferenceConstants.ACCESSTOKEN,""));
        call.enqueue(new retrofit2.Callback<RoomBean>() {
            @Override
            public void onResponse(Call<RoomBean> call, Response<RoomBean> response) {
                if(response.body().getRet() == 0){
                    roomBeanList = new ArrayList<>();
                    roomBeanList = response.body().getData();
                    MyAdapter adapter = new MyAdapter();
                    rv.setAdapter(adapter);
                    StyledDialog.dismissLoading();
                }

            }

            @Override
            public void onFailure(Call<RoomBean> call, Throwable t) {
                System.out.println("连接失败");
            }
        });
    }
}
