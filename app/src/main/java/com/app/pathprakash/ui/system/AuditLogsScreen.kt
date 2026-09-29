package com.app.pathprakash.ui.admin.system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val PathBlue = Color(0xFF0D47A1)
private val PathOrangeDark = Color(0xFFF57C00)
private val PathBackground = Color(0xFFF7F9FC)

private data class AuditLog(
    val action: String,
    val description: String,
    val date: String,
    val user: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditLogsScreen(
    onBack: () -> Unit
) {
    val logs = listOf(
        AuditLog(
            action = "Admin Login",
            description = "Super Admin logged into PathPrakash",
            date = "Today, 09:30 PM",
            user = "PathPrakash Admin"
        ),
        AuditLog(
            action = "School Created",
            description = "A new school was added",
            date = "Today, 06:20 PM",
            user = "PathPrakash Admin"
        ),
        AuditLog(
            action = "Settings Updated",
            description = "System settings were updated",
            date = "Yesterday, 04:10 PM",
            user = "PathPrakash Admin"
        )
    )

    Scaffold(
        containerColor = PathBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Audit Logs",
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                Spacer(Modifier.height(12.dp))

                Text(
                    "System Activity",
                    color = PathBlue,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))
            }

            items(logs) { log ->
                AuditLogCard(log)
            }
        }
    }
}

@Composable
private fun AuditLogCard(
    log: AuditLog
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            Icon(
                imageVector = Icons.Outlined.History,
                contentDescription = null,
                tint = PathOrangeDark,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = log.action,
                    color = PathBlue,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = log.description,
                    color = Color.DarkGray
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = log.user,
                    color = Color.Gray
                )

                Text(
                    text = log.date,
                    color = Color.Gray
                )
            }
        }
    }
}