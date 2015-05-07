package com.global.thread;

import android.os.Handler;
import android.os.HandlerThread;

public class HandlerThreadNew {

	public static void newThread(Runnable run) {
		HandlerThread thread = new HandlerThread("baseThread");
		thread.start();
		Handler handler = new Handler(thread.getLooper());
		handler.post(run);
	}
}
