package com.mari.appp.ui.screens.room

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mari.appp.ui.navigation.ROUTES

@Composable
fun RoomScreen(
    navController: NavHostController,
    propertyId: String,
    unitId: String
) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    var loading by remember { mutableStateOf(true) }

    var unitName by remember { mutableStateOf("") }
    var occupied by remember { mutableStateOf(false) }

    var tenantName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nationalId by remember { mutableStateOf("") }
    var rentAmount by remember { mutableStateOf("") }
    var depositPaid by remember { mutableStateOf("") }

    var nextUnitId by remember { mutableStateOf("") }
    var previousUnitId by remember { mutableStateOf("") }

    var currentIndex by remember { mutableStateOf(0) }
    var totalRooms by remember { mutableStateOf(0) }

    LaunchedEffect(unitId) {

        loading = true

        nextUnitId = ""
        previousUnitId = ""

        firestore.collection("properties")
            .document(propertyId)
            .collection("units")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->

                val units = result.documents

                totalRooms = units.size

                for (i in units.indices) {

                    if (units[i].id == unitId) {

                        currentIndex = i

                        if (i > 0) previousUnitId = units[i - 1].id
                        if (i < units.size - 1) nextUnitId = units[i + 1].id

                        break
                    }
                }
            }

        firestore.collection("properties")
            .document(propertyId)
            .collection("units")
            .document(unitId)
            .get()
            .addOnSuccessListener { doc ->

                if (doc.exists()) {

                    unitName = doc.getString("unitName") ?: ""
                    occupied = doc.getBoolean("occupied") ?: false
                    tenantName = doc.getString("tenantName") ?: ""
                    phone = doc.getString("phone") ?: ""
                    email = doc.getString("email") ?: ""
                    nationalId = doc.getString("nationalId") ?: ""
                    rentAmount = doc.getLong("rentAmount")?.toString() ?: ""
                    depositPaid = doc.getLong("depositPaid")?.toString() ?: ""
                }

                loading = false
            }
            .addOnFailureListener {

                loading = false

                Toast.makeText(
                    context,
                    "Failed to load room",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    val bg = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F0F14),
            Color(0xFF141420),
            Color(0xFF0F0F14)
        )
    )

    if (loading) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.padding(20.dp),
                color = Color(0xFF7C4DFF)
            )
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(20.dp)
    ) {

        // ROOM TITLE
        Text(
            text = "Room: $unitName",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Room ${currentIndex + 1} of $totalRooms",
            color = Color(0xFFB0B0B8),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (occupied) "Status: OCCUPIED" else "Status: VACANT",
            color = if (occupied) Color(0xFF7C4DFF) else Color(0xFFB0B0B8),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // =========================
        // VACANT ROOM
        // =========================

        if (!occupied) {

            Text(
                text = "Assign Tenant",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = tenantName,
                onValueChange = { tenantName = it },
                label = { Text("Tenant Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C4DFF),
                    unfocusedBorderColor = Color(0xFFB0B0B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.35f),
                    unfocusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.25f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C4DFF),
                    unfocusedBorderColor = Color(0xFFB0B0B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.35f),
                    unfocusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.25f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C4DFF),
                    unfocusedBorderColor = Color(0xFFB0B0B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.35f),
                    unfocusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.25f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = nationalId,
                onValueChange = { nationalId = it },
                label = { Text("National ID") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C4DFF),
                    unfocusedBorderColor = Color(0xFFB0B0B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.35f),
                    unfocusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.25f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = rentAmount,
                onValueChange = { rentAmount = it },
                label = { Text("Rent Amount") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C4DFF),
                    unfocusedBorderColor = Color(0xFFB0B0B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.35f),
                    unfocusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.25f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = depositPaid,
                onValueChange = { depositPaid = it },
                label = { Text("Deposit Paid") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7C4DFF),
                    unfocusedBorderColor = Color(0xFFB0B0B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.35f),
                    unfocusedContainerColor = Color(0xFF1C1C24).copy(alpha = 0.25f)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    firestore.collection("properties")
                        .document(propertyId)
                        .collection("units")
                        .document(unitId)
                        .update(
                            mapOf(
                                "occupied" to true,
                                "tenantName" to tenantName,
                                "phone" to phone,
                                "email" to email,
                                "nationalId" to nationalId,
                                "rentAmount" to (rentAmount.toIntOrNull() ?: 0),
                                "depositPaid" to (depositPaid.toIntOrNull() ?: 0)
                            )
                        )
                        .addOnSuccessListener {

                            occupied = true

                            Toast.makeText(
                                context,
                                "Tenant assigned successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {

                            Toast.makeText(
                                context,
                                "Failed to assign tenant",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C4DFF)
                )
            ) {
                Text("Assign Tenant", color = Color.White)
            }

        } else {

            // =========================
            // OCCUPIED ROOM
            // =========================

            Text(
                text = "Tenant Details",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text("Name: $tenantName", color = Color.White)
            Text("Phone: $phone", color = Color(0xFFB0B0B8))
            Text("Email: $email", color = Color(0xFFB0B0B8))
            Text("National ID: $nationalId", color = Color(0xFFB0B0B8))
            Text("Rent: $rentAmount", color = Color.White)
            Text("Deposit: $depositPaid", color = Color(0xFFB0B0B8))

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {

                    firestore.collection("properties")
                        .document(propertyId)
                        .collection("units")
                        .document(unitId)
                        .update(
                            mapOf(
                                "occupied" to false,
                                "tenantName" to "",
                                "phone" to "",
                                "email" to "",
                                "nationalId" to "",
                                "rentAmount" to 0,
                                "depositPaid" to 0
                            )
                        )
                        .addOnSuccessListener {

                            occupied = false

                            tenantName = ""
                            phone = ""
                            email = ""
                            nationalId = ""
                            rentAmount = ""
                            depositPaid = ""

                            Toast.makeText(
                                context,
                                "Room vacated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {

                            Toast.makeText(
                                context,
                                "Failed to vacate room",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1C1C24)
                )
            ) {
                Text("Vacate Room", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if (previousUnitId.isNotEmpty()) {

                Button(
                    onClick = {
                        navController.navigate("room/$propertyId/$previousUnitId")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1C1C24)
                    )
                ) {
                    Text("Previous Room", color = Color.White)
                }

            } else {

                Text(
                    text = "First Room",
                    color = Color(0xFFB0B0B8),
                    fontWeight = FontWeight.Bold
                )
            }

            if (nextUnitId.isNotEmpty()) {

                Button(
                    onClick = {
                        navController.navigate("room/$propertyId/$nextUnitId")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1C1C24)
                    )
                ) {
                    Text("Next Room", color = Color.White)
                }

            } else {

                Text(
                    text = "Last Room",
                    color = Color(0xFFB0B0B8),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (nextUnitId.isEmpty()) {

            Button(
                onClick = {

                    navController.navigate(ROUTES.Home.name) {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C4DFF)
                )
            ) {
                Text("Finish & Go To Home", color = Color.White)
            }
        }
    }
}