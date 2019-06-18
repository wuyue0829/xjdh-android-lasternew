package com.chinatelecom.xjdh.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.bean.UserInfo;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.utils.FileImgpath;
import com.chinatelecom.xjdh.utils.ImageItem;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.utils.URLs;
import com.chinatelecom.xjdh.view.AutoGridView;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EActivity(R.layout.edit_view)
public class EditActivity extends BaseActivity {
	@Extra
	String question;
	@Extra
	String Titles;
	@Extra
	String[] editimgpath;
	@Extra
	String Device_id;
	@Extra
	int station_code;
	@Extra
	int question_ID;
	@ViewById
	TextView name,title_name;
	@ViewById
	AutoGridView answer_gridview;
	@ViewById
	GridView gridview;
	@ViewById
	Button submit;
	private ImgGridAdapter adapter;
	@RestService
	ApiRestClientInterface mApiClient;
	private LinearLayout ll_popup;
	private PopupWindow pop = null;
	public static Bitmap bimap;
	public static final int CAPTURE_PICTURE = 1;
	private static final int REQUEST_IMAGE = 3;
	public static final int VIEW_PHOTOS = 2;
	public static final int SEE_PHOTOS =4;
	private GridAdapter upadapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("局站验收");
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
	}

	
	@AfterViews
	void showView() {
		name.setText(question);
		title_name.setText(Titles);
		Init();
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		upadapter = new GridAdapter(this);
		adapter = new ImgGridAdapter(this,editimgpath);
		upadapter.SetBitmapList(mBitmapList);
		gridview.setAdapter(upadapter);
		answer_gridview.setAdapter(adapter);
		upadapter.notifyDataSetChanged();
		getUserInfo();
	}
	
	UserInfo mUserInfo;

	@Background
	void getUserInfo() {
		try {
			ApiResponse mApiResps = mApiClient.getUserInfo();
			if (mApiResps.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				L.d("&&&&&&&&&&&&&&&&&&&&&&&&&&", mapper.writeValueAsString(mApiClient.getUserInfo().toString()));

				mUserInfo = mapper.readValue(mApiResps.getData(), UserInfo.class);
				L.d("++++++++++++", mapper.writeValueAsString(mUserInfo.toString()));
				return;
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
	}

	
	public void Init() {

		pop = new PopupWindow(EditActivity.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQUEST_IMAGE);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		upadapter.notifyDataSetChanged(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
	}

	@Background
	public void DoWorkerUpload(int station_code, int user_ID, int question_ID) {
		try {
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			for (ImageItem path : mBitmapList) {
				// byte[] imageBytes = FileUtils.getFileData(path);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				path.getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, stream);
				ByteArrayResource image = new ByteArrayResource(stream.toByteArray(), path.getImagePath()) {
					@Override
					public String getFilename() throws IllegalStateException {
						return super.getDescription();
					}

					@Override
					public String getDescription() {
						return super.getDescription();
					}
				};
				formData.add("uploadImg[]", image);

			}
			formData.add("type", String.valueOf(CAPTURE_PICTURE));
			formData.add("id", String.valueOf(station_code));
			// formData.add("roomID",String.valueOf(room_ID) );
//			formData.add("userID", String.valueOf(user_ID));
			formData.add("questionID", String.valueOf(question_ID));
			L.e("@@@@formData" + formData);
			mApiClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
			ApiResponseUpLoad resp = mApiClient.editQuestion(formData);

			L.e("上传照片........" + resp.getRet() + "" + resp.getData() + "formData"
					+ formData.get(SharedConst.UPLOAD_IMG));
			L.e("NewGrouping :" + resp.getNewGrouping());
			if (resp.getRet() == 0) {
				ShowResponse(resp);
				return;
			}
		} catch (Exception ex) {
			ApiResponseUpLoad resp = new ApiResponseUpLoad();
			resp.setRet(1);
			resp.setData("请求失败");
			ShowResponse(resp);
			L.e("Exception" + ex.toString());
			L.e("Exception" + resp.getRet());
		}

	}

	@UiThread
	public void ShowResponse(ApiResponseUpLoad resp) {
		if (resp.getRet() == 0) {
			T.showLong(this, "上传成功");
			finish();
		} else {
			T.showLong(this, resp.getData());
		}
	}

	private ArrayList<ImageItem> mBitmapList = new ArrayList<ImageItem>();
	
	

	@ItemClick(R.id.gridview)
	void OnGridItemClick(int position) {
		if (position == mBitmapList.size()) {
			ll_popup.startAnimation(
					AnimationUtils.loadAnimation(EditActivity.this, R.anim.activity_translate_in));
			pop.showAtLocation(gridview, Gravity.BOTTOM, 0, 0);
		} else {
			ViewPhotosActivity_.intent(this).extra("position", position).imagePathList(imagePathList)
					.startForResult(VIEW_PHOTOS);
		}
	}
	
	@ItemClick(R.id.answer_gridview)
	void seeImageClick(int pos){
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		ImageView imgView = getView(pos);
		dialog.setView(imgView);
		dialog.show();
		imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
	private ImageView getView(int pos) {
		ImageView imgView = new ImageView(this);
		imgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				Picasso.with(getApplicationContext())
				.load(URLs.HTTP + URLs.HOST + "/public/portal/Check_image/" + editimgpath[pos])
				.into(imgView);

		return imgView;
	}

	@Click(R.id.submit)
	void submitClick() {
		DoWorkerUpload(station_code, mUserInfo.getId(), question_ID);

	}

	
	private static final int TAKE_PICTURE = 0x000001;
	File vFile;

	public void photo() {
		String fileName = String.valueOf(System.currentTimeMillis());
		vFile = new File(FileImgpath.getImagePath(fileName));
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(vFile));
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	private ArrayList<String> imagePathList = new ArrayList<String>();

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (vFile.exists() && resultCode == RESULT_OK) {
				try {
					FileInputStream fis = new FileInputStream(vFile);

					Display display = getWindowManager().getDefaultDisplay();
					int dw = display.getWidth();
					int dh = display.getHeight();

					// 加载图像
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;// 设置之后可以设置长宽
					Bitmap bitmap = BitmapFactory.decodeFile(vFile.getAbsolutePath(), options);

					int heightRatio = (int) Math.ceil(options.outHeight / (float) dh);
					int widthRatio = (int) Math.ceil(options.outWidth / (float) dw);

					// 判断长宽哪个大
					if (heightRatio > 1 && widthRatio > 1) {
						if (heightRatio > widthRatio) {
							options.inSampleSize = heightRatio;
						} else {
							options.inSampleSize = widthRatio;
						}
					}
					// 对它进行真正的解码
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeFile(vFile.getAbsolutePath(), options);

					fis.close();

					imagePathList.add(vFile.getAbsolutePath());
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(vFile.getName());
					takePhoto.setBitmap(bitmap);
					mBitmapList.add(takePhoto);
					upadapter.SetBitmapList(mBitmapList);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case REQUEST_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			if (!TextUtils.isEmpty(uri.getAuthority())) {

				// 查询选择图片
				Cursor cursor = getContentResolver().query(uri, new String[] { MediaStore.Images.Media.DATA }, null,
						null, null);
				// 返回 没找到选择图片
				if (null == cursor) {
					return;
				}
				// 光标移动至开头 获取图片路径
				cursor.moveToFirst();
				String pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
				// FileInputStream fis = new FileInputStream(vFile);

				Display display = getWindowManager().getDefaultDisplay();
				int dw = display.getWidth();
				int dh = display.getHeight();

				// 加载图像
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;// 设置之后可以设置长宽
				Bitmap bitmap = BitmapFactory.decodeFile(pathImage, options);

				int heightRatio = (int) Math.ceil(options.outHeight / (float) dh);
				int widthRatio = (int) Math.ceil(options.outWidth / (float) dw);

				// 判断长宽哪个大
				if (heightRatio > 1 && widthRatio > 1) {
					if (heightRatio > widthRatio) {
						options.inSampleSize = heightRatio;
					} else {
						options.inSampleSize = widthRatio;
					}
				}
				// 对它进行真正的解码
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(pathImage, options);

				imagePathList.add(pathImage);
				ImageItem takePhoto = new ImageItem();
				takePhoto.setImagePath(pathImage);
				takePhoto.setBitmap(bitmap);
				mBitmapList.add(takePhoto);
				upadapter.SetBitmapList(mBitmapList);
			}
			}
			break;
		case VIEW_PHOTOS:
			if (resultCode == Activity.RESULT_OK) {
				imagePathList = (ArrayList<String>) data.getExtras().get("imageList");
				mBitmapList = new ArrayList<ImageItem>();
				for (String imagePath : imagePathList) {
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(imagePath);
					mBitmapList.add(takePhoto);
				}
				upadapter.SetBitmapList(mBitmapList);
				upadapter.notifyDataSetChanged();
			}
			break;
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;
		private ArrayList<ImageItem> mBitmapList;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void SetBitmapList(ArrayList<ImageItem> imageList) {
			mBitmapList = imageList;
		}

		public int getCount() {
			return (mBitmapList.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == mBitmapList.size()) {
				holder.image
						.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
			} else {
				holder.image.setImageBitmap(mBitmapList.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

	}



	class ImgGridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context con;
		private String[] editimgpath;

		public ImgGridAdapter(Context con, String[] editimgpath) {
			this.editimgpath = editimgpath;
			inflater = LayoutInflater.from(con);
		}

		@Override
		public int getCount() {
			return editimgpath.length;
			
		}

		@Override
		public Object getItem(int position) {
			return editimgpath[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.lv_item_img, null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.lv_item_iv_img);
				LayoutParams p = holder.img.getLayoutParams();
				p.width = p.height = mGetScreenWidth() / 3 - 20;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (editimgpath.length == 0) {
				holder.img.setImageResource(R.drawable.ic_launcher);
			}else{
			Picasso.with(getApplicationContext())
					.load(URLs.HTTP + URLs.HOST + "/public/portal/Check_image/" + editimgpath[position])
					.into(holder.img);
			}
			return convertView;
		}

		private class ViewHolder {
			ImageView img;
		}

		private int mGetScreenWidth() {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			return dm.widthPixels;
		}
	}
}
