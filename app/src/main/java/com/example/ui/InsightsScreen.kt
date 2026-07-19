package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.components.ValueCard
import com.example.viewmodel.SharedTelemetryViewModel

@Composable
fun InsightsScreen(viewModel: SharedTelemetryViewModel) {
    val driverBehaviour by viewModel.driverBehaviour.collectAsState()
    val health by viewModel.healthPrediction.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("AI Insights", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text("Driver Behaviour", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        
        if (driverBehaviour != null) {
            val behaviour = driverBehaviour!!
            ValueCard(
                title = "Classification",
                value = behaviour.behaviourClass,
                modifier = Modifier.fillMaxWidth()
            )
            
            val chunks = behaviour.featuresDebug?.toList()?.chunked(2) ?: emptyList()
            chunks.forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowItems.forEach { (key, value) ->
                        val cleanKey = key.replace(Regex("\\[.*?\\]"), "").replace("_", " ").trim()
                        ValueCard(
                            title = cleanKey,
                            value = "%.1f".format(value),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            Text("Gathering enough data for driver analysis...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Vehicle Health", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        
        if (health != null) {
            val h = health!!
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ValueCard(
                    title = "Overall Status",
                    value = h.status,
                    modifier = Modifier.weight(1f)
                )
                ValueCard(
                    title = "Confidence",
                    value = "%.1f".format(h.confidence * 100),
                    unit = "%",
                    modifier = Modifier.weight(1f)
                )
            }
            
            val probChunks = h.probabilities.toList().chunked(2)
            probChunks.forEach { rowItems ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    rowItems.forEach { (key, value) ->
                        ValueCard(
                            title = key,
                            value = "%.1f".format(value * 100),
                            unit = "%",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        } else {
            Text("Waiting for health prediction...", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
