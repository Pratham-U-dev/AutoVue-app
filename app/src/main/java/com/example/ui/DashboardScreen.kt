package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.CircularGauge
import com.example.components.ValueCard
import com.example.repository.ConnectionStatus
import com.example.ui.theme.Amber400
import com.example.ui.theme.Emerald400
import com.example.ui.theme.Emerald500
import com.example.ui.theme.Indigo400
import com.example.ui.theme.Indigo500
import com.example.ui.theme.Indigo900
import com.example.ui.theme.Slate800
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed
import com.example.ui.theme.StatusYellow
import com.example.viewmodel.SharedTelemetryViewModel

@Composable
fun DashboardScreen(viewModel: SharedTelemetryViewModel) {
    val tick by viewModel.latestTick.collectAsState()
    val status by viewModel.connectionStatus.collectAsState()
    val health by viewModel.healthPrediction.collectAsState()
    val simulatorStatus by viewModel.simulatorStatus.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row {
                    Text("Auto", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Vue", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Indigo400)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = when (status) {
                        ConnectionStatus.CONNECTED -> Emerald500
                        ConnectionStatus.CONNECTING -> StatusYellow
                        ConnectionStatus.DISCONNECTED, ConnectionStatus.ERROR -> StatusRed
                    }
                    val statusText = when (status) {
                        ConnectionStatus.CONNECTED -> "LIVE TELEMETRY"
                        ConnectionStatus.CONNECTING -> "CONNECTING..."
                        else -> "OFFLINE"
                    }
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(statusColor))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = statusText,
                        color = statusColor,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        val data = tick?.data ?: com.example.model.TelemetryData(
            coolantTemp = 0.0,
            mapKpa = 0.0,
            rpm = 0.0,
            vss = 0.0,
            intakeAirTemp = 0.0,
            maf = 0.0,
            throttlePos = 0.0,
            ambientTemp = 0.0,
            pedalD = 0.0,
            pedalE = 0.0
        )
            
        // Primary Gauge Section
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularGauge(
                    title = "Speed",
                    value = data.vss.toFloat(),
                    maxValue = 240f,
                    unit = "km/h",
                    modifier = Modifier.size(220.dp),
                    color = if (data.vss > 120) StatusRed else Indigo500
                )
                
                // RPM floating bar
                Column(
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("RPM", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(6.dp)
                            .height(100.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Slate800),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(fraction = (data.rpm / 8000.0).toFloat().coerceIn(0f, 1f))
                                .background(Indigo500)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${"%.1f".format(data.rpm / 1000.0)}k", style = MaterialTheme.typography.labelSmall, color = Indigo400, fontFamily = MaterialTheme.typography.displayLarge.fontFamily)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Telemetry Grid
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ValueCard(
                        title = "Load",
                        value = "%.0f".format(data.throttlePos),
                        unit = "%",
                        progress = (data.throttlePos / 100.0).toFloat(),
                        progressColor = Indigo400,
                        modifier = Modifier.weight(1f)
                    )
                    ValueCard(
                        title = "Coolant",
                        value = "%.0f".format(data.coolantTemp),
                        unit = "°C",
                        progress = (data.coolantTemp / 130.0).toFloat(),
                        progressColor = Amber400,
                        modifier = Modifier.weight(1f)
                    )
                    ValueCard(
                        title = "Intake",
                        value = "%.0f".format(data.intakeAirTemp),
                        unit = "°C",
                        progress = (data.intakeAirTemp / 100.0).toFloat(),
                        progressColor = Emerald400,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ValueCard(
                        title = "Ambient",
                        value = "%.0f".format(data.ambientTemp),
                        unit = "°C",
                        progress = (data.ambientTemp / 50.0).toFloat().coerceIn(0f, 1f),
                        progressColor = Indigo400,
                        modifier = Modifier.weight(1f)
                    )
                    ValueCard(
                        title = "MAP",
                        value = "%.0f".format(data.mapKpa),
                        unit = "kPa",
                        progress = (data.mapKpa / 255.0).toFloat().coerceIn(0f, 1f),
                        progressColor = Amber400,
                        modifier = Modifier.weight(1f)
                    )
                    ValueCard(
                        title = "MAF",
                        value = "%.1f".format(data.maf),
                        unit = "g/s",
                        progress = (data.maf / 200.0).toFloat().coerceIn(0f, 1f),
                        progressColor = Emerald400,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ValueCard(
                        title = "Pedal D",
                        value = "%.0f".format(data.pedalD),
                        unit = "%",
                        progress = (data.pedalD / 100.0).toFloat(),
                        progressColor = Indigo400,
                        modifier = Modifier.weight(1f)
                    )
                    ValueCard(
                        title = "Pedal E",
                        value = "%.0f".format(data.pedalE),
                        unit = "%",
                        progress = (data.pedalE / 100.0).toFloat(),
                        progressColor = Amber400,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // AI Health Insights Card
            val healthGradient = Brush.linearGradient(
                colors = listOf(Indigo900.copy(alpha = 0.4f), MaterialTheme.colorScheme.surface)
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Indigo500.copy(alpha = 0.2f)),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(modifier = Modifier.background(healthGradient).padding(16.dp)) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Indigo500.copy(alpha = 0.2f)).padding(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Indigo400,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("HEALTH AI PREDICTION", style = MaterialTheme.typography.labelSmall, color = Indigo400, fontWeight = FontWeight.Bold)
                                    Text(health?.status ?: "Evaluating System...", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                }
                            }
                            
                            if (health != null) {
                                Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Indigo500).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                    Text("${"%.0f".format(health!!.confidence * 100)}% CONF.", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Black, fontSize = 9.sp)
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (health != null && health!!.status == "Normal") "No immediate mechanical issues detected. Maintenance recommended in 2,450 km." else "Waiting for enough telemetry data to perform a reliable health prediction.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        val confidence = health?.confidence?.toFloat() ?: 0f
                        Box(
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(Indigo500.copy(alpha = 0.2f))
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(fraction = confidence).height(6.dp).background(Indigo500)
                            )
                        }
                    }
                }
            }
            
    }
}
