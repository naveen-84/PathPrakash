package com.app.pathprakash.ui.admin.system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val PathBlue = Color(0xFF0D47A1)
private val PathOrange = Color(0xFFF57C00)
private val PathBackground = Color(0xFFF7F9FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettingsScreen(
    onBack: () -> Unit
) {
    var platformName by remember { mutableStateOf("PathPrakash") }
    var supportEmail by remember { mutableStateOf("support@pathprakash.com") }
    var supportPhone by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var timezone by remember { mutableStateOf("Asia/Kolkata") }
    var language by remember { mutableStateOf("English") }

    Scaffold(
        containerColor = PathBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "General Settings",
                        color = PathBlue,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Outlined.ArrowBack,
                            "Back",
                            tint = PathBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Text(
                "Platform Information",
                color = PathBlue,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(18.dp))

            OutlinedTextField(
                value = platformName,
                onValueChange = { platformName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Platform Name") },
                leadingIcon = {
                    Icon(Icons.Outlined.Settings, null)
                },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = supportEmail,
                onValueChange = { supportEmail = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Support Email") },
                leadingIcon = {
                    Icon(Icons.Outlined.Email, null)
                },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = supportPhone,
                onValueChange = { supportPhone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Support Phone") },
                leadingIcon = {
                    Icon(Icons.Outlined.Phone, null)
                },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = website,
                onValueChange = { website = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Website URL") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = timezone,
                onValueChange = { timezone = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Default Timezone") },
                leadingIcon = {
                    Icon(Icons.Outlined.Language, null)
                },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = language,
                onValueChange = { language = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Default Language") },
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    // Firestore save later
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PathBlue
                )
            ) {
                Icon(Icons.Outlined.Save, null)

                Spacer(Modifier.padding(horizontal = 4.dp))

                Text(
                    "Save Settings",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}