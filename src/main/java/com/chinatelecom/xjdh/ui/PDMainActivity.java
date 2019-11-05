package com.chinatelecom.xjdh.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.linphone.LinphoneUtils;

public class PDMainActivity extends BaseActivity{

    private RelativeLayout rl_emote_voice,rl_broadcast,rl_monitoring,rl_conversation,rl_support;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("机房双向语音对讲");
        setContentView(R.layout.activity_main_pd);
        initView();
    }

    private void initView(){
        rl_emote_voice = (RelativeLayout) findViewById(R.id.rl_emote_voice);
        rl_broadcast = (RelativeLayout) findViewById(R.id.rl_broadcast);
        rl_monitoring = (RelativeLayout) findViewById(R.id.rl_monitoring);
        rl_conversation = (RelativeLayout) findViewById(R.id.rl_conversation);
        rl_support = (RelativeLayout) findViewById(R.id.rl_support);

        rl_emote_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChooseAreaActivity.class);
                intent.putExtra("isMany","1");
                startActivity(intent);
            }
        });
        rl_broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseAreaActivity.class);
                intent.putExtra("isMany","2");
                startActivity(intent);

            }
        });
        rl_monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseAreaActivity.class);
                intent.putExtra("isMany","3");
                startActivity(intent);

            }
        });
        rl_conversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChooseConversationActivity.class));
            }
        });

        findViewById(R.id.rl_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show1();
            }
        });
    }


    private void show1() {
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_normal, null);
        bottomDialog.setContentView(contentView);
        contentView.findViewById(R.id.tv_yuyin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinphoneUtils.call(mContext,mEngine,"2000001",false,"远程支援",false);
                bottomDialog.dismiss();
            }
        });

        contentView.findViewById(R.id.tv_shipin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinphoneUtils.call(mContext,mEngine,"2000001",true,"远程支援",false);
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
}
