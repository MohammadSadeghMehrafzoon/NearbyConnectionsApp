package ir.misterdeveloper.nearbyconnectionsapp.routing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

//Developed by Mohammad Sadegh Mehrafzoon
/**
 * Class defining all possible screens in the app.
 */
sealed class Screen {
    object Home : Screen()
    object Hosting : Screen()
    object Discovering : Screen()
    object Game : Screen()
}

/**
 * Allows you to change the screen in the [MainActivity]
 *
 * Also keeps track of the current screen.
 */
object TicTacToeGameRouter {
    var currentScreen: Screen by mutableStateOf(Screen.Home)

    fun navigateTo(destination: Screen) {
        currentScreen = destination
    }
}