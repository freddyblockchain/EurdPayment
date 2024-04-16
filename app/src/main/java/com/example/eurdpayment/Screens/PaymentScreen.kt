import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eurdpayment.Algorand.AssetTransfer
import com.example.eurdpayment.Algorand.Models.Asset
import com.example.eurdpayment.Components.SlideComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun PaymentScreen(asset: Asset, amount: Float, receiver: String, navController: NavController) {
    var paymentInitiated by remember {
        mutableStateOf(false)
    }
    var paymentDone by remember {
        mutableStateOf(false)
    }
    var paymentSuccessfull by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(), // Fill the maximum height to allow vertical centering
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // Center the contents vertically
    )  {
        if(!paymentInitiated){
            Text("${asset.name} : $amount")
            Spacer(modifier = Modifier.height(16.dp)) // You can adjust the height for desired spacing
            SlideComponent {
                paymentInitiated = true
                CoroutineScope(Dispatchers.IO).launch {
                    // Perform your network operation here
                    try {
                        val result = AssetTransfer(asset, amount, receiver)// Replace this with your actual network call
                        withContext(Dispatchers.Main) {
                            paymentSuccessfull = result
                        }
                    } catch (e: Exception) {
                        // Handle any exceptions
                        withContext(Dispatchers.Main) {
                            paymentSuccessfull = false
                        }
                    } finally {
                        paymentDone = true
                    }
                }
            }
        }  //Spinner
        else if(paymentInitiated && !paymentDone){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = MaterialTheme.colors.primary, modifier = Modifier.size(100.dp))
            }
        }
    }
    if(paymentDone){
        ResultIcon(isSuccess = paymentSuccessfull)
    }
}
@Composable
fun ResultIcon(isSuccess: Boolean) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        if (isSuccess) {
            // Display a green checkmark
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Success",
                tint = Color.Green,
                modifier = Modifier.size(100.dp)
            )
        } else {
            // Display a red cross
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}