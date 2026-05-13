package com.mari.appp.ui.screens.setup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.mari.appp.ui.navigation.ROUTES

@Composable
fun PropertySetupScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    var propertyName by remember { mutableStateOf(TextFieldValue("")) }
    var unitName by remember { mutableStateOf(TextFieldValue("")) }

    val unitsList = remember { mutableStateListOf<String>() }

    val context = androidx.compose.ui.platform.LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    val bg = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A121A),
            Color(0xFF0F1C24),
            Color(0xFF0A121A)
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bg)
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Set up your property",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = propertyName,
            onValueChange = { propertyName = it },
            label = { Text("Property name", color = Color(0xFFB8D8D8)) },
            placeholder = { Text("e.g. Sunset Apartments") },
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00C2A8),
                unfocusedBorderColor = Color(0xFF2A3B44),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color(0xFF00C2A8),
                focusedContainerColor = Color(0xFF0F1C24),
                unfocusedContainerColor = Color(0xFF0F1C24)
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = unitName,
            onValueChange = { unitName = it },
            label = { Text("Add Unit / Room", color = Color(0xFFB8D8D8)) },
            placeholder = { Text("e.g. Room A1") },
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF00C2A8),
                unfocusedBorderColor = Color(0xFF2A3B44),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color(0xFF00C2A8),
                focusedContainerColor = Color(0xFF0F1C24),
                unfocusedContainerColor = Color(0xFF0F1C24)
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                val room = unitName.text.trim()

                if (room.isNotEmpty()) {

                    if (!unitsList.contains(room)) {
                        unitsList.add(room)
                        unitName = TextFieldValue("")
                    } else {
                        Toast.makeText(
                            context,
                            "Unit already added",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(14.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00C2A8)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Add Unit", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (unitsList.isNotEmpty()) {

            Text(
                text = "Units Added",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.weight(1f, fill = false)
            ) {
                items(unitsList) { unit ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .shadow(6.dp, RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF0F1C24)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                unit,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )

                            IconButton(
                                onClick = {
                                    unitsList.remove(unit)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Unit",
                                    tint = Color(0xFFFF5C5C)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                val name = propertyName.text.trim()

                if (name.isEmpty()) {
                    Toast.makeText(context, "Enter property name", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (unitsList.isEmpty()) {
                    Toast.makeText(context, "Add at least one unit", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val property = hashMapOf(
                    "name" to name,
                    "createdAt" to System.currentTimeMillis()
                )

                firestore.collection("properties")
                    .add(property)
                    .addOnSuccessListener { document ->

                        val propertyId = document.id
                        var completed = 0

                        var firstUnitId = ""

                        for (unit in unitsList) {

                            val unitData = hashMapOf(
                                "unitName" to unit,
                                "occupied" to false,
                                "tenantId" to "",
                                "tenantName" to "",
                                "rentAmount" to 0,
                                "createdAt" to System.currentTimeMillis()
                            )

                            firestore.collection("properties")
                                .document(propertyId)
                                .collection("units")
                                .add(unitData)
                                .addOnSuccessListener { unitDoc ->

                                    if (firstUnitId.isEmpty()) {
                                        firstUnitId = unitDoc.id
                                    }

                                    completed++

                                    if (completed == unitsList.size) {

                                        Toast.makeText(
                                            context,
                                            "Property & units saved",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        navController.navigate("room/$propertyId/$firstUnitId")
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Failed to save some units",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00C2A8)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Save Property", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}