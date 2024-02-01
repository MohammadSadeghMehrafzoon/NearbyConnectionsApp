package ir.misterdeveloper.nearbyconnectionsapp.domin.model


//Developed by Mohammad Sadegh Mehrafzoon
class TicTacToeGame(private val players: Int = 2, private val boardSize: Int = 3, private val winningCount: Int = 3) {
    var playerTurn = 1
        private set

    var playerWon = 0
        private set

    var isOver = false
        private set

    val board = List(boardSize) {
        MutableList(boardSize) { 0 }
    }

    fun play(player: Int, position: Pair<Int, Int>) {
        if (player < 1 || player > players) throw IllegalArgumentException("Invalid player")
        if (player != playerTurn) throw IllegalArgumentException("Wrong turn")
        if (isOver) throw IllegalArgumentException("Game over")
        if (board[position.first][position.second] > 0) throw IllegalArgumentException("Already played")

        board[position.first][position.second] = player
        if (hasPlayerWon(playerTurn)) {
            playerWon = playerTurn
            isOver = true
        } else if (!anyNonPlayedBucket()) {
            isOver = true
        }
        playerTurn = (player % players) + 1
    }

    private fun anyNonPlayedBucket() = board.flatten().any { it == 0 }

    fun isPlayedBucket(position: Pair<Int, Int>) = board[position.first][position.second] != 0

    private fun hasPlayerWon(player: Int): Boolean {
        if (player < 1 || player > players) return false

        var count: Int
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                if (board[i][j] == player) {
                    // Check horizontal
                    if (j + winningCount <= boardSize) {
                        count = 1
                        for (J in j + 1 until j + winningCount) {
                            if (board[i][J] != player) break
                            count++
                            if (count == winningCount) return true
                        }
                    }

                    // Check vertical
                    if (i + winningCount <= boardSize) {
                        count = 1
                        for (I in i + 1 until i + winningCount) {
                            if (board[I][j] != player) break
                            count++
                            if (count == winningCount) return true
                        }
                    }

                    // Check diagonal 1
                    if (j + winningCount <= boardSize && i + winningCount <= boardSize) {
                        count = 1
                        for ((I, J) in (i + 1 until i + winningCount).zip(j + 1 until j + winningCount)) {
                            if (board[I][J] != player) break
                            count++
                            if (count == winningCount) return true
                        }
                    }

                    // Check diagonal 2
                    if (j - winningCount + 1 >= 0 && i + winningCount <= boardSize) {
                        count = 1
                        for ((I, J) in (i + 1 until i + winningCount).zip(j - 1 downTo 0)) {
                            if (board[I][J] != player) break
                            count++
                            if (count == winningCount) return true
                        }
                    }
                }
            }
        }

        return false
    }

    override fun toString(): String {
        return board.joinToString("\n") { it.joinToString(",") }
    }
}