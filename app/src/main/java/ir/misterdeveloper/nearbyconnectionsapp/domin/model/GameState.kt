package ir.misterdeveloper.nearbyconnectionsapp.domin.model

//Developed by Mohammad Sadegh Mehrafzoon
data class GameState(
    val localPlayer: Int,
    val playerTurn: Int,
    val playerWon: Int,
    val isOver: Boolean,
    val board: List<List<Int>>
) {
    companion object {
        val Uninitialized = GameState(0, 0, 0, false, emptyList())
    }
}