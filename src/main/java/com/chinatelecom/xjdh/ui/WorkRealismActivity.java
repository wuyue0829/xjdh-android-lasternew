package com.chinatelecom.xjdh.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.api.rest.MediaType;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.SearchAdapter;
import com.chinatelecom.xjdh.adapter.SubstationListAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.ApiResponseStationList;
import com.chinatelecom.xjdh.bean.ApiResponseUpLoad;
import com.chinatelecom.xjdh.bean.CityItem;
import com.chinatelecom.xjdh.bean.CountyItem;
import com.chinatelecom.xjdh.bean.RoomItem;
import com.chinatelecom.xjdh.bean.StationList;
import com.chinatelecom.xjdh.bean.SubstationItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.ui.AnswerUploadActivity.GridAdapter;
import com.chinatelecom.xjdh.ui.AnswerUploadActivity.GridAdapter.ViewHolder;
import com.chinatelecom.xjdh.utils.FileImgpath;
import com.chinatelecom.xjdh.utils.FileUtils;
import com.chinatelecom.xjdh.utils.ImageItem;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.view.MyGridView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
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
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;

@EActivity(R.layout.realistic_fragment)
public class WorkRealismActivity extends BaseActivity {
	@ViewById
	TextView tvDate;
	@ViewById
	EditText etPerson, etMemo,spCategory;

	@RestService
	ApiRestClientInterface mApiClient;
	@ViewById(R.id.sp_city)
	Spinner mSpCity;
	@ViewById(R.id.sp_county)
	Spinner mSpCounty;
	@ViewById(R.id.sp_substation)
	Spinner mSpSubstation;
	@ViewById(R.id.sp_room)
	Spinner mSpRoom;
	private List<CityItem> cityList = new ArrayList<CityItem>(0);
	ApiResponse apiResp;
	ProgressDialog pDialog;
	@ViewById
	MyGridView gggridview;
	private LinearLayout ll_popup;
	private PopupWindow pop = null;
	public static Bitmap bimap;
	public static final int CAPTURE_PICTURE = 1;
	private static final int REQUEST_IMAGE = 3;
	public static final int VIEW_PHOTOS = 2;
	private GridAdapter gadapter;
	private ArrayList<ImageItem> mBitmapList = new ArrayList<ImageItem>();
	private ArrayAdapter<String> mCityAdapter;
	private ArrayAdapter<String> mCountyAdapter;
	private ArrayAdapter<String> mSubstationtyAdapter;
	private ArrayAdapter<String> mRoomAdapter;
	
