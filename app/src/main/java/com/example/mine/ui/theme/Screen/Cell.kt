package com.example.mine.ui.theme.Screen

import com.example.mine.countNeighboringMines
import kotlin.random.Random

data class Cell(
    val isMine: Boolean = false,
    val neighboringMines: Int = 0,
    val isRevealed: Boolean = false,
    val isFlagged: Boolean = false
)

fun generateGameWithMines(size: Int, mineCount: Int): List<List<Cell>> {
    val board = List(size) { MutableList(size) { Cell() } }
    var placedMines = 0

    while (placedMines < mineCount) {
        val randomRow = Random.nextInt(size)
        val randomCol = Random.nextInt(size)

        if (!board[randomRow][randomCol].isMine) {
            board[randomRow][randomCol] = board[randomRow][randomCol].copy(isMine = true)
            placedMines++
        }
    }

    board.forEachIndexed { rowIndex, rowList ->
        rowList.forEachIndexed { colIndex, cell ->
            if (!cell.isMine) {
                val neighboringMineCount = countNeighboringMines(board, rowIndex, colIndex)
                board[rowIndex][colIndex] = cell.copy(neighboringMines = neighboringMineCount)
            }
        }
    }
    return board
}


