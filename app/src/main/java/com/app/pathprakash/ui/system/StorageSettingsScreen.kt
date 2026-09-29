package com.app.pathprakash.ui.admin.system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
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
fun StorageSettingsScreen(
    onBack: () -> Unit
) {
    var maxImageSize by remember { mutableStateOf("5") }
    var maxDocumentSize by remember { mutableStateOf("10") }
    var allowedDocuments by remember {
        mutableStateOf("PDF, DOC, DOCX, XLS, XLSX")
    }

    Scaffold(
        containerColor = PathBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Storage Settings",
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
                "Upload Limits",
                color = PathBlue,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = maxImageSize,
                onValueChange = { maxImageSize = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Maximum Image Size (MB)") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = maxDocumentSize,
                onValueChange = { maxDocumentSize = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Maximum Document Size (MB)") },
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = allowedDocuments,
                onValueChange = { allowedDocuments = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Allowed Document Types") },
                minLines = 2
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
                    "Save Storage Settings",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}