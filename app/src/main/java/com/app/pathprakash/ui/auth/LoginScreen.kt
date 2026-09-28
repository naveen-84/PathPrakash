package com.app.pathprakash.ui.auth

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.sp
import com.app.pathprakash.R

// =====================================================
// PATHPRAKASH COLORS
// =====================================================

private val PathBlue = Color(0xFF0B438F)

private val BorderBlue = Color(0xFF1976D2)

private val PathOrange = Color(0xFFFF9800)

private val DarkText = Color(0xFF111111)

private val HintText = Color(0xFF777777)

private val White = Color(0xFFFFFFFF)


// =====================================================
// LOGIN SCREEN
// =====================================================

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

    // =================================================
    // STATES
    // =================================================

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var selectedRole by remember {
        mutableStateOf("")
    }

    var roleDropdownExpanded by remember {
        mutableStateOf(false)
    }


    // =================================================
    // ROLES
    // =================================================

    val roles = listOf(
        "Admin",
        "School Admin",
        "Teacher",
        "Parent",
        "Student"
    )


    // =================================================
    // VALIDATION
    // =================================================

    val isEmailValid =
        email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS
                    .matcher(email.trim())
                    .matches()

    val isPasswordValid =
        password.isNotBlank()

    val canLogin =
        isEmailValid &&
                isPasswordValid &&
                selectedRole.isNotBlank() &&
                !isLoading


    // =================================================
    // SCREEN
    // =================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally,

            verticalArrangement =
                Arrangement.Center
        ) {

            // =================================================
            // LOGO
            // =================================================

            Image(
                painter = painterResource(
                    id = R.drawable.pathprakash_full_logo
                ),

                contentDescription =
                    "PathPrakash Logo",

                modifier = Modifier.size(250.dp)
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            // =================================================
            // WELCOME TEXT
            // =================================================

            Text(
                text = "Welcome to PathPrakash",

                color = PathBlue,

                style =
                    MaterialTheme.typography
                        .headlineSmall,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =================================================
            // ROLE DROPDOWN
            // =================================================

            ExposedDropdownMenuBox(

                expanded =
                    roleDropdownExpanded,

                onExpandedChange = {

                    if (!isLoading) {

                        roleDropdownExpanded =
                            !roleDropdownExpanded
                    }
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(

                    value = selectedRole,

                    onValueChange = {},

                    readOnly = true,

                    enabled = !isLoading,

                    label = {

                        Text(
                            text = "User"
                        )
                    },

                    placeholder = {

                        Text(
                            text = "Select your Role"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Person,

                            contentDescription =
                                "User",

                            tint = PathBlue
                        )
                    },

                    trailingIcon = {

                        ExposedDropdownMenuDefaults
                            .TrailingIcon(
                                expanded =
                                    roleDropdownExpanded
                            )
                    },

                    colors =
                        OutlinedTextFieldDefaults.colors(

                            // Selected role text
                            focusedTextColor =
                                DarkText,

                            unfocusedTextColor =
                                DarkText,

                            // Placeholder
                            focusedPlaceholderColor =
                                HintText,

                            unfocusedPlaceholderColor =
                                HintText,

                            // Label
                            focusedLabelColor =
                                PathBlue,

                            unfocusedLabelColor =
                                PathBlue,

                            // Border
                            focusedBorderColor =
                                BorderBlue,

                            unfocusedBorderColor =
                                BorderBlue,

                            // Cursor
                            cursorColor =
                                PathBlue,

                            // Leading icon
                            focusedLeadingIconColor =
                                PathBlue,

                            unfocusedLeadingIconColor =
                                PathBlue
                        ),

                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )


                // =================================================
                // ROLE MENU
                // =================================================

                ExposedDropdownMenu(

                    expanded =
                        roleDropdownExpanded,

                    onDismissRequest = {

                        roleDropdownExpanded =
                            false
                    },

                    modifier =
                        Modifier.background(
                            White
                        )
                ) {

                    roles.forEach { role ->

                        DropdownMenuItem(

                            text = {

                                Text(
                                    text = role,

                                    color =
                                        PathBlue
                                )
                            },

                            leadingIcon = {

                                Icon(
                                    imageVector =
                                        Icons.Default.Person,

                                    contentDescription =
                                        null,

                                    tint =
                                        PathBlue
                                )
                            },

                            onClick = {

                                selectedRole =
                                    role

                                roleDropdownExpanded =
                                    false
                            },

                            colors =
                                MenuDefaults
                                    .itemColors(
                                        textColor =
                                            PathBlue,

                                        leadingIconColor =
                                            PathBlue
                                    )
                        )
                    }
                }
            }


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            // =================================================
            // EMAIL
            // =================================================

            OutlinedTextField(

                value = email,

                onValueChange = {
                    email = it
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        text = "Email Address"
                    )
                },

                placeholder = {

                    Text(
                        text = "Enter your email"
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.Email,

                        contentDescription =
                            "Email",

                        tint =
                            if (
                                email.isNotBlank() &&
                                !isEmailValid
                            ) {
                                MaterialTheme
                                    .colorScheme
                                    .error
                            } else {
                                PathBlue
                            }
                    )
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
                            text =
                                "Please enter a valid email address",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error
                        )
                    }
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Email
                    ),

                colors =
                    OutlinedTextFieldDefaults.colors(

                        // =========================================
                        // TYPED TEXT
                        // =========================================

                        focusedTextColor =
                            DarkText,

                        unfocusedTextColor =
                            DarkText,

                        // =========================================
                        // PLACEHOLDER
                        // =========================================

                        focusedPlaceholderColor =
                            HintText,

                        unfocusedPlaceholderColor =
                            HintText,

                        // =========================================
                        // NORMAL LABEL
                        // =========================================

                        focusedLabelColor =
                            PathBlue,

                        unfocusedLabelColor =
                            PathBlue,

                        // =========================================
                        // NORMAL BORDER
                        // =========================================

                        focusedBorderColor =
                            BorderBlue,

                        unfocusedBorderColor =
                            BorderBlue,

                        // =========================================
                        // ERROR STATE
                        // =========================================

                        errorTextColor =
                            DarkText,

                        errorLabelColor =
                            MaterialTheme
                                .colorScheme
                                .error,

                        errorBorderColor =
                            MaterialTheme
                                .colorScheme
                                .error,

                        errorLeadingIconColor =
                            MaterialTheme
                                .colorScheme
                                .error,

                        // =========================================
                        // CURSOR
                        // =========================================

                        cursorColor =
                            PathBlue
                    )
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // =================================================
            // PASSWORD
            // =================================================

            OutlinedTextField(

                value = password,

                onValueChange = {
                    password = it
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        text = "Password"
                    )
                },

                placeholder = {

                    Text(
                        text = "Enter your password"
                    )
                },

                leadingIcon = {

                    Icon(
                        imageVector =
                            Icons.Default.Lock,

                        contentDescription =
                            "Password",

                        tint =
                            PathBlue
                    )
                },

                trailingIcon = {

                    IconButton(

                        onClick = {

                            passwordVisible =
                                !passwordVisible
                        }
                    ) {

                        Icon(

                            imageVector =
                                if (
                                    passwordVisible
                                ) {

                                    Icons.Default
                                        .VisibilityOff

                                } else {

                                    Icons.Default
                                        .Visibility
                                },

                            contentDescription =
                                if (
                                    passwordVisible
                                ) {

                                    "Hide password"

                                } else {

                                    "Show password"
                                },

                            tint =
                                PathBlue
                        )
                    }
                },

                singleLine = true,

                enabled = !isLoading,

                visualTransformation =
                    if (passwordVisible) {

                        VisualTransformation.None

                    } else {

                        PasswordVisualTransformation()
                    },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Password
                    ),

                colors =
                    OutlinedTextFieldDefaults.colors(

                        // Typed password
                        focusedTextColor =
                            DarkText,

                        unfocusedTextColor =
                            DarkText,

                        // Placeholder
                        focusedPlaceholderColor =
                            HintText,

                        unfocusedPlaceholderColor =
                            HintText,

                        // Label
                        focusedLabelColor =
                            PathBlue,

                        unfocusedLabelColor =
                            PathBlue,

                        // Border
                        focusedBorderColor =
                            BorderBlue,

                        unfocusedBorderColor =
                            BorderBlue,

                        // Cursor
                        cursorColor =
                            PathBlue,

                        // Icons
                        focusedLeadingIconColor =
                            PathBlue,

                        unfocusedLeadingIconColor =
                            PathBlue,

                        focusedTrailingIconColor =
                            PathBlue,

                        unfocusedTrailingIconColor =
                            PathBlue
                    )
            )


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // =================================================
            // FORGOT PASSWORD
            // =================================================

            Text(

                text = "Forgot Password?",

                color = PathBlue,

                fontWeight =
                    FontWeight.Medium,

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 6.dp
                    )
                    .clickable(
                        enabled = !isLoading
                    ) {
                        onForgotPassword()
                    }
            )


            // =================================================
            // FIREBASE / LOGIN ERROR
            // =================================================

            if (!errorMessage.isNullOrBlank()) {

                Text(

                    text = errorMessage,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error,

                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 4.dp,
                            bottom = 8.dp
                        )
                )
            }


            // =================================================
            // LOGIN BUTTON
            // =================================================

            Button(

                onClick = {

                    onLogin(
                        email.trim(),
                        password,
                        selectedRole
                    )
                },

                enabled =
                    canLogin,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults.buttonColors(

                        containerColor =
                            PathOrange,

                        contentColor =
                            White,

                        disabledContainerColor =
                            PathOrange.copy(
                                alpha = 0.45f
                            ),

                        disabledContentColor =
                            White
                    )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(

                        modifier =
                            Modifier.size(22.dp),

                        strokeWidth = 2.dp,

                        color = White
                    )

                } else {

                    Icon(

                        imageVector =
                            Icons.Default.Login,

                        contentDescription =
                            null,

                        tint =
                            White
                    )

                    Spacer(
                        modifier =
                            Modifier.size(10.dp)
                    )

                    Text(

                        text = "Login",

                        fontSize = 17.sp,

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // =================================================
            // OR DIVIDER
            // =================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                HorizontalDivider(

                    modifier =
                        Modifier.weight(1f),

                    color =
                        PathOrange
                )

                Text(

                    text = "  OR  ",

                    color =
                        PathBlue,

                    fontWeight =
                        FontWeight.Bold
                )

                HorizontalDivider(

                    modifier =
                        Modifier.weight(1f),

                    color =
                        PathOrange
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            // =================================================
            // GOOGLE LOGIN
            // =================================================

            OutlinedButton(

                onClick =
                    onGoogleLogin,

                enabled =
                    !isLoading,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                shape =
                    RoundedCornerShape(14.dp),

                colors =
                    ButtonDefaults
                        .outlinedButtonColors(

                            contentColor =
                                PathBlue
                        ),

                border =
                    BorderStroke(
                        1.5.dp,
                        BorderBlue
                    )
            ) {

                Image(

                    painter =
                        painterResource(
                            id =
                                R.drawable.google_logo
                        ),

                    contentDescription =
                        "Google",

                    modifier =
                        Modifier.size(24.dp)
                )

                Spacer(
                    modifier =
                        Modifier.size(10.dp)
                )

                Text(

                    text =
                        "Continue with Google",

                    color =
                        PathBlue,

                    fontSize = 16.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}