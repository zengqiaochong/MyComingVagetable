package com.caomei.comingvagetable.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caomei.comingvagetable.R;

/**
 * Created by 123456 on 2016/7/20.
 */
public class ProductDetailExplainActivity extends Activity{
    private ImageView iv_back;
    private View product_detail_content_cancle, product_detail_content_cancle2;
    private ProgressBar progressBar, progressBar1, progressBar2;
    private int total_number, total_number1, total_number2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_explain);
        init();
        setData();
    }

    private void init(){
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        product_detail_content_cancle = findViewById(R.id.product_detail_content_cancle);
        product_detail_content_cancle2 = findViewById(R.id.product_detail_content_cancle2);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListenerImpl());
        product_detail_content_cancle.setOnClickListener(new OnClickListenerImpl());
        product_detail_content_cancle2.setOnClickListener(new OnClickListenerImpl());
    }

    private void setData(){
        Intent intent = getIntent();
        if(intent != null){
            total_number = intent.getIntExtra("total_number", 0);
            total_number1 = intent.getIntExtra("total_number1", 0);
            total_number2 = intent.getIntExtra("total_number2", 0);
        }
        int sum = total_number + total_number1 + total_number2;
        if(sum != 0){
            progressBar.setProgress(total_number * 100 / sum);
            progressBar1.setProgress(total_number1 * 100 / sum);
            progressBar2.setProgress(total_number2 * 100 / sum);
        }
    }

    private class OnClickListenerImpl implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_back:
                case R.id.product_detail_content_cancle:
                case R.id.product_detail_content_cancle2:
                    ProductDetailExplainActivity.this.finish();
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
