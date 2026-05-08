package com.mari.appp.ui.screens.authentication.forgotpassword

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

import coil3.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.mari.appp.R

@Composable
fun ForgotpasswordScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    var emailInput by remember { mutableStateOf(TextFieldValue("")) }
    var passwordInput by remember { mutableStateOf(TextFieldValue("")) } // optional UI only
    var passwordVisible by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {

        AsyncImage(
            model = "https://images.pexels.com/photos/5403840/pexels-photo-5403840.jpeg",
            contentDescription = "Forgot Password Image",
            modifier = Modifier
                .size(280.dp)
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Forgot your password?",
            color = Color(0xFFC05800),
            style = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Enter your email to receive reset link",
            color = Color(0xFF008000)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            placeholder = { Text("Johndoe@gmail.com") },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = "Email")
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                val email = emailInput.text.trim()

                if (email.isEmpty()) {
                    Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                    return@OutlinedButton
                }

                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Reset link sent to your email 📩",
                                Toast.LENGTH_LONG
                            ).show()

                            navController.popBackStack()

                        } else {
                            Toast.makeText(
                                context,
                                task.exception?.message ?: "Failed to send reset email",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        ) {
            Text(text = "Send reset link")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}