package com.example.mine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mine.ui.theme.Screen.Cell
import com.example.mine.ui.theme.Screen.MineSweeperGameViewModel
import com.example.mine.ui.theme.Screen.generateGameWithMines

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GameScreen() }
    }
}

@Composable
fun GameScreen() {
    val boardSize = 8
    val numberOfMines = 8
    var gameBoard by remember { mutableStateOf(generateGameWithMines(boardSize, numberOfMines)) }
    var firstMove by remember { mutableStateOf(true) }
    var statusEmoji by remember { mutableStateOf("🏎️") }
    var flagMode by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = statusEmoji, fontSize = 46.sp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Flag", fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
            Checkbox(checked = flagMode, onCheckedChange = { flagMode = it })
        }

        MineSweeperGameViewModel(gameBoard) { row, col ->
            when {
                flagMode -> gameBoard = toggleFlag(gameBoard, row, col)
                firstMove -> {
                    if (gameBoard[row][col].isMine) {
                        statusEmoji = "☠️"
                        gameBoard = revealMines(gameBoard)
                    } else {
                        gameBoard = handleFirstClick(gameBoard, row, col)
                        firstMove = false
                    }
                }
                else -> {
                    if (gameBoard[row][col].isMine) {
                        statusEmoji = "☠️"
                        gameBoard = revealMines(gameBoard)
                    } else {
                        gameBoard = revealCell(gameBoard, row, col)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            gameBoard = generateGameWithMines(boardSize, numberOfMines)
            firstMove = true
            statusEmoji = "🤖"
        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) {
            Text(text = "Reset Game")
        }
    }
}

fun toggleFlag(board: List<List<Cell>>, row: Int, col: Int): List<List<Cell>> {
    return board.mapIndexed { r, cells ->
        cells.mapIndexed { c, cell ->
            if (r == row && c == col) cell.copy(isFlagged = !cell.isFlagged) else cell
        }
    }
}

fun revealMines(board: List<List<Cell>>): List<List<Cell>> {
    return board.map { row -> row.map { if (it.isMine) it.copy(isRevealed = true) else it } }
}

fun handleFirstClick(board: List<List<Cell>>, row: Int, col: Int): List<List<Cell>> {
    val mutableBoard = board.map { it.toMutableList() }.toMutableList()
    expandRevealedArea(mutableBoard, row, col)
    return mutableBoard
}

fun revealCell(board: List<List<Cell>>, row: Int, col: Int): List<List<Cell>> {
    return board.mapIndexed { r, cells ->
        cells.mapIndexed { c, cell ->
            if (r == row && c == col) cell.copy(isRevealed = true) else cell
        }
    }
}

fun expandRevealedArea(board: MutableList<MutableList<Cell>>, row: Int, col: Int) {
    if (row !in board.indices || col !in board[row].indices || board[row][col].isRevealed) return
    board[row][col] = board[row][col].copy(isRevealed = true)

    val neighbors = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
    neighbors.forEach { (dr, dc) ->
        val newRow = row + dr
        val newCol = col + dc
        if (newRow in board.indices && newCol in board[newRow].indices && !board[newRow][newCol].isMine) {
            board[newRow][newCol] = board[newRow][newCol].copy(isRevealed = true)
        }
    }
}

fun countNeighboringMines(board: List<List<Cell>>, row: Int, col: Int): Int {
    val neighbors = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
    return neighbors.count { (dr, dc) ->
        val newRow = row + dr
        val newCol = col + dc
        newRow in board.indices && newCol in board[newRow].indices && board[newRow][newCol].isMine
    }
}
