package com.example.mine.ui.theme.Screen

import androidx.compose.foundation.Image
import com.example.mine.R
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MineSweeperGameViewModel(board: List<List<Cell>>, onCellClick: (Int, Int) -> Unit) {
    Column {
        board.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, cell ->
                    MinesweeperCells(
                        cell = cell,
                        onClick = { onCellClick(rowIndex, colIndex) }
                    )
                }
            }
        }
    }
}

@Composable
fun MinesweeperCells(cell: Cell, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.cell_img),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        if (cell.isRevealed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        when {
                            cell.isMine -> Color.Red
                            cell.neighboringMines == 0 -> Color(135, 206, 250)
                            else -> Color.Blue.copy(alpha = 0.5f)
                        }
                    )
            )

            Text(
                text = when {
                    cell.isMine -> "\uD83D\uDCA3"
                    cell.neighboringMines > 0 -> cell.neighboringMines.toString()
                    else -> ""
                },
                color = Color.Green
            )
        }

        if (cell.isFlagged) {
            Text(text = "⛳️", fontSize = 24.sp)
        }
    }
}


