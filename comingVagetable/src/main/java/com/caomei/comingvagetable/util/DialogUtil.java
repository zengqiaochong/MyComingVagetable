package com.caomei.comingvagetable.util;

import com.caomei.comingvagetable.IDialogOperation;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class DialogUtil {

	public static void DefaultDialog(Context mContext, String title,
			String msg, String posString, String negString,final IDialogOperation iOperation) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage(msg);

		builder.setTitle(title);

		builder.setPositiveButton(posString, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				iOperation.onPositive();
			}
		});
		builder.setNegativeButton(negString, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				iOperation.onNegative();
			}
		});
		builder.create().show();
	}

	public static void DefaultDialog(Context mContext, String title,
			String msg, String posString,final IDialogOperation iOperation) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage(msg);

		builder.setTitle(title);

		builder.setPositiveButton(posString, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				iOperation.onPositive();
			}
		}); 
		builder.create().show();
	}
}