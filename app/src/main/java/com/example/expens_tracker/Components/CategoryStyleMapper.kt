package com.example.expens_tracker.Components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategoryStyle(
    val icon: ImageVector,
    val color: Color
)

object CategoryStyleMapper {

    fun getStyle(category: String?): CategoryStyle {
        return when (category) {
            "Food" -> CategoryStyle(Icons.Default.Fastfood, Color(0xFFBA1A1A))
            "Transport" -> CategoryStyle(Icons.Default.DirectionsCar, Color(0xFF8B5000))
            "Shopping" -> CategoryStyle(Icons.Default.ShoppingBag, Color(0xFF6750A4))
            "Groceries" -> CategoryStyle(Icons.Default.ShoppingCart, Color(0xFF006D3A))
            "Health" -> CategoryStyle(Icons.Default.MedicalServices, Color(0xFF006494))
            "Bills" -> CategoryStyle(Icons.Default.ReceiptLong, Color(0xFF496305))
            else -> CategoryStyle(Icons.Default.Payments, Color(0xFF625B71))
        }
    }
}
