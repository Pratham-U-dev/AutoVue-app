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
fun AnalyticsScreen(viewModel: SharedTelemetryViewModel) {
    val driverBehaviour by viewModel.driverBehaviour.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Driver Behaviour Analysis", style = MaterialTheme.typography.headlineMedium)
        
        if (driverBehaviour != null) {
            val behaviour = driverBehaviour!!
            ValueCard(
                title = "Classification",
                value = behaviour.behaviourClass
            )
            
            Text("Debug Features", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
            behaviour.featuresDebug?.forEach { (key, value) ->
                ValueCard(
                    title = key,
                    value = "%.2f".format(value),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Text("Gathering enough data for driver analysis...")
        }
    }
}
