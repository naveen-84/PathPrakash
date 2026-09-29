package com.app.pathprakash.ui.admin.system

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val PathBlue = Color(0xFF0D47A1)
private val PathOrange = Color(0xFFFF9800)
private val PathOrangeDark = Color(0xFFF57C00)
private val PathBackground = Color(0xFFF7F9FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemSettingsScreen(
    onBack: () -> Unit,
    onGeneral: () -> Unit,
    onUsersRoles: () -> Unit,
    onSchoolManagement: () -> Unit,
    onNotifications: () -> Unit,
    onSecurity: () -> Unit,
    onStorage: () -> Unit,
    onAcademicDefaults: () -> Unit,
    onSubscription: () -> Unit,
    onMaintenance: () -> Unit,
    onAuditLogs: () -> Unit
) {
    Scaffold(
        containerColor = PathBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "System Settings",
                        color = PathBlue,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = PathBlue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {

            Text(
                text = "Manage your PathPrakash platform",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            SystemSettingItem(
                icon = Icons.Outlined.Settings,
                title = "General",
                subtitle = "Platform name, logo and support details",
                onClick = onGeneral
            )

            SystemSettingItem(
                icon = Icons.Outlined.People,
                title = "Users & Roles",
                subtitle = "Manage login access and role controls",
                onClick = onUsersRoles
            )

            SystemSettingItem(
                icon = Icons.Outlined.Business,
                title = "School Management",
                subtitle = "School creation and user limits",
                onClick = onSchoolManagement
            )

            SystemSettingItem(
                icon = Icons.Outlined.Notifications,
                title = "Notifications",
                subtitle = "Push, email and announcement settings",
                onClick = onNotifications
            )

            SystemSettingItem(
                icon = Icons.Outlined.Security,
                title = "Security",
                subtitle = "Password and login security",
                onClick = onSecurity
            )

            SystemSettingItem(
                icon = Icons.Outlined.Storage,
                title = "Storage",
                subtitle = "File and image upload settings",
                onClick = onStorage
            )

            SystemSettingItem(
                icon = Icons.Outlined.School,
                title = "Academic Defaults",
                subtitle = "Academic year, grading and attendance",
                onClick = onAcademicDefaults
            )

            SystemSettingItem(
                icon = Icons.Outlined.CreditCard,
                title = "Subscription",
                subtitle = "Plans, limits and subscription settings",
                onClick = onSubscription
            )

            SystemSettingItem(
                icon = Icons.Outlined.Build,
                title = "Maintenance",
                subtitle = "Maintenance mode and system message",
                onClick = onMaintenance
            )

            SystemSettingItem(
                icon = Icons.Outlined.History,
                title = "Audit Logs",
                subtitle = "View system and admin activities",
                onClick = onAuditLogs
            )
        }
    }
}

@Composable
private fun SystemSettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        PathOrange.copy(alpha = 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PathOrangeDark
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = PathBlue,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subtitle,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = "Open",
                tint = Color.Gray
            )
        }
    }
}