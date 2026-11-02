package co.tera.iadedemo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.tera.iadedemo.util.ControlLightUtil

@Composable
fun ControlLightContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var statusText by remember { mutableStateOf("Status: No action yet") }
    var activeColor by remember { mutableStateOf(Color.Gray) }
    var activeBrightness by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(activeColor, shape = CircleShape)
                .border(2.dp, Color.LightGray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${(activeBrightness * 100).toInt()}%",
                color = if (activeBrightness > 0.5f) Color.Black else Color.White
            )
        }

        Text(text = statusText)

        Button(onClick = {
            statusText = ControlLightUtil.setGreenLed(context)
            activeColor = Color(0x00FF00 or 0xFF000000.toInt())
            activeBrightness = 1.0f
        }) {
            Text("Green LED")
        }

        Button(onClick = {
            statusText = ControlLightUtil.setRedLed(context)
            activeColor = Color(0xFF0000 or 0xFF000000.toInt())
            activeBrightness = 1.0f
        }) {
            Text("Red LED")
        }

        Button(onClick = {
            statusText = ControlLightUtil.setAmberLed(context)
            activeColor = Color(0xFFBF00 or 0xFF000000.toInt())
            activeBrightness = 1.0f
        }) {
            Text("Amber LED")
        }

        Button(onClick = {
            statusText = ControlLightUtil.turnOffLed(context)
            activeColor = Color.DarkGray
            activeBrightness = 0f
        }) {
            Text("LED")
        }

        Button(onClick = {
            val (text, data) = ControlLightUtil.getActiveStatus(context)
            statusText = text
            if (data != null) {
                activeColor = data.color
                activeBrightness = if (data.brightness > 0f) data.brightness else 0f
            } else {
                activeColor = Color.Gray
                activeBrightness = 0f
            }
        }) {
            Text("Show Active Color & Status")
        }
    }
}
