package com.mari.appp.ui.screens.authentication.registration

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp

import com.google.firebase.auth.FirebaseAuth
import com.mari.appp.R
import com.mari.appp.ui.navigation.ROUTES
import androidx.navigation.NavHostController
import com.mari.appp.ui.screens.authentication.login.LottieAnimationWidget

@Composable
fun RegistrationScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    var usernameInput by remember { mutableStateOf(TextFieldValue("")) }
    var emailInput by remember { mutableStateOf(TextFieldValue("")) }
    var passwordInput by remember { mutableStateOf(TextFieldValue("")) }

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

        Spacer(modifier = Modifier.height(16.dp))

        LottieAnimationWidget(R.raw.auth_login, 220.dp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "New here? Let's help you create a new account",
            color = Color(0xFF008000),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Enter your username",
            color = Color(0xFF4285F4),
            style = TextStyle(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = usernameInput,
            onValueChange = { usernameInput = it },
            placeholder = { Text("John Doe") },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = "Username")
            },
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Enter your email address",
            color = Color(0xFF4285F4),
            style = TextStyle(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            placeholder = { Text("johndoe@gmail.com") },
            leadingIcon = {
                Icon(Icons.Filled.Email, contentDescription = "Email")
            },
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Create your password",
            color = Color(0xFF4285F4),
            style = TextStyle(fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            placeholder = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_password_24),
                    contentDescription = "Password"
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                val email = emailInput.text.trim()
                val password = passwordInput.text.trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Email and password required", Toast.LENGTH_SHORT).show()
                    return@OutlinedButton
                }

                if (password.length < 6) {
                    Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    return@OutlinedButton
                }

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(context, "Registration successful 🎉", Toast.LENGTH_SHORT).show()

                            navController.navigate(ROUTES.PropertySetup.name) {
                                popUpTo(0)
                            }

                        } else {
                            Toast.makeText(
                                context,
                                task.exception?.message ?: "Registration failed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        ) {
            Text(text = "Create account")
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

