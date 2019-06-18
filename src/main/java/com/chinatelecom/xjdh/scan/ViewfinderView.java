/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chinatelecom.xjdh.scan;

import java.util.Collection;
import java.util.HashSet;

import com.chinatelecom.xjdh.R;
import com.google.zxing.ResultPoint;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 鑷畾涔夌粍浠跺疄鐜�,鎵弿鍔熻兘
 */
public final class ViewfinderView extends View {

    private static final long ANIMATION_DELAY = 100L;
    private static final int OPAQUE = 0xFF;

    private final Paint paint;
    private Bitmap resultBitmap;
    private final int maskColor;
    private final int resultColor;
    private final int resultPointColor;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);
        resultPointColor = resources.getColor(R.color.possible_result_points);
        possibleResultPoints = new HashSet<ResultPoint>(5);

        scanLight = BitmapFactory.decodeResource(resources,
                R.drawable.scan_light);

        initInnerRect(context, attrs);
    }

    /**
     * 鍒濆鍖栧唴閮ㄦ鐨勫ぇ灏�
     * @param context
     * @param attrs
     */
    private void initInnerRect(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.innerrect);

        // 鎵弿妗嗚窛绂婚《閮�
        int innerMarginTop = ta.getInt(R.styleable.innerrect_inner_margintop, -1);
        if (innerMarginTop != -1) {
            CameraManager.FRAME_MARGINTOP = dip2px(context, innerMarginTop);
        }

        // 鎵弿妗嗙殑瀹藉害
        int innerrectWidth = ta.getInt(R.styleable.innerrect_inner_width, 210);
        CameraManager.FRAME_WIDTH = dip2px(context, innerrectWidth);

        // 鎵弿妗嗙殑楂樺害
        int innerrectHeight = ta.getInt(R.styleable.innerrect_inner_height, 210);
        CameraManager.FRAME_HEIGHT = dip2px(context, innerrectHeight);

        // 鎵弿妗嗚竟瑙掗鑹�
        innercornercolor = ta.getColor(R.styleable.innerrect_inner_corner_color, Color.parseColor("#45DDDD"));
        // 鎵弿妗嗚竟瑙掗暱搴�
        innercornerlength = ta.getInt(R.styleable.innerrect_inner_corner_length, 65);
        // 鎵弿妗嗚竟瑙掑搴�
        innercornerwidth = ta.getInt(R.styleable.innerrect_inner_corner_width, 15);

        // 鎵弿bitmap
        Drawable drawable = ta.getDrawable(R.styleable.innerrect_inner_scan_bitmap);
        if (drawable != null) {
        }

        // 鎵弿鎺т欢
        scanLight = BitmapFactory.decodeResource(getResources(), ta.getResourceId(R.styleable.innerrect_inner_scan_bitmap, R.drawable.scan_light));
        // 鎵弿閫熷害
        SCAN_VELOCITY = ta.getInt(R.styleable.innerrect_inner_scan_speed, 5);

        ta.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            drawFrameBounds(canvas, frame);

            drawScanLight(canvas, frame);

            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top + point.getY(), 3.0f, paint);
                }
            }

            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    // 鎵弿绾跨Щ鍔ㄧ殑y
    private int scanLineTop;
    // 鎵弿绾跨Щ鍔ㄩ�熷害
    private int SCAN_VELOCITY;
    // 鎵弿绾�
    Bitmap scanLight;

    /**
     * 缁樺埗绉诲姩鎵弿绾�
     *
     * @param canvas
     * @param frame
     */
    private void drawScanLight(Canvas canvas, Rect frame) {

        if (scanLineTop == 0) {
            scanLineTop = frame.top;
        }

        if (scanLineTop >= frame.bottom - 30) {
            scanLineTop = frame.top;
        } else {
            scanLineTop += SCAN_VELOCITY;
        }
        Rect scanRect = new Rect(frame.left, scanLineTop, frame.right,
                scanLineTop + 30);
        canvas.drawBitmap(scanLight, null, scanRect, paint);
    }


    // 鎵弿妗嗚竟瑙掗鑹�
    private int innercornercolor;
    // 鎵弿妗嗚竟瑙掗暱搴�
    private int innercornerlength;
    // 鎵弿妗嗚竟瑙掑搴�
    private int innercornerwidth;

    /**
     * 缁樺埗鍙栨櫙妗嗚竟妗�
     *
     * @param canvas
     * @param frame
     */
    private void drawFrameBounds(Canvas canvas, Rect frame) {

        /*paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawRect(frame, paint);*/

        paint.setColor(innercornercolor);
        paint.setStyle(Paint.Style.FILL);

        int corWidth = innercornerwidth;
        int corLength = innercornerlength;

        // 宸︿笂瑙�
        canvas.drawRect(frame.left, frame.top, frame.left + corWidth, frame.top
                + corLength, paint);
        canvas.drawRect(frame.left, frame.top, frame.left
                + corLength, frame.top + corWidth, paint);
        // 鍙充笂瑙�
        canvas.drawRect(frame.right - corWidth, frame.top, frame.right,
                frame.top + corLength, paint);
        canvas.drawRect(frame.right - corLength, frame.top,
                frame.right, frame.top + corWidth, paint);
        // 宸︿笅瑙�
        canvas.drawRect(frame.left, frame.bottom - corLength,
                frame.left + corWidth, frame.bottom, paint);
        canvas.drawRect(frame.left, frame.bottom - corWidth, frame.left
                + corLength, frame.bottom, paint);
        // 鍙充笅瑙�
        canvas.drawRect(frame.right - corWidth, frame.bottom - corLength,
                frame.right, frame.bottom, paint);
        canvas.drawRect(frame.right - corLength, frame.bottom - corWidth,
                frame.right, frame.bottom, paint);
    }


    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }


    /**
     * 鏍规嵁鎵嬫満鐨勫垎杈ㄧ巼浠� dp 鐨勫崟浣� 杞垚涓� px(鍍忕礌)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
