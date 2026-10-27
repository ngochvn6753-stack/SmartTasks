package com.example.smarttasks.ui.theme

import android.util.Patterns
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen(onNext: (email: String) -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AuthScreen(
        title = "Forget Password?",
        subtitle = "Enter your Email, we will send you a verification code."
    ) {
        AuthTextField(
            value = email,
            onValueChange = {
                email = it
                error = null
            },
            placeholder = "Your Email",
            icon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            error = error
        )
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Next") {
            val e = email.trim()
            if (Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                onNext(e)
            } else {
                error = "Please enter a valid email"
            }
        }
    }
}

@Composable
fun VerifyCodeScreen(email: String, onBack: () -> Unit, onNext: (otp: String) -> Unit) {
    var otp by rememberSaveable { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AuthScreen(
        title = "Verify Code",
        subtitle = "Enter the code we just sent to\n$email",
        onBack = onBack
    ) {
        OtpInput(
            value = otp,
            onValueChange = {
                otp = it
                error = null
            }
        )
        error?.let {
            Text(
                it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Next") {
            if (otp.length == 5) {
                onNext(otp)
            } else {
                error = "Please enter the 5-digit code"
            }
        }
    }
}

@Composable
fun CreatePasswordScreen(onBack: () -> Unit, onNext: (password: String) -> Unit) {
    var pass by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var passError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }

    AuthScreen(
        title = "Create new password",
        subtitle = "Your new password must be different form previously used password",
        onBack = onBack
    ) {
        AuthTextField(
            value = pass,
            onValueChange = {
                pass = it
                passError = null
            },
            placeholder = "Password",
            icon = Icons.Default.Lock,
            isPassword = true,
            error = passError
        )
        Spacer(Modifier.height(12.dp))
        AuthTextField(
            value = confirm,
            onValueChange = {
                confirm = it
                confirmError = null
            },
            placeholder = "Confirm Password",
            icon = Icons.Default.Lock,
            isPassword = true,
            error = confirmError
        )
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Next") {
            if (pass.length < 6) {
                passError = "Password must be at least 6 characters"
            } else if (pass != confirm) {
                confirmError = "Passwords do not match"
            } else {
                onNext(pass)
            }
        }
    }
}

@Composable
fun ConfirmScreen(
    email: String,
    otp: String,
    password: String,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    AuthScreen(
        title = "Confirm",
        subtitle = "We are here to help you!",
        onBack = onBack
    ) {
        AuthTextField(
            value = email,
            onValueChange = {},
            placeholder = "Email",
            icon = Icons.Default.Email,
            readOnly = true
        )
        Spacer(Modifier.height(12.dp))
        AuthTextField(
            value = otp,
            onValueChange = {},
            placeholder = "Code",
            icon = Icons.Default.Check,
            readOnly = true
        )
        Spacer(Modifier.height(12.dp))
        AuthTextField(
            value = password,
            onValueChange = {},
            placeholder = "Password",
            icon = Icons.Default.Lock,
            isPassword = true,
            readOnly = true
        )
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Submit", onSubmit)
    }
}