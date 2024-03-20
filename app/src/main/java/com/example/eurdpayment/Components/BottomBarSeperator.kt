import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun BottomNavigationSeparator() {
    Divider(
        color = Color.White,
        modifier = Modifier
            .height(55.dp) // Adjust the height to match your BottomNavigationItem height or as desired
            .width(6.dp)
    )
}