package com.example.expens_tracker.Components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TrendBarChart(
    monthlyTotals: List<Pair<String, Double>>,
    onMonthClick: (String) -> Unit, // 👈 Added callback
    modifier: Modifier = Modifier.fillMaxWidth().height(250.dp)
) {
    val maxSpending = monthlyTotals.maxOfOrNull { it.second } ?: 1.0

    Row(
        modifier = modifier.padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        monthlyTotals.forEach { (month, amount) ->
            // Inside TrendBarChart.kt
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onMonthClick(month) }, // 👈 Just use .clickable
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ){
                val barHeightRatio = (amount / maxSpending).toFloat()

                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .fillMaxHeight(barHeightRatio.coerceAtLeast(0.01f))
                    ) {
                        drawRoundRect(
                            color = Color(0xFF00A676),
                            size = size,
                            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = month,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }
}