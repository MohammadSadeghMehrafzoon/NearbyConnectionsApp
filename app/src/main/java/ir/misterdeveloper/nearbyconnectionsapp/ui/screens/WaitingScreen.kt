package ir.misterdeveloper.nearbyconnectionsapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.misterdeveloper.nearbyconnectionsapp.ViewModel.TicTacToeGameViewModel
import ir.misterdeveloper.nearbyconnectionsapp.domin.model.TicTacToeGame

//Developed by Mohammad Sadegh Mehrafzoon
@Composable
fun HostingScreen(
    viewModel: TicTacToeGameViewModel
) {
    BackHandler(onBack = {
        viewModel.goToHome()
    })

    WaitingScreen(
        title = "Hosting...",
        onStopClick = { viewModel.goToHome() }
    )
}

@Composable
fun DiscoveringScreen(
    viewModel: TicTacToeGameViewModel
) {
    BackHandler(onBack = {
        viewModel.goToHome()
    })

    WaitingScreen(
        title = "Discovering...",
        onStopClick = { viewModel.goToHome() }
    )
}

@Composable
fun WaitingScreen(
    title: String,
    onStopClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title)
        CircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp)
                .size(80.dp)
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onStopClick
        ) {
            Text(text = "Stop")
        }
    }
}

@Preview
@Composable
fun WaitingScreenPreview() {
    WaitingScreen(title = "Hosting...", onStopClick = {})
}