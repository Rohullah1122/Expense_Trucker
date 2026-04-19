package com.example.expens_tracker.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expens_tracker.API.Models.Expense
import com.example.expens_tracker.Classes.Routes
import com.example.expens_tracker.ui.theme.AppTheme

@Composable
fun TransactionListSection(
    navController: NavController,
    transactions: List<Expense>,
    fullListForNavigation: List<Expense>,
    title: String
) {
    val dimens = AppTheme.dimens

    Column(modifier = Modifier.fillMaxWidth()) {

        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimens.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Always show See All if more than 5 items
            if (fullListForNavigation.size > 5) {
                TextButton(onClick = {
                    navController.navigate(Routes.ThismonthTransaction)
                }) {
                    Text("See All", color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        // Glass Style Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimens.extraLarge))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                    RoundedCornerShape(dimens.extraLarge)
                )
                .padding(dimens.medium)
        ) {
            Column {
                transactions.forEachIndexed { index, expense ->
                    ExpenseItem(expense)

                    if (index < transactions.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}
