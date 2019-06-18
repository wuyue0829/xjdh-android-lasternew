package com.chinatelecom.xjdh.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

public class CustomCameraSurface extends SurfaceView implements SurfaceHolder.Callback {

    static private CustomCameraSurface instance;

    private LinearLayout frame = null;
    private RelativeLayout innerFrame = null;

    private Camera camera;
    private SurfaceHolder previewHolder;

    private boolean inPreview = false;
    private boolean cameraConfigured = false;

    private Camera.Size size;


    static public CustomCameraSurface getInstance(Activity activity) {
        if (CustomCameraSurface.instance == null) {
            CustomCameraSurface.instance = new CustomCameraSurface(activity);
        }
        return CustomCameraSurface.instance;
    }

    public void onStart() {
        camera = Camera.open(1);
        if (size != null) {
            initPreview(size.width, size.height);
        }
        startPreview();
    }


    public void onStop() {
        if (inPreview && camera != null) {
            camera.stopPreview();
        }
        if (camera != null)
            camera.release();
        camera = null;
        inPreview = false;
        setVisibility(GONE);
    }


    private CustomCameraSurface(Context context) {
        super(context);
        init();
    }

    public CustomCameraSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCameraSurface(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化
    void init() {
        previewHolder = getHolder();
        previewHolder.addCallback(this);
        previewHolder.setFormat(PixelFormat.TRANSPARENT);
        innerFrame = new RelativeLayout(getContext());
        innerFrame.addView(this);
        frame = new LinearLayout(getContext());
        frame.addView(innerFrame);



    }


    public LinearLayout getFrame() {
        return frame;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        initPreview(width, height);
        startPreview();
    }

    public void surfaceCreated(SurfaceHolder holder) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // 记得销毁，释放相机
        onStop();
    }

    private Camera.Size getBestPreviewSize(int width, int height,
                                           Camera.Parameters parameters) {
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        this.size = result;
        return (result);
    }

    private void initPreview(int width, int height) {
        //利用布局，重设宽高来撑开界面，超出的部分直接撑出屏幕，至于为什么用这两个布局，自己去谷歌了！
        if (camera != null && previewHolder.getSurface() != null) {

            if (!cameraConfigured) {
                Camera.Parameters parameters = camera.getParameters();
                Camera.Size preSize = getCloselyPreSize(true, width, height, parameters.getSupportedPreviewSizes());
                parameters.setPreviewSize(preSize.width, preSize.height);
                camera.setParameters(parameters);
            }

            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setDisplayOrientation(90);
            } catch (Throwable t) {
            //错误处理，自己做相应的逻辑
                setVisibility(GONE);
            }
        }
    }

    private void startPreview() {
        if (cameraConfigured && camera != null) {
            camera.startPreview();
            inPreview = true;
        }
    }

    public static  Camera.Size getCloselyPreSize(boolean isPortrait, int surfaceWidth, int surfaceHeight, List<Camera.Size> preSizeList) {
        int reqTmpWidth;
        int reqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (isPortrait) {
            reqTmpWidth = surfaceHeight;
            reqTmpHeight = surfaceWidth;
        } else {
            reqTmpWidth = surfaceWidth;
            reqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for (Camera.Size size : preSizeList) {
            if ((size.width == reqTmpWidth) && (size.height == reqTmpHeight)) {
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) reqTmpWidth) / reqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : preSizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }

        return retSize;
    }

}