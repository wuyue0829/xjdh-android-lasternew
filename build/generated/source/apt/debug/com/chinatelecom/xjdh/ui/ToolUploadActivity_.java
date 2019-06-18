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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.bean.DevTypeItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface_;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class ToolUploadActivity_
    extends ToolUploadActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String STATION_CODE_EXTRA = "station_code";
    public final static String ROOM_ID_EXTRA = "room_ID";
    public final static String POS_EXTRA = "pos";
    public final static String DEV_TYPE_ITEM_EXTRA = "devTypeItem";
    public final static String QUESTION_DEV_ID_EXTRA = "question_DevID";
    public final static String QUESTION_EXTRA = "question";
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.act_newdata);
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

    public static ToolUploadActivity_.IntentBuilder_ intent(Context context) {
        return new ToolUploadActivity_.IntentBuilder_(context);
    }

    public static ToolUploadActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new ToolUploadActivity_.IntentBuilder_(fragment);
    }

    public static ToolUploadActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new ToolUploadActivity_.IntentBuilder_(supportFragment);
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
        yes = ((CheckBox) hasViews.findViewById(com.chinatelecom.xjdh.R.id.yes));
        submit = ((Button) hasViews.findViewById(com.chinatelecom.xjdh.R.id.submit));
        gridview = ((GridView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.gridview));
        question_name = ((TextView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.question_name));
        if (submit!= null) {
            submit.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    ToolUploadActivity_.this.submitClick();
                }

            }
            );
        }
        {
            View view = hasViews.findViewById(com.chinatelecom.xjdh.R.id.new_data);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        ToolUploadActivity_.this.newDataClick();
                    }

                }
                );
            }
        }
        if (gridview!= null) {
            gridview.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ToolUploadActivity_.this.OnGridItemClick(position);
                }

            }
            );
        }
        ShowView();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(STATION_CODE_EXTRA)) {
                station_code = extras_.getInt(STATION_CODE_EXTRA);
            }
            if (extras_.containsKey(ROOM_ID_EXTRA)) {
                room_ID = extras_.getInt(ROOM_ID_EXTRA);
            }
            if (extras_.containsKey(POS_EXTRA)) {
                pos = extras_.getInt(POS_EXTRA);
            }
            if (extras_.containsKey(DEV_TYPE_ITEM_EXTRA)) {
                devTypeItem = extras_.getParcelable(DEV_TYPE_ITEM_EXTRA);
            }
            if (extras_.containsKey(QUESTION_DEV_ID_EXTRA)) {
                question_DevID = extras_.getString(QUESTION_DEV_ID_EXTRA);
            }
            if (extras_.containsKey(QUESTION_EXTRA)) {
                question = extras_.getString(QUESTION_EXTRA);
            }
        }
    }

    @Override
    public void setIntent(Intent newIntent) {
        super.setIntent(newIntent);
        injectExtras_();
    }

    @Override
    public void ShowResponse(final ApiResponseUpLoad resp) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                ToolUploadActivity_.super.ShowResponse(resp);
            }

        }
        );
    }

    @Override
    public void getUserInfo() {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ToolUploadActivity_.super.getUserInfo();
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    @Override
    public void DoWorkerUpload(final int station_code, final int user_ID, final int room_ID, final String question_DevID) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    ToolUploadActivity_.super.DoWorkerUpload(station_code, user_ID, room_ID, question_DevID);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<ToolUploadActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, ToolUploadActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), ToolUploadActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), ToolUploadActivity_.class);
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

        public ToolUploadActivity_.IntentBuilder_ station_code(int station_code) {
            return super.extra(STATION_CODE_EXTRA, station_code);
        }

        public ToolUploadActivity_.IntentBuilder_ room_ID(int room_ID) {
            return super.extra(ROOM_ID_EXTRA, room_ID);
        }

        public ToolUploadActivity_.IntentBuilder_ pos(int pos) {
            return super.extra(POS_EXTRA, pos);
        }

        public ToolUploadActivity_.IntentBuilder_ devTypeItem(DevTypeItem devTypeItem) {
            return super.extra(DEV_TYPE_ITEM_EXTRA, devTypeItem);
        }

        public ToolUploadActivity_.IntentBuilder_ question_DevID(String question_DevID) {
            return super.extra(QUESTION_DEV_ID_EXTRA, question_DevID);
        }

        public ToolUploadActivity_.IntentBuilder_ question(String question) {
            return super.extra(QUESTION_EXTRA, question);
        }

    }

}
