package com.mealplanner1234.app.ui.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mealplanner1234.app.data.Dates
import com.mealplanner1234.app.data.InvoiceCurrency
import com.mealplanner1234.app.data.InvoiceStatus
import com.mealplanner1234.app.data.Money
import com.mealplanner1234.app.ui.InvoiceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceFormScreen(
    viewModel: InvoiceViewModel,
    invoiceId: Long?,
    onDone: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var clientName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf(InvoiceCurrency.USD) }
    var status by remember { mutableStateOf(InvoiceStatus.PENDING) }
    var dateMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var clientError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var loaded by remember { mutableStateOf(invoiceId == null) }

    LaunchedEffect(invoiceId) {
        if (invoiceId != null) {
            val existing = viewModel.invoiceById(invoiceId)
            if (existing != null) {
                clientName = existing.clientName
                description = existing.description
                amountText = Money.centsToInput(existing.amountCents)
                currency = existing.currency
                status = existing.invoiceStatus
                dateMillis = existing.dateMillis
            }
            loaded = true
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = { Text(if (invoiceId == null) "New invoice" else "Edit invoice") },
                windowInsets = TopAppBarDefaults.windowInsets,
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (!loaded) return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = clientName,
                onValueChange = {
                    clientName = it
                    clientError = false
                },
                label = { Text("Client name") },
                isError = clientError,
                supportingText = if (clientError) {
                    { Text("Enter a client name") }
                } else null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Date  ·  ${Dates.formatDisplay(dateMillis)}")
            }

            Spacer(Modifier.height(16.dp))
            Text("Currency", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = currency == InvoiceCurrency.USD,
                    onClick = { currency = InvoiceCurrency.USD },
                    label = { Text("$  USD") }
                )
                FilterChip(
                    selected = currency == InvoiceCurrency.ILS,
                    onClick = { currency = InvoiceCurrency.ILS },
                    label = { Text("₪  ILS") }
                )
            }

            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it.filter { ch -> ch.isDigit() || ch == '.' }
                    amountError = false
                },
                label = { Text("Amount (${currency.symbol})") },
                isError = amountError,
                supportingText = if (amountError) {
                    { Text("Enter a valid amount") }
                } else null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Item description") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Text("Status", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = status == InvoiceStatus.PENDING,
                    onClick = { status = InvoiceStatus.PENDING },
                    label = { Text("Pending") }
                )
                FilterChip(
                    selected = status == InvoiceStatus.PAID,
                    onClick = { status = InvoiceStatus.PAID },
                    label = { Text("Paid") }
                )
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val cents = Money.parseToCents(amountText)
                    clientError = clientName.isBlank()
                    amountError = cents == null || cents < 0
                    if (clientError || amountError) return@Button
                    viewModel.save(
                        existingId = invoiceId,
                        clientName = clientName,
                        dateMillis = dateMillis,
                        amountCents = cents!!,
                        currency = currency,
                        description = description,
                        status = status
                    )
                    scope.launch { onDone() }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save invoice")
            }
        }
    }

    if (showDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { dateMillis = it }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}
