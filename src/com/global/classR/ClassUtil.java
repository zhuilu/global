package com.global.classR;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import com.global.R;
import com.global.bean.Node;
import com.global.toast.Toaster;

public class ClassUtil {
	public static void jumpFragment(Context context, FragmentManager manager,
			int layoutId, String fragmentStrShort) {
		System.out.println(fragmentStrShort);
		Class cls = null;
		try {
			String fragmentStr = context.getPackageName() + ".fragment."
					+ fragmentStrShort;
			cls = Class.forName(fragmentStr);
			System.out.println(fragmentStr);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Toaster.getInstance()
					.displayToast(R.string.err_debug_step_fragment);
			return;
		}
		FragmentTransaction transAction = manager.beginTransaction();
		try {
			transAction.replace(layoutId, (Fragment) cls.newInstance());
			transAction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			Toaster.getInstance()
					.displayToast(R.string.err_debug_step_fragment);
			return;
		}

	}
}
