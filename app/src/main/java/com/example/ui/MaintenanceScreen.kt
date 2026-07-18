package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.viewmodel.SharedTelemetryViewModel

@Composable
fun MaintenanceScreen(viewModel: SharedTelemetryViewModel) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Maintenance (Coming Soon)", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
