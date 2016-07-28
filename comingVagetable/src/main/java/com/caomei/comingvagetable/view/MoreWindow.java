package com.caomei.comingvagetable.view;

import com.caomei.comingvagetable.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout; 

public class MoreWindow extends PopupWindow { 
    public MoreWindow(Context context,View parent){
            LayoutInflater inflater = (LayoutInflater) context  
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            View mMenuView = inflater.inflate(R.layout.order_time_list, null);  
            mMenuView.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.slide_in_from_top)); 
            this.setContentView(mMenuView);   
            this.setWidth(LayoutParams.WRAP_CONTENT);   
            this.setHeight(LayoutParams.WRAP_CONTENT);   
            this.setFocusable(true);        
        }  
       
}