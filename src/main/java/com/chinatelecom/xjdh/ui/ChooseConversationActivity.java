package com.chinatelecom.xjdh.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.Conversation;
import com.chinatelecom.xjdh.bean.ConversationBean;
import com.chinatelecom.xjdh.linphone.LinphoneUtils;
import com.chinatelecom.xjdh.tool.GetRequest_Interface;
import com.chinatelecom.xjdh.utils.PinyinComparator;
import com.chinatelecom.xjdh.utils.PinyinUtils;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SortModel;
import com.chinatelecom.xjdh.utils.URLs;
import com.chinatelecom.xjdh.view.WaveSideBar;
import com.hss01248.dialog.StyledDialog;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChooseConversationActivity extends BaseActivity {
    private RecyclerView rv;
    private RelativeLayout rl_head_return;
    private List<Conversation> conversationBeanBeanList;
    private WaveSideBar mSideBar;
    private PinyinComparator mComparator;
    private LinearLayoutManager manager;
    private MyAdapter adapter;
    private List<SortModel> mSortList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_conversation);
        setTitle("选择人员");
        rv = (RecyclerView)findViewById(R.id.rv);
        rl_head_return = (RelativeLayout) findViewById(R.id.rl_head_return);
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rl_head_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSideBar = (WaveSideBar) findViewById(R.id.sideBar);
        //设置右侧SideBar触摸监听
        mSideBar.setOnTouchLetterChangeListener(new WaveSideBar.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(letter.charAt(0));
                LogUtil.e(letter);
                LogUtil.e(position+"");
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
        mComparator = new PinyinComparator();
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        request();
    }


    private class MyAdapter extends  RecyclerView.Adapter<MyHolder> {

        public MyAdapter() {
        }

        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_choose, parent, false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        public void onBindViewHolder(MyHolder holder, final int position) {

            String item = mSortList.get(position).getName();
            holder.textView.setText(item);
            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show1(position);
                }
            });
        }

        public int getItemCount() {
            return mSortList.size();
        }

        /**
         * 根据ListView的当前位置获取分类的首字母的char ascii值
         */
        public int getSectionForPosition(int position) {
            return mSortList.get(position).getLetters().charAt(0);
        }

        public int getPositionForSection(int section) {
            for (int i = 0; i < getItemCount(); i++) {
                String sortStr = mSortList.get(i).getLetters();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }
    }


    private void show1(final int position) {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_normal, null);
        bottomDialog.setContentView(contentView);
        contentView.findViewById(R.id.tv_yuyin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinphoneUtils.call(mContext,mEngine,mSortList.get(position).getPhoneNum(),false,mSortList.get(position).getName());
                bottomDialog.dismiss();
            }
        });

        contentView.findViewById(R.id.tv_shipin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinphoneUtils.call(mContext,mEngine,mSortList.get(position).getPhoneNum(),true,mSortList.get(position).getName());
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
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();
        StyledDialog.buildLoading("数据加载中").show();
        final GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        Call<ConversationBean> call = request.getConversationCall(PreferenceUtils.getPrefString(mContext, PreferenceConstants.ACCESSTOKEN,""));
        call.enqueue(new retrofit2.Callback<ConversationBean>() {
            @Override
            public void onResponse(Call<ConversationBean> call, Response<ConversationBean> response) {
                if(response.body().getRet() == 0){
                    mSortList = new ArrayList<>();
                    conversationBeanBeanList = new ArrayList<>();
                    conversationBeanBeanList = response.body().getData();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < conversationBeanBeanList.size(); i++) {
                                SortModel sortModel = new SortModel();
                                sortModel.setName(conversationBeanBeanList.get(i).getFull_name());
                                sortModel.setPhoneNum(conversationBeanBeanList.get(i).getPhone_number());
                                //汉字转换成拼音
                                String pinyin = PinyinUtils.getPingYin(conversationBeanBeanList.get(i).getFull_name());
                                String sortString = pinyin.substring(0, 1).toUpperCase();

                                // 正则表达式，判断首字母是否是英文字母
                                if (sortString.matches("[A-Z]")) {
                                    sortModel.setLetters(sortString.toUpperCase());
                                } else {
                                    sortModel.setLetters("#");
                                }
                                mSortList.add(sortModel);
                            }
                            handler.sendEmptyMessage(1000);
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ConversationBean> call, Throwable t) {
                System.out.println("连接失败");
            }
        });
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Collections.sort(mSortList, mComparator);
            adapter = new MyAdapter();
            rv.setAdapter(adapter);
            StyledDialog.dismissLoading();
        }
    };
}
