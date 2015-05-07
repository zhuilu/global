package com.global.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.global.app.SysApplicationImpl;
import com.global.log.CLog;
import com.global.toast.Toaster;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * Image通用操作封装工具类
 * 
 */
public class ImageUtil {
	private static final String TAG = "ImageUtil";
	/**
	 * 时间戳格式
	 */
	public static final SimpleDateFormat TIMESTAMP_DF = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	/**
	 * 构造图片类
	 */
	private ImageUtil() {

	}

	/**
	 * 根据路径获取大图
	 * 
	 * @param imageName
	 */
	private Bitmap getLargeImage(String imageName) {
		try {
			FileInputStream stream = new FileInputStream(new File(imageName));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inSampleSize = 1;
			Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
			stream.close();
			return bitmap;
		} catch (IOException e) {
			if (SysApplicationImpl.getInstance().isDebug()) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * 生成图片对应的圆形边框图片
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getBitmapClippedCircle(Bitmap bitmap) {

		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();
		final Bitmap outputBitmap = Bitmap.createBitmap(width, height,
				Config.ARGB_8888);

		final Path path = new Path();
		path.addCircle((float) (width / 2), (float) (height / 2),
				(float) Math.min(width, (height / 2)), Path.Direction.CCW);

		final Canvas canvas = new Canvas(outputBitmap);
		canvas.clipPath(path);
		canvas.drawBitmap(bitmap, 0, 0, null);
		return outputBitmap;
	}

	/**
	 * 读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 从view 得到图片
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap getBitmapFromView(View view) {
		view.destroyDrawingCache();
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache(true);
		return bitmap;
	}

	/**
	 * 
	 * 将图片转化给byte[]操作
	 * 
	 * @param bm
	 *            图片对象
	 * @return 图片byte[]
	 */
	public static byte[] bitmap2Bytes(Bitmap bm) {
		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		bytes = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	public Bitmap resizeBitmap(Bitmap bitmap, int newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap = null;
		return resizedBitmap;
	}

	/**
	 * 用于给指定头像生成圆角的头像
	 * 
	 * @date 2012-06-26 下午2:40:11
	 * @Title: resizeImage
	 * @Description: 对位图缩放
	 * @param bitmap
	 *            传进来位图
	 * @param w
	 *            长
	 * @param h
	 *            宽
	 * @return bitmap
	 */
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		// 原来的图片
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int newWidth = w;
		int newHeight = h;
		if (width == newWidth && height == newHeight) {
			return bitmap;
		}
		// 计算缩放
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 创建一个矩阵操纵
		Matrix matrix = new Matrix();
		// 调整位图
		matrix.postScale(scaleWidth, scaleHeight);

		// 调整新位图
		try {
			return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
					true);
		} catch (OutOfMemoryError e) {
			CLog.e("[resizeImage]OutOfMemoryError");
		}
		return null;
	}

	/**
	 * 
	 * [将图片变成圆角]<BR>
	 * [功能详细描述]
	 * 
	 * @param bitmap
	 *            要处理的图片
	 * @param roundPx
	 *            圆角
	 * @return 转换完毕的图片
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output;
		try {
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			int color = 0xff424242;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			RectF rectF = new RectF(rect);
			if (roundPx < 0) {
				roundPx = 5;
			}

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (OutOfMemoryError e) {
			output = null;
			CLog.e("[getRoundedCornerBitmap]OutOfMemoryError");
		}
		return output;
	}

	/**
	 * 
	 * [将图片变成圆角]<BR>
	 * [功能详细描述]
	 * 
	 * @param bitmap
	 *            要处理的图片
	 * @param backBitmap
	 *            图像背景
	 * @param roundPx
	 *            圆角
	 * @return 转换完毕的图片
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,
			Bitmap backBitmap, float roundPx) {
		Drawable dwbRound = drawRoundCornerForDrawable(bitmap,
				bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth() / 2);

		LayerDrawable layerSnsAvatar = null;
		if (bitmap != null) {
			layerSnsAvatar = new LayerDrawable(new Drawable[] { dwbRound,
					new BitmapDrawable(backBitmap) });
		}

		return drawableToBitmap(layerSnsAvatar);
	}

	/**
	 * 把文字画在图片中间
	 * 
	 * @date 2011-11-30 下午1:40:11
	 * @Title: drawTextInBitmapProfileCenter
	 * @Description: 把文字画在图片中间
	 * @param canvas
	 *            背景画布
	 * @param asset
	 *            管理器
	 * @param rect
	 *            所画区域
	 * @param drawText
	 *            需要画上的文字
	 * @param textSize
	 *            文字像素
	 */
	public static void drawTextInBitmapCenter(Canvas canvas,
			AssetManager asset, Rect rect, String drawText, float textSize) {
		Paint p = null;
		p = new Paint();
		p.setColor(Color.parseColor("#333333"));
		p.setTextSize(textSize);
		p.setTypeface(Typeface.createFromAsset(asset, "font/Roboto-Medium.ttf"));
		p.setAntiAlias(true); // 消除锯齿
		p.setTextAlign(Align.CENTER); // 居中对齐
		if (null != drawText) {
			canvas.drawText(drawText, rect.left + (rect.right - rect.left) / 2,
					rect.bottom + 28, p);
		}
	}

	/**
	 * 
	 * 将Drawable转换为Bitmap
	 * 
	 * @param drawable
	 *            Drawable对象
	 * @return bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap;
		try {
			bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
		} catch (OutOfMemoryError e) {
			bitmap = null;
			CLog.e("[drawableToBitmap]OutOfMemoryError");
		}
		return bitmap;
	}

	/**
	 * 图片旋转
	 * 
	 * @param bmpOrg
	 *            原图片
	 * @param rotate
	 *            旋转角度（0~360度）
	 * @return Bitmap 旋转后的图片
	 */
	public static Bitmap rotateBitmap(Bitmap bmpOrg, int rotate) {
		int width = bmpOrg.getWidth();
		int height = bmpOrg.getHeight();
		Matrix matrix = new Matrix();
		matrix.postRotate(rotate);
		Bitmap resizeBitmap;
		try {
			resizeBitmap = Bitmap.createBitmap(bmpOrg, 0, 0, width, height,
					matrix, false);

		} catch (OutOfMemoryError e) {
			resizeBitmap = null;
			CLog.e("[rotateBitmap]OutOfMemoryError");
			Toaster.getInstance().displayToast("旋转失败");
			return bmpOrg;
		}

		if (null != bmpOrg && !bmpOrg.isRecycled()) {
			bmpOrg.recycle();
			bmpOrg = null;
		}

		return resizeBitmap;
	}

	/**
	 * 
	 * 通过byte数组去给指定的ImageView设置圆角背景
	 * 
	 * @param data
	 *            图片数据
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param adii
	 *            圆角大小
	 * @param imageView
	 *            ImageView对象
	 */
	public static void drawRoundCorner(byte[] data, int width, int height,
			int adii, ImageView imageView) {
		Bitmap bitmap = getBitmapFromBytes(data, width, height);
		if (bitmap != null) {
			drawRoundCorner(bitmap, width, height, adii, imageView);
		}
	}

	/**
	 * 
	 * 绘制顶部带圆角的图片， 如果图片宽度小于传递的width值，则进行高宽等比缩放，所以不需要传递期望的height值
	 * 
	 * @param bitmap
	 *            原图
	 * @param width
	 *            宽度，单位为DIP
	 * @param adii
	 *            圆角值
	 */
	public static Drawable getTopRoundCorner(Bitmap bitmap, int width, int adii) {
		// 根据DIP值获取PX值
		width = (int) getPXSize(TypedValue.COMPLEX_UNIT_DIP, width);

		// 原图大小
		int srcWidth = bitmap.getWidth();
		int srcHeight = bitmap.getHeight();

		// 按等比计算获取期望的高度值
		int height = srcHeight * width / srcWidth;

		if (width > 0 && height > 0) {

			// 原图高度与指定高度不同时 进行缩放处理
			if (width != srcWidth) {
				bitmap = resizeImage(bitmap, width, height);
			}
		} else {
			width = bitmap.getWidth();
			height = bitmap.getHeight();
		}
		Drawable dwbRound = drawTopRoundCornerForDrawable(bitmap, width,
				height, adii);
		return dwbRound;
	}

	/**
	 * 
	 * 用于给指定头像生成圆角的头像
	 * 
	 * @param bitmap
	 *            头像图片
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片长度
	 * @param adii
	 *            圆角大小
	 * @param imageView
	 *            ImageView对象
	 */
	public static void drawRoundCorner(Bitmap bitmap, int width, int height,
			int adii, ImageView imageView) {
		Drawable dwbRound = drawRoundCornerForDrawable(bitmap, width, height,
				adii);
		imageView.setImageDrawable(dwbRound);
	}

	/**
	 * 
	 * 用于给指定图片生成上面两个圆角的图片
	 * 
	 * @param bitmap
	 *            头像图片
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片长度
	 * @param adii
	 *            圆角大小
	 * @return Drawable类型的圆脚头像
	 */
	public static Drawable drawTopRoundCornerForDrawable(Bitmap bitmap,
			int width, int height, int adii) {
		ShapeDrawable dwbRound;
		try {
			Shape shpRound = new RoundRectShape(new float[] { adii, adii, adii,
					adii, 0, 0, 0, 0 }, null, null);
			dwbRound = new ShapeDrawable(shpRound);
			dwbRound.setIntrinsicWidth(width);
			dwbRound.setIntrinsicHeight(height);
			Shader shdBitmap = new BitmapShader(bitmap, Shader.TileMode.MIRROR,
					Shader.TileMode.MIRROR);
			dwbRound.getPaint().setShader(shdBitmap);
			dwbRound.getPaint().setFlags(
					dwbRound.getPaint().getFlags() | Paint.ANTI_ALIAS_FLAG);
		} catch (OutOfMemoryError e) {
			dwbRound = null;
			CLog.e("[drawTopRoundCornerForDrawable]OutOfMemoryError");
		}
		return dwbRound;
	}

	/**
	 * 获取当前分辨率下指定单位对应的像素大小（根据设备信息） px,dip,sp -> px Paint.setTextSize()单位为px
	 * 
	 * @param unit
	 *            size值对应的单位，TypedValue.COMPLEX_UNIT_*
	 * @param size
	 *            指定单位的值
	 * @return 对应的像素值
	 */
	public static float getPXSize(int unit, float size) {
		Resources r = Resources.getSystem();
		return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}

	/**
	 * 
	 * 用于给指定头像生成圆角的头像
	 * 
	 * @param bitmap
	 *            头像图片
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片长度
	 * @param adii
	 *            圆角大小
	 * @return Drawable类型的圆脚头像
	 */
	public static Drawable drawRoundCornerForDrawable(Bitmap bitmap, int width,
			int height, int adii) {
		ShapeDrawable dwbRound;
		try {
			Shape shpRound = new RoundRectShape(new float[] { adii, adii, adii,
					adii, adii, adii, adii, adii }, null, null);
			dwbRound = new ShapeDrawable(shpRound);
			dwbRound.setIntrinsicWidth(width);
			dwbRound.setIntrinsicHeight(height);
			if (null != bitmap) {
				Shader shdBitmap = new BitmapShader(bitmap,
						Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
				Matrix matrix = new Matrix();
				matrix.setScale((float) width / bitmap.getWidth(),
						(float) height / bitmap.getHeight());
				shdBitmap.setLocalMatrix(matrix);
				dwbRound.getPaint().setShader(shdBitmap);
				dwbRound.getPaint().setFlags(
						dwbRound.getPaint().getFlags() | Paint.ANTI_ALIAS_FLAG);
			}
		} catch (OutOfMemoryError e) {
			dwbRound = null;
			CLog.e("[drawRoundCornerForDrawable]OutOfMemoryError");
		}
		return dwbRound;
	}

	/**
	 * 
	 * 给传进来的drawable加上背景<BR>
	 * 
	 * @param context
	 *            context
	 * @param backgroundId
	 *            背景的资源id
	 * @param source
	 *            需要添加背景的drawable
	 * @param width
	 *            图片的宽
	 * @param height
	 *            图片的高
	 * @return 添加了背景的drawable
	 */
	public static Drawable addDrawableBg(Resources resources, int backgroundId,
			Drawable source, int width, int height) {
		Drawable drawable;
		try {
			Drawable background = resources.getDrawable(backgroundId);
			// 首先需要生成指定大小的背景，画到canvas上去
			Bitmap bitmap = Bitmap
					.createBitmap(
							width,
							height,
							background.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			source.setBounds(0, 0, width, height);
			source.draw(canvas);
			background.setBounds(0, 0, width, height);
			background.draw(canvas);
			drawable = drawRoundCornerForDrawable(bitmap, width, height, 0);
		} catch (OutOfMemoryError e) {
			drawable = null;
			CLog.e("[addDrawableBg]OutOfMemoryError");
		}
		return drawable;
	}

	/**
	 * 
	 * 根据传进来的聊吧成员头像生成聊吧头像<BR>
	 * [功能详细描述]
	 * 
	 * @param backgroundId
	 *            背景图片
	 * @param drawables
	 *            聊吧头像
	 * @param width
	 *            头像宽度
	 * @param height
	 *            头像高度
	 * @param context
	 *            context对象
	 * @return Bitmap 生成的聊吧头像
	 */
	public static Bitmap createChatBarBitmap(int backgroundId,
			List<Drawable> drawables, int width, int height, Context context) {
		Bitmap bitmap = null;
		try {
			Drawable background = context.getResources().getDrawable(
					backgroundId);
			// 首先需要生成指定大小的背景，画到canvas上去
			bitmap = Bitmap
					.createBitmap(
							width,
							height,
							background.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			background.setBounds(0, 0, width, height);
			background.draw(canvas);
			if (drawables != null && drawables.size() > 0) {
				Rect[][] rects = createRects(width, height);
				// 然后根据list的大小进行不同画法
				Drawable drawable = null;
				int size = drawables.size();
				if (size > 4) {
					size = 4;
				}
				switch (size) {
				case 1:
					drawable = drawables.get(0);
					drawable.setBounds(rects[0][0]);
					drawable.draw(canvas);
					break;
				case 2:
					drawable = drawables.get(0);
					drawable.setBounds(rects[0][0]);
					drawable.draw(canvas);
					drawable = drawables.get(1);
					drawable.setBounds(rects[0][1]);
					drawable.draw(canvas);
					break;
				case 3:
					drawable = drawables.get(0);
					drawable.setBounds(rects[0][0]);
					drawable.draw(canvas);
					drawable = drawables.get(1);
					drawable.setBounds(rects[0][1]);
					drawable.draw(canvas);
					drawable = drawables.get(2);
					drawable.setBounds(rects[1][0]);
					drawable.draw(canvas);
					break;
				case 4:
					drawable = drawables.get(0);
					drawable.setBounds(rects[0][0]);
					drawable.draw(canvas);
					drawable = drawables.get(1);
					drawable.setBounds(rects[0][1]);
					drawable.draw(canvas);
					drawable = drawables.get(2);
					drawable.setBounds(rects[1][0]);
					drawable.draw(canvas);
					drawable = drawables.get(3);
					drawable.setBounds(rects[1][1]);
					drawable.draw(canvas);
					break;
				default:
					break;
				}
			}
		} catch (OutOfMemoryError oom) {
			CLog.e("[createChatBarBitmap]OutOfMemoryError");
		}
		return bitmap;
	}

	/**
	 * 
	 * 根据指定的高度和宽度生成对应的坐标<BR>
	 * [功能详细描述]
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	private static Rect[][] createRects(int width, int height) {
		Rect[][] rects = new Rect[2][2];
		int blank = 0;
		// 先算出坐标
		int imageWidth = (width - blank * 3) / 2;
		int imageHeight = (height - blank * 3) / 2;
		Rect rect = null;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				rect = new Rect();
				rect.left = imageWidth * j + blank * (j + 1);
				rect.top = imageHeight * i + blank * (i + 1);
				rect.right = imageWidth * (j + 1) + blank * (j + 1);
				rect.bottom = imageHeight * (i + 1) + blank * (i + 1);
				rects[i][j] = rect;
			}
		}
		return rects;
	}

	/**
	 * 根据最小边长进行压缩图片，以便向服务器上传
	 * 
	 * @param path
	 *            图片路径
	 * @return 压缩后的位图
	 */
	public static Bitmap getFitBitmap(String path) {
		if (path == null) {
			CLog.e("image path is null");
			return null;
		}
		try {
			// 图片最大宽度/高度
			int imageWidth = 800;

			int imageHeight = 480;
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);

			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;

			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			// if (srcWidth * srcHeight < (IMAGE_WIDTH * IMAGE_HEIGHT))
			// {
			// return BitmapFactory.decodeFile(path);
			// }
			if (srcWidth < srcHeight) {
				ratio = (double) srcWidth / imageWidth;
				if (ratio > 1.0) {
					destHeight = (int) (srcHeight / ratio);
					destWidth = imageWidth;
				} else {
					return BitmapFactory.decodeFile(path);
				}

			} else {
				ratio = (double) srcHeight / imageHeight;
				if (ratio > 1.0) {
					destWidth = (int) (srcWidth / ratio);
					destHeight = imageHeight;
				} else {
					return BitmapFactory.decodeFile(path);
				}

			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			double x = Math.log(ratio) / Math.log(2);
			int k = (int) Math.ceil(x);
			int j = (int) Math.pow(2, k);
			newOpts.inSampleSize = j;
			newOpts.inJustDecodeBounds = false;
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;

			// Tell to gc that whether it needs free memory, the Bitmap can
			// be cleared
			newOpts.inPurgeable = true;
			// Which kind of reference will be used to recover the Bitmap
			// data after being clear, when it will be used in the future
			newOpts.inInputShareable = true;
			// Allocate some temporal memory for decoding
			newOpts.inTempStorage = new byte[64 * 1024];

			Bitmap destBm = BitmapFactory.decodeFile(path, newOpts);

			return destBm;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断图片类型
	 * 
	 * @param fileName
	 *            文件名
	 * @return 是图片扩展名返回true
	 */
	public static boolean isPictureType(String fileName) {
		// TODO:使用endsWith()方法即可 edit by pierce.
		int index = fileName.lastIndexOf(".");
		if (index != -1) {
			String type = fileName.substring(index).toLowerCase();
			if (".png".equals(type) || ".gif".equals(type)
					|| ".jpeg".equals(type) || ".jpg".equals(type)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 从资源获取Bitmap对象
	 * 
	 * @param resources
	 *            Resources
	 * @param resId
	 *            资源id
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFromResources(Resources resources, int resId) {
		if (resources == null) {
			CLog.e("[getBitmapFromResources]resources is NULL.");
			return null;
		}
		return BitmapFactory.decodeResource(resources, resId);
	}

	/**
	 * 从资源中获取Bitmap对象
	 * 
	 * @param resources
	 *            文件路径
	 * @param resId
	 *            资源id
	 * @param destW
	 *            期望宽度
	 * @param destH
	 *            期望高度
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFromResources(Resources resources, int resId,
			int destW, int destH) {
		if (resources == null) {
			CLog.e("[getBitmapFromResources]resources is NULL.");
			return null;
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resId, opt);
		opt.inJustDecodeBounds = false;
		opt.inSampleSize = computeSampleSize(opt.outWidth, opt.outHeight,
				destW, destH);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeResource(resources, resId, opt);
		} catch (OutOfMemoryError oom) {
			CLog.e("[getBitmapFromResources]OutOfMemoryError!");
			return null;
		}
		return bitmap;
	}

	/**
	 * 从文件获取Bitmap对象
	 * 
	 * @param filePath
	 *            文件路径
	 * @param destW
	 *            期望宽度
	 * @param destH
	 *            期望高度
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFromFile(String filePath, int destW, int destH) {
		if (filePath == null) {
			CLog.e("[getBitmap]filePath is NULL!");
			return null;
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opt);
		opt.inJustDecodeBounds = false;
		opt.inSampleSize = computeSampleSize(opt.outWidth, opt.outHeight,
				destW, destH);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeFile(filePath, opt);
		} catch (OutOfMemoryError oom) {
			CLog.e("[getBitmap]OutOfMemoryError!");
			return null;
		}
		return bitmap;
	}

	/**
	 * 从字节数组获取Bitmap对象
	 * 
	 * @param data
	 *            图片字节数组
	 * @param destW
	 *            期望宽度
	 * @param destH
	 *            期望高度
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFromBytes(byte[] data, int destW, int destH) {
		if (data == null) {
			CLog.e("[getBitmap]data is NULL!");
			return null;
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opt);
		opt.inJustDecodeBounds = false;
		opt.inSampleSize = computeSampleSize(opt.outWidth, opt.outHeight,
				destW, destH);
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
		} catch (OutOfMemoryError oom) {
			CLog.e("[getBitmap]OutOfMemoryError!");
			return null;
		}
		return bitmap;
	}

	/**
	 * 计算图片的缩放比 参照options.inSampleSize注释，options.inSampleSize的值为2的倍数较好
	 * 
	 * @param srcW
	 *            源图片宽度
	 * @param srcH
	 *            源图片高度
	 * @param destW
	 *            预期的图片宽度
	 * @param destH
	 *            预期的图片高度
	 * @return 缩放比inSampleSize
	 */
	public static int computeSampleSize(int srcW, int srcH, int destW, int destH) {
		int inSampleSize = 1;
		if (destW > 0 && destH > 0) {
			while (srcW > destW && srcH > destH) {
				srcW >>= 1;
				srcH >>= 1;
				// 在计算缩放比时，要保证最终生成的图片宽高不会小于预期宽高，否则图片会出现不清晰。
				if (srcW < destW || srcH < destH) {
					break;
				}
				inSampleSize <<= 1;
			}
		}
		return inSampleSize;
	}

	/**
	 * 
	 * 根据默认的格式获得当前时间字符串
	 * 
	 * @return 当前时间
	 */
	public static String getCurrentDateString() {
		return TIMESTAMP_DF.format(new Date());
	}
}
