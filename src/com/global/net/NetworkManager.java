package com.global.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author kurt 此处这种写法更应该命名为NetworkUtils,而非NetworkManager。
 * 
 *         此外并未完全抽像为一个模块。而是一个查询当前网络状态的工具类。
 */
public class NetworkManager
{

    public static NetworkState getNetworkState(Context context)
    {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkState networkState = new NetworkState(false, false);
        if (networkInfo != null && networkInfo.isAvailable()
                && networkInfo.isConnected())
        {
            networkState.mIsConnected = true;
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                networkState.mIsWifi = true;
            }
        }
        return networkState;
    }

    public static class NetworkState
    {
        private boolean mIsConnected;
        private boolean mIsWifi;

        private NetworkState(boolean isConnected, boolean isWifi)
        {
            this.mIsConnected = isConnected;
            this.mIsWifi = isWifi;
        }

        @Override
        public boolean equals(Object o)
        {
            if (o != null && o instanceof NetworkState)
            {
                NetworkState state = (NetworkState) o;
                return (mIsConnected == state.mIsConnected)
                        && (mIsWifi == state.mIsWifi);
            }
            return false;
        }

        public boolean isConnected()
        {
            return mIsConnected;
        }

        public boolean isIsWifi()
        {
            return mIsWifi;
        }
    }
}
