package com.app.pathprakash.ui.auth

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.app.pathprakash.R
import com.app.pathprakash.ui.theme.PathPrakashOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLogin: (
        email: String,
        password: String,
        role: String
    ) -> Unit,

    onGoogleLogin: () -> Unit,

    onForgotPassword: () -> Unit = {},

    isLoading: Boolean = false,

    errorMessage: String? = null
) {

    // -------------------------------------------------
    // Email
    // -------------------------------------------------

    var email by remember {
        mutableStateOf("")
    }

    // -------------------------------------------------
    // Password
    // -------------------------------------------------

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    // -------------------------------------------------
    // Role
    // -------------------------------------------------

    var selectedRole by remember {
        mutableStateOf("")
    }

    var roleDropdownExpanded by remember {
        mutableStateOf(false)
    }

    // -------------------------------------------------
    // Available Roles
    // -------------------------------------------------

    val roles = listOf(
        "Admin",
        "School Admin",
        "Teacher",
        "Parent",
        "Student"
    )

    // -------------------------------------------------
    // Email Validation
    // -------------------------------------------------

    val isEmailValid =
        email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS
                    .matcher(email.trim())
                    .matches()

    // -------------------------------------------------
    // Password Validation
    // -------------------------------------------------

    val isPasswordValid =
        password.isNotBlank()

    // -------------------------------------------------
    // Login Validation
    // -------------------------------------------------

    val canLogin =
        isEmailValid &&
                isPasswordValid &&
                selectedRole.isNotBlank() &&
                !isLoading

    // -------------------------------------------------
    // Screen
    // -------------------------------------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        // -------------------------------------------------
        // PathPrakash Logo
        // -------------------------------------------------

        Image(
            painter = painterResource(
                id = R.drawable.pathprakash_logo
            ),

            contentDescription = "PathPrakash Logo",

            modifier = Modifier.size(200.dp)
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        // -------------------------------------------------
        // Welcome
        // -------------------------------------------------

        Text(
            text = "Welcome to PathPrakash",

            style = MaterialTheme.typography.headlineSmall,

            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        // -------------------------------------------------
        // Role Dropdown
        // -------------------------------------------------

        ExposedDropdownMenuBox(
            expanded = roleDropdownExpanded,

            onExpandedChange = {

                if (!isLoading) {
                    roleDropdownExpanded =
                        !roleDropdownExpanded
                }
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                value = selectedRole,

                onValueChange = {},

                readOnly = true,

                enabled = !isLoading,

                label = {
                    Text("User")
                },

                placeholder = {
                    Text("Select your Role")
                },

                trailingIcon = {

                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = roleDropdownExpanded
                    )
                },

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = roleDropdownExpanded,

                onDismissRequest = {
                    roleDropdownExpanded = false
                }
            ) {

                roles.forEach { role ->

                    DropdownMenuItem(

                        text = {
                            Text(role)
                        },

                        onClick = {

                            selectedRole = role

                            roleDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // -------------------------------------------------
        // Email
        // -------------------------------------------------

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Email")
            },

            placeholder = {
                Text("Enter your email")
            },

            singleLine = true,

            enabled = !isLoading,

            isError =
                email.isNotBlank() &&
                        !isEmailValid,

            supportingText = {

                if (
                    email.isNotBlank() &&
                    !isEmailValid
                ) {

                    Text(
                        text = "Please enter a valid email address"
                    )
                }
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        // -------------------------------------------------
        // Password
        // -------------------------------------------------

        OutlinedTextField(
            value = password,

            onValueChange = {
                password = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Password")
            },

            placeholder = {
                Text("Enter your password")
            },

            singleLine = true,

            enabled = !isLoading,

            visualTransformation =
                if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),

            trailingIcon = {

                IconButton(
                    onClick = {
                        passwordVisible =
                            !passwordVisible
                    }
                ) {

                    Icon(
                        imageVector =
                            if (passwordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },

                        contentDescription =
                            if (passwordVisible) {
                                "Hide password"
                            } else {
                                "Show password"
                            }
                    )
                }
            }
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // -------------------------------------------------
        // Forgot Password
        // -------------------------------------------------

        Text(
            text = "Forgot Password?",

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),

            color = MaterialTheme.colorScheme.primary,

            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        // -------------------------------------------------
        // Error Message
        // -------------------------------------------------

        if (!errorMessage.isNullOrBlank()) {

            Text(
                text = errorMessage,

                color = MaterialTheme.colorScheme.error,

                style = MaterialTheme.typography.bodyMedium,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )
        }

        // -------------------------------------------------
        // Login Button
        // -------------------------------------------------

        Button(
            onClick = {

                onLogin(
                    email.trim(),
                    password,
                    selectedRole
                )
            },

            enabled = canLogin,

            colors = ButtonDefaults.buttonColors(
                containerColor = PathPrakashOrange,
                contentColor = Color.White
            ),

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),

                    strokeWidth = 2.dp,

                    color = Color.White
                )

            } else {

                Text(
                    text = "Login",

                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // -------------------------------------------------
        // Google Login
        // -------------------------------------------------

        OutlinedButton(
            onClick = onGoogleLogin,

            enabled = !isLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            Text(
                text = "Continue with Google",

                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}