package com.mari.appp.ui.screens.Home

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.mari.appp.ui.navigation.ROUTES

@Composable
fun Home(navController: NavHostController, modifier: Modifier) {

    val firestore = FirebaseFirestore.getInstance()

    var totalRooms by remember { mutableStateOf(0) }
    var occupiedRooms by remember { mutableStateOf(0) }
    var vacantRooms by remember { mutableStateOf(0) }
    var totalIncome by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {

        firestore.collection("properties")
            .get()
            .addOnSuccessListener { properties ->

                var rooms = 0
                var occupied = 0
                var income = 0

                for (property in properties) {

                    property.reference.collection("units")
                        .get()
                        .addOnSuccessListener { units ->

                            rooms += units.documents.size

                            for (unit in units) {

                                val isOccupied = unit.getBoolean("occupied") ?: false
                                val rent = unit.getLong("rentAmount") ?: 0

                                if (isOccupied) occupied += 1

                                if (isOccupied) {
                                    income += rent.toInt()
                                }
                            }

                            totalRooms = rooms
                            occupiedRooms = occupied
                            vacantRooms = rooms - occupied
                            totalIncome = income

                            loading = false
                        }
                }

                if (properties.isEmpty) {
                    loading = false
                }
            }
    }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F0F14),
            Color(0xFF141420),
            Color(0xFF0F0F14)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "RentTrack Dashboard",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            ),
            modifier = Modifier.padding(top = 10.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (loading) {
            CircularProgressIndicator(color = Color(0xFF7C4DFF))
            return
        }

        // =========================
        // SUMMARY CARDS (GLASS STYLE)
        // =========================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C1C24).copy(alpha = 0.55f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Total Rooms: $totalRooms", color = Color.White, fontWeight = FontWeight.Medium)
                Text("Occupied: $occupiedRooms", color = Color(0xFFB0B0B8))
                Text("Vacant: $vacantRooms", color = Color(0xFFB0B0B8))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C1C24).copy(alpha = 0.55f)
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Estimated Monthly Income", color = Color(0xFFB0B0B8))
                Text(
                    text = "Ksh $totalIncome",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // =========================
        // BUTTONS (MODERN PRIMARY STYLE)
        // =========================

        Button(
            onClick = {
                navController.navigate(ROUTES.PropertySetup.name)
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C4DFF)
            )
        ) {
            Text("Property Setup", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                firestore.collection("properties")
                    .limit(1)
                    .get()
                    .addOnSuccessListener { properties ->

                        if (properties.isEmpty) return@addOnSuccessListener

                        val propertyDoc = properties.documents[0]
                        val propertyId = propertyDoc.id

                        propertyDoc.reference
                            .collection("units")
                            .limit(1)
                            .get()
                            .addOnSuccessListener { units ->

                                if (units.isEmpty) return@addOnSuccessListener

                                val firstUnitId = units.documents[0].id

                                if (propertyId.isNotBlank() && firstUnitId.isNotBlank()) {
                                    navController.navigate("room/$propertyId/$firstUnitId")
                                }
                            }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1C1C24).copy(alpha = 0.7f)
            )
        ) {
            Text("Room Management", color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                firestore.collection("properties")
                    .limit(1)
                    .get()
                    .addOnSuccessListener { properties ->

                        if (properties.isEmpty) return@addOnSuccessListener

                        val propertyDoc = properties.documents[0]
                        val propertyId = propertyDoc.id

                        propertyDoc.reference
                            .collection("units")
                            .limit(1)
                            .get()
                            .addOnSuccessListener { units ->

                                if (units.isEmpty) return@addOnSuccessListener

                                val firstUnitId = units.documents[0].id

                                if (propertyId.isNotBlank() && firstUnitId.isNotBlank()) {
                                    navController.navigate("payment/$propertyId/$firstUnitId")
                                }
                            }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1C1C24).copy(alpha = 0.7f)
            )
        ) {
            Text("Payments", color = Color.White)
        }
    }
}