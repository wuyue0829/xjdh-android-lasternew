package com.chinatelecom.xjdh.ui;//package com.chinatelecom.xjdh.ui;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//
//import org.androidannotations.annotations.AfterViews;
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.ViewById;
//
//import com.chinatelecom.xjdh.R;
//import com.chinatelecom.xjdh.utils.FileImgpath;
//import com.chinatelecom.xjdh.utils.L;
//import com.chinatelecom.xjdh.view.SignatureView;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//@EActivity(R.layout.autograph_view)
//public class AutographActivity extends BaseActivity {
//	@ViewById
//	SignatureView signature_pad;
//	@ViewById
//	Button clear_button,save_button;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setTitle("局站验收");
//	}
//	
//	@AfterViews
//	void show(){
//		signature_pad.setOnSignedListener(new SignatureView.OnSignedListener() {
//			@Override
//			public void onSigned() {
//				save_button.setEnabled(true);
//				clear_button.setEnabled(true);
//			}
//
//			@Override
//			public void onClear() {
//				save_button.setEnabled(false);
//				clear_button.setEnabled(false);
//			}
//		});
//	}
//	
//	@Click({R.id.save_button,R.id.clear_button})
//	void btnOnClick(View v){
//		switch (v.getId()) {
//		case R.id.save_button:
//			Bitmap signatureBitmap = signature_pad.getSignatureBitmap();
//			L.d(">>>>>>>>>>>>>>>>>", signatureBitmap.toString());
//			if (addSignatureToGallery(signatureBitmap)) {
//				Toast.makeText(AutographActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
//				finish();
////				UploadSignActivity_.intent(this).signatureBitmap(signatureBitmap).start();
//			} 
//				else {
//				Toast.makeText(AutographActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
//			}
//			break;
//		case R.id.clear_button:
//			signature_pad.clear();
//			break;
//		}
//	}
//	
//	public File getAlbumStorageDir(String albumName) {
//		// Get the directory for the user's public pictures directory.
//		File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
//		if (!file.mkdirs()) {
//			Log.e("SignaturePad", "Directory not created");
//		}
//		return file;
//	}
//
//	public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
//		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//		Canvas canvas = new Canvas(newBitmap);
//		canvas.drawColor(Color.WHITE);
//		canvas.drawBitmap(bitmap, 0, 0, null);
//		OutputStream stream = new FileOutputStream(photo);
//		newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
//		stream.close();
//	}
//
//	public boolean addSignatureToGallery(Bitmap signature) {
//		boolean result = false;
//		try {
//			String fileName = String.valueOf(System.currentTimeMillis());
//			File photo = new File(FileImgpath.getImagePath(fileName));
//			saveBitmapToJPG(signature, photo);
//			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//			Uri contentUri = Uri.fromFile(photo);
//			mediaScanIntent.setData(contentUri);
//			AutographActivity.this.sendBroadcast(mediaScanIntent);
//			result = true;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//}
