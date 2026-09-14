package com.invoices.app.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.invoices.app.data.Dates
import com.invoices.app.data.Money
import com.invoices.app.pdf.PdfShare
import com.invoices.app.ui.InvoiceViewModel
import com.invoices.app.ui.components.StatusChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceDetailScreen(
    viewModel: InvoiceViewModel,
    invoiceId: Long,
    onEdit: (Long) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val invoices by viewModel.allInvoices.collectAsStateWithLifecycle()
    val current = invoices.find { it.id == invoiceId }
    var confirmDelete by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(current?.number ?: "Invoice") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (current != null) {
                        IconButton(onClick = { onEdit(current.id) }) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { confirmDelete = true }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (current == null) {
            Column(Modifier.padding(padding).padding(24.dp)) {
                Text("This invoice is no longer available.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(current.clientName, style = MaterialTheme.typography.headlineMedium)
                StatusChip(current.invoiceStatus)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                Money.format(current.amountCents, current.currency),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))
            Labeled("Date", Dates.formatDisplay(current.dateMillis))
            Labeled("Currency", "${current.currency.symbol}  ${current.currency.code}")
            Labeled("Item description", current.description.ifBlank { "—" })

            Spacer(Modifier.height(28.dp))
            Button(
                onClick = { PdfShare.share(context, current) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Outlined.Share, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Share PDF")
            }
            Spacer(Modifier.height(8.dp))
            FilledTonalButton(
                onClick = { onEdit(current.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit invoice")
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { confirmDelete = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        }
    }

    if (confirmDelete && current != null) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete invoice?") },
            text = { Text("This removes ${current.number} from this device. You cannot undo this.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(current)
                    confirmDelete = false
                    onBack()
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun Labeled(label: String, value: String) {
    Text(
        text = label.uppercase(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(Modifier.height(4.dp))
    Text(value, style = MaterialTheme.typography.bodyLarge)
    Spacer(Modifier.height(16.dp))
}
