package co.tera.iadedemo.util

import android.content.Context
import android.hardware.pac.PhysicalAccessControlManagerEx

object ControlDoorUtil {

    fun requestToOpen(context: Context): String {
        return try {
            val manager = context.getSystemService("physical_access_control")
                as? PhysicalAccessControlManagerEx
                ?: return "Failed: Physical access control service unavailable."

            if (manager.requestToOpen()) {
                "Success: Door opening requested."
            } else {
                "Failed: Door opening request was rejected."
            }
        } catch (e: SecurityException) {
            "Failed: Missing PHYSICAL_ACCESS_CONTROL permission or privileged access."
        } catch (e: Exception) {
            "Error opening door: ${e.message}"
        }
    }
}
