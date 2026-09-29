package com.app.pathprakash.ui.admin.system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
private val PathBackground = Color(0xFFF7F9FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolManagementSettingsScreen(
    onBack: () -> Unit
) {
    var temporaryPasswordExpiry by remember { mutableStateOf("24") }
    var maxStudents by remember { mutableStateOf("2000") }
    var maxTeachers by remember { mutableStateOf("200") }
    var maxStorage by remember { mutableStateOf("5") }

    Scaffold(
        containerColor = PathBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "School Management",
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
                "School Limits",
                color = PathBlue,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = temporaryPasswordExpiry,
                onValueChange = { temporaryPasswordExpiry = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Temporary Password Expiry (Hours)") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = maxStudents,
                onValueChange = { maxStudents = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Maximum Students / School") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = maxTeachers,
                onValueChange = { maxTeachers = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Maximum Teachers / School") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = maxStorage,
                onValueChange = { maxStorage = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Maximum Storage (GB)") },
                singleLine = true
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {},
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