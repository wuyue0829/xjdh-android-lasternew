package com.chinatelecom.xjdh.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.RoomBean;

import java.util.List;


public class ChooseVdioeRoomActivity extends AppCompatActivity {

    private RecyclerView rv;
    private RelativeLayout rl_head_return;
    private static final int PERMISSIONS_REQUEST_CAMERA = 202;
    private String stationcode;
    private List<RoomBean.Data> roomBeanList;
    private int points;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_room);
        rv = (RecyclerView) findViewById(R.id.rv);
        rl_head_return = (RelativeLayout) findViewById(R.id.rl_head_return);
        rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        stationcode = getIntent().getExtras().getString("stationcode");
        rl_head_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        request();

    }


   /* public void displayCustomToast(final String message, final int duration) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toastRoot));

        TextView toastText = (TextView) layout.findViewById(R.id.toastMessage);
        toastText.setText(message);

        final Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }


    private class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        private final List<String> list;

        public MyAdapter() {
            list = new ArrayList<String>();
            for (RoomBean.Data substation:roomBeanList) {
                list.add(substation.getName());
            }
        }

        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_choose, parent, false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        public void onBindViewHolder(MyHolder holder, final int position) {

            String item = list.get(position);
            holder.textView.setText(item);
            holder.rl_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int camera = getPackageManager().checkPermission(Manifest.permission.CAMERA, getPackageName());
                    if (camera == PackageManager.PERMISSION_GRANTED) {
                        StyledDialog.buildLoading("正在获取机房数据").show();
                        callOutgoing(roomBeanList.get(position).getPhone_number(),roomBeanList.get(position).getName());
                    } else {
                        checkAndRequestPermission(Manifest.permission.CAMERA, PERMISSIONS_REQUEST_CAMERA);
                        points = position;
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
            textView = itemView.findViewById(R.id.tv_title);
            rl_item = itemView.findViewById(R.id.rl_item);
        }
    }


    private void callOutgoing(String number, String showName) {
        LinphonePreferences mPrefs = LinphonePreferences.instance();
        mPrefs.enableVideo(true);
        LinphoneCore lc = LinphoneManager.getLc();
        lc.muteMic(true);
        dialNumber(number,showName);
    }

    public void checkAndRequestPermission(String permission, int result) {

        ArrayList<String> permissionsList = new ArrayList<String>();

        int recordAudio = getPackageManager().checkPermission(Manifest.permission.RECORD_AUDIO, getPackageName());

        int permissionGranted = getPackageManager().checkPermission(permission, getPackageName());


        if (recordAudio != PackageManager.PERMISSION_GRANTED) {
            if (LinphonePreferences.instance().firstTimeAskingForPermission(Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                org.linphone.mediastream.Log.i("[Permission] Asking for record audio");
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            }
        }

        if (permissionsList.size() > 0) {
            String[] permissions = new String[permissionsList.size()];
            permissions = permissionsList.toArray(permissions);
            ActivityCompat.requestPermissions(this, permissions, 0);
        }


        if (permissionGranted != PackageManager.PERMISSION_GRANTED) {
            if (LinphonePreferences.instance().firstTimeAskingForPermission(permission) || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, result);
            }
        } else {
            StyledDialog.buildLoading("正在获取机房数据").show();
            callOutgoing(roomBeanList.get(points).getPhone_number(),roomBeanList.get(points).getName());
        }


    }


    public static void dialNumber(String number, String showName) {
        if (LinphoneManager.getLc().getCallsNb() == 0) {
            LinphoneManager.getInstance().newOutgoingCall(number, showName);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, final int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            org.linphone.mediastream.Log.i("[Permission] " + permissions[i] + " is " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "granted" : "denied"));
        }

        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA:
                UIThreadDispatcher.dispatch(new Runnable() {
                    @Override
                    public void run() {
                        acceptCallUpdate(grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    }
                });
                break;
        }
    }

    public void acceptCallUpdate(boolean accept) {

        LinphoneCall call = LinphoneManager.getLc().getCurrentCall();
        if (call == null) {
            return;
        }

        LinphoneCallParams params = LinphoneManager.getLc().createCallParams(call);
        if (accept) {
            params.setVideoEnabled(true);
            LinphoneManager.getLc().enableVideo(true, true);
        }
    }


    public void request() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://xt.devops123.net/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();
        StyledDialog.buildLoading("数据加载中").show();
        final GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        Call<RoomBean> call = request.getRoomCall("Api/Get_Room_List/" + stationcode, SysConfig.getConfig(getApplicationContext()).getToken());
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            StyledDialog.dismissLoading();
            LinphoneCall call = LinphoneManager.getLc().getCurrentCall();
            LinphoneManager.getLc().terminateCall(call);
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
