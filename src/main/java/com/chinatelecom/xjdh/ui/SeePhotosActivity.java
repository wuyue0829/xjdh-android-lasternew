package com.chinatelecom.xjdh.ui;

import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.utils.ImageItem;
import com.chinatelecom.xjdh.zoom.PhotoView;
import com.chinatelecom.xjdh.zoom.ViewPagerFixed;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

@EActivity(R.layout.view_photos)
public class SeePhotosActivity extends BaseActivity {
	@ViewById(R.id.vpGallery)
	ViewPagerFixed vpGallery;
	@ViewById
	Button btnDelete,btnDone;
	@Extra
	int position;
	@Extra
	String[] editimgpath;
	@Extra
	ArrayList<String> image_list;
	
	private MyPageAdapter adapter;
	private ArrayList<View> listViews = null;
	private ArrayList<ImageItem> itemList = new ArrayList<ImageItem>();
	private int currentIndex = 0;
	
	@AfterViews
	public void ShowView()
	{
		btnDelete.setVisibility(View.GONE);
		btnDone.setVisibility(View.GONE);
		LoadData();
	}
	@Background
	public void LoadData()
	{
		if(editimgpath != null)
		{
			for(String imgPath:editimgpath)
			{
				ImageItem item = new ImageItem();
				item.setImagePath(imgPath);
				item.getBitmap();
				itemList.add(item);
			}
		}
		Show();
		
	}
	
	@UiThread
	public void Show()
	{
		listViews = new ArrayList<View>();
		if(itemList.size() > 0)
		{
			for(ImageItem item:itemList)
			{
				PhotoView img = new PhotoView(this);
				img.setBackgroundColor(0xff000000);
				img.setImageBitmap(item.getBitmap());
				img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
				listViews.add(img);
			}
		}else if(image_list.size() > 0)
		{
			//hide the buttons
			ViewGroup view = (ViewGroup) getWindow().getDecorView();
			view.findViewById(R.id.bottom_layout).setVisibility(View.GONE);
			for(String url : image_list)
			{
				PhotoView img = new PhotoView(this);
				img.setBackgroundColor(0xff000000);
				Picasso.with(this).load(url).into(img);
				img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
				listViews.add(img);
			}
		}
		
		adapter = new MyPageAdapter(listViews);
		vpGallery.setAdapter(adapter);
		vpGallery.addOnPageChangeListener(pageChangeListener);
		vpGallery.setCurrentItem(position);
	}
	
	
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			currentIndex = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
//	@Click(R.id.btnDelete)
//	public void OnBtnDeleteClicked()
//	{
//		if (currentIndex !=0) {
//			editimgpath.remove(currentIndex);
//			listViews.remove(currentIndex);
//		}
//		adapter.setListViews(listViews);
//		adapter.notifyDataSetChanged();
//	}
//	
//	@Click(R.id.btnDone)
//	public void OnBtnDoneClicked()
//	{
//		//we need to send what left back
//		Intent intent = new Intent();
//		intent.putExtra("imageList", editimgpath);
//		setResult(RESULT_OK, intent);
//		finish();
//	}
	
	
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
				((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
				
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
	
	
	
}
