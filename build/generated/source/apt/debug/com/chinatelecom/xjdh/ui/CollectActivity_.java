//
// DO NOT EDIT THIS FILE.Generated using AndroidAnnotations 3.3.1.
//  You can create a larger work that contains this file and distribute that work under terms of your choice.
//


package com.chinatelecom.xjdh.ui;

import java.sql.SQLException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import com.baidu.mapapi.map.MapView;
import com.chinatelecom.xjdh.R.layout;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.db.DatabaseHelper;
import com.chinatelecom.xjdh.model.FileNameGPSTable;
import com.chinatelecom.xjdh.model.StationTable;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface_;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import org.androidannotations.api.BackgroundExecutor;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class CollectActivity_
    extends CollectActivity
    implements HasViews, OnViewChangedListener
{

    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();
    private DatabaseHelper databaseHelper_;
    public final static String NEW_GROUPING_EXTRA = "newGrouping";
    public final static String STATION_NAME_EXTRA = "stationName";
    private Handler handler_ = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(layout.activity_collet);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        mApiClient = new ApiRestClientInterface_(this);
        databaseHelper_ = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        try {
            stDao = databaseHelper_.getDao(StationTable.class);
        } catch (SQLException e) {
            Log.e("CollectActivity_", "Could not create DAO stDao", e);
        }
        try {
            sDao = databaseHelper_.getDao(FileNameGPSTable.class);
        } catch (SQLException e) {
            Log.e("CollectActivity_", "Could not create DAO sDao", e);
        }
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

    public static CollectActivity_.IntentBuilder_ intent(Context context) {
        return new CollectActivity_.IntentBuilder_(context);
    }

    public static CollectActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new CollectActivity_.IntentBuilder_(fragment);
    }

    public static CollectActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new CollectActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        mMapView = ((MapView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.bmapView));
        tv_Longitude = ((TextView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.tv_Longitude));
        tv_Latitude = ((TextView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.tv_Latitude));
        noScrollgridview = ((GridView) hasViews.findViewById(com.chinatelecom.xjdh.R.id.noScrollgridview));
        {
            View view = hasViews.findViewById(com.chinatelecom.xjdh.R.id.btn_upload_);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CollectActivity_.this.uploadClicked();
                    }

                }
                );
            }
        }
        {
            View view = hasViews.findViewById(com.chinatelecom.xjdh.R.id.btn_gps_collect);
            if (view!= null) {
                view.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View view) {
                        CollectActivity_.this.collectClicked();
                    }

                }
                );
            }
        }
        if (noScrollgridview!= null) {
            noScrollgridview.setOnItemClickListener(new OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CollectActivity_.this.OnGridItemClick(position);
                }

            }
            );
        }
        {
            AdapterView<?> view = ((AdapterView<?> ) hasViews.findViewById(com.chinatelecom.xjdh.R.id.lv_station_list));
            if (view!= null) {
                view.setOnItemClickListener(new OnItemClickListener() {


                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CollectActivity_.this.listStationClicked(position);
                    }

                }
                );
            }
        }
        initData();
    }

    @Override
    public void onDestroy() {
        OpenHelperManager.releaseHelper();
        databaseHelper_ = null;
        super.onDestroy();
    }

    private void injectExtras_() {
        Bundle extras_ = getIntent().getExtras();
        if (extras_!= null) {
            if (extras_.containsKey(NEW_GROUPING_EXTRA)) {
                newGrouping = extras_.getString(NEW_GROUPING_EXTRA);
            }
            if (extras_.containsKey(STATION_NAME_EXTRA)) {
                stationName = extras_.getString(STATION_NAME_EXTRA);
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
                CollectActivity_.super.ShowResponse(resp);
            }

        }
        );
    }

    @Override
    public void DoWorkerUpload(final double longitude, final double latitude, final String stationName, final String newGrouping) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0, "") {


            @Override
            public void execute() {
                try {
                    CollectActivity_.super.DoWorkerUpload(longitude, latitude, stationName, newGrouping);
                } catch (Throwable e) {
                    Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }

        }
        );
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<CollectActivity_.IntentBuilder_>
    {

        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, CollectActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), CollectActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), CollectActivity_.class);
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

        public CollectActivity_.IntentBuilder_ newGrouping(String newGrouping) {
            return super.extra(NEW_GROUPING_EXTRA, newGrouping);
        }

        public CollectActivity_.IntentBuilder_ stationName(String stationName) {
            return super.extra(STATION_NAME_EXTRA, stationName);
        }

    }

}