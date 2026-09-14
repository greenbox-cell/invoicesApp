package com.mealplanner1234.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Invoice::class], version = 1, exportSchema = false)
abstract class InvoiceDatabase : RoomDatabase() {
    abstract fun invoiceDao(): InvoiceDao

    companion object {
        fun create(context: Context): InvoiceDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                InvoiceDatabase::class.java,
                "invoices.db"
            ).build()
    }
}
