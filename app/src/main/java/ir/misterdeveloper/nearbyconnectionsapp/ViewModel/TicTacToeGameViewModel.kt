package ir.misterdeveloper.nearbyconnectionsapp.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import ir.misterdeveloper.nearbyconnectionsapp.domin.model.GameState
import ir.misterdeveloper.nearbyconnectionsapp.domin.model.TicTacToeGame
import ir.misterdeveloper.nearbyconnectionsapp.routing.Screen
import ir.misterdeveloper.nearbyconnectionsapp.routing.TicTacToeGameRouter
import java.util.UUID
import ir.misterdeveloper.nearbyconnectionsapp.BuildConfig

//Developed by Mohammad Sadegh Mehrafzoon
class TicTacToeGameViewModel(private val connectionsClient: ConnectionsClient):ViewModel() {

    private val localUsername = UUID.randomUUID().toString()
    private var localPlayer: Int = 0
    private var opponentPlayer: Int = 0
    private var opponentEndpointId: String = ""

    private var game = TicTacToeGame()

    private val _state = MutableLiveData(GameState.Uninitialized)
    val state: LiveData<GameState> = _state

    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            Log.d(TAG, "onPayloadReceived")

            // You check if the payload type is BYTES.
            if (payload.type == Payload.Type.BYTES) {
                //You convert back the Payload to a position Pair object.
                val position = payload.toPosition()
                Log.d(TAG, "Received [${position.first},${position.second}] from $endpointId")
                //Instruct the game that the opponent has played this position.
                play(opponentPlayer, position)
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Log.d(TAG, "onPayloadTransferUpdate")
        }
    }

    //The Advertiser and Discoverer need to accept the connection, both will get notified via ConnectionLifecycleCallback.onConnectionInitiated()
    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            Log.d(TAG, "onConnectionInitiated")

            Log.d(TAG, "Accepting connection...")
            connectionsClient.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
            Log.d(TAG, "onConnectionResult")

            when (resolution.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> {
                    Log.d(TAG, "ConnectionsStatusCodes.STATUS_OK")

                    connectionsClient.stopAdvertising()
                    connectionsClient.stopDiscovery()
                    opponentEndpointId = endpointId
                    Log.d(TAG, "opponentEndpointId: $opponentEndpointId")
                    newGame()
                    TicTacToeGameRouter.navigateTo(Screen.Game)
                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                    Log.d(TAG, "ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED")
                }
                ConnectionsStatusCodes.STATUS_ERROR -> {
                    Log.d(TAG, "ConnectionsStatusCodes.STATUS_ERROR")
                }
                else -> {
                    Log.d(TAG, "Unknown status code ${resolution.status.statusCode}")
                }
            }
        }

        override fun onDisconnected(endpointId: String) {
            Log.d(TAG, "onDisconnected")
            goToHome()
        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Log.d(TAG, "onEndpointFound")

            Log.d(TAG, "Requesting connection...")
            connectionsClient.requestConnection(
                //You need to pass a local endpoint name.
                localUsername,
                //Pass the endpointId you’ve just found.
                endpointId,
                connectionLifecycleCallback
            ).addOnSuccessListener {
                //Once the client successfully requests a connection, it logs to the console.
                Log.d(TAG, "Successfully requested a connection")
            }.addOnFailureListener {
                //If the client fails, it logs to the console.
                Log.d(TAG, "Failed to request the connection")
            }
        }

        override fun onEndpointLost(endpointId: String) {
            Log.d(TAG, "onEndpointLost")
        }
    }


    fun startHosting() {
        Log.d(TAG, "Start advertising...")
        TicTacToeGameRouter.navigateTo(Screen.Hosting)
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(GAME_STRATEGY).build()

        connectionsClient.startAdvertising(
            //You need to pass a local endpoint name.
            localUsername,
            //You set BuildConfig.APPLICATION_ID for service ID because you want a Discoverer to find you with this unique id.
            BuildConfig.APPLICATION_ID,
            //Calls to the connectionLifecycleCallback methods occur when establishing a connection with a Discoverer.
            connectionLifecycleCallback,
            //You pass the options containing the strategy previously configured.
            advertisingOptions
        ).addOnSuccessListener {
            //Once the client successfully starts advertising, you set the local player as player 1, and the opponent will be player 2.
            Log.d(TAG, "Advertising...")
            localPlayer = 1
            opponentPlayer = 2
        }.addOnFailureListener {
            //If the client fails to advertise, it logs to the console and returns to the home screen.
            Log.d(TAG, "Unable to start advertising")
            TicTacToeGameRouter.navigateTo(Screen.Home)
        }
    }

    fun startDiscovering() {
        Log.d(TAG, "Start discovering...")
        TicTacToeGameRouter.navigateTo(Screen.Discovering)
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(GAME_STRATEGY).build()

        connectionsClient.startDiscovery(
            BuildConfig.APPLICATION_ID,
            //Calls to the endpointDiscoveryCallback methods occur when establishing a connection with an Advertiser.
            endpointDiscoveryCallback,
            //You pass the options containing the strategy previously configured.
            discoveryOptions
        ).addOnSuccessListener {
            //Once the client successfully starts discovering you set the local player as player 2, the opponent will be player 1.
            Log.d(TAG, "Discovering...")
            localPlayer = 2
            opponentPlayer = 1
        }.addOnFailureListener {
            //If the client fails to discover, it logs to the console and returns to the home screen.
            Log.d(TAG, "Unable to start discovering")
            TicTacToeGameRouter.navigateTo(Screen.Home)
        }
    }

    fun newGame() {
        Log.d(TAG, "Starting new game")
        game = TicTacToeGame()
        _state.value = GameState(localPlayer, game.playerTurn, game.playerWon, game.isOver, game.board)
    }

    fun play(position: Pair<Int, Int>) {
        if (game.playerTurn != localPlayer) return
        if (game.isPlayedBucket(position)) return

        play(localPlayer, position)
        sendPosition(position)
    }

    private fun play(player: Int, position: Pair<Int, Int>) {
        Log.d(TAG, "Player $player played [${position.first},${position.second}]")

        game.play(player, position)
        _state.value = GameState(localPlayer, game.playerTurn, game.playerWon, game.isOver, game.board)
    }

    private fun sendPosition(position: Pair<Int, Int>) {
        Log.d(TAG, "Sending [${position.first},${position.second}] to $opponentEndpointId")
        connectionsClient.sendPayload(
            opponentEndpointId,
            position.toPayLoad()
        )
    }


    //You need to disconnect the client whenever one player decides to exit the game. Add the following to ensure the client is stopped whenever the ViewModel is destroyed:
    override fun onCleared() {
        stopClient()
        super.onCleared()
    }

    fun goToHome() {
        stopClient()
        TicTacToeGameRouter.navigateTo(Screen.Home)
    }

    private fun stopClient() {
        Log.d(TAG, "Stop advertising, discovering, all endpoints")
        connectionsClient.stopAdvertising()
        connectionsClient.stopDiscovery()
        connectionsClient.stopAllEndpoints()
        localPlayer = 0
        opponentPlayer = 0
        opponentEndpointId = ""
    }

    //Here, you’re using the opponentEndpointId you previously saved to send the position. You need to convert the position, which is a Pair to a Payload object. To do that, add the following extension to the end of the file:
    //You’ve now converted the pair into a comma separated string which is converted to a ByteArray that is finally used to create a Payload.
    fun Pair<Int, Int>.toPayLoad() = Payload.fromBytes("$first,$second".toByteArray(Charsets.UTF_8))

    fun Payload.toPosition(): Pair<Int, Int> {
        val positionStr = String(asBytes()!!, Charsets.UTF_8)
        val positionArray = positionStr.split(",")
        return positionArray[0].toInt() to positionArray[1].toInt()
    }

    private companion object {
        const val TAG = "TicTacToeGameVM"
        val GAME_STRATEGY = Strategy.P2P_POINT_TO_POINT
    }

}