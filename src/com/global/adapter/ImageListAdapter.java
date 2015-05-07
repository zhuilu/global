//package com.global.adapter;
//
//import java.io.DataInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.ref.SoftReference;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
//import android.graphics.drawable.Drawable;
//import android.os.Environment;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.global.R;
//import com.global.bean.CWBaseType;
//import com.global.log.HJLog;
//
//public class ImageListAdapter {
//
//	LayoutInflater mInflater;
//	private Object lock = new Object();
//	ListView mListView;
//	private boolean mAllowLoad = true;
//	private boolean firstLoad = true;
//	Map<String, BookModel> mModels;
//	private int mStartLoadLimit = 0;
//
//	private int mStopLoadLimit = 0;
//	ImageListAdapter syncImageLoader;
//
//	final Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//		};
//	};
//	private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
//
//	interface OnImageLoadListener {
//		public void onImageLoad(Integer t, Drawable drawable);
//
//		public void onError(Integer t);
//
//	}
//
//	ImageListAdapter.OnImageLoadListener imageLoadListener = new ImageListAdapter.OnImageLoadListener() {
//
//		@Override
//		public void onImageLoad(Integer t, Drawable drawable) {
//
//			// BookModel model = (BookModel) getItem(t);
//
//			View view = mListView.findViewWithTag(t);
//			if (view != null) {
//
//				ImageView iv = (ImageView) view
//						.findViewById(R.id.contact_photo_view);
//
//				iv.setBackgroundDrawable(drawable);
//
//			}
//
//		}
//
//		@Override
//		public void onError(Integer t) {
//
//			BookModel model = (BookModel) getItem(t);
//
//			View view = mListView.findViewWithTag(model);
//
//			if (view != null) {
//
//				ImageView iv = (ImageView) view
//						.findViewById(R.id.contact_photo_view);
//
//				iv.setBackgroundResource(R.drawable.ic_launcher);
//
//			}
//
//		}
//	};
//
//	public void loadImage() {
//
//		int start = mListView.getFirstVisiblePosition();
//
//		int end = mListView.getLastVisiblePosition();
//
//		if (end >= getCount()) {
//
//			end = getCount() - 1;
//
//		}
//
//		syncImageLoader.setLoadLimit(start, end);
//
//		syncImageLoader.unlock();
//
//	}
//
//	public void restore() {
//
//		mAllowLoad = true;
//
//		firstLoad = true;
//
//	}
//
//	public void lock() {
//
//		mAllowLoad = false;
//
//		firstLoad = false;
//
//	}
//
//	//
//	public void unlock() {
//
//		mAllowLoad = true;
//
//		synchronized (lock) {
//
//			lock.notifyAll();
//
//		}
//
//	}
//
//	public void loadImage(Integer t, String imageUrl,
//
//	OnImageLoadListener listener) {
//
//		final OnImageLoadListener mListener = listener;
//
//		final String mImageUrl = imageUrl;
//
//		final Integer mt = t;
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//
//				if (!mAllowLoad) {
//
//					// HJLog.i("prepare to load");
//
//					synchronized (lock) {
//
//						try {
//
//							lock.wait();
//
//						} catch (InterruptedException e) {
//
//							e.printStackTrace();
//
//						}
//
//					}
//
//				}
//
//				if (mAllowLoad && firstLoad) {
//
//					loadImage(mImageUrl, mt, mListener);
//
//				}
//
//				if (mAllowLoad && mt <= mStopLoadLimit && mt >= mStartLoadLimit) {
//
//					loadImage(mImageUrl, mt, mListener);
//
//				}
//
//			}
//
//		}).start();
//
//	}
//
//	public void setLoadLimit(int startLoadLimit, int stopLoadLimit) {
//
//		if (startLoadLimit > stopLoadLimit) {
//
//			return;
//
//		}
//
//		mStartLoadLimit = startLoadLimit;
//		mStopLoadLimit = stopLoadLimit;
//	}
//
//	private void loadImage(final String mImageUrl, final Integer mt,
//			final OnImageLoadListener mListener) {
//
//		if (imageCache.containsKey(mImageUrl)) {
//
//			SoftReference<Drawable> softReference = imageCache.get(mImageUrl);
//
//			final Drawable d = softReference.get();
//
//			if (d != null) {
//
//				handler.post(new Runnable() {
//
//					@Override
//					public void run() {
//
//						if (mAllowLoad) {
//
//							mListener.onImageLoad(mt, d);
//
//						}
//
//					}
//
//				});
//
//				return;
//
//			}
//
//		} else {
//			try {
//
//				final Drawable d = loadImageFromUrl(mImageUrl);
//
//				if (d != null) {
//
//					imageCache.put(mImageUrl, new SoftReference<Drawable>(d));
//
//				}
//
//				handler.post(new Runnable() {
//
//					@Override
//					public void run() {
//
//						if (mAllowLoad) {
//
//							mListener.onImageLoad(mt, d);
//
//						}
//
//					}
//
//				});
//
//			} catch (IOException e) {
//
//				handler.post(new Runnable() {
//
//					@Override
//					public void run() {
//
//						mListener.onError(mt);
//
//					}
//
//				});
//
//				e.printStackTrace();
//
//			}
//		}
//	}
//
//	public static Drawable loadImageFromUrl(String url) throws IOException {
//		HJLog.i(url);
//
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//
//			File f = new File(Environment.getExternalStorageDirectory()
//					+ "/TestSyncListView/" + url);
//
//			if (f.exists()) {
//
//				FileInputStream fis = new FileInputStream(f);
//
//				Drawable d = Drawable.createFromStream(fis, "src");
//
//				return d;
//
//			}
//
//			URL m = new URL(url);
//			InputStream i = (InputStream) m.getContent();
//
//			DataInputStream in = new DataInputStream(i);
//
//			FileOutputStream out = new FileOutputStream(f);
//
//			byte[] buffer = new byte[1024];
//
//			int byteread = 0;
//
//			while ((byteread = in.read(buffer)) != -1) {
//
//				out.write(buffer, 0, byteread);
//
//			}
//
//			in.close();
//
//			out.close();
//
//			Drawable d = Drawable.createFromStream(i, "src");
//
//			return loadImageFromUrl(url);
//
//		} else {
//
//			URL m = new URL(url);
//
//			InputStream i = (InputStream) m.getContent();
//
//			Drawable d = Drawable.createFromStream(i, "src");
//
//			return d;
//
//		}
//	}
//
//	class ImageAdapter extends BaseAdapter {
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			if (convertView == null) {
//
//				convertView = mInflater.inflate(R.layout.todo_list_item, null);
//
//			}
//
//			BookModel model = mModels.get(position);
//
//			convertView.setTag(position);
//
//			ImageView iv = (ImageView) convertView
//					.findViewById(R.id.contact_photo_view);
//
//			iv.setBackgroundResource(R.drawable.ic_launcher);
//
//			syncImageLoader.loadImage(position, model.out_book_pic,
//					imageLoadListener);
//
//			return convertView;
//
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//	}
//
//	class BookModel {
//		String out_book_pic;
//	}
//
//	AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
//
//		@Override
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//			switch (scrollState) {
//
//			case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//
//				HJLog.i("SCROLL_STATE_FLING");
//				syncImageLoader.lock();
//
//				break;
//
//			case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//
//				HJLog.i("SCROLL_STATE_IDLE");
//
//				loadImage();
//
//				// loadImage();
//
//				break;
//
//			case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//
//				syncImageLoader.lock();
//
//				break;
//
//			default:
//
//				break;
//
//			}
//
//		}
//
//		@Override
//		public void onScroll(AbsListView view, int firstVisibleItem,
//
//		int visibleItemCount, int totalItemCount) {
//		}
//
//	};
//
// }
