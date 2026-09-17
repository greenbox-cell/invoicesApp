package com.mealplanner1234.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.mealplanner1234.app.ui.InvoiceViewModel
import com.mealplanner1234.app.ui.InvoicesApp
import com.mealplanner1234.app.ui.theme.InvoicesTheme

class MainActivity : ComponentActivity() {
    private val viewModel: InvoiceViewModel by viewModels {
        InvoiceViewModel.Factory((application as InvoicesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            InvoicesTheme {
                InvoicesApp(viewModel = viewModel)
            }
        }
    }
}
