package com.global.util;


import android.net.wifi.WifiInfo;

import android.net.wifi.WifiManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

// 的获取设备唯一标识，我们可以实现这样的一个类，为每个设备产生唯一的UUID，以ANDROID_ID为基础，在获取失败时以TelephonyManager.getDeviceId()为备选方法，如果再失败，使用UUID的生成策略。
// 重申下，以下方法是生成Device ID，在大多数情况下Installtion ID能够满足我们的需求，但是如果确实需要用到Device ID，那可以通过以下方式实现：
public class DeviceUUID {

    protected static final String PREFS_FILE      = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";
    protected static UUID         uuid;
    private WifiInfo              info;

    public DeviceUUID(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        info = wifi.getConnectionInfo();

        if (uuid == null) {
            synchronized (DeviceUUID.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file

                    } else {
                        final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
                        // Use the Android ID unless it's broken, in which case fallback on deviceId,
                        // unless it's not available, then fallback on a random number which we store
                        // to a prefs file
                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                            } else {
                                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                            }

                        }
                        catch (UnsupportedEncodingException e) {
                            uuid = UUID.fromString(String.valueOf(System.currentTimeMillis()));
                        }
                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                    }
                }
            }
        }
    }

    /**
     * Returns a unique UUID for the current android device. As with all UUIDs, this unique ID is "very highly likely"
     * to be unique across all Android devices. Much more so than ANDROID_ID is.
     * The UUID is generated by using ANDROID_ID as the base key if appropriate, falling back on
     * TelephonyManager.getDeviceID() if ANDROID_ID is known to be incorrect, and finally falling back
     * on a random UUID that's persisted to SharedPreferences if getDeviceID() does not return a
     * usable value.
     * In some rare circumstances, this ID may change. In particular, if the device is factory reset a new device ID
     * may be generated. In addition, if a user upgrades their phone from certain buggy implementations of Android 2.2
     * to a newer, non-buggy version of Android, the device ID may change. Or, if a user uninstalls your app on
     * a device that has neither a proper Android ID nor a Device ID, this ID may change on reinstallation.
     * Note that if the code falls back on using TelephonyManager.getDeviceId(), the resulting ID will NOT
     * change after a factory reset. Something to be aware of.
     * Works around a bug in Android 2.2 for many devices when using ANDROID_ID directly.
     * @see http://code.google.com/p/android/issues/detail?id=10603
     * @return a UUID that may be used to uniquely identify your device for most purposes.
     */
    public String getDeviceUuid() {
        if (info != null && info.getMacAddress() != null) {
            return info.getMacAddress().replace(":", "-"); //
        } else {
            return uuid.toString();
        }
    }
}
