package com.app.pathprakash.ui.admin.system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val PathBlue = Color(0xFF0D47A1)
private val PathOrangeDark = Color(0xFFF57C00)
private val PathBackground = Color(0xFFF7F9FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaintenanceSettingsScreen(
    onBack: () -> Unit
) {
    var maintenanceMode by remember { mutableStateOf(false) }
    var allowAdminLogin by remember { mutableStateOf(true) }

    var maintenanceMessage by remember {
        mutableStateOf(
            "PathPrakash is currently under maintenance. Please try again later."
        )
    }

    Scaffold(
        containerColor = PathBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Maintenance",
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Outlined.Warning,
                        null,
                        tint = PathOrangeDark
                    )

                    Spacer(Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Maintenance Mode",
                            color = PathBlue,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Restrict normal platform access",
                            color = Color.Gray
                        )
                    }

                    Switch(
                        checked = maintenanceMode,
                        onCheckedChange = {
                            maintenanceMode = it
                        }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Allow Admin Login",
                            color = PathBlue,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Allow Super Admin access during maintenance",
                            color = Color.Gray
                        )
                    }

                    Switch(
                        checked = allowAdminLogin,
                        onCheckedChange = {
                            allowAdminLogin = it
                        }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = maintenanceMessage,
                onValueChange = {
                    maintenanceMessage = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Maintenance Message")
                },
                minLines = 4
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
                Spacer(Modifier.width(8.dp))
                Text(
                    "Save Maintenance Settings",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}