package com.global.image;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class CreateBitImage {
	public static void setBgDrawable(Context c, View v, Bitmap map) {
		BitmapDrawable bd = new BitmapDrawable(map);
		v.setBackgroundDrawable(bd);
	}

	public static Bitmap createIamge(Context c, int reid) {
		// 根据资源图片创建位图
		InputStream is = c.getResources().openRawResource(reid);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inSampleSize = computeSampleSize(options, -1, 128 * 128);
		options.inJustDecodeBounds = false;
		Bitmap map = BitmapFactory.decodeStream(is, null, options);
		return map;
	}

	/**
	 * 计算图片压缩比率
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options,

	int minSideLength, int maxNumOfPixels) {

		int initialSize = computeInitialSampleSize(options, minSideLength,

		maxNumOfPixels);

		int roundedSize;

		if (initialSize <= 8) {

			roundedSize = 1;

			while (roundedSize < initialSize) {

				roundedSize <<= 1;

			}

		} else {

			roundedSize = (initialSize + 7) / 8 * 8;

		}

		return roundedSize;

	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,

	int minSideLength, int maxNumOfPixels) {

		double w = options.outWidth;

		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 :

		(int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

		int upperBound = (minSideLength == -1) ? 128 :

		(int) Math.min(Math.floor(w / minSideLength),

		Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;

		} else if (minSideLength == -1) {

			return lowerBound;

		} else {

			return upperBound;

		}

	}
}
