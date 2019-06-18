package com.chinatelecom.xjdh.ui;//package com.chinatelecom.xjdh.ui;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.Background;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.Extra;
//import org.androidannotations.annotations.UiThread;
//import org.androidannotations.annotations.ViewById;
//import org.androidannotations.annotations.rest.RestService;
//import org.codehaus.jackson.map.ObjectMapper;
//
//import com.chinatelecom.xjdh.R;
//import com.chinatelecom.xjdh.bean.ApiResponse;
//import com.chinatelecom.xjdh.bean.ApiResponseStationList;
//import com.chinatelecom.xjdh.bean.Technology;
//import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
//import com.chinatelecom.xjdh.utils.L;
//import com.chinatelecom.xjdh.utils.PreferenceConstants;
//import com.chinatelecom.xjdh.utils.PreferenceUtils;
//import com.chinatelecom.xjdh.utils.SharedConst;
//import com.chinatelecom.xjdh.utils.T;
//import com.chinatelecom.xjdh.utils.URLs;
//import com.chinatelecom.xjdh.view.AutoGridView;
//import com.squareup.picasso.Picasso;
//import android.view.ViewGroup.LayoutParams;
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.util.DisplayMetrics;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//@EActivity(R.layout.activity_download_station)
//public class DetailsActivity extends BaseActivity {
//	@Extra
//	int room_ID;
//	@ViewById
//	SwipeRefreshLayout srl_alarm;
//	@ViewById
//	ListView lv_stationName_grouping;
//	@RestService
//	ApiRestClientInterface mApiClient;
//	private List<Technology> stList = new ArrayList<Technology>();
//	private DetailAdapter adapter;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		String token = PreferenceUtils.getPrefString(this, PreferenceConstants.ACCESSTOKEN, "");
//		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, token);
//		adapter = new DetailAdapter(this);
//	}
//
//	@AfterViews
//	void initData() {
//		getData();
//	}
//
//	@Background
//	void getData() {
//		try {
//			ApiResponse apiResp = mApiClient.GetApplyInfo(room_ID);
//			L.d("22222222222222222222", apiResp.getData());
//			if (apiResp.getRet() == 0) {
//				ObjectMapper mapper = new ObjectMapper();
//				ApiResponseStationList apiResponseStationList =
//
//				mapper.readValue(apiResp.getData(), ApiResponseStationList.class);
//				List<Technology> list = Arrays.asList(apiResponseStationList.getApplyInfoList());
//				stList.addAll(list);
//				if (apiResp.getRet() == 0) {
//					ShowResponse(apiResp);
//				}
//			}
//		} catch (Exception e) {
//			L.e("Exception" + e.toString());
//		}
//	}
//
//	@UiThread
//	void ShowResponse(ApiResponse apiResp) {
//		if (apiResp.getRet() == 0) {
//			adapter.setData(stList);
//			lv_stationName_grouping.setAdapter(adapter);
//			T.showLong(this, "加载成功");
//		} else {
//			T.showLong(this, apiResp.getData());
//		}
//	}
//
//	class DetailAdapter extends BaseAdapter {
//
//		private Context con;
//		private LayoutInflater inflater;
//		private List<Technology> data;
//		ArrayList<String> mImageUrlList = new ArrayList<String>();
//
//		public DetailAdapter(Context con) {
//			super();
//			this.con = con;
//			inflater = LayoutInflater.from(con);
//		}
//
//		public void setData(List<Technology> data) {
//			this.data = data;
//			this.notifyDataSetChanged();
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return data.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return data.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			Holder holder = null;
//			if (convertView == null) {
//				convertView = inflater.inflate(R.layout.lv_item_item, null);
//				holder = new Holder();
//				holder.title = (TextView) convertView.findViewById(R.id.name);
//				holder.gvImgs = (AutoGridView) convertView.findViewById(R.id.answer_gridview);
//				convertView.setTag(holder);
//			} else {
//				holder = (Holder) convertView.getTag();
//			}
//			holder.title.setText(data.get(position).getContent());
//			holder.gvImgs.setAdapter(new ImgGridAdapter(con, data.get(position).getAnswer()));
//			return convertView;
//		}
//
//		class Holder {
//			TextView title;
//			AutoGridView gvImgs;
//		}
//
//	}
//
//	class ImgGridAdapter extends BaseAdapter {
//
//		private LayoutInflater inflater;
////		ArrayList<String> mImageUrlList = new ArrayList<String>();
//		private Context con;
//		private String[] imgPath;
//		public ImgGridAdapter(Context con, String[] imgPath) {
//			this.imgPath = imgPath;
//			inflater = LayoutInflater.from(con);
//		}
//
//		@Override
//		public int getCount() {
//			return imgPath.length;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return imgPath[position];
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder = null;
//			if (convertView == null) {
//				convertView = inflater.inflate(R.layout.lv_item_img, null);
//				holder = new ViewHolder();
//				holder.img = (ImageView) convertView.findViewById(R.id.lv_item_iv_img);
//				 LayoutParams p = holder.img.getLayoutParams();
//				 p.width = p.height = mGetScreenWidth() / 3 - 20;
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			Picasso.with(getApplicationContext()).load(URLs.HTTP+URLs.HOST+"/public/portal/Check_image/"+imgPath[position]).into(holder.img);
//			return convertView;
//		}
//
//		private class ViewHolder {
//			ImageView img;
//		}
//		
//		private int mGetScreenWidth() {
//			DisplayMetrics dm = new DisplayMetrics();
//			getWindowManager().getDefaultDisplay().getMetrics(dm);
//			return dm.widthPixels;
//		}
//	}
//}
