package co.tera.iadedemo.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.lights.NamedLightEx;
import android.lights.NamedLightsManagerEx;
import android.util.Log;


public class NamedLightsManager {

    private static final String TAG = "NamedLightsManagerCompat";
    private static final boolean DEBUG = false;

    public final static int EX_VERSION = 20190521;

    public static NamedLightsManager getInstance(Context context) {
        if (context == null) {
            return null;
        }
        @SuppressLint("WrongConstant") final Object namedLightsManager = context.getSystemService("named_lights");
        if (namedLightsManager == null) {
            return null;
        }
        return new NamedLightsManager(namedLightsManager);
    }

    private static void onException(Throwable ex) {
        if (DEBUG) Log.d(TAG, "EX_VERSION=" + EX_VERSION, ex);
    }

    private final Object mNamedLightsManager;
    private final NamedLightsManagerEx mNamedLightsManagerEx;

    private NamedLightsManager(Object namedLightsManager) {
        NamedLightsManagerEx namedLightsManagerEx = null;
        try {
            if (EX_VERSION > 0) {
                namedLightsManagerEx = (NamedLightsManagerEx) ProxyUtil.wrap(namedLightsManager, NamedLightsManagerEx.class);
            }
        } catch (Exception ex) {
            onException(ex);
        }
        mNamedLightsManager = namedLightsManager;
        mNamedLightsManagerEx = namedLightsManagerEx;
    }

    public Object getNamedLightsManager() {
        return mNamedLightsManager;
    }

    public NamedLight getLightEx(int id) {
        try {
            if (mNamedLightsManagerEx != null) {
                if (EX_VERSION >= 20190221) {
                    final NamedLightEx lightEx = mNamedLightsManagerEx.getLightEx(id);
                    return (lightEx == null) ? null : new NamedLight(lightEx);
                }
            }
        } catch (Exception ex) {
            onException(ex);
        }
        return null;
    }

    public NamedLight[] getLightsEx() {
        try {
            if (mNamedLightsManagerEx != null) {
                if (EX_VERSION >= 20190221) {
                    final NamedLightEx[] lightsEx = mNamedLightsManagerEx.getLightsEx();
                    if (lightsEx != null) {
                        final NamedLight[] lightsCompat = new NamedLight[lightsEx.length];
                        for (int i = 0; i < lightsEx.length; i++) {
                            final NamedLightEx lightEx = lightsEx[i];
                            lightsCompat[i] = (lightEx == null) ? null : new NamedLight(lightEx);
                        }
                        return lightsCompat;
                    }
                }
            }
        } catch (Exception ex) {
            onException(ex);
        }
        return new NamedLight[0];
    }

    public NamedLight[] getLightsByNameEx(String name) {
        try {
            if (mNamedLightsManagerEx != null) {
                if (EX_VERSION >= 20190221) {
                    final NamedLightEx[] lightsEx = mNamedLightsManagerEx.getLightsByNameEx(name);
                    if (lightsEx != null) {
                        final NamedLight[] lightsCompat = new NamedLight[lightsEx.length];
                        for (int i = 0; i < lightsEx.length; i++) {
                            final NamedLightEx lightEx = lightsEx[i];
                            lightsCompat[i] = (lightEx == null) ? null : new NamedLight(lightEx);
                        }
                        return lightsCompat;
                    }
                }
            }
        } catch (Exception ex) {
            onException(ex);
        }
        return new NamedLight[0];
    }
}
