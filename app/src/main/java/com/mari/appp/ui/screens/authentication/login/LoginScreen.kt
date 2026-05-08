package com.mari.appp.ui.screens.authentication.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import com.google.firebase.auth.FirebaseAuth
import com.mari.appp.R
import com.mari.appp.ui.navigation.ROUTES
import com.mari.appp.ui.theme.greenColor
import com.mari.appp.ui.theme.primaryColor

@Composable
fun LoginScreen(navController: NavHostController, modifier: Modifier) {
    var emailInput by remember { mutableStateOf(TextFieldValue("")) }
    var passwordInput by remember { mutableStateOf(TextFieldValue("")) }

    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        LottieAnimationWidget(R.raw.auth_login, 250.dp)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Login to RentTrack!",
            style = TextStyle(
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("johndoe@gmail.com") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Email",
                    tint = greenColor
                )
            },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = greenColor,
                unfocusedBorderColor = primaryColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = passwordInput,
            onValueChange = { passwordInput = it },
            label = { Text("password") },
            leadingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_password_24),
                    contentDescription = "Password",
                    tint = greenColor
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = greenColor,
                unfocusedBorderColor = primaryColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        OutlinedButton(
            onClick = {

                val email = emailInput.text.trim()
                val password = passwordInput.text.trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {

                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                navController.navigate(ROUTES.Home.name)
                            } else {
                                Toast.makeText(
                                    context,
                                    task.exception?.message ?: "Login failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        ) {
            Text(text = "home page")
        }

        TextButton(
            onClick = {
                navController.navigate(ROUTES.ForgotPassword.name)
            }
        ) {
            Text("Forgot Password?")
        }

        TextButton(
            onClick = {
                navController.navigate(ROUTES.Register.name)
            }
        ) {
            Text("New here? Create account")
        }
    }
}

@Composable
fun LottieAnimationWidget(drawable: Int, size: Dp) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(drawable))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(size)
    )
}