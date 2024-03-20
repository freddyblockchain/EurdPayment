import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eurdpayment.Asset.Asset
import com.example.eurdpayment.Components.SlideComponent


@Composable
fun PaymentScreen(asset: Asset, amount: Int, navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), // Fill the maximum height to allow vertical centering
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center the contents vertically
    )  {
        Text("${asset.name} : $amount")
        Spacer(modifier = Modifier.height(16.dp)) // You can adjust the height for desired spacing
        SlideComponent {

        }
    }
}