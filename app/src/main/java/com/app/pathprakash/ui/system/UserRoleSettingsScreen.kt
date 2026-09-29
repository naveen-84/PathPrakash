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
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
private val PathBackground = Color(0xFFF7F9FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRoleSettingsScreen(
    onBack: () -> Unit
) {
    var teacherLogin by remember { mutableStateOf(true) }
    var parentLogin by remember { mutableStateOf(true) }
    var studentLogin by remember { mutableStateOf(true) }
    var googleLogin by remember { mutableStateOf(true) }
    var forcePasswordChange by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = PathBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Users & Roles",
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
                "Login Access",
                color = PathBlue,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            RoleSwitch(
                title = "Teacher Login",
                subtitle = "Allow teachers to access the application",
                checked = teacherLogin,
                onCheckedChange = { teacherLogin = it }
            )

            RoleSwitch(
                title = "Parent Login",
                subtitle = "Allow parents to access the application",
                checked = parentLogin,
                onCheckedChange = { parentLogin = it }
            )

            RoleSwitch(
                title = "Student Login",
                subtitle = "Allow students to access the application",
                checked = studentLogin,
                onCheckedChange = { studentLogin = it }
            )

            RoleSwitch(
                title = "Google Login",
                subtitle = "Allow Google authentication",
                checked = googleLogin,
                onCheckedChange = { googleLogin = it }
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Account Rules",
                color = PathBlue,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            RoleSwitch(
                title = "Force Password Change",
                subtitle = "New accounts must change temporary password",
                checked = forcePasswordChange,
                onCheckedChange = { forcePasswordChange = it }
            )
        }
    }
}

@Composable
private fun RoleSwitch(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
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
                Icons.Outlined.People,
                contentDescription = null,
                tint = Color(0xFFF57C00)
            )

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    color = PathBlue,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    subtitle,
                    color = Color.Gray
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}