package com.mari.appp.ui.screens.payments

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PaymentScreen(
    propertyId: String,
    unitId: String
) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    var selectedUnitId by remember { mutableStateOf(unitId) }

    var units by remember { mutableStateOf(listOf<Pair<String, String>>()) }

    var amount by remember { mutableStateOf("") }
    var method by remember { mutableStateOf("Cash") }
    var note by remember { mutableStateOf("") }

    var payments by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    var rentAmount by remember { mutableStateOf(0) }
    var tenantName by remember { mutableStateOf("") }
    var unitName by remember { mutableStateOf("") }

    LaunchedEffect(propertyId) {

        firestore.collection("properties")
            .document(propertyId)
            .collection("units")
            .orderBy("createdAt")
            .get()
            .addOnSuccessListener { snap ->

                val list = snap.documents.map { doc ->
                    doc.id to (doc.getString("unitName") ?: "Room")
                }

                units = list

                if (selectedUnitId.isBlank() && list.isNotEmpty()) {
                    selectedUnitId = list.first().first
                }
            }
    }

    LaunchedEffect(selectedUnitId) {

        if (selectedUnitId.isBlank()) return@LaunchedEffect

        val unitRef = firestore.collection("properties")
            .document(propertyId)
            .collection("units")
            .document(selectedUnitId)

        unitRef.get().addOnSuccessListener { doc ->
            rentAmount = (doc.getLong("rentAmount") ?: 0).toInt()
            tenantName = doc.getString("tenantName") ?: "No Tenant"
            unitName = doc.getString("unitName") ?: ""
        }

        unitRef.collection("payments")
            .addSnapshotListener { snap, _ ->
                payments = snap?.documents?.mapNotNull { it.data } ?: emptyList()
            }
    }

    val totalPaid = payments.sumOf {
        (it["amount"] as? Long ?: 0L).toInt()
    }

    val balance = rentAmount - totalPaid

    // 🌌 NEW COOL NAVY GRADIENT BACKGROUND
    val bg = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0B1020),
            Color(0xFF121A2E),
            Color(0xFF0B1020)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(20.dp)
    ) {

        Text(
            text = "Payments - $tenantName ($unitName)",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.height(12.dp))

        Text("Select Room", color = Color(0xFFB8C0D9), fontWeight = FontWeight.SemiBold)

        LazyColumn(
            modifier = Modifier.height(140.dp)
        ) {

            items(units) { unit ->

                val isSelected = unit.first == selectedUnitId

                Button(
                    onClick = { selectedUnitId = unit.first },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected)
                            Color(0xFF6D5EF6)
                        else
                            Color(0xFF1A2440)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(unit.second, color = Color.White)
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        Text("Rent: $rentAmount", color = Color.White)
        Text("Paid: $totalPaid", color = Color(0xFFB8C0D9))
        Text("Balance: $balance", color = Color(0xFF6D5EF6), fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount Paid") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6D5EF6),
                unfocusedBorderColor = Color(0xFF2A3556),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF121A2E),
                unfocusedContainerColor = Color(0xFF121A2E)
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = method,
            onValueChange = { method = it },
            label = { Text("Method (Cash / M-Pesa / Bank)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6D5EF6),
                unfocusedBorderColor = Color(0xFF2A3556),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF121A2E),
                unfocusedContainerColor = Color(0xFF121A2E)
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF6D5EF6),
                unfocusedBorderColor = Color(0xFF2A3556),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF121A2E),
                unfocusedContainerColor = Color(0xFF121A2E)
            )
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {

                val payAmount = amount.toIntOrNull() ?: 0

                if (payAmount <= 0) {
                    Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val payment = hashMapOf(
                    "amount" to payAmount,
                    "method" to method,
                    "note" to note,
                    "timestamp" to System.currentTimeMillis(),
                    "propertyId" to propertyId,
                    "unitId" to selectedUnitId,
                    "tenantName" to tenantName
                )

                firestore.collection("properties")
                    .document(propertyId)
                    .collection("units")
                    .document(selectedUnitId)
                    .collection("payments")
                    .add(payment)
                    .addOnSuccessListener {

                        Toast.makeText(context, "Payment recorded", Toast.LENGTH_SHORT).show()

                        amount = ""
                        note = ""
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6D5EF6)
            )
        ) {
            Text("Record Payment", color = Color.White, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(20.dp))

        Text("Payment History", fontWeight = FontWeight.Bold, color = Color.White)

        LazyColumn {

            items(payments) { p ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .shadow(8.dp, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF121A2E)
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Amount: ${p["amount"]}", color = Color.White)
                        Text("Method: ${p["method"]}", color = Color(0xFFB8C0D9))
                        Text("Note: ${p["note"]}", color = Color(0xFFB8C0D9))
                    }
                }
            }
        }
    }
}