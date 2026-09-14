package com.invoices.app.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.invoices.app.BuildConfig
import com.invoices.app.ui.InvoiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: InvoiceViewModel,
    onPrivacyPolicy: () -> Unit
) {
    var confirmClear by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "This app stores invoices only on this device. Nothing is uploaded to a server.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            ListItem(
                headlineContent = { Text("Privacy Policy") },
                supportingContent = { Text("How Invoices handles your data") },
                leadingContent = { Icon(Icons.Outlined.Policy, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onPrivacyPolicy),
                trailingContent = {
                    TextButton(onClick = onPrivacyPolicy) { Text("Open") }
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Clear all invoices") },
                supportingContent = { Text("Permanently delete every invoice on this device") },
                leadingContent = { Icon(Icons.Outlined.DeleteSweep, contentDescription = null) },
                trailingContent = {
                    TextButton(onClick = { confirmClear = true }) { Text("Clear") }
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("About") },
                supportingContent = {
                    Text("Invoices ${BuildConfig.VERSION_NAME}  ·  Local invoicing for small businesses")
                },
                leadingContent = { Icon(Icons.Outlined.Info, contentDescription = null) }
            )
            Spacer(Modifier.height(24.dp))
        }
    }

    if (confirmClear) {
        AlertDialog(
            onDismissRequest = { confirmClear = false },
            title = { Text("Delete all invoices?") },
            text = { Text("This cannot be undone. PDFs you already shared elsewhere are not affected.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAll()
                    confirmClear = false
                }) { Text("Delete all") }
            },
            dismissButton = {
                TextButton(onClick = { confirmClear = false }) { Text("Cancel") }
            }
        )
    }
}
