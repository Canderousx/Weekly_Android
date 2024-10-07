import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.Weekly.android.model.Response.ServerResponse

@Composable
fun LoadingComposable(error: ServerResponse?){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF6200EE),
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp))  // Odstęp
            Text("Loading...", style = MaterialTheme.typography.bodyLarge)
            AnimatedVisibility (visible = error != null, enter = fadeIn(), exit = fadeOut()) {
                Text(text = "Device has no internet connection...Retrying")
            }
        }
    }
}
