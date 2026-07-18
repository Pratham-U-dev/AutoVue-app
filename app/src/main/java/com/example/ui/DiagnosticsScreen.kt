package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.components.ValueCard
import com.example.viewmodel.SharedTelemetryViewModel

@Composable
fun DiagnosticsScreen(viewModel: SharedTelemetryViewModel) {
    val health by viewModel.healthPrediction.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Vehicle Health", style = MaterialTheme.typography.headlineMedium)
        
        if (health != null) {
            val h = health!!
            ValueCard(
                title = "Overall Status",
                value = h.status,
                modifier = Modifier.fillMaxWidth()
            )
            ValueCard(
                title = "Confidence",
                value = "%.1f".format(h.confidence * 100),
                unit = "%",
                modifier = Modifier.fillMaxWidth()
            )
            
            Text("Probabilities", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            h.probabilities.forEach { (key, value) ->
                ValueCard(
                    title = key,
                    value = "%.1f".format(value * 100),
                    unit = "%",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Text("Waiting for health prediction...")
        }
    }
}
