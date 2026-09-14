package com.mealplanner1234.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mealplanner1234.app.data.InvoiceStatus
import com.mealplanner1234.app.ui.theme.Paid
import com.mealplanner1234.app.ui.theme.PaidContainer
import com.mealplanner1234.app.ui.theme.Pending
import com.mealplanner1234.app.ui.theme.PendingContainer

@Composable
fun StatusChip(status: InvoiceStatus, modifier: Modifier = Modifier) {
    val (bg, fg) = if (status == InvoiceStatus.PAID) {
        PaidContainer to Paid
    } else {
        PendingContainer to Pending
    }
    Surface(
        modifier = modifier,
        color = bg,
        contentColor = fg,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = status.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp
        )
    }
}
