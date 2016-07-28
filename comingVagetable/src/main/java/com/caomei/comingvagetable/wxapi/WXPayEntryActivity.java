package com.caomei.comingvagetable.wxapi;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.CommonData.Constants;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.activity.BaseActivity;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	 
    private IWXAPI api;
	private TextView tvNotice;
	private ImageView ivBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        ivBack=(ImageView)findViewById(R.id.iv_back);
        tvNotice=(TextView)findViewById(R.id.tv_notice);
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
        ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			switch (resp.errCode){
			case 0:
				 tvNotice.setText("支付成功");
				 EventBus.getDefault().post(
							new EventMsg(OpCodes.PAY_ORDER_DONE, "支付完成"));
				break;
				
			case -1:
				 tvNotice.setText("支付失败");
				break;
				
			case -2:
				 tvNotice.setText("支付取消");			 
				break;
				
			default:
				break;
			}
		
		}
	}
}