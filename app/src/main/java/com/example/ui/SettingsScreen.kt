package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.viewmodel.SharedTelemetryViewModel

@Composable
fun SettingsScreen(viewModel: SharedTelemetryViewModel) {
    val status by viewModel.simulatorStatus.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Settings & Simulator Control", style = MaterialTheme.typography.headlineMedium)
        
        if (status != null) {
            val s = status!!
            Text("Simulator State: ${s.state}")
            Text("Dataset: ${s.datasetName}")
            Text("Speed: ${s.speed}x")
            Text("Progress: ${"%.1f".format(s.playbackPercent)}%")
        } else {
            Text("Simulator status unknown")
        }
        
        Button(onClick = { viewModel.startSimulation() }) {
            Text("Start / Reset Simulator")
        }
    }
}
