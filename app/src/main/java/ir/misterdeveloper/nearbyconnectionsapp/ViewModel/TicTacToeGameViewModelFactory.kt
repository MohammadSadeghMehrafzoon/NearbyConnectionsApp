package ir.misterdeveloper.nearbyconnectionsapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.nearby.connection.ConnectionsClient

//Developed by Mohammad Sadegh Mehrafzoon
class TicTacToeGameViewModelFactory(private val connectionsClient: ConnectionsClient) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TicTacToeGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TicTacToeGameViewModel(connectionsClient) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}