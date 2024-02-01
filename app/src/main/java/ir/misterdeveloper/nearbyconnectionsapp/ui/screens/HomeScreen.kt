package ir.misterdeveloper.nearbyconnectionsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.misterdeveloper.nearbyconnectionsapp.ViewModel.TicTacToeGameViewModel

//Developed by Mohammad Sadegh Mehrafzoon
@Composable
fun HomeScreen(viewModel: TicTacToeGameViewModel) {
    HomeScreen(
        onHostClick = { viewModel.startHosting() },
        onDiscoverClick = { viewModel.startDiscovering() }
    )
}

@Composable
fun HomeScreen(
    onHostClick: () -> Unit,
    onDiscoverClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onHostClick
        ) {
            Text(text = "Host")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onDiscoverClick
        ) {
            Text(text = "Discover")
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(
) {
    HomeScreen(onHostClick = {}, onDiscoverClick = {})
}