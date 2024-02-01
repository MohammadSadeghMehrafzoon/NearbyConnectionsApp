package ir.misterdeveloper.nearbyconnectionsapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import ir.misterdeveloper.nearbyconnectionsapp.ViewModel.TicTacToeGameViewModel
import ir.misterdeveloper.nearbyconnectionsapp.domin.model.GameState

//Developed by Mohammad Sadegh Mehrafzoon
@Composable
fun GameScreen(viewModel: TicTacToeGameViewModel) {
    val state: GameState by viewModel.state.observeAsState(GameState.Uninitialized)

    BackHandler(onBack = {
        viewModel.goToHome()
    })

    if (state.isOver) {
        GameOverScreen(
            playerWon = state.playerWon,
            onNewGameClick = { viewModel.newGame() }
        )
    } else {
        OngoingGameScreen(
            localPlayer = state.localPlayer,
            playerTurn = state.playerTurn,
            board = state.board,
            onBucketClick = { position -> viewModel.play(position) }
        )
    }
}

@Composable
fun GameOverScreen(
    playerWon: Int,
    onNewGameClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Game over")
        Text(
            text = if (playerWon > 0) "Player $playerWon won!" else "It's a tie!",
            fontWeight = FontWeight.Bold
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp),
            onClick = onNewGameClick
        ) {
            Text(text = "New game!")
        }
    }
}

@Composable
fun OngoingGameScreen(
    localPlayer: Int,
    playerTurn: Int,
    board: List<List<Int>>,
    onBucketClick: (position: Pair<Int, Int>) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "You're player $localPlayer")
            Box(
                modifier = Modifier
                    .padding(4.dp, 0.dp)
                    .size(10.dp)
                    .background(color = getPlayerColor(localPlayer))
            )
        }
        Text(
            text = if (localPlayer == playerTurn) "Your turn!" else "Waiting for player $playerTurn...",
            fontWeight = FontWeight.Bold
        )
        Board(
            board = board,
            onBucketClick = { position -> onBucketClick(position) }
        )
    }
}

@Composable
fun Board(
    board: List<List<Int>>,
    onBucketClick: (position: Pair<Int, Int>) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        for (i in board.indices) {
            Column(modifier = Modifier.weight(1f)) {
                for (j in board.indices) {
                    Bucket(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        player = board[i][j],
                        onClick = { onBucketClick(i to j) }
                    )
                }
            }
        }
    }
}

@Composable
fun Bucket(
    modifier: Modifier,
    player: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(getPlayerColor(player)),
        onClick = onClick
    ) {}
}

private fun getPlayerColor(player: Int): Color {
    return when (player) {
        0 -> Color.White
        1 -> Color.Red
        2 -> Color.Green
        else -> throw IllegalArgumentException("Missing color for player $player")
    }
}

@Preview
@Composable
fun GameOverPlayerWonScreenPreview() {
    GameOverScreen(playerWon = 1, onNewGameClick = {})
}

@Preview
@Composable
fun GameOverTieScreenPreview() {
    GameOverScreen(playerWon = 0, onNewGameClick = {})
}

@Preview
@Composable
fun OngoingGameScreenPreview() {
    OngoingGameScreen(
        localPlayer = 1,
        playerTurn = 2,
        board = listOf(listOf(0, 0, 0), listOf(0, 0, 0), listOf(0, 0, 0)),
        onBucketClick = {}
    )
}