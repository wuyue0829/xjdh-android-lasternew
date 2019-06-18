//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class DoingToolActivity_
    extends DoingToolActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String STATION_ID_EXTRA = "Station_ID";
    public final static String ROOM_ID_EXTRA = "room_ID";
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_download_station);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        mApiClient = new ApiRestClientInterface_(this);
        injectExtras_();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static DoingToolActivity_.IntentBuilder_ intent(Context context) {
        return new DoingToolActivity_.IntentBuilder_(context);
    }

    public static DoingToolActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new DoingToolActivity_.IntentBuilder_(fragment);
    }

    public static DoingToolActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new DoingToolActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (((SdkVersionHelper.getSdkInt()< 5)&&(keyCode == KeyEvent.KEYCODE_BACK))&&(event.getRepeatCount() == 0)) {
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        mSrlAlarm = ((SwipeRefreshLayout) hasViews.findViewById(com.chinatelecom.xjdh.R.id.srl_alarm));
        lv_stationName_grouping = ((ListView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.lv_stationName_grouping));
        if (lv_stationName_grouping!= null) {
            lv_stationName_grouping.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DoingToolActivity_.this.questionClick(position);
                }

            }
            );
        }
        show();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(STATION_ID_EXTRA)) {
                Station_ID = extras_.getInt(STATION_ID_EXTRA);
            }
            if (extras_.containsKey(ROOM_ID_EXTRA)) {
                room_ID = extras_.getInt(ROOM_ID_EXTRA);
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    @Override
    public void updateRoomDevListView() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                DoingToolActivity_.super.updateRoomDevListView();
            }

        }
        );
    }

    @Override
    public void getQuestionData(final int type, final int room_ID) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    DoingToolActivity_.super.getQuestionData(type, room_ID);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<DoingToolActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, DoingToolActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), DoingToolActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), DoingToolActivity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public void startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    fragment_.startActivityForResult(intent, requestCode, lastOptions);
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        activity.startActivityForResult(intent, requestCode, lastOptions);
                    } else {
                        context.startActivity(intent, lastOptions);
                    }
                }
            }
        }

        public DoingToolActivity_.IntentBuilder_ Station_ID(int Station_ID) {
            return super.extra(STATION_ID_EXTRA, Station_ID);
        }

        public DoingToolActivity_.IntentBuilder_ room_ID(int room_ID) {
            return super.extra(ROOM_ID_EXTRA, room_ID);
        }

    }

}
