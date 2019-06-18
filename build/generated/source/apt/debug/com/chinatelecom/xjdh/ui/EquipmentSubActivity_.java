//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.chinatelecom.xjdh.R.id;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface_;
import com.chinatelecom.xjdh.view.MyViewPagerIndicators;
import org.androidannotations.api.SdkVersionHelper;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class EquipmentSubActivity_
    extends EquipmentSubActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    public final static String STATION_ID_EXTRA = "Station_ID";
    public final static String ROOM_ID_EXTRA = "room_ID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.manage_main);
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

    public static EquipmentSubActivity_.IntentBuilder_ intent(Context context) {
        return new EquipmentSubActivity_.IntentBuilder_(context);
    }

    public static EquipmentSubActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new EquipmentSubActivity_.IntentBuilder_(fragment);
    }

    public static EquipmentSubActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new EquipmentSubActivity_.IntentBuilder_(supportFragment);
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
        indicator = ((MyViewPagerIndicators) hasViews.findViewById(id.indicator));
        viewpager = ((ViewPager) hasViews.findViewById(id.viewpager));
        ShowView();
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

    public static class IntentBuilder_
        extends ActivityIntentBuilder<EquipmentSubActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, EquipmentSubActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), EquipmentSubActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), EquipmentSubActivity_.class);
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

        public EquipmentSubActivity_.IntentBuilder_ Station_ID(int Station_ID) {
            return super.extra(STATION_ID_EXTRA, Station_ID);
        }

        public EquipmentSubActivity_.IntentBuilder_ room_ID(int room_ID) {
            return super.extra(ROOM_ID_EXTRA, room_ID);
        }

    }

}
