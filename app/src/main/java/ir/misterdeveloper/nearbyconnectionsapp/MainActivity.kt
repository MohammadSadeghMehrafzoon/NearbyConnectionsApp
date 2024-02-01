package ir.misterdeveloper.nearbyconnectionsapp

import android.os.Build
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.android.gms.nearby.Nearby
import ir.misterdeveloper.nearbyconnectionsapp.ViewModel.TicTacToeGameViewModel
import ir.misterdeveloper.nearbyconnectionsapp.ViewModel.TicTacToeGameViewModelFactory
import ir.misterdeveloper.nearbyconnectionsapp.routing.Screen
import ir.misterdeveloper.nearbyconnectionsapp.routing.TicTacToeGameRouter
import ir.misterdeveloper.nearbyconnectionsapp.ui.screens.DiscoveringScreen
import ir.misterdeveloper.nearbyconnectionsapp.ui.screens.GameScreen
import ir.misterdeveloper.nearbyconnectionsapp.ui.screens.HomeScreen
import ir.misterdeveloper.nearbyconnectionsapp.ui.screens.HostingScreen
import ir.misterdeveloper.nearbyconnectionsapp.ui.theme.NearbyConnectionsAppTheme


//Developed by Mohammad Sadegh Mehrafzoon
class MainActivity : ComponentActivity() {

    private val viewModel: TicTacToeGameViewModel by viewModels {
        TicTacToeGameViewModelFactory(Nearby.getConnectionsClient(applicationContext))
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.any { !it.value }) {
            Toast.makeText(this, "Required permissions needed", Toast.LENGTH_LONG).show()
            finish()
        } else {
            recreate()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestMultiplePermissions.launch(
                REQUIRED_PERMISSIONS
            )
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.isEmpty() || permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    @Composable
    private fun MainActivityScreen() {
        Surface {
            when (TicTacToeGameRouter.currentScreen) {
                is Screen.Home -> HomeScreen(viewModel)
                is Screen.Hosting -> HostingScreen(viewModel)
                is Screen.Discovering -> DiscoveringScreen(viewModel)
                is Screen.Game -> GameScreen(viewModel)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NearbyConnectionsAppTheme {
                MainActivityScreen()
            }
        }
    }

    //Some of these are dangerous permissions, therefore you’ll need to request user consent.
    private companion object {
        val REQUIRED_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NearbyConnectionsAppTheme {
        Greeting("Android")
    }
}