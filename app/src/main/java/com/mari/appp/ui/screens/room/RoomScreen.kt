package com.mari.appp.ui.screens.room


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RoomScreen(
    navController: NavHostController,
    propertyId: String,
    unitId: String
) {

    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    var loading by remember { mutableStateOf(true) }

    var unitName by remember { mutableStateOf("") }
    var occupied by remember { mutableStateOf(false) }

    // Tenant fields
    var tenantName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nationalId by remember { mutableStateOf("") }
    var rentAmount by remember { mutableStateOf("") }
    var depositPaid by remember { mutableStateOf("") }

    // Load room data
    LaunchedEffect(Unit) {

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
                    rentAmount = doc.getLong("rentAmount")?.toString() ?: "0"
                    depositPaid = doc.getLong("depositPaid")?.toString() ?: "0"
                }

                loading = false
            }
            .addOnFailureListener {
                loading = false
                Toast.makeText(context, "Failed to load room", Toast.LENGTH_SHORT).show()
            }
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.padding(20.dp))
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Room: $unitName",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // STATUS
        Text(
            text = if (occupied) "Status: OCCUPIED" else "Status: VACANT",
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (!occupied) {

            // 🟢 ASSIGN TENANT FORM
            Text("Assign Tenant", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = tenantName,
                onValueChange = { tenantName = it },
                label = { Text("Tenant Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nationalId,
                onValueChange = { nationalId = it },
                label = { Text("National ID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rentAmount,
                onValueChange = { rentAmount = it },
                label = { Text("Rent Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = depositPaid,
                onValueChange = { depositPaid = it },
                label = { Text("Deposit Paid") },
                modifier = Modifier.fillMaxWidth()
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
                                "rentAmount" to rentAmount.toIntOrNull(),
                                "depositPaid" to depositPaid.toIntOrNull()
                            )
                        )
                        .addOnSuccessListener {
                            Toast.makeText(context, "Tenant assigned", Toast.LENGTH_SHORT).show()
                            occupied = true
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Assign Tenant")
            }

        } else {

            // 🔴 TENANT DETAILS VIEW
            Text("Tenant Details", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(10.dp))

            Text("Name: $tenantName")
            Text("Phone: $phone")
            Text("Email: $email")
            Text("National ID: $nationalId")
            Text("Rent: $rentAmount")
            Text("Deposit: $depositPaid")

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
                            Toast.makeText(context, "Room vacated", Toast.LENGTH_SHORT).show()
                            occupied = false
                        }

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Vacate Room")
            }
        }
    }
}