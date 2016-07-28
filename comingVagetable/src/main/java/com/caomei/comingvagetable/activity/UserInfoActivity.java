package com.caomei.comingvagetable.activity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.AppData;
import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.CommunityData;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.UserInfoBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.HeadImageViewUtils;
import com.caomei.comingvagetable.util.MD5;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import de.greenrobot.event.EventBus;

public class UserInfoActivity extends BaseActivity {
	private static String path = Environment.getExternalStorageDirectory()
			+ "/head.jpg";// sd路径
	private TextView tvNickName;
	private TextView tvPhone;
	private TextView tvFathCode;
	private TextView tvCode;
	private ImageView ivBack;
	private UserInfoBean ucBean;
	private CommonListener mListener;
	private RelativeLayout panelNickname;
	private RelativeLayout panelPhoneNo;
	private RelativeLayout panelUserAddr;
	private TextView tvUserAddr;
	private ImageView ivUserIcon;
	private RelativeLayout panelUserHeader;
	private RelativeLayout panelInviteCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_user_info);
		EventBus.getDefault().register(this);
		initView();
		initData();
	}

	private void initView() {
		tvNickName = (TextView) findViewById(R.id.tv_nick_name);
		tvPhone = (TextView) findViewById(R.id.tv_phone_num);
		tvCode = (TextView) findViewById(R.id.tv_code);
		tvFathCode = (TextView) findViewById(R.id.tv_father_code);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		ivUserIcon = (ImageView) findViewById(R.id.iv_user_head);
		tvUserAddr = (TextView) findViewById(R.id.tv_user_addr);
		panelNickname = (RelativeLayout) findViewById(R.id.rl_panel_nickname);
		panelPhoneNo = (RelativeLayout) findViewById(R.id.rl_panel_phone_num);
		panelUserAddr = (RelativeLayout) findViewById(R.id.rl_panel_user_addr);
		panelUserHeader = (RelativeLayout) findViewById(R.id.rl_panel_user_head);
		panelInviteCode = (RelativeLayout) findViewById(R.id.rl_panel_father_invite_code);
	}

	private void initData() {
		mListener = new CommonListener();
		ucBean = (UserInfoBean) getIntent().getSerializableExtra("data");
		fillViews();
		ivBack.setOnClickListener(mListener);
		ImageLoader.getInstance().displayImage(ucBean.getPhotoUrl(),
				ivUserIcon, options);
		panelNickname.setOnClickListener(mListener);
		panelPhoneNo.setOnClickListener(mListener);
		panelUserAddr.setOnClickListener(mListener);
		panelInviteCode.setOnClickListener(mListener);
		panelUserHeader.setOnClickListener(mListener);
	}

	public void onEventMainThread(EventMsg msg) {
		switch (msg.getFlag()) {
		case OpCodes.UPDATE_USER_INFO_ERROR:
			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			break;

		case OpCodes.UPDATE_USER_INFO_DONE:
		case OpCodes.PAY_ORDER_DONE:

			Toast.makeText(mContext, msg.getData().toString(),
					Toast.LENGTH_SHORT).show();
			requestUserInfo();
			break;

		case OpCodes.GET_USER_INFO_DONE:
			fillViews();
			break;

		case OpCodes.SET_HOME_COMMUNITY_DONE:
			CommunityData community = (CommunityData) msg.getData();
			updateUserInfo(3, community.getCommunity_id());
			break;
		default:
			break;
		}
	}

	private void requestUserInfo() {
		final String url = CommonAPI.BASE_URL
				+ String.format(CommonAPI.URL_GET_USER_INFO, ShareUtil
						.getInstance(mContext).getUserId());
		Log.e("url", "获取用户信息接口： " + url);
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						ucBean = new Gson().fromJson(bean.getResult(),
								UserInfoBean.class);
						EventBus.getDefault()
								.post(new EventMsg(OpCodes.GET_USER_INFO_DONE,
										ucBean));
					} catch (Exception e) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.GET_USER_INFO_ERROR, bean
										.getState()));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.GET_USER_INFO_ERROR,
									"请求用户信息出错"));
				}
			}
		}).start();
	}

	private void fillViews() {
		tvNickName.setText(ucBean.getNickName());
		tvCode.setText(ucBean.getMyIntroduceCode());
		tvPhone.setText(ucBean.getPhone());
		tvFathCode.setText(ucBean.getIntroduceCode());
		tvUserAddr.setText(ucBean.getCommunityName() == null ? "未设置" : ucBean
				.getCommunityName());
	}

	class CommonListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.rl_panel_nickname:
				ShowDialog("修改我的昵称", ucBean.getNickName(), 1);
				break;
			case R.id.rl_panel_phone_num:
				ShowDialog("修改联系电话", ucBean.getPhone(), 2);
				break;
			case R.id.rl_panel_user_addr:

				startNewActivity(SelectAreaActivity.class,
						R.anim.activity_slide_right_in,
						R.anim.activity_slide_left_out, false, null);

				// showContactUsDialog();

				break;
			case R.id.rl_panel_father_invite_code:
				if (ucBean.getIntroduceCode() == null) {
					ShowDialog("修改邀请人邀请码", "设置后无法修改", 4);
				} else {

				}
				break;

			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.rl_panel_user_head:
