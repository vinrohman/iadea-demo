package co.tera.iadedemo.util

import android.content.Context
import android.lights.NamedLightEx
import android.os.Build
import android.os.Bundle
import androidx.compose.ui.graphics.Color

data class LightStatusData(
    val lightName: String,
    val mode: Int,
    val colorInt: Int,
    val brightness: Float,
    val color: Color
)

object ControlLightUtil {

    private fun requireIadeaDevice(): String? {
        val manufacturer = Build.MANUFACTURER ?: ""
        return if (!manufacturer.equals("iadea", ignoreCase = true)) {
            "Failed: Not an IAdea device (manufacturer='$manufacturer'). LED control only works on IAdea hardware."
        } else {
            null
        }
    }

    fun doPost(context: Context, name: String, brightness: Float, color: Int): String {
        requireIadeaDevice()?.let { return it }
        return try {
            val manager = NamedLightsManager.getInstance(context)
                ?: return "Failed: NamedLightsManager unavailable. Install the APK as a privileged app (/system/priv-app) on an IAdea device, then reboot."

            val lights = manager.getLightsByNameEx(name)
            if (lights.isEmpty()) {
                return "Failed: Light '$name' not found."
            }

            val effectiveBrightness = brightness.coerceIn(0f, 1f)
            val normalizedColor = if ((color and 0xffffff) == 0) 0 else color
            val effectiveIntColor =
                ((((effectiveBrightness * 255f).toInt()
                    .coerceIn(0, 255)) shl 24) or (normalizedColor and 0xffffff))

            lights.forEach { light ->
                light?.setColor(effectiveIntColor)
            }

            "Success (Name: $name, Color: #${
                Integer.toHexString(normalizedColor).uppercase()
            }, Brightness: $effectiveBrightness)"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    fun setLed(context: Context, name: String, brightness: Float, colorInt: Int): String {
        return doPost(context, name, brightness, colorInt)
    }

    fun setGreenLed(context: Context, name: String = "frame"): String {
        return doPost(context, name, 1.0f, 0x00FF00)
    }

    fun setRedLed(context: Context, name: String = "frame"): String {
        return doPost(context, name, 1.0f, 0xFF0000)
    }

    fun setAmberLed(context: Context, name: String = "frame"): String {
        return doPost(context, name, 1.0f, 0xFFBF00)
    }

    fun setBlueLed(context: Context, name: String = "frame"): String {
        return doPost(context, name, 1.0f, 0x0000FF)
    }

    fun turnOffLed(context: Context, name: String = "frame"): String {
        return doPost(context, name, 0.0f, 0)
    }

    fun getActiveStatus(context: Context): Pair<String, LightStatusData?> {
        requireIadeaDevice()?.let { return it to null }
        return try {
            val manager = NamedLightsManager.getInstance(context)
                ?: return "Info: NamedLightsManager unavailable. Install as privileged app and reboot." to null

            val lights = manager.lightsEx
            if (lights.isNullOrEmpty()) {
                return "Info: No lights detected." to null
            }

            val light = lights.firstOrNull() ?: return "Info: No lights detected." to null
            val params: Bundle? = light.currentParameters

            val packedColor = params?.getInt(NamedLight.PARAMETER_COLOR, 0) ?: 0
            val brightnessParam = params?.getFloat(NamedLight.PARAMETER_BRIGHTNESS, -1f) ?: -1f

            val alpha = (packedColor ushr 24) and 0xFF
            val rgb = packedColor and 0xFFFFFF

            val resolvedBrightness = when {
                brightnessParam >= 0f -> brightnessParam
                alpha > 0 -> alpha / 255f
                else -> 0f
            }
            val resolvedColorInt = if (resolvedBrightness > 0f) {
                if (rgb != 0) rgb else packedColor and 0xFFFFFF
            } else {
                0
            }
            val activeColor = if (resolvedBrightness > 0f) {
                Color(resolvedColorInt or 0xFF000000.toInt())
            } else {
                Color.DarkGray
            }

            val statusData = LightStatusData(
                lightName = light.name ?: "unknown",
                mode = if (resolvedBrightness > 0f) NamedLightEx.LIGHT_MODE_ON else NamedLightEx.LIGHT_MODE_OFF,
                colorInt = resolvedColorInt,
                brightness = resolvedBrightness,
                color = activeColor
            )

            "Active: ${light.name} | Color=#${
                Integer.toHexString(resolvedColorInt).uppercase()
            } | Brightness=$resolvedBrightness" to statusData
        } catch (e: Exception) {
            "Error reading status: ${e.message}" to null
        }
    }
}