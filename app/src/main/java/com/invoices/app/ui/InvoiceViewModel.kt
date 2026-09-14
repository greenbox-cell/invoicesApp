package com.invoices.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.invoices.app.data.Invoice
import com.invoices.app.data.InvoiceCurrency
import com.invoices.app.data.InvoiceFilter
import com.invoices.app.data.InvoiceRepository
import com.invoices.app.data.InvoiceStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InvoiceViewModel(private val repository: InvoiceRepository) : ViewModel() {

    val filter = MutableStateFlow(InvoiceFilter.ALL)

    val invoices: StateFlow<List<Invoice>> = combine(
        repository.observeAll(),
        filter
    ) { list, selected ->
        when (selected) {
            InvoiceFilter.ALL -> list
            InvoiceFilter.PENDING -> list.filter { it.invoiceStatus == InvoiceStatus.PENDING }
            InvoiceFilter.PAID -> list.filter { it.invoiceStatus == InvoiceStatus.PAID }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val allInvoices: StateFlow<List<Invoice>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    suspend fun invoiceById(id: Long): Invoice? = repository.getById(id)

    fun save(
        existingId: Long?,
        clientName: String,
        dateMillis: Long,
        amountCents: Long,
        currency: InvoiceCurrency,
        description: String,
        status: InvoiceStatus
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val existing = existingId?.let { repository.getById(it) }
            repository.save(
                Invoice(
                    id = existing?.id ?: 0L,
                    clientName = clientName.trim(),
                    dateMillis = dateMillis,
                    amountCents = amountCents,
                    currencyCode = currency.code,
                    description = description.trim(),
                    status = status.storageValue,
                    createdAt = existing?.createdAt ?: now,
                    updatedAt = now
                )
            )
        }
    }

    fun delete(invoice: Invoice) {
        viewModelScope.launch { repository.delete(invoice) }
    }

    fun deleteAll() {
        viewModelScope.launch { repository.deleteAll() }
    }

    class Factory(private val repository: InvoiceRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return InvoiceViewModel(repository) as T
        }
    }
}