//				未完成
//				ShowDialog();  
				break;
			default:
				break;
			}
		}
	}

	private static AlertDialog.Builder mBuilder;
	private static String[] mListbank = new String[] { "拍照", "相册" };
	private static AlertDialog mAlertDialog;

	public void ShowDialog() {
		mBuilder = new AlertDialog.Builder(this);
		mBuilder.setTitle("修改头像：");
		mBuilder.setItems(mListbank, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					opeanCamera();
					ToastUtil.Show(mContext, "准备拍照");
					break;
				case 1:
					opeanPhotoalbum();
					break;
				default:
					break;
				}
			}
		});

		mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mAlertDialog.dismiss();
			}
		});
		mAlertDialog = mBuilder.create();
		mAlertDialog.show();
	}

	private void opeanCamera() {
		Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), "head.jpg")));
		startActivityForResult(intent2, 2);// 采用ForResult打开
	}

	private void opeanPhotoalbum() {
		Intent intent1 = new Intent(Intent.ACTION_PICK, null);
		intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent1, 1);
	}

	private Bitmap head;// 头像Bitmap
	private Uri photoUri;
	private Runnable uploadImageRunnable = new Runnable() {

		@Override
		public void run() {
			try {

				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("user_id", ShareUtil.getInstance(mContext)
						.getUserId());
				File file = new File(path);
				params.put("file", file);
				params.put("fileFileName", "head.jpg");
				params.put(
						"fileContentType",
						MimeTypeMap
								.getSingleton()
								.getMimeTypeFromExtension(
										MimeTypeMap
												.getFileExtensionFromUrl(CommonAPI.BASE_URL
														+ CommonAPI.URL_PICTURE_UPLOAD)));
			         	client.post(CommonAPI.BASE_URL + CommonAPI.URL_PICTURE_UPLOAD,
						params, new AsyncHttpResponseHandler() {

							@Override
							public void onFailure(int arg0, Header[] arg1,
									byte[] arg2, Throwable arg3) {
								Log.e("data", "dta  fa " + arg3.toString());
							}

							@Override
							public void onSuccess(int arg0, Header[] arg1,
									byte[] arg2) {
								Log.e("data", "dta   " + arg2.toString());

							}
						});

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	private Uri uritempFile;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Toast.makeText(mContext, "reuslt code " + resultCode, Toast.LENGTH_LONG)
				.show();
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());// 裁剪图片
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/head.jpg");
				cropPhoto(Uri.fromFile(temp));
			}// 裁剪图片
			break;
		case 3:
			if (data != null) {
				// Bundle extras = data.getExtras();
				// head = extras.getParcelable("data");

				BitmapFactory.Options option = new BitmapFactory.Options();
				option.inSampleSize = 1;
				Bitmap bm = BitmapFactory.decodeFile(path, option);
				ivUserIcon.setImageBitmap(bm);
				/**
				 * 上传服务器代码
				 */
				new Thread(uploadImageRunnable).start();

			} else {
				ToastUtil.Show(mContext, "设置头像出错");

			}
			break;

		default:
			break;

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		// intent.putExtra("return-data", true);
		uritempFile = Uri.parse("file://" + "/"
				+ Environment.getExternalStorageDirectory().getPath() + "/"
				+ "head.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent, 3);

	}

	// 向服务器传入图像
	private void sendImage(Bitmap bm, String url) {
		// ByteArrayOutputStream stream = new ByteArrayOutputStream();
		// bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
		// byte[] byteArray = stream.toByteArray();
		// AsyncHttpClient client = new AsyncHttpClient();
		// RequestParams params = new RequestParams();
		// params.put("user_id", ShareUtil.getInstance(mContext).getUserId());
		// params.put("file", byteArray);
		// params.put("fileFileName", "head.jpg");
		// params.put("fileContentType",
		// MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(new
		// URL())));
		// client.post(CommonAPI.BASE_URL+CommonAPI.URL_PICTURE_UPLOAD, params,
		// new AsyncHttpResponseHandler() {
		//
		// @Override
		// public void onFailure(int arg0, Header[] arg1, byte[] arg2,
		// Throwable arg3) {
		// Log.e("data", "dta  fa " + arg3.toString());
		// }
		//
		// @Override
		// public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// Log.e("data", "dta   " + arg2.toString());
		//
		// }
		// });
		mBuilder = new AlertDialog.Builder(mContext);
		mBuilder.setMessage("头像上传成功");
		mBuilder.setPositiveButton("我知道了",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mAlertDialog.dismiss();
					}
				});
		mAlertDialog = mBuilder.create();
		mAlertDialog.show();
	}

	/**
	 * 显示更新信息的对话框
	 * 
	 * @param title
	 * @param textHint
	 * @param type
	 */
	public void ShowDialog(String title, String textHint, final int type) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.dialog_edit_user_info, null);
		final EditText etInfo = ((EditText) layout.findViewById(R.id.et_editor));
		etInfo.setHint(textHint);

		((TextView) layout.findViewById(R.id.tv_title)).setText(title);
		final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		dialog.setView(layout);
		dialog.show();
		layout.findViewById(R.id.tv_confirm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Log.e("url", etInfo.getText().toString());
						updateUserInfo(type, etInfo.getText().toString());
						dialog.cancel();
					}
				});
		layout.findViewById(R.id.tv_cancle).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.cancel();
					}
				});
	}

	public void showAddAddrDialog() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater
				.inflate(R.layout.dialog_add_user_addr_info, null);

		final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		dialog.setView(layout);
		dialog.show();
		layout.findViewById(R.id.tv_confirm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						startNewActivity(SelectAreaActivity.class,
								R.anim.activity_slide_right_in,
								R.anim.activity_slide_left_out, false, null);

						dialog.cancel();
					}
				});
		layout.findViewById(R.id.tv_cancle).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dialog.cancel();
						onBackPressed();
					}
				});
	}

	private void updateUserInfo(int type, String value) {
		String url = "";
		if (TextUtils.isEmpty(value)) {
			Toast.makeText(mContext, "参数不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (type == 1) {
			url = CommonAPI.BASE_URL
					+ String.format(CommonAPI.URL_UPDATE_USER_INFO, ShareUtil
							.getInstance(mContext).getUserId(), value, ucBean
							.getPhone(), "");
		} else if (type == 2) {
			url = CommonAPI.BASE_URL
					+ String.format(CommonAPI.URL_UPDATE_USER_INFO, ShareUtil
							.getInstance(mContext).getUserId(), ucBean
							.getNickName(), value, "");
		} else if (type == 3) {
			url = CommonAPI.BASE_URL
					+ String.format(CommonAPI.URL_UPDATE_USER_INFO, ShareUtil
							.getInstance(mContext).getUserId(), ucBean
							.getNickName(), ucBean.getPhone(), value);
		} else if (type == 4) {
			url = CommonAPI.BASE_URL
					+ String.format(CommonAPI.URL_COMPLETE_USER_INFO, ShareUtil
							.getInstance(mContext).getUserId(), ucBean
							.getNickName(), ucBean.getPhone(), value, ucBean
							.getCommunity_id());
		}

		Log.e("url", "更新用户数据的接口：" + url);
		requestAPI(url);
	}

	private void requestAPI(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				AccessNetResultBean bean = NetUtil.getInstance(mContext)
						.getDataFromNetByGet(url);
				if (bean.getState() == AccessNetState.Success) {
					try {
						TypeMsgBean mbean = new Gson().fromJson(
								bean.getResult(), TypeMsgBean.class);
						EventBus.getDefault().post(
								new EventMsg(OpCodes.UPDATE_USER_INFO_DONE,
										mbean.getRESULT_MSG()));
					} catch (Exception ex) {
						EventBus.getDefault().post(
								new EventMsg(OpCodes.UPDATE_USER_INFO_ERROR,
										"请求结果数据出错"));
					}
				} else {
					EventBus.getDefault().post(
							new EventMsg(OpCodes.UPDATE_USER_INFO_ERROR, bean
									.getState()));
				}
			}
		}).start();
	}

	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.head_mask) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.head_mask) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.head_mask) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.displayer(new RoundedBitmapDisplayer(100)) // 设置成圆角图片
			.build(); // 创建配置过得DisplayImageOption对象

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.activity_slide_left_in,
				R.anim.activity_slide_right_out);
	}
}
