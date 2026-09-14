package com.mealplanner1234.app

import android.app.Application
import com.mealplanner1234.app.data.InvoiceDatabase
import com.mealplanner1234.app.data.InvoiceRepository

class InvoicesApplication : Application() {
    lateinit var repository: InvoiceRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = InvoiceRepository(InvoiceDatabase.create(this).invoiceDao())
    }
}
