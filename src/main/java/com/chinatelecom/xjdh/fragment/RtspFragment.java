package com.chinatelecom.xjdh.fragment;//package com.chinatelecom.xjdh.fragment;
//
//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.EFragment;
//import org.androidannotations.annotations.ViewById;
//
//import com.chinatelecom.xjdh.R;
////import com.chinatelecom.xjdh.ui.RtspPlayerActivity_;
//import com.chinatelecom.xjdh.utils.L;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;
//@EFragment(R.layout.search_rtsp)
//public class RtspFragment extends Fragment{
//	@ViewById
//	VideoView surface_view;
//	private String path;
//	
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		Bundle bundle = getArguments();
//		path = (String) bundle.get("url");
//	}
//	
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		surface_view.setVideoPath(path);
//		surface_view.setMediaController(new MediaController(getActivity()));
//		surface_view.requestFocus();
//
//		surface_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//			@Override
//			public void onPrepared(MediaPlayer mediaPlayer) {
//				// optional need Vitamio 4.0
//				mediaPlayer.setPlaybackSpeed(1.0f);
//			}
//		});
//	}
//	
//	@Override
//	public void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		surface_view.pause();
//	}
//	
//	@AfterViews
//	void showView(){
//		L.d(">>>>>>>"+path);
//		
//	}
//}
