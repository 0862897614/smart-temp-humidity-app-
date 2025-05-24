package com.example.sensordhtt22

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var ledRef: DatabaseReference
    private lateinit var tempRef: DatabaseReference
    private lateinit var humRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        try {
            database = FirebaseDatabase.getInstance("https://esp32sensorproject-e83c0-default-rtdb.firebaseio.com/")
            database.setPersistenceEnabled(true) // Enable offline persistence
            ledRef = database.getReference("Led/status")
            tempRef = database.getReference("Sensor/temperature")
            humRef = database.getReference("Sensor/humidity")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setContent {
            SensorApp()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SensorApp() {
        var temperature by remember { mutableStateOf("--") }
        var humidity by remember { mutableStateOf("--") }
        var ledStatus by remember { mutableStateOf(false) }
        var connectionStatus by remember { mutableStateOf("Connecting...") }

        // Gentle gradient background
        val gradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFFF8FAFC), // Very light gray-blue
                Color(0xFFE2E8F0)  // Light gray
            )
        )

        LaunchedEffect(true) {
            // Temperature listener
            tempRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue()
                    temperature = when (value) {
                        is Double -> "%.1f".format(value)
                        is Float -> "%.1f".format(value)
                        is Long -> value.toString()
                        is String -> value
                        else -> "--"
                    }
                    connectionStatus = "Connected"
                }

                override fun onCancelled(error: DatabaseError) {
                    temperature = "Error"
                    connectionStatus = "Error: ${error.message}"
                }
            })

            // Humidity listener
            humRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue()
                    humidity = when (value) {
                        is Double -> "%.1f".format(value)
                        is Float -> "%.1f".format(value)
                        is Long -> value.toString()
                        is String -> value
                        else -> "--"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    humidity = "Error"
                }
            })

            // LED listener
            ledRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ledStatus = snapshot.getValue(Boolean::class.java) ?: false
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                HeaderCard(connectionStatus)

                Spacer(modifier = Modifier.height(24.dp))

                // Sensor Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Temperature Card
                    SensorCard(
                        modifier = Modifier.weight(1f),
                        title = "Temperature",
                        value = temperature,
                        unit = "°C",
                        icon = "🌡️",
                        color = Color(0xFFEF4444)
                    )

                    // Humidity Card
                    SensorCard(
                        modifier = Modifier.weight(1f),
                        title = "Humidity",
                        value = humidity,
                        unit = "%",
                        icon = "💧",
                        color = Color(0xFF06B6D4)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // LED Control Card
                LedControlCard(
                    ledStatus = ledStatus,
                    onToggle = {
                        ledRef.setValue(!ledStatus)
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Footer
                FooterText()
            }
        }
    }

    @Composable
    fun HeaderCard(status: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏠 Smart Home",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "IoT Dashboard",
                    fontSize = 16.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (status == "Connected") Color(0xFF10B981) else Color(0xFFEF4444),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = status,
                        fontSize = 14.sp,
                        color = if (status == "Connected") Color(0xFF10B981) else Color(0xFFEF4444),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    @Composable
    fun SensorCard(
        modifier: Modifier = Modifier,
        title: String,
        value: String,
        unit: String,
        icon: String,
        color: Color
    ) {
        Card(
            modifier = modifier
                .shadow(3.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = icon,
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = title,
                    fontSize = 14.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = value,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    Text(
                        text = unit,
                        fontSize = 14.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(start = 2.dp, bottom = 4.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun LedControlCard(ledStatus: Boolean, onToggle: () -> Unit) {
        val ledColor by animateColorAsState(
            targetValue = if (ledStatus) Color(0xFFFBBF24) else Color(0xFF9CA3AF),
            animationSpec = tween(300),
            label = "led_color"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (ledStatus) Color(0xFFFEF3C7) else Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (ledStatus) "💡" else "💡",
                        fontSize = 32.sp,
                        color = ledColor
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "LED Control",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = if (ledStatus) "ON" else "OFF",
                            fontSize = 14.sp,
                            color = ledColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Switch(
                    checked = ledStatus,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFFBBF24),
                        checkedTrackColor = Color(0xFFFBBF24).copy(alpha = 0.3f),
                        uncheckedThumbColor = Color(0xFF9CA3AF),
                        uncheckedTrackColor = Color(0xFF9CA3AF).copy(alpha = 0.3f)
                    )
                )
            }
        }
    }

    @Composable
    fun FooterText() {
        Text(
            text = "ESP32 IoT Dashboard\n PhamNghia❤️",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF64748B),
            lineHeight = 16.sp
        )
    }
}