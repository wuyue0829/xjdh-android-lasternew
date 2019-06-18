package com.chinatelecom.xjdh.ui;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.app.AppContext;
import com.chinatelecom.xjdh.bean.LoginRequest;
import com.chinatelecom.xjdh.bean.PostCreateConference;
import com.chinatelecom.xjdh.bean.RoomBean;
import com.chinatelecom.xjdh.linphone.CallManager;
import com.chinatelecom.xjdh.linphone.LinphoneUtils;
import com.chinatelecom.xjdh.linphone.Session;
import com.chinatelecom.xjdh.receiver.MessageWrap;
import com.chinatelecom.xjdh.receiver.PortMessageReceiver;
import com.chinatelecom.xjdh.service.PortSipService;
import com.chinatelecom.xjdh.tool.GetRequest_Interface;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.URLs;
import com.google.gson.Gson;
import com.hss01248.dialog.StyledDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChooseManyRoomActivity extends BaseActivity{
    public PortMessageReceiver receiver = null;
    private RecyclerView rv;
    private RelativeLayout rl_head_return;
    private RelativeLayout sure;
    private CheckBox checkBox;
    private MyAdapter adapter;
    private List<String> datas;
    private RelativeLayout rl_itemAll;
    private TextView tv_title;
    private List<String> selectDatas = new ArrayList<>();
    private List<String> selectNumDatas = new ArrayList<>();
    private List<RoomBean.Data> roomBeanList;
    private String titleShow;
    private String content;
    private String stationcode;
    private long mSessionid;
    AppContext application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_roommany);
        setTitle("选择机房");
        rv = (RecyclerView) findViewById(R.id.rv);
        application = (AppContext) getApplication();
        tv_title = (TextView) findViewById(R.id.tv_title);
        rl_head_return = (RelativeLayout) findViewById(R.id.rl_head_return);
        rl_itemAll = (RelativeLayout) findViewById(R.id.rl_item);
        sure = (RelativeLayout) findViewById(R.id.sure);
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        EventBus.getDefault().register(this);
        stationcode = getIntent().getExtras().getString("stationcode");
        titleShow = getIntent().getExtras().getString("titleShow");
        rl_head_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        request();

        rl_itemAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectDatas.size() == datas.size()){
                    inverse();
                    tv_title.setText("全选");
                }else if(selectDatas.size() < datas.size()){
                    all();
                }
            }
        });


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("选择的是",selectDatas.size()+"");
                if(selectDatas.size()>0){
                    String strs = "";
                    for(String str:selectDatas){
                        strs = strs + str+"，";
                    }
                    joinChat(strs.substring(0,strs.length()-2));
                }else{
                    Toast.makeText(getApplicationContext(),"情选择需要广播的机房！", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMessage(MessageWrap messageEvent) {
        StyledDialog.dismissLoading();
        Toast.makeText(this,"接入广播成功！",Toast.LENGTH_SHORT).show();
        mSessionid = messageEvent.message;
        Intent intent = new Intent(this,BroadcastVoiceActivity.class);
        intent.putExtra("titleShow",titleShow + ":（" + content + "）");
        intent.putExtra("mSessionid",mSessionid);
        startActivity(intent);
        finish();
    }


    /**
     * 进入聊天室
     */
    private void joinChat(final String title){
        StyledDialog.buildLoading("正在接入广播...").show();
        content = title;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.URL_API_HOST) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        final PostCreateConference postCreateConference = new PostCreateConference();
        postCreateConference.setAccess_token(PreferenceUtils.getPrefString(mContext, PreferenceConstants.ACCESSTOKEN,""));
        postCreateConference.setCaller_number(PreferenceUtils.getPrefString(mContext,PreferenceConstants.LINEPHONENUM,""));
        postCreateConference.setCalled_number(new Gson().toJson(selectNumDatas));
        Call call = request.postCreateConference(postCreateConference);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
            }

            @Override
            public void onFailure(Call call, Throwable throwable) {
            }
        });



        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.chinatelecom.xjdh.service.PortSipService".equals(service.service.getClassName())) {
                LogUtil.e("服务正在运行");
            }
        }
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private class MyAdapter extends  RecyclerView.Adapter<MyHolder> {
        public HashMap<Integer, Boolean> isSelected;



        public MyAdapter() {
            datas = new ArrayList<String>();

            for (RoomBean.Data substation:roomBeanList) {
                datas.add(substation.getName());
            }
            init();
        }

        public void init() {
            isSelected = new HashMap<Integer, Boolean>();
            for (int i = 0; i < datas.size(); i++) {
                isSelected.put(i, false);
            }
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
        {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }


        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_choose_check, parent, false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        public void onBindViewHolder(final MyHolder holder, int position) {

            String item = datas.get(position);
            holder.textView.setText(item);
            holder.mCheckBox.setChecked(isSelected.get(position));
            holder.itemView.setSelected(isSelected.get(position));

            if (mOnItemClickLitener != null)
            {
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        mOnItemClickLitener.onItemClick(holder.itemView, holder.getPosition());
                    }
                });
            }
        }

        public int getItemCount() {
            return datas.size();
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout rl_item;
        public CheckBox mCheckBox;
        public MyHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
            rl_item = (RelativeLayout) itemView.findViewById(R.id.rl_item);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox);
        }
    }

    /**
     * 全选
     */
    public void all(){
        selectDatas.clear();
        selectNumDatas.clear();
        for (int i = 0; i < datas.size(); i++) {
            adapter.isSelected.put(i, true);
            selectDatas.add(datas.get(i));
            selectNumDatas.add(roomBeanList.get(i).getPhone_number());
        }
        adapter.notifyDataSetChanged();
        tv_title.setText("已选中"+selectDatas.size()+"个机房");
    }
    /**
     * 反选
     */
    public void inverse(){
        for (int i=0; i<datas.size(); i++) {
            if(adapter.isSelected.get(i)){
                adapter.isSelected.put(i,false);
                selectDatas.remove(datas.get(i));
                selectNumDatas.remove(roomBeanList.get(i).getPhone_number());
            } else {
                adapter.isSelected.put(i,true);
                selectDatas.add(datas.get(i));
                selectNumDatas.add(roomBeanList.get(i).getPhone_number());
            }
        }
        adapter.notifyDataSetChanged();
        tv_title.setText("已选中"+selectDatas.size()+"个机房");

    }
    /**
     * 取消已选
     */
    public void cancel(){
        for (int i=0; i<datas.size(); i++) {
            if(adapter.isSelected.get(i)){
                adapter.isSelected.put(i,false);
                selectDatas.remove(datas.get(i));
                selectNumDatas.remove(roomBeanList.get(i).getPhone_number());
            }
        }
        adapter.notifyDataSetChanged();
        tv_title.setText("已选中"+selectDatas.size()+"个机房");
    }


    public void request() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.URL_API_HOST) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
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
                    adapter = new MyAdapter();
                    adapter.setOnItemClickLitener(new OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if(!adapter.isSelected.get(position)){
                                adapter.isSelected.put(position, true); // 修改map的值保存状态
                                adapter.notifyItemChanged(position);
                                selectDatas.add(datas.get(position));
                                selectNumDatas.add(roomBeanList.get(position).getPhone_number());

                            }else {
                                adapter.isSelected.put(position, false); // 修改map的值保存状态
                                adapter.notifyItemChanged(position);
                                selectDatas.remove(datas.get(position));
                                selectNumDatas.remove(roomBeanList.get(position).getPhone_number());
                            }

                            LogUtil.e(selectDatas.size()+"");
                            if(selectDatas.size() == datas.size()){
                                tv_title.setText("反选");
                            }else if(selectDatas.size() < 0){
                                tv_title.setText("全选");
                            }else{
                                tv_title.setText("已选中"+selectDatas.size()+"个机房");
                            }
                        }

                    });

                    rv.setAdapter(adapter);
                }
                StyledDialog.dismissLoading();
            }

            @Override
            public void onFailure(Call<RoomBean> call, Throwable t) {
                System.out.println("连接失败");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
