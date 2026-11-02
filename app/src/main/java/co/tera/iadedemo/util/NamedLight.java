package co.tera.iadedemo.util;

import android.lights.NamedLightEx;
import android.os.Bundle;
import android.util.Log;

public class NamedLight {

    private static final String TAG = "NamedLightCompat";
    private static final boolean DEBUG = false;

    public final static int EX_VERSION = 20190521;

    public final static String PARAMETER_COLOR = NamedLightEx.PARAMETER_COLOR;
    public final static String PARAMETER_TRIGGER = NamedLightEx.PARAMETER_TRIGGER;
    public final static String PARAMETER_BRIGHTNESS = NamedLightEx.PARAMETER_BRIGHTNESS;

    private static void onException(Throwable ex) {
        if (DEBUG) Log.d(TAG, "EX_VERSION=" + EX_VERSION, ex);
    }

    final private Object mNamedLight;
    final private NamedLightEx mNamedLightEx;

    public NamedLight(Object namedLight) {

        NamedLightEx namedLightEx = null;
        try {
            if (EX_VERSION > 0) {
                namedLightEx = (NamedLightEx) ProxyUtil.wrap(namedLight, NamedLightEx.class);
            }
        } catch (Exception ex) {
            onException(ex);
        }
        mNamedLight = namedLight;
        mNamedLightEx = namedLightEx;
    }

    public Object getNamedLight() {
        return mNamedLight;
    }

    public int getId() {
        try {
            if (mNamedLightEx != null) {
                if (EX_VERSION >= 20190221) {
                    return mNamedLightEx.getId();
                }
            }
        } catch (Exception ex) {
            onException(ex);
        }
        return -1;
    }

    public String getName() {
        try {
            if (mNamedLightEx != null) {
                if (EX_VERSION >= 20190221) {
                    return mNamedLightEx.getName();
                }
            }
        } catch (Exception ex) {
            onException(ex);
        }
        return null;
    }

    public Bundle getCurrentParameters() {
        try {
            if (mNamedLightEx != null) {
                if (EX_VERSION >= 20190221) {
                    return mNamedLightEx.getCurrentParameters();
                }
            }
        } catch (Exception ex) {
            onException(ex);
        }
        return null;
    }

    public Bundle getSavedParameters() {
        try {
            if (mNamedLightEx != null) {
                if (EX_VERSION >= 20190221) {
                    return mNamedLightEx.getSavedParameters();
                }
            }
        } catch (Exception ex) {
            onException(ex);
        }
        return null;
    }

    public void saveCurrentParameters() {
        try {
            if (mNamedLightEx != null) {
                if (EX_VERSION >= 20190221) {
                    mNamedLightEx.saveCurrentParameters();
                }
            }
        } catch (Exception ex) {
            onException(ex);
        }
    }

    public void setColor(int color) {
        try {
            if (mNamedLightEx != null) {
                if (EX_VERSION >= 20190221) {
                    mNamedLightEx.setColor(color);
                }
            }
        } catch (Exception ex) {
            onException(ex);
        }
    }
}
