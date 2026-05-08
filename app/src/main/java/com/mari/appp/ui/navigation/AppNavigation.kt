//package com.mari.appp.ui.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.navArgument
//import androidx.navigation.NavType
//
//import com.mari.appp.ui.screens.Home.Home
//import com.mari.appp.ui.screens.authentication.forgotpassword.ForgotpasswordScreen
//import com.mari.appp.ui.screens.authentication.login.LoginScreen
//import com.mari.appp.ui.screens.authentication.registration.RegistrationScreen
//import com.mari.appp.ui.screens.onboarding.OnboardingScreen
//import com.mari.appp.ui.screens.room.RoomScreen
//import com.mari.appp.ui.screens.setup.PropertySetupScreen
//
//@Composable
//fun AppNavigation(
//    navController: NavHostController,
//    modifier: Modifier
//) {
//
//    NavHost(
//        navController = navController,
//        startDestination = ROUTES.Onboarding.name,
//        modifier = modifier
//    ) {
//
//        composable(ROUTES.Onboarding.name) {
//            OnboardingScreen(navController, modifier)
//        }
//
//        composable(ROUTES.ForgotPassword.name) {
//            ForgotpasswordScreen(navController, modifier)
//        }
//
//        composable(ROUTES.Register.name) {
//            RegistrationScreen(navController, modifier)
//        }
//
//        composable(ROUTES.Login.name) {
//            LoginScreen(navController, modifier)
//        }
//
//        composable(ROUTES.PropertySetup.name) {
//            PropertySetupScreen(navController, modifier)
//        }
//
//        composable(ROUTES.Home.name) {
//            Home(navController, modifier)
//        }
//
//        // ✅ ONLY ROOM ROUTE (CORRECT)
//        composable(
//            route = "room/{propertyId}/{unitId}",
//            arguments = listOf(
//                navArgument("propertyId") {
//                    type = NavType.StringType
//                },
//                navArgument("unitId") {
//                    type = NavType.StringType
//                }
//            )
//        ) { backStackEntry ->
//
//            val propertyId = backStackEntry.arguments?.getString("propertyId") ?: ""
//            val unitId = backStackEntry.arguments?.getString("unitId") ?: ""
//
//            RoomScreen(
//                navController = navController,
//                propertyId = propertyId,
//                unitId = unitId
//            )
//        }
//    }
//}



package com.mari.appp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.mari.appp.ui.screens.Home.Home
import com.mari.appp.ui.screens.authentication.forgotpassword.ForgotpasswordScreen
import com.mari.appp.ui.screens.authentication.login.LoginScreen
import com.mari.appp.ui.screens.authentication.registration.RegistrationScreen
import com.mari.appp.ui.screens.onboarding.OnboardingScreen
import com.mari.appp.ui.screens.room.RoomScreen
import com.mari.appp.ui.screens.setup.PropertySetupScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = ROUTES.Onboarding.name,
        modifier = modifier
    ) {

        // ONBOARDING
        composable(ROUTES.Onboarding.name) {
            OnboardingScreen(navController, modifier)
        }

        // LOGIN
        composable(ROUTES.Login.name) {
            LoginScreen(navController, modifier)
        }

        // REGISTER
        composable(ROUTES.Register.name) {
            RegistrationScreen(navController, modifier)
        }

        // FORGOT PASSWORD
        composable(ROUTES.ForgotPassword.name) {
            ForgotpasswordScreen(navController, modifier)
        }

        // PROPERTY SETUP
        composable(ROUTES.PropertySetup.name) {
            PropertySetupScreen(navController, modifier)
        }

        // HOME
        composable(ROUTES.Home.name) {
            Home(navController, modifier)
        }

        // ROOM SCREEN
        composable(
            route = "room/{propertyId}/{unitId}",
            arguments = listOf(
                navArgument("propertyId") {
                    type = NavType.StringType
                },
                navArgument("unitId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val propertyId =
                backStackEntry.arguments?.getString("propertyId") ?: ""

            val unitId =
                backStackEntry.arguments?.getString("unitId") ?: ""

            RoomScreen(
                navController = navController,
                propertyId = propertyId,
                unitId = unitId
            )
        }
    }
}