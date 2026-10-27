package com.example.smarttasks

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smarttasks.ui.theme.ConfirmScreen
import com.example.smarttasks.ui.theme.CreatePasswordScreen
import com.example.smarttasks.ui.theme.ForgotPasswordScreen
import com.example.smarttasks.ui.theme.VerifyCodeScreen

object Routes {
    const val FORGOT = "forgot"
    const val VERIFY = "verify/{email}"
    const val NEW_PASSWORD = "new_password/{email}/{otp}"
    const val CONFIRM = "confirm/{email}/{otp}/{pass}"

    fun verify(email: String) = "verify/${Uri.encode(email)}"

    fun newPassword(email: String, otp: String) =
        "new_password/${Uri.encode(email)}/${Uri.encode(otp)}"

    fun confirm(email: String, otp: String, pass: String) =
        "confirm/${Uri.encode(email)}/${Uri.encode(otp)}/${Uri.encode(pass)}"
}

@Composable
fun AppNavigation() {
    val nav = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = nav, startDestination = Routes.FORGOT) {

        composable(Routes.FORGOT) {
            ForgotPasswordScreen(
                onNext = { email -> nav.navigate(Routes.verify(email)) }
            )
        }

        composable(Routes.VERIFY) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            VerifyCodeScreen(
                email = email,
                onBack = { nav.popBackStack() },
                onNext = { otp -> nav.navigate(Routes.newPassword(email, otp)) }
            )
        }

        composable(Routes.NEW_PASSWORD) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            val otp = entry.arguments?.getString("otp").orEmpty()
            CreatePasswordScreen(
                onBack = { nav.popBackStack() },
                onNext = { pass -> nav.navigate(Routes.confirm(email, otp, pass)) }
            )
        }

        composable(Routes.CONFIRM) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            val otp = entry.arguments?.getString("otp").orEmpty()
            val pass = entry.arguments?.getString("pass").orEmpty()
            ConfirmScreen(
                email = email,
                otp = otp,
                password = pass,
                onBack = { nav.popBackStack() },
                onSubmit = {
                    Toast.makeText(context, "Password updated for $email", Toast.LENGTH_SHORT).show()
                    // Xong thì quay về màn đầu và xóa hết các màn cũ
                    nav.navigate(Routes.FORGOT) {
                        popUpTo(Routes.FORGOT) { inclusive = true }
                    }
                }
            )
        }
    }
}