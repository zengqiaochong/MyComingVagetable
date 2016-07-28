package com.caomei.comingvagetable.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class HeadImageViewUtils {
	// 保存头像
	public static void setPicToView(Bitmap mBitmap, String path) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();// 创建文件夹
		}
		String filename = "head.jpg";
		File cacheFile = new File(file, filename);
		cacheFile.delete();
		if (!cacheFile.exists()) {
			try {
				cacheFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				b = new FileOutputStream(cacheFile);
				if (b != null) {
					mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
				} else {
					return;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					// 关闭流
					if (b != null) {
						b.flush();
						b.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Bitmap compressImage(Bitmap image, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
		while (baos.toByteArray().length / 1024 > size) {
			// 重置baos即清空baos
			baos.reset();
			// 每次都减少10
			options -= 10;
			// 这里压缩options%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		// 把ByteArrayInputStream数据生成图片
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}
}
