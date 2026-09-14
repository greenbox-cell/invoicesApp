package com.invoices.app.data

import kotlinx.coroutines.flow.Flow

class InvoiceRepository(private val dao: InvoiceDao) {
    fun observeAll(): Flow<List<Invoice>> = dao.observeAll()

    suspend fun getById(id: Long): Invoice? = dao.getById(id)

    suspend fun save(invoice: Invoice): Long {
        return if (invoice.id == 0L) {
            dao.insert(invoice)
        } else {
            dao.update(invoice)
            invoice.id
        }
    }

    suspend fun delete(invoice: Invoice) = dao.delete(invoice)

    suspend fun deleteAll() = dao.deleteAll()
}
