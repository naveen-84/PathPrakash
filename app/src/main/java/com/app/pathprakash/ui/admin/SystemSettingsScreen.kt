package com.app.pathprakash.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val PathBlue = Color(0xFF0D47A1)
private val PathBlueLight = Color(0xFF1565C0)
private val PathOrange = Color(0xFFFF9800)
private val PathOrangeDark = Color(0xFFF57C00)
private val PathBackground = Color(0xFFF7F9FC)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemSettingsScreen(
    onBack: () -> Unit
) {
    var googleLoginEnabled by remember { mutableStateOf(true) }
    var teacherLoginEnabled by remember { mutableStateOf(true) }
    var parentLoginEnabled by remember { mutableStateOf(true) }
    var studentLoginEnabled by remember { mutableStateOf(true) }

    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var emailNotificationsEnabled by remember { mutableStateOf(true) }

    var maintenanceMode by remember { mutableStateOf(false) }

    var twoFactorEnabled by remember { mutableStateOf(false) }

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
                    IconButton(
                        onClick = onBack
                    ) {
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
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Manage PathPrakash platform settings",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )


            // =====================================================
            // GENERAL SETTINGS
            // =====================================================

            SettingsSectionTitle(
                title = "General Settings"
            )

            SettingsActionCard(
                icon = Icons.Outlined.Settings,
                title = "Platform Information",
                subtitle = "Manage PathPrakash name and support information",
                onClick = {
                    // Later
                }
            )

            SettingsActionCard(
                icon = Icons.Outlined.Language,
                title = "Language & Region",
                subtitle = "Default language, timezone and date format",
                onClick = {
                    // Later
                }
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =====================================================
            // USER & ROLE SETTINGS
            // =====================================================

            SettingsSectionTitle(
                title = "Users & Roles"
            )

            SettingsSwitchCard(
                icon = Icons.Outlined.People,
                title = "Teacher Login",
                subtitle = "Allow teachers to access PathPrakash",
                checked = teacherLoginEnabled,
                onCheckedChange = {
                    teacherLoginEnabled = it
                }
            )

            SettingsSwitchCard(
                icon = Icons.Outlined.People,
                title = "Parent Login",
                subtitle = "Allow parents to access PathPrakash",
                checked = parentLoginEnabled,
                onCheckedChange = {
                    parentLoginEnabled = it
                }
            )

            SettingsSwitchCard(
                icon = Icons.Outlined.People,
                title = "Student Login",
                subtitle = "Allow students to access PathPrakash",
                checked = studentLoginEnabled,
                onCheckedChange = {
                    studentLoginEnabled = it
                }
            )

            SettingsSwitchCard(
                icon = Icons.Outlined.Security,
                title = "Google Login",
                subtitle = "Allow users to sign in using Google",
                checked = googleLoginEnabled,
                onCheckedChange = {
                    googleLoginEnabled = it
                }
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =====================================================
            // SCHOOL MANAGEMENT
            // =====================================================

            SettingsSectionTitle(
                title = "School Management"
            )

            SettingsActionCard(
                icon = Icons.Outlined.Business,
                title = "School Defaults",
                subtitle = "Manage default school and academic settings",
                onClick = {
                    // Later
                }
            )

            SettingsActionCard(
                icon = Icons.Outlined.Tune,
                title = "User Limits",
                subtitle = "Configure student, teacher and storage limits",
                onClick = {
                    // Later
                }
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =====================================================
            // NOTIFICATIONS
            // =====================================================

            SettingsSectionTitle(
                title = "Notifications"
            )

            SettingsSwitchCard(
                icon = Icons.Outlined.Notifications,
                title = "Push Notifications",
                subtitle = "Enable system push notifications",
                checked = pushNotificationsEnabled,
                onCheckedChange = {
                    pushNotificationsEnabled = it
                }
            )

            SettingsSwitchCard(
                icon = Icons.Outlined.Notifications,
                title = "Email Notifications",
                subtitle = "Enable system email notifications",
                checked = emailNotificationsEnabled,
                onCheckedChange = {
                    emailNotificationsEnabled = it
                }
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =====================================================
            // SECURITY
            // =====================================================

            SettingsSectionTitle(
                title = "Security"
            )

            SettingsActionCard(
                icon = Icons.Outlined.Security,
                title = "Password Policy",
                subtitle = "Configure minimum password and security rules",
                onClick = {
                    // Later
                }
            )

            SettingsSwitchCard(
                icon = Icons.Outlined.Security,
                title = "Two-Factor Authentication",
                subtitle = "Require additional verification for admin accounts",
                checked = twoFactorEnabled,
                onCheckedChange = {
                    twoFactorEnabled = it
                }
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =====================================================
            // STORAGE
            // =====================================================

            SettingsSectionTitle(
                title = "Storage"
            )

            SettingsActionCard(
                icon = Icons.Outlined.Storage,
                title = "File & Storage Settings",
                subtitle = "Manage upload limits and allowed file types",
                onClick = {
                    // Later
                }
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =====================================================
            // MAINTENANCE
            // =====================================================

            SettingsSectionTitle(
                title = "System Maintenance"
            )

            SettingsSwitchCard(
                icon = Icons.Outlined.Warning,
                title = "Maintenance Mode",
                subtitle = "Temporarily restrict platform access",
                checked = maintenanceMode,
                onCheckedChange = {
                    maintenanceMode = it
                }
            )

            SettingsActionCard(
                icon = Icons.Outlined.Build,
                title = "System Maintenance",
                subtitle = "Manage maintenance message and scheduled maintenance",
                onClick = {
                    // Later
                }
            )


            Spacer(
                modifier = Modifier.height(22.dp)
            )


            // =====================================================
            // AUDIT LOG
            // =====================================================

            SettingsSectionTitle(
                title = "System Logs"
            )

            SettingsActionCard(
                icon = Icons.Outlined.Security,
                title = "Audit Logs",
                subtitle = "View important system and admin activities",
                onClick = {
                    // Later
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "PathPrakash System Settings",
                modifier = Modifier.fillMaxWidth(),
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


// =====================================================
// SECTION TITLE
// =====================================================

@Composable
private fun SettingsSectionTitle(
    title: String
) {
    Text(
        text = title,
        color = PathBlue,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(
            bottom = 10.dp
        )
    )
}


// =====================================================
// ACTION CARD
// =====================================================

@Composable
private fun SettingsActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            SettingsIcon(
                icon = icon
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = PathBlue,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = subtitle,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


// =====================================================
// SWITCH CARD
// =====================================================

@Composable
private fun SettingsSwitchCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            SettingsIcon(
                icon = icon
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = PathBlue,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = subtitle,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}


// =====================================================
// SETTINGS ICON
// =====================================================

@Composable
private fun SettingsIcon(
    icon: ImageVector
) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(46.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                PathOrange.copy(alpha = 0.12f)
            ),

        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PathOrangeDark,
            modifier = Modifier.size(24.dp)
        )
    }
}