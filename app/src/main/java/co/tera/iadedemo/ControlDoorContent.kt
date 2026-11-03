package co.tera.iadedemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.tera.iadedemo.util.ControlDoorUtil

@Composable
fun ControlDoorContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var statusText by remember { mutableStateOf("Status: No action yet") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = statusText)

        Button(onClick = {
            statusText = ControlDoorUtil.requestToOpen(context)
        }) {
            Text("Open Door")
        }
    }
}
