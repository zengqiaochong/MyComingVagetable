package com.caomei.comingvagetable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.caomei.comingvagetable.CommonData.CommonAPI;
import com.caomei.comingvagetable.CommonData.OpCodes;
import com.caomei.comingvagetable.Enum.AccessNetState;
import com.caomei.comingvagetable.R;
import com.caomei.comingvagetable.bean.AccessNetResultBean;
import com.caomei.comingvagetable.bean.TypeMsgBean;
import com.caomei.comingvagetable.bean.eventbus.EventMsg;
import com.caomei.comingvagetable.util.NetUtil;
import com.caomei.comingvagetable.util.ShareUtil;
import com.caomei.comingvagetable.util.ToastUtil;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.greenrobot.event.EventBus;

/**
 * 忘记密码tActivity
 * 2016/7/21
 */
public class Forgetpassword extends BaseActivity {
    private EditText Forgetpassword_et_phone, Forgetpassword_Set_pass_text, Forgetpassword_Vre_ok_pass_edittext, Forgetpassword_Ver_code_edittext;
    private Button Forgetpassword_get_coder, Forgetpassword_ok__btn;
    private ImageView Forgetpassword_return;
    private CommonListner mListner;//点击监听事件
    private myTimerTask myTask;
    /*验证码剩余时间*/
    private Timer mTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        EventBus.getDefault().register(this);
        setView();
        setData();
    }

    public void setView() {
        /*返回*/
        Forgetpassword_return = (ImageView) findViewById(R.id.Forgetpassword_return);
        /*电话号码*/
        Forgetpassword_et_phone = (EditText) findViewById(R.id.Forgetpassword_et_phone);
        /*获取验证码*/
        Forgetpassword_get_coder = (Button) findViewById(R.id.Forgetpassword_get_coder);
        /*输入验证码*/
        Forgetpassword_Ver_code_edittext = (EditText) findViewById(R.id.Forgetpassword_Ver_code_edittext);
        /*输入密码*/
        Forgetpassword_Set_pass_text = (EditText) findViewById(R.id.Forgetpassword_Set_pass_text);
        /*确认密码*/
        Forgetpassword_Vre_ok_pass_edittext = (EditText) findViewById(R.id.Forgetpassword_Vre_ok_pass_edittext);
        /*确定提交*/
        Forgetpassword_ok__btn = (Button) findViewById(R.id.Forgetpassword_ok__btn);
    }

    public void setData() {
        mListner = new CommonListner();
        Forgetpassword_get_coder.setOnClickListener(mListner);
        Forgetpassword_ok__btn.setOnClickListener(mListner);
        Forgetpassword_return.setOnClickListener(mListner);

    }

    public class CommonListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                 /*返回*/
                case R.id.Forgetpassword_return:
                    finish();
                    break;
                /*获取验证码*/
                case R.id.Forgetpassword_get_coder:
                    getMSMCoder();
                    break;
                /*找回密码*/
                case R.id.Forgetpassword_ok__btn:
                    /*if (checkUserInfoFull()) {
                        checkMSMCoder();
                    }*/
                    Forgetpasswordin();
                    break;
            }
        }
    }

    /*获取短信验证码*/
    public void getMSMCoder() {
        if (!CheckPhoneNumber()) {
            Toast.makeText(mContext, "电话号码不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        Forgetpassword_get_coder.setClickable(false);
        myTask = new myTimerTask(Forgetpassword_get_coder, 60);
        mTimer.schedule(myTask, 1000, 1000);
        final String url = CommonAPI.BASE_URL
                + String.format(CommonAPI.URL_GET_SMS_CODE, Forgetpassword_et_phone.getEditableText().toString(), -1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                AccessNetResultBean bean = NetUtil.getInstance(mContext)
                        .getDataFromNetByGet(url);
                if (bean.getState() == AccessNetState.Success) {
                    TypeMsgBean smsBean = new Gson().fromJson(bean.getResult(),
                            TypeMsgBean.class);
                    if (smsBean.getRESULT_TYPE() == 1) {

                    } else {

                        EventBus.getDefault().post(
                                new EventMsg(OpCodes.GET_SMS_CODE_FAILED, smsBean.getRESULT_MSG()));
                    }
                } else {
                    EventBus.getDefault().post(new EventMsg(OpCodes.GET_SMS_CODE_FAILED, "短信验证码请求失败"));
                }
            }
        }).start();
    }

    /*验证短信验证码*/
    public void checkMSMCoder() {
        final String url = CommonAPI.BASE_URL
                + String.format(CommonAPI.URL_CHECK_SMS_CODE, Forgetpassword_et_phone.getEditableText().toString(),
                Forgetpassword_Ver_code_edittext.getEditableText().toString(),
                -1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                AccessNetResultBean bean = NetUtil.getInstance(mContext)
                        .getDataFromNetByGet(url);
                if (bean.getState() == AccessNetState.Success){
                    TypeMsgBean sBean = new Gson().fromJson(bean.getResult(),
                            TypeMsgBean.class);
                    if (sBean.getRESULT_TYPE() == 1) {
                        EventBus.getDefault().post(new EventMsg(OpCodes.CHECK_SMS_CODE_SUCCESS, sBean));
                    }else{
                        EventBus.getDefault().post(new EventMsg(OpCodes.CHECK_SMS_CODE_FAILED, sBean.getRESULT_MSG()));
                    }
                }else{
                    EventBus.getDefault().post(new EventMsg(OpCodes.CHECK_SMS_CODE_FAILED, "注册失败"));
                }
            }
        }).start();
    }

    /*找回密码方法*/
    private void Forgetpasswordin() {
        if (!checkUserInfoFull()) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = CommonAPI.BASE_URL + String.format(CommonAPI.URL_BACK_PASSWORD, Forgetpassword_Vre_ok_pass_edittext.getEditableText().toString(),
                        -1, Forgetpassword_et_phone.getEditableText().toString(), Forgetpassword_Ver_code_edittext.getEditableText().toString());
                Log.i("重置密码-----------", url);
                try {
                    AccessNetResultBean bean = NetUtil.getInstance(mContext).getDataFromNetByGet(url);
                    TypeMsgBean tb = new Gson().fromJson(bean.getResult(), TypeMsgBean.class);
                    if (bean.getState() == AccessNetState.Success) {
                        //String Result = bean.getResult();
                        if (tb.getRESULT_TYPE() != 1) {
                            /*修改密码成功*/
                            EventBus.getDefault().post(new EventMsg(OpCodes.BACK_PASSWORD_SUCCESS, tb.getRESULT_MSG()));
                        } else {
                           /* 修改密码失败*/
                            EventBus.getDefault().post(new EventMsg(OpCodes.BACK_PASSWORD_ERROR, tb.getRESULT_MSG()));
                        }
                    } else {
                       /*短信验证失败*/
                        EventBus.getDefault().post(new EventMsg(OpCodes.BACK_PASSWORD_ERROR, tb.getRESULT_MSG()));
                    }
                } catch (Exception ex) {
                    EventBus.getDefault().post(new EventMsg(OpCodes.BACK_PASSWORD_ERROR, "修改密码失败！"));
                }
            }
        }
        ).start();
    }

    private boolean checkUserInfoFull() {
        if (!CheckPhoneNumber()) {
            ToastUtil.Show(mContext, "电话号码不合法");
            return false;
        }
        if (Forgetpassword_Ver_code_edittext.getEditableText().toString() == null || Forgetpassword_Ver_code_edittext.getEditableText().toString().length() < 4) {
            ToastUtil.Show(mContext, "验证码为空或者验证码长度超过4位");
            return false;
        }
        if (Forgetpassword_Set_pass_text.getEditableText().toString() == null || Forgetpassword_Set_pass_text.getEditableText().toString().length() < 6) {
            ToastUtil.Show(mContext, "密码至少6位");
            return false;
        }
        if (!Forgetpassword_Set_pass_text.getEditableText().toString().equals(Forgetpassword_Vre_ok_pass_edittext.getEditableText().toString())) {
            ToastUtil.Show(mContext, "两次输入的密码不一致");
            return false;
        }
        return true;
    }

    public void onEventMainThread(EventMsg msg) {
        switch (msg.getFlag()) {
            /*短信验证成功*/
            case OpCodes.CHECK_SMS_CODE_SUCCESS:
                Forgetpasswordin();
                break;
            /*短信验证失败*/
            case OpCodes.CHECK_SMS_CODE_FAILED:
                /*提示信息*/
                ToastUtil.Show(mContext, msg.getData().toString());
                break;
            /*找回密码成功*/
            case OpCodes.BACK_PASSWORD_SUCCESS:
                /*提示密码找回成功*/
                ToastUtil.Show(mContext, msg.getData().toString());
                ShareUtil.getInstance(Forgetpassword.this).setUserName(Forgetpassword_et_phone.getText().toString());
                startNewActivity(LoginActivity.class, R.anim.activity_slide_right_in, R.anim.activity_slide_left_out, true, null);
                break;
            case OpCodes.BACK_PASSWORD_ERROR:
                ToastUtil.Show(mContext, msg.getData().toString());
                break;
        }
    }

    private boolean CheckPhoneNumber() {
        String str = Forgetpassword_et_phone.getEditableText().toString();
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        if (str == null || str.length() < 11) {
            return false;
        }
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    class myTimerTask extends TimerTask {
        private int length;
        private Button btn;

        public myTimerTask(Button btn, int len) {
            this.length = len;
            this.btn = btn;
        }

        @Override
        public void run() {
            runOnUiThread(new Runnable() { // UI thread
                @Override
                public void run() {
                    length--;
                    btn.setText(String.valueOf(length) + "s后重发");
                    if (length < 0) {
                        if (myTask != null) {
                            myTask.cancel();
                        }
                        Forgetpassword_get_coder.setClickable(true);
                        btn.setText("获取验证码");
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
