package com.invoices.app

import android.app.Application
import com.invoices.app.data.InvoiceDatabase
import com.invoices.app.data.InvoiceRepository

class InvoicesApplication : Application() {
    lateinit var repository: InvoiceRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = InvoiceRepository(InvoiceDatabase.create(this).invoiceDao())
    }
}