	private int selCity = 0;
	private int selCounty = 0;
	private int selSubstation = 0;
	private int selRoom = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("工作写实");
		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
		pDialog = new ProgressDialog(this);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
	}

	@AfterViews
	void bindData() {
		Init();
		showView();
		gggridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gadapter = new GridAdapter(this);
		gadapter.SetBitmapList(mBitmapList);
		gggridview.setAdapter(gadapter);
		gadapter.notifyDataSetChanged();

	}
	
	public void showView(){
		ObjectMapper mapper = new ObjectMapper();
		/**
		 * cityList全部数据
		 */
		if (cityList.size() == 0) {
			String cityData = new String(FileUtils.getFileData(this, SharedConst.FILE_AREA_JSON));
			try {
				List<CityItem> l = mapper.readValue(cityData, new TypeReference<List<CityItem>>() {
				});
				cityList.addAll(l);
				List<String> cities = new ArrayList<>();
				cities.add("全网");
				for (CityItem cityItem : cityList) {
					cities.add(cityItem.getName());
				}
				mCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
				mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mSpCity.setAdapter(mCityAdapter);
			} catch (Exception e) {
				L.e(e.toString());
			}
		}
		
		
		
		mSpCity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selCity = position;
				List<String> counties = new ArrayList<>();
				counties.add("所有分局");
				if (position > 0) {
					CountyItem[] countyList = cityList.get(selCity - 1).getCountylist();
					for (CountyItem e : countyList) {
						counties.add(e.getName());
					}
				}
				mCountyAdapter = new ArrayAdapter<>(WorkRealismActivity.this, android.R.layout.simple_spinner_item, counties);
				mCountyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mSpCounty.setAdapter(mCountyAdapter);
				selCounty = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});

		mSpCounty.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selCounty = position;
				List<String> substations = new ArrayList<String>();
				substations.add("所有局站");
				if (position > 0) {
					CountyItem countyObj = cityList.get(selCity - 1).getCountylist()[selCounty - 1];
					for (SubstationItem e : countyObj.getSubstationlist()) {
						substations.add(e.getName());
					}
				}
				mSubstationtyAdapter = new ArrayAdapter<>(WorkRealismActivity.this, android.R.layout.simple_spinner_item,
						substations);
				mSubstationtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mSpSubstation.setAdapter(mSubstationtyAdapter);
				selSubstation = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
		mSpSubstation.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selSubstation = position;
				List<String> rooms = new ArrayList<>();
				rooms.add("所有机房");
				if (position > 0) {
					SubstationItem substationObj = cityList.get(selCity - 1).getCountylist()[selCounty - 1]
							.getSubstationlist()[selSubstation - 1];
					for (RoomItem e : substationObj.getRoomlist()) {
						rooms.add(e.getName());
					}
				}
				mRoomAdapter = new ArrayAdapter<>(WorkRealismActivity.this, android.R.layout.simple_spinner_item, rooms);
				mRoomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				mSpRoom.setAdapter(mRoomAdapter);
				selRoom = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});
		mSpRoom.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View v, int position, long arg3) {
				selRoom = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {

			}
		});

	}

	public void Init() {

		pop = new PopupWindow(WorkRealismActivity.this);

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
		gadapter.notifyDataSetChanged(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
	}
	ApiResponse resp;
	@Background
	public void DoWorkerUpload(String citycode, String workCategoryId, String countycode, String person, String memo,
			String date, String substationId, String roomId) {
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
				formData.add("citycode", citycode);
				formData.add("countycode", countycode);
				formData.add("substationId", substationId);
				formData.add("roomId", roomId);
				formData.add("workCategoryId", workCategoryId);
				formData.add("person", person);
				formData.add("memo", memo);
				formData.add("date", date);
				L.d("00000000000000", workCategoryId);
			}
			
			mApiClient.setHeader("Content-Type", MediaType.MULTIPART_FORM_DATA);
			ApiResponseUpLoad resp = mApiClient.SaveUserWork(formData);

			L.e("上传照片........" + resp.getRet() + "" + resp.getData() + "formData"
					+ formData.get(SharedConst.UPLOAD_IMG));
			L.e("NewGrouping :"+resp.getNewGrouping());
				ShowResponsess(resp);
		} catch (Exception ex) {
			ApiResponseUpLoad resp = new ApiResponseUpLoad();
			resp.setRet(1);
			resp.setData("请求失败");
			ShowResponsess(resp);
			L.e("Exception" + ex.toString());
			L.e("Exception" + resp.getRet());

		}

	}

	@UiThread
	public void ShowResponsess(ApiResponseUpLoad resp) {
		if (resp.getRet() == 0) {
			T.showLong(this, "提交成功");
			finish();
		} else {
			T.showLong(this, resp.getData());
		}
	}

	@ItemClick(R.id.gggridview)
	void OnGridItemClick(int position) {
		if (position == mBitmapList.size()) {
			ll_popup.startAnimation(
					AnimationUtils.loadAnimation(WorkRealismActivity.this, R.anim.activity_translate_in));
			pop.showAtLocation(gggridview, Gravity.BOTTOM, 0, 0);
		} else {
			ViewPhotosActivity_.intent(this).extra("position", position).imagePathList(imagePathList)
					.startForResult(VIEW_PHOTOS);
		}
	}

	@Click(R.id.saveMassage)
	void submitClick() {
		
		String citycode = "";
		String countycode = "";
		String substationId = "";
		String roomId = "";
		if (selCity > 0) {
			citycode = cityList.get(selCity - 1).getCity_code();
			if (selCounty > 0) {
				countycode = cityList.get(selCity - 1).getCountylist()[selCounty - 1].getCode();
				if (selSubstation > 0) {
					substationId = cityList.get(selCity - 1).getCountylist()[selCounty - 1]
							.getSubstationlist()[selSubstation - 1].getCode();
					if (selRoom > 0) {
						roomId = cityList.get(selCity - 1).getCountylist()[selCounty - 1]
								.getSubstationlist()[selSubstation - 1].getRoomlist()[selRoom - 1].getId();
					}
				}
			}
		}
		String workCategoryId = spCategory.getText().toString().trim();
		String date = tvDate.getText().toString().trim();
		String person = etPerson.getText().toString().trim();
		String memo = etMemo.getText().toString().trim();
		if (date.isEmpty()) {
			T.showShort(this, "请选择日期");
			return;
		}
		if (person.isEmpty()) {
			T.showShort(this, "请填写参与人");
			return;
		}
//		if (memo.isEmpty()) {
//			T.showShort(this, "请填写工作内容");
//			return;
//		}
		if (workCategoryId.isEmpty()) {
			T.showShort(this, "请填写工作类别");
			return;
		}
		DoWorkerUpload(citycode,workCategoryId, countycode, person, memo, date, substationId, roomId);
	}



	@Click(R.id.quxiao)
	void Canncel(){
		finish();
	}
	
	@FocusChange(R.id.etDate)
	public void onDateClick(View hello, boolean hasFocus) {
		if (hasFocus) {
			onDateClick();
		}
	}

	@Click(R.id.etDate)
	public void onDateClick() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(tvDate.getWindowToken(), 0); // 强制隐藏键盘
		Calendar c = Calendar.getInstance();
		new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker dp, int year, int mounth, int day) {
				tvDate.setText(year + "-" + (mounth + 1) + "-" + day);
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
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
					gadapter.SetBitmapList(mBitmapList);
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
					gadapter.SetBitmapList(mBitmapList);
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
				gadapter.SetBitmapList(mBitmapList);
				gadapter.notifyDataSetChanged();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
