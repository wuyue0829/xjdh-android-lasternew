package com.chinatelecom.xjdh.view;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;

@EViewGroup(R.layout.grid_image_item)
public class GridImageView extends LinearLayout {
	@ViewById(R.id.ivPhoto)
    protected ImageView ivPhoto;

	private Context context;
    public GridImageView(Context context) {
        super(context);
        this.context = context;
    }

    public void setImage(Bitmap bmp) {
    	ivPhoto.setImageBitmap(bmp);
    }
    
    public void setImageUrl(String url)
    {
   	Picasso.with(context).load(url).into(ivPhoto);
    }
}
