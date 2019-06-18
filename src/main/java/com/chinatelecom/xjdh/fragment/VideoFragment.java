package com.chinatelecom.xjdh.fragment;//package com.chinatelecom.xjdh.fragment;
//
//
//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.EFragment;
//import org.androidannotations.annotations.ViewById;
//
//import com.chinatelecom.xjdh.R;
//
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.widget.MediaController;
//import android.widget.Toast;
//import android.widget.VideoView;
//@EFragment(R.layout.rtsp_view)
//public class VideoFragment extends Fragment{
//	@ViewById
//	VideoView surface_view;
//	  private static final String KEY_CONTENT = "TestFragment:Content";
//
//	    public static VideoFragment newInstance(String content) {
//	    	VideoFragment fragment = new VideoFragment();
//
//	    	content = content.replace("http", "rtsp");
//	    	content = content.replace("130", "135");
//			String path=content+"/proxyStream";
//	        fragment.mContent = path.toString();
//
//	        return fragment;
//	    }
//
//	    private String mContent = "";
//
//	    @Override
//	    public void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//
//	        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
//	            mContent = savedInstanceState.getString(KEY_CONTENT);
//	        }
//	    }
//
//	    @AfterViews
//		void ShowView()
//		{		
//	    	if (mContent == "") {
//				// Tell the user to provide a media file URL/path.
//				Toast.makeText(getActivity(), "Please edit VideoViewDemo Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
//				return;
//			} else {
//				surface_view.setVideoPath(mContent);
//				surface_view.setMediaController(new MediaController(getActivity()));
//				surface_view.requestFocus();
//
//				surface_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//					@Override
//					public void onPrepared(MediaPlayer mediaPlayer) {
//						// optional need Vitamio 4.0
////						mediaPlayer.setPlaybackSpeed(1.0f);
//					}
//				});
//			}	
//	    }
//
//	    @Override
//	    public void onSaveInstanceState(Bundle outState) {
//	        super.onSaveInstanceState(outState);
//	        outState.putString(KEY_CONTENT, mContent);
//	    }
//	}
