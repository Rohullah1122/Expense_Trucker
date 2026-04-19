package com.example.expens_tracker.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.expens_tracker.ui.theme.AppTheme

@Composable
fun SummaryCard(content: @Composable () -> Unit) {
    val dimens = AppTheme.dimens
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(dimens.extraLarge),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(dimens.glassBorder, MaterialTheme.colorScheme.outline)
    ) {
        Box(
            modifier = Modifier
                .padding(dimens.large)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Now any composable you pass in will sit perfectly in the center
            content()
        }
    }
}