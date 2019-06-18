package com.chinatelecom.xjdh.fragment;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.adapter.MessageListAdapter;
import com.chinatelecom.xjdh.bean.ApiResponse;
import com.chinatelecom.xjdh.bean.MessageItem;
import com.chinatelecom.xjdh.rest.client.ApiRestClientInterface;
import com.chinatelecom.xjdh.ui.WebViewActivity_;
import com.chinatelecom.xjdh.utils.L;
import com.chinatelecom.xjdh.utils.PreferenceConstants;
import com.chinatelecom.xjdh.utils.PreferenceUtils;
import com.chinatelecom.xjdh.utils.SharedConst;
import com.chinatelecom.xjdh.utils.T;
import com.chinatelecom.xjdh.utils.URLs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author peter
 * 
 */
@EFragment(R.layout.normal_list_view)
public class MessageCenterFragment extends Fragment {
	@ViewById(R.id.lv_items)
	ListView mLvMessage;
	MessageListAdapter mMesgListAdapter;
	ProgressDialog pDialog;
	public int position;
	List<MessageItem> mMessageList = new ArrayList<MessageItem>();
	@RestService
	ApiRestClientInterface mApiClient;

	public static Fragment newInstance(int position) {
		MessageCenterFragment_ f = new MessageCenterFragment_();
		f.position = position;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pDialog = new ProgressDialog(getActivity());
		pDialog.setMessage(getString(R.string.progress_loading_msg));
		mApiClient.setHeader(SharedConst.HTTP_AUTHORIZATION, PreferenceUtils.getPrefString(getActivity(), PreferenceConstants.ACCESSTOKEN, ""));
	}

	@AfterViews
	void bindData() {
		mMesgListAdapter = new MessageListAdapter(getActivity(), mMessageList);
		mLvMessage.setAdapter(mMesgListAdapter);
		if (position == SharedConst.MESG_TYPE_PERSONAL) {
			pDialog.show();
			getData();
		}
	}

	@Background
	void getData() {
		try {
			ApiResponse mApiResp = mApiClient.getMessage("" + position);
			if (mApiResp.getRet() == 0) {
				ObjectMapper mapper = new ObjectMapper();
				List<MessageItem> l = mapper.readValue(mApiResp.getData(), new TypeReference<List<MessageItem>>() {
				});
				mMessageList.clear();
				mMessageList.addAll(l);
				updateView(true);
				return;
			}
		} catch (Exception e) {
			L.e(e.toString());
		}
		updateView(false);
	}

	@UiThread
	void updateView(boolean isSuccess) {
		pDialog.dismiss();
		if (isSuccess) {
			mMesgListAdapter.notifyDataSetChanged();
			if (mMessageList.size() == 0)
				T.showShort(getActivity(), "没有更多消息");
		} else
			T.showLong(getActivity(), "加载失败...");
	}

	@ItemClick(R.id.lv_items)
	void onListItemClicked(int position) {
		MessageItem item = mMessageList.get(position);
		if (item.Is_web()) {
			WebViewActivity_
					.intent(getActivity())
					.originalUrl(
							URLs.URL_API_HOST + item.getContent() + "&access_token="
									+ PreferenceUtils.getPrefString(getActivity(), PreferenceConstants.ACCESSTOKEN, "")).title("反馈内容").start();
		}
	}

	public void update() {
		pDialog.show();
		getData();
	}
}
