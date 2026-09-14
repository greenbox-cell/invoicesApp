package com.invoices.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.invoices.app.ui.InvoiceViewModel
import com.invoices.app.ui.InvoicesApp
import com.invoices.app.ui.theme.InvoicesTheme

class MainActivity : ComponentActivity() {
    private val viewModel: InvoiceViewModel by viewModels {
        InvoiceViewModel.Factory((application as InvoicesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvoicesTheme {
                InvoicesApp(viewModel = viewModel)
            }
        }
    }
}
