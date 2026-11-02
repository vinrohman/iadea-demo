package co.tera.iadedemo.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextEx
import android.lights.NamedLightEx
import android.lights.NamedLightsManagerEx
import androidx.compose.ui.graphics.Color

data class LightStatusData(
    val lightName: String,
    val mode: Int,
    val colorInt: Int,
    val brightness: Float,
    val color: Color
)

@SuppressLint("WrongConstant")
object ControlLightUtil {

    fun setLed(context: Context, name: String, brightness: Float, colorInt: Int): String {
        return try {
            val lightsManager =
                context.getSystemService(ContextEx.NAMED_LIGHTS_SERVICE) as? NamedLightsManagerEx
            val lights = lightsManager?.getLightsByNameEx(name)?.takeIf { it.isNotEmpty() }
                ?: lightsManager?.lightsEx

            if (lights.isNullOrEmpty()) {
                return "Failed: Light '$name' not found."
            }

            lights.forEach { light ->
                val params = light.currentParameters
                if (brightness > 0f) {
                    params.putInt(NamedLightEx.PARAMETER_MODE, NamedLightEx.LIGHT_MODE_ON)
                    params.putFloat(NamedLightEx.PARAMETER_BRIGHTNESS, brightness)
                    params.putInt(NamedLightEx.PARAMETER_COLOR, colorInt)
                } else {
                    params.putInt(NamedLightEx.PARAMETER_MODE, NamedLightEx.LIGHT_MODE_OFF)
                    params.putFloat(NamedLightEx.PARAMETER_BRIGHTNESS, 0f)
                    params.putInt(NamedLightEx.PARAMETER_COLOR, 0x000000)
                }
                light.currentParameters = params
                light.saveCurrentParameters()
            }
            "Success (Color: #${Integer.toHexString(colorInt).uppercase()}, Brightness: $brightness)"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    fun setGreenLed(context: Context, name: String = "frame"): String {
        return setLed(context, name, 1.0f, 0x00FF00)
    }

    fun setRedLed(context: Context, name: String = "frame"): String {
        return setLed(context, name, 1.0f, 0xFF0000)
    }

    fun setAmberLed(context: Context, name: String = "level"): String {
        return setLed(context, name, 1.0f, 0xFFBF00)
    }

    fun turnOffLed(context: Context, name: String = "frame"): String {
        return setLed(context, name, 0.0f, 0x000000)
    }

    fun getActiveStatus(context: Context): Pair<String, LightStatusData?> {
        return try {
            val lightsManager =
                context.getSystemService(ContextEx.NAMED_LIGHTS_SERVICE) as? NamedLightsManagerEx
            val lights = lightsManager?.lightsEx
            if (lights.isNullOrEmpty()) {
                "Info: No lights detected." to null
            } else {
                val light = lights.first()
                val params = light.currentParameters
                val mode = params.getInt(NamedLightEx.PARAMETER_MODE, -1)
                val colorInt = params.getInt(NamedLightEx.PARAMETER_COLOR, 0)
                val brightness = params.getFloat(NamedLightEx.PARAMETER_BRIGHTNESS, 0f)

                val activeBrightness = if (mode == NamedLightEx.LIGHT_MODE_ON) brightness else 0f
                val activeColor = if (activeBrightness > 0f) {
                    Color(colorInt or 0xFF000000.toInt())
                } else {
                    Color.DarkGray
                }

                val statusData = LightStatusData(
                    lightName = light.name,
                    mode = mode,
                    colorInt = colorInt,
                    brightness = brightness,
                    color = activeColor
                )

                "Active: ${light.name} | Mode=$mode | Color=#${Integer.toHexString(colorInt).uppercase()} | Brightness=$brightness" to statusData
            }
        } catch (e: Exception) {
            "Error reading status: ${e.message}" to null
        }
    }
}
