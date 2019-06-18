package com.chinatelecom.xjdh.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import com.chinatelecom.xjdh.view.GridImageView_;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;

@EActivity(R.layout.answer_view)
public class AnswerActivity extends BaseActivity {

	@Extra
	String question;
	@Extra
	int room_ID;
	@Extra
	int station_code;
	@Extra
	int question_ID;
	@Extra
	double question_DevID;
	@ViewById
	TextView question_name;
	@ViewById
	GridView img_gridview;
	@ViewById
	Button submit;
	@ViewById
	RadioButton yes;
	public static Bitmap bimap;
	private GridAdapter adapter;
	public static final int CAPTURE_PICTURE = 1;
	public static final int VIEW_PHOTOS = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
		adapter = new GridAdapter(this);
		adapter.SetBitmapList(mBitmapList);

	}

	@AfterViews
	public void ShowView() {
		question_name.setText(question);
		img_gridview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		getUserInfo();
	}

	@RestService
	ApiRestClientInterface mApiClient;

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

	@Background
	public void DoWorkerUpload(int station_code, int room_ID, int user_ID, int question_ID) {
		try {
			MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
			for (ImageItem item : mBitmapList) {
				// byte[] imageBytes = FileUtils.getFileData(path);
				// for (int i = 0; i < 2; i++) {
				// L.e("----" + item.getImagePath());
				// }
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				item.getBitmap().compress(Bitmap.CompressFormat.JPEG, 90, stream);// 压缩10%把压缩后的数据存放到stream中
				// byte[] imageBytes = stream.toByteArray();
				ByteArrayResource image = new ByteArrayResource(stream.toByteArray(), item.getImagePath())
				 {
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
				formData.add("typeID", String.valueOf(CAPTURE_PICTURE));
				formData.add("substationID", String.valueOf(station_code));
				formData.add("roomID",String.valueOf(room_ID) );
				formData.add("userID", String.valueOf(user_ID));
				formData.add("questionID", String.valueOf(question_ID));
			}
			L.e("@@@@formData" + formData);
			mApiClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
			ApiResponseUpLoad resp = mApiClient.CheckUpload(formData);

			L.e("上传照片........" + resp.getRet() + "" + resp.getData() + "formData"
					+ formData.get(SharedConst.UPLOAD_IMG));
			L.e("NewGrouping :" + resp.getNewGrouping());
			if (resp.getRet() == 0) {
				ShowResponse(resp);
				return;
			}
		}catch (Exception ex) {
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

	@ItemClick(R.id.img_gridview)
	public void OnGridItemClick(int position) {
		if (position == mBitmapList.size()) {
			String fileName = String.valueOf(System.currentTimeMillis());
			cameraFile = new File(FileImgpath.getImagePath(fileName));
			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
			startActivityForResult(openCameraIntent, CAPTURE_PICTURE);
		} else {
			// Show Big Images
			ViewPhotosActivity_.intent(this).extra("position", position).imagePathList(imagePathList)
					.startForResult(VIEW_PHOTOS);
		}
	}

	@Click(R.id.submit)
	void submitClick() {
		DoWorkerUpload(station_code, room_ID, mUserInfo.getId(), question_ID);
	}

	File cameraFile;
	private ArrayList<ImageItem> mBitmapList = new ArrayList<ImageItem>();

	private ArrayList<String> imagePathList = new ArrayList<String>();

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		switch (requestCode) {
		case CAPTURE_PICTURE:
			if (resultCode == Activity.RESULT_OK) {

				if (cameraFile.exists()) {
					try {
						FileInputStream fis = new FileInputStream(cameraFile);

						Display display = getWindowManager().getDefaultDisplay();
						int dw = display.getWidth();
						int dh = display.getHeight();

						// 加载图像
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inJustDecodeBounds = true;// 设置之后可以设置长宽
						Bitmap bitmap = BitmapFactory.decodeFile(cameraFile.getAbsolutePath(), options);

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
						bitmap = BitmapFactory.decodeFile(cameraFile.getAbsolutePath(), options);

						fis.close();

						imagePathList.add(cameraFile.getAbsolutePath());

						ImageItem takePhoto = new ImageItem();
						takePhoto.setImagePath(cameraFile.getName());
						takePhoto.setBitmap(bitmap);
						mBitmapList.add(takePhoto);
						// adapter.SetBitmapList(mBitmapList);
						adapter.notifyDataSetChanged();
					} catch (Exception ex) {

					}
				}
			}
			break;
		case VIEW_PHOTOS:
			if (resultCode == Activity.RESULT_OK) {
				imagePathList = (ArrayList<String>) intent.getExtras().get("imageList");
				mBitmapList = new ArrayList<ImageItem>();
				for (String imagePath : imagePathList) {
					ImageItem takePhoto = new ImageItem();
					takePhoto.setImagePath(imagePath);
					mBitmapList.add(takePhoto);
				}
				adapter.SetBitmapList(mBitmapList);
				adapter.notifyDataSetChanged();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private int selectedPosition = -1;
		private boolean shape;

		private ArrayList<ImageItem> mBitmapList;
		private Context context;// 运行上下文

		public void SetBitmapList(ArrayList<ImageItem> imageList) {
			mBitmapList = imageList;
		}

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			super();
			this.context = context;
		}

		public int getCount() {
			return mBitmapList.size() + 1;
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
			if (convertView == null) {
				convertView = GridImageView_.build(context);
			}

			if (position == mBitmapList.size()) {
				((GridImageView_) convertView)
						.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
			} else {
				((GridImageView_) convertView).setImage(mBitmapList.get(position).getBitmap());
			}
			return convertView;
		}
	}

}
