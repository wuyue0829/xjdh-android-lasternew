//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.ui;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import com.chinatelecom.xjdh.R.id;
import com.chinatelecom.xjdh.R.layout;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class TextBluetoothActivity_
    extends TextBluetoothActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_bluetooth);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
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

    public static TextBluetoothActivity_.IntentBuilder_ intent(Context context) {
        return new TextBluetoothActivity_.IntentBuilder_(context);
    }

    public static TextBluetoothActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new TextBluetoothActivity_.IntentBuilder_(fragment);
    }

    public static TextBluetoothActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new TextBluetoothActivity_.IntentBuilder_(supportFragment);
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
        list_search_devices = ((ListView) hasViews.findViewById(id.list_search_devices));
        rd302A = ((RadioButton) hasViews.findViewById(id.rd302A));
        list_bonded_devices = ((ListView) hasViews.findViewById(id.list_bonded_devices));
        btn_close_devices = ((Button) hasViews.findViewById(id.btn_close_devices));
        btn_search_devices = ((Button) hasViews.findViewById(id.btn_search_devices));
        rd301E = ((RadioButton) hasViews.findViewById(id.rd301E));
        if (btn_search_devices!= null) {
            btn_search_devices.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    TextBluetoothActivity_.this.onBtnClick(view);
                }

            }
            );
        }
        if (btn_close_devices!= null) {
            btn_close_devices.setOnClickListener(new OnClickListener() {


                @Override
                public void onClick(View view) {
                    TextBluetoothActivity_.this.onBtnClick(view);
                }

            }
            );
        }
        Show();
    }

    @Override
    public void ShowMessage(final String msg) {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                TextBluetoothActivity_.super.ShowMessage(msg);
            }

        }
        );
    }

    @Override
    public void HideProgress() {
        handler_.post(new Runnable() {


            @Override
            public void run() {
                TextBluetoothActivity_.super.HideProgress();
            }

        }
        );
    }

    @Override
    public void setDevice(final BluetoothDevice device) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    TextBluetoothActivity_.super.setDevice(device);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<TextBluetoothActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, TextBluetoothActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), TextBluetoothActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), TextBluetoothActivity_.class);
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

    }

}
