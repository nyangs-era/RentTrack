//package com.mari.appp.ui.screens.setup
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.google.firebase.firestore.FirebaseFirestore
//import com.mari.appp.ui.navigation.ROUTES
//
//@Composable
//fun PropertySetupScreen(
//    navController: NavHostController,
//    modifier: Modifier = Modifier
//) {
//
//    var propertyName by remember { mutableStateOf(TextFieldValue("")) }
//    var unitName by remember { mutableStateOf(TextFieldValue("")) }
//
//    val unitsList = remember { mutableStateListOf<String>() }
//
//    val context = LocalContext.current
//    val firestore = FirebaseFirestore.getInstance()
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(20.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Text(
//            text = "Set up your property",
//            style = TextStyle(
//                fontSize = 26.sp,
//                fontWeight = FontWeight.Bold
//            )
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = propertyName,
//            onValueChange = { propertyName = it },
//            label = { Text("Property name") },
//            placeholder = { Text("e.g. Sunset Apartments") },
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = unitName,
//            onValueChange = { unitName = it },
//            label = { Text("Add Unit / Room") },
//            placeholder = { Text("e.g. Room A1") },
//            shape = RoundedCornerShape(16.dp),
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Button(
//            onClick = {
//
//                val room = unitName.text.trim()
//
//                if (room.isNotEmpty()) {
//
//                    if (!unitsList.contains(room)) {
//                        unitsList.add(room)
//                        unitName = TextFieldValue("")
//                    } else {
//                        Toast.makeText(
//                            context,
//                            "Unit already added",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Add Unit")
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        if (unitsList.isNotEmpty()) {
//
//            Text(
//                text = "Units Added",
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            LazyColumn(
//                modifier = Modifier.weight(1f, fill = false)
//            ) {
//                items(unitsList) { unit ->
//
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp)
//                    ) {
//
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(12.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//
//                            Text(unit)
//
//                            IconButton(
//                                onClick = {
//                                    unitsList.remove(unit)
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Delete,
//                                    contentDescription = "Delete Unit"
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Button(
//            onClick = {
//
//                val name = propertyName.text.trim()
//
//                if (name.isEmpty()) {
//                    Toast.makeText(context, "Enter property name", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
//
//                if (unitsList.isEmpty()) {
//                    Toast.makeText(context, "Add at least one unit", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
//
//                val property = hashMapOf(
//                    "name" to name,
//                    "createdAt" to System.currentTimeMillis()
//                )
//
//                firestore.collection("properties")
//                    .add(property)
//                    .addOnSuccessListener { document ->
//
//                        val propertyId = document.id
//                        var completed = 0
//
//                        for (unit in unitsList) {
//
//                            val unitData = hashMapOf(
//                                "unitName" to unit,
//                                "occupied" to false,
//                                "tenantId" to "",
//                                "tenantName" to "",
//                                "rentAmount" to 0,
//                                "createdAt" to System.currentTimeMillis()
//                            )
//
//                            firestore.collection("properties")
//                                .document(propertyId)
//                                .collection("units")
//                                .add(unitData)
//                                .addOnSuccessListener {
//
//                                    completed++
//
//                                    if (completed == unitsList.size) {
//
//                                        Toast.makeText(
//                                            context,
//                                            "Property & units saved",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//
//                                        // ✅ FIXED NAVIGATION
//                                        navController.navigate(ROUTES.Room.name)
//                                    }
//                                }
//                                .addOnFailureListener {
//                                    Toast.makeText(
//                                        context,
//                                        "Failed to save some units",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                        }
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(
//                            context,
//                            "Failed to save property",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Save Property")
//        }
//    }
//}
//
//package com.mari.appp.ui.screens.setup
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.google.firebase.firestore.FirebaseFirestore
//
//@Composable
//fun PropertySetupScreen(
//    navController: NavHostController,
//    modifier: Modifier = Modifier
//) {
//
//    var propertyName by remember { mutableStateOf(TextFieldValue("")) }
//    var unitName by remember { mutableStateOf(TextFieldValue("")) }
//
//    val unitsList = remember { mutableStateListOf<String>() }
//
//    val context = LocalContext.current
//    val firestore = FirebaseFirestore.getInstance()
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(20.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Text(
//            text = "Set up your property",
//            style = TextStyle(
//                fontSize = 26.sp,
//                fontWeight = FontWeight.Bold
//            )
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = propertyName,
//            onValueChange = { propertyName = it },
//            label = { Text("Property name") },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp)
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = unitName,
//            onValueChange = { unitName = it },
//            label = { Text("Add Unit / Room") },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp)
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Button(
//            onClick = {
//                val room = unitName.text.trim()
//
//                if (room.isNotEmpty()) {
//                    if (!unitsList.contains(room)) {
//                        unitsList.add(room)
//                        unitName = TextFieldValue("")
//                    } else {
//                        Toast.makeText(context, "Unit already added", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Add Unit")
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        if (unitsList.isNotEmpty()) {
//
//            Text(
//                text = "Units Added",
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            LazyColumn(
//                modifier = Modifier.weight(1f, fill = false)
//            ) {
//                items(unitsList) { unit ->
//
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 4.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(12.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//
//                            Text(unit)
//
//                            IconButton(
//                                onClick = { unitsList.remove(unit) }
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Delete,
//                                    contentDescription = "Delete Unit"
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Button(
//            onClick = {
//
//                val name = propertyName.text.trim()
//
//                if (name.isEmpty()) {
//                    Toast.makeText(context, "Enter property name", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
//
//                if (unitsList.isEmpty()) {
//                    Toast.makeText(context, "Add at least one unit", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
//
//                val property = hashMapOf(
//                    "name" to name,
//                    "createdAt" to System.currentTimeMillis()
//                )
//
//                firestore.collection("properties")
//                    .add(property)
//                    .addOnSuccessListener { propertyDoc ->
//
//                        val propertyId = propertyDoc.id
//                        var completed = 0
//
//                        for (unit in unitsList) {
//
//                            val unitData = hashMapOf(
//                                "unitName" to unit,
//                                "occupied" to false,
//                                "tenantName" to "",
//                                "phone" to "",
//                                "email" to "",
//                                "nationalId" to "",
//                                "rentAmount" to 0,
//                                "depositPaid" to 0,
//                                "createdAt" to System.currentTimeMillis()
//                            )
//
//                            firestore.collection("properties")
//                                .document(propertyId)
//                                .collection("units")
//                                .add(unitData)
//                                .addOnSuccessListener { unitDoc ->
//
//                                    val unitId = unitDoc.id
//                                    completed++
//
//                                    if (completed == unitsList.size) {
//
//                                        Toast.makeText(
//                                            context,
//                                            "Property & units saved",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//
//                                        // ✅ CORRECT NAVIGATION TO ROOM SCREEN
//                                        navController.navigate("room/$propertyId/$unitId")
//                                    }
//                                }
//                                .addOnFailureListener {
//                                    Toast.makeText(
//                                        context,
//                                        "Failed to save unit",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                        }
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(
//                            context,
//                            "Failed to save property",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Save Property")
//        }
//    }
//}



//
//package com.mari.appp.ui.screens.setup
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.google.firebase.firestore.FirebaseFirestore
//
//@Composable
//fun PropertySetupScreen(
//    navController: NavHostController,
//    modifier: Modifier = Modifier
//) {
//
//    var propertyName by remember { mutableStateOf(TextFieldValue("")) }
//    var unitName by remember { mutableStateOf(TextFieldValue("")) }
//
//    val unitsList = remember { mutableStateListOf<String>() }
//
//    val context = LocalContext.current
//    val firestore = FirebaseFirestore.getInstance()
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(20.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        Text(
//            text = "Set up your property",
//            style = TextStyle(
//                fontSize = 26.sp,
//                fontWeight = FontWeight.Bold
//            )
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = propertyName,
//            onValueChange = { propertyName = it },
//            label = { Text("Property name") },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp)
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = unitName,
//            onValueChange = { unitName = it },
//            label = { Text("Add Unit") },
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(16.dp)
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        Button(
//            onClick = {
//                val room = unitName.text.trim()
//
//                if (room.isNotEmpty()) {
//                    unitsList.add(room)
//                    unitName = TextFieldValue("")
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Add Unit")
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        LazyColumn {
//            items(unitsList) { unit ->
//                Text(unit)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Button(
//            onClick = {
//
//                println("SAVE CLICKED") // ✅ TEST 1
//
//                val name = propertyName.text.trim()
//
//                if (name.isEmpty()) {
//                    Toast.makeText(context, "Enter property name", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
//
//                if (unitsList.isEmpty()) {
//                    Toast.makeText(context, "Add units first", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
//
//                val property = hashMapOf(
//                    "name" to name,
//                    "createdAt" to System.currentTimeMillis()
//                )
//
//                firestore.collection("properties")
//                    .add(property)
//                    .addOnSuccessListener { propertyDoc ->
//
//                        println("PROPERTY CREATED") // ✅ TEST 2
//
//                        val propertyId = propertyDoc.id
//
//                        var firstUnitId = ""
//                        var completed = 0
//
//                        for (unit in unitsList) {
//
//                            val unitData = hashMapOf(
//                                "unitName" to unit,
//                                "occupied" to false,
//                                "createdAt" to System.currentTimeMillis()
//                            )
//
//                            firestore.collection("properties")
//                                .document(propertyId)
//                                .collection("units")
//                                .add(unitData)
//                                .addOnSuccessListener { unitDoc ->
//
//                                    if (firstUnitId.isEmpty()) {
//                                        firstUnitId = unitDoc.id
//                                    }
//
//                                    completed++
//
//                                    if (completed == unitsList.size) {
//
//                                        Toast.makeText(
//                                            context,
//                                            "Saved successfully",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//
//                                        println("NAVIGATING NOW") // ✅ TEST 3
//
//                                        navController.navigate(
//                                            "room/$propertyId/$firstUnitId"
//                                        )
//                                    }
//                                }
//                                .addOnFailureListener {
//                                    println("UNIT FAILED") // ❌ TEST FAIL
//                                }
//                        }
//                    }
//                    .addOnFailureListener {
//                        println("PROPERTY FAILED") // ❌ TEST FAIL
//                    }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Save Property")
//        }
//    }
//}
//
//
//package com.mari.appp.ui.screens.setup
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.google.firebase.firestore.FirebaseFirestore
//
//@Composable
//fun PropertySetupScreen(
//    navController: NavHostController,
//    modifier: Modifier
//) {
//
//    val context = LocalContext.current
//    val firestore = FirebaseFirestore.getInstance()
//
//    var propertyName by remember { mutableStateOf(TextFieldValue("")) }
//    var unitName by remember { mutableStateOf(TextFieldValue("")) }
//
//    val unitsList = remember { mutableStateListOf<String>() }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(20.dp)
//    ) {
//
//        Text("PROPERTY SETUP DEBUG")
//
//        Spacer(Modifier.height(10.dp))
//
//        Button(
//            onClick = {
//                Toast.makeText(context, "BUTTON WORKS", Toast.LENGTH_SHORT).show()
//                println("BUTTON CLICKED")
//            }
//        ) {
//            Text("TEST BUTTON")
//        }
//
//        Spacer(Modifier.height(20.dp))
//
//        OutlinedTextField(
//            value = propertyName,
//            onValueChange = { propertyName = it },
//            label = { Text("Property Name") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(10.dp))
//
//        OutlinedTextField(
//            value = unitName,
//            onValueChange = { unitName = it },
//            label = { Text("Unit") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(Modifier.height(10.dp))
//
//        Button(
//            onClick = {
//                unitsList.add(unitName.text)
//                unitName = TextFieldValue("")
//                println("UNIT ADDED: ${unitsList.size}")
//            }
//        ) {
//            Text("Add Unit")
//        }
//
//        LazyColumn {
//            items(unitsList) { unit ->
//                Text(unit)
//            }
//        }
//
//        Spacer(Modifier.height(20.dp))
//
//        Button(
//            onClick = {
//
//                println("SAVE CLICKED")
//
//                val name = propertyName.text.trim()
//
//                if (name.isEmpty()) {
//                    Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
//
//                if (unitsList.isEmpty()) {
//                    Toast.makeText(context, "No units", Toast.LENGTH_SHORT).show()
//                    return@Button
//                }
//
//                firestore.collection("properties")
//                    .add(mapOf("name" to name))
//                    .addOnSuccessListener { propertyDoc ->
//
//                        println("PROPERTY CREATED")
//
//                        val propertyId = propertyDoc.id
//                        var firstUnitId = ""
//                        var completed = 0
//
//                        for (unit in unitsList) {
//
//                            firestore.collection("properties")
//                                .document(propertyId)
//                                .collection("units")
//                                .add(mapOf("unitName" to unit))
//                                .addOnSuccessListener { unitDoc ->
//
//                                    if (firstUnitId.isEmpty()) {
//                                        firstUnitId = unitDoc.id
//                                    }
//
//                                    completed++
//                                    println("UNIT SAVED: $completed/${unitsList.size}")
//
//                                    if (completed == unitsList.size) {
//
//                                        println("NAVIGATING NOW")
//
//                                        navController.navigate(
//                                            "room/$propertyId/$firstUnitId"
//                                        )
//                                    }
//                                }
//                                .addOnFailureListener {
//                                    println("UNIT FAILED")
//                                }
//                        }
//                    }
//                    .addOnFailureListener {
//                        println("PROPERTY FAILED")
//                    }
//            }
//        ) {
//            Text("SAVE PROPERTY")
//        }
//    }
//}





package com.mari.appp.ui.screens.setup

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current
    val firestore = FirebaseFirestore.getInstance()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Set up your property",
            style = TextStyle(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = propertyName,
            onValueChange = { propertyName = it },
            label = { Text("Property name") },
            placeholder = { Text("e.g. Sunset Apartments") },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = unitName,
            onValueChange = { unitName = it },
            label = { Text("Add Unit / Room") },
            placeholder = { Text("e.g. Room A1") },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Unit")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (unitsList.isNotEmpty()) {

            Text(
                text = "Units Added",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.weight(1f, fill = false)
            ) {
                items(unitsList) { unit ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(unit)

                            IconButton(
                                onClick = {
                                    unitsList.remove(unit)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Unit"
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
                                .addOnSuccessListener {

                                    completed++

                                    if (completed == unitsList.size) {

                                        Toast.makeText(
                                            context,
                                            "Property & units saved",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        // ✅ FIXED NAVIGATION
                                        navController.navigate(ROUTES.Room.name)
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
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Failed to save property",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Property")
        }
    }
}