package com.example.expens_tracker.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expens_tracker.ui.theme.AppTheme

@Composable
fun CategoryProgressRow(
    category: String?,   // backend category_name
    amount: Double,
    totalSpending: Double
) {
    val dimens = AppTheme.dimens

    // NEW: backend category style mapper
    val style = CategoryStyleMapper.getStyle(category)

    val percentage = if (totalSpending > 0) (amount / totalSpending).toFloat() else 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimens.small)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = style.icon,
                    contentDescription = null,
                    tint = style.color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(dimens.small))
                Text(
                    text = category ?: "Other",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "$${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = style.color,
            trackColor = style.color.copy(alpha = 0.1f)
        )
    }
}



//work on category section changing date and also lines for expenses