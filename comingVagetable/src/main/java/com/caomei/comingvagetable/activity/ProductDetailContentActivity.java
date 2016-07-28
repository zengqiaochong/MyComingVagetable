package com.caomei.comingvagetable.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.caomei.comingvagetable.R;

/**
 * Created by 123456 on 2016/7/20.
 */
public class ProductDetailContentActivity extends Activity{
    private TextView tv_product_detail;//商品详情
    private ImageView iv_back;
    private View product_detail_content_cancle, product_detail_content_cancle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_content);
        init();
        setData();
    }

    private void init(){
        tv_product_detail = (TextView) findViewById(R.id.tv_product_detail);
        product_detail_content_cancle = findViewById(R.id.product_detail_content_cancle);
        product_detail_content_cancle2 = findViewById(R.id.product_detail_content_cancle2);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListenerImpl());
        product_detail_content_cancle.setOnClickListener(new OnClickListenerImpl());
        product_detail_content_cancle2.setOnClickListener(new OnClickListenerImpl());
    }

    private void setData(){
        if(getIntent() != null){
            tv_product_detail.setText(getIntent().getStringExtra("detail"));
        }
    }

    private class OnClickListenerImpl implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back:
                case R.id.product_detail_content_cancle:
                case R.id.product_detail_content_cancle2:
                    ProductDetailContentActivity.this.finish();
                    break;
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(R.anim.bottom_end,0);
    }
}
