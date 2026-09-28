package com.app.pathprakash.ui.admin

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


private val PathBlue = Color(0xFF0D47A1)
private val PathOrange = Color(0xFFFF9800)
private val PathOrangeDark = Color(0xFFF57C00)
private val PathBackground = Color(0xFFF7F9FC)


// =====================================================
// SCHOOL MODEL
// =====================================================

data class SchoolItem(
    val id: String = "",
    val schoolName: String = "",
    val ownerName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isActive: Boolean = true
)


// =====================================================
// SCHOOLS SCREEN
// =====================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolsScreen(
    onBack: () -> Unit
) {

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    val scope = rememberCoroutineScope()

    val snackbarHostState = remember {
        SnackbarHostState()
    }


    var schools by remember {
        mutableStateOf<List<SchoolItem>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var selectedSchool by remember {
        mutableStateOf<SchoolItem?>(null)
    }

    var generatedPassword by remember {
        mutableStateOf<String?>(null)
    }

    var passwordSchool by remember {
        mutableStateOf<SchoolItem?>(null)
    }

    var isGeneratingPassword by remember {
        mutableStateOf(false)
    }


    // =====================================================
    // LOAD SCHOOLS
    // =====================================================

    suspend fun loadSchools() {

        isLoading = true

        try {

            val snapshot =
                firestore
                    .collection("schools")
                    .get()
                    .await()

            schools =
                snapshot.documents.map { document ->

                    SchoolItem(
                        id = document.id,

                        schoolName =
                            document.getString(
                                "schoolName"
                            ) ?: "",

                        ownerName =
                            document.getString(
                                "ownerName"
                            ) ?: "",

                        email =
                            document.getString(
                                "email"
                            ) ?: "",

                        phone =
                            document.getString(
                                "phone"
                            ) ?: "",

                        address =
                            document.getString(
                                "address"
                            ) ?: "",

                        latitude =
                            document.getDouble(
                                "latitude"
                            ),

                        longitude =
                            document.getDouble(
                                "longitude"
                            ),

                        isActive =
                            document.getBoolean(
                                "isActive"
                            ) ?: true
                    )
                }

        } catch (e: Exception) {

            snackbarHostState.showSnackbar(
                e.message ?: "Unable to load schools."
            )

        } finally {

            isLoading = false
        }
    }


    LaunchedEffect(Unit) {
        loadSchools()
    }


    Scaffold(

        containerColor = PathBackground,

        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Schools",
                            color = PathBlue,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text =
                                "${schools.size} registered schools",
                            color = Color.Gray
                        )
                    }
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBack
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.ArrowBack,
                            contentDescription =
                                "Back",
                            tint = PathBlue
                        )
                    }
                },

                actions = {

                    IconButton(
                        onClick = {
                            scope.launch {
                                loadSchools()
                            }
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Refresh,
                            contentDescription =
                                "Refresh",
                            tint = PathBlue
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            Color.White
                    )
            )
        }

    ) { paddingValues ->

        when {

            isLoading -> {

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                paddingValues
                            ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = PathOrange
                    )
                }
            }


            schools.isEmpty() -> {

                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                paddingValues
                            ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.School,
                            contentDescription = null,
                            tint = PathOrange,
                            modifier =
                                Modifier.size(55.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        Text(
                            text =
                                "No schools found",
                            color = PathBlue,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Add a school from the dashboard.",
                            color = Color.Gray
                        )
                    }
                }
            }


            else -> {

                LazyColumn(

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                paddingValues
                            )
                            .padding(
                                horizontal = 16.dp
                            ),

                    contentPadding =
                        androidx.compose.foundation.layout
                            .PaddingValues(
                                top = 16.dp,
                                bottom = 20.dp
                            ),

                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = schools,
                        key = {
                            it.id
                        }
                    ) { school ->

                        SchoolCard(

                            school = school,

                            onClick = {
                                selectedSchool =
                                    school
                            }
                        )
                    }
                }
            }
        }
    }


    // =====================================================
    // SCHOOL DETAILS
    // =====================================================

    selectedSchool?.let { school ->

        SchoolDetailsDialog(

            school = school,

            onDismiss = {
                selectedSchool = null
            },

            onGeneratePassword = {

                passwordSchool = school
                selectedSchool = null
                generatedPassword = null
            }
        )
    }


    // =====================================================
    // PASSWORD DIALOG
    // =====================================================

    passwordSchool?.let { school ->

        FirstPasswordDialog(

            school = school,

            password = generatedPassword,

            isLoading = isGeneratingPassword,

            onDismiss = {

                if (!isGeneratingPassword) {
                    passwordSchool = null
                    generatedPassword = null
                }
            },

            onGenerate = {

                /*
                 * IMPORTANT:
                 *
                 * Yahan Cloud Function call hoga.
                 *
                 * Android client se directly Firebase Auth
                 * ka password set nahi karna hai.
                 */

                scope.launch {

                    isGeneratingPassword = true

                    try {

                        /*
                         * Temporary demo:
                         *
                         * Actual production implementation
                         * mein password Cloud Function se
                         * generate hoga.
                         */

                        val password =
                            generateTemporaryPassword()

                        generatedPassword =
                            password

                        snackbarHostState.showSnackbar(
                            "First-time password generated."
                        )

                    } catch (e: Exception) {

                        snackbarHostState.showSnackbar(
                            e.message
                                ?: "Unable to generate password."
                        )

                    } finally {

                        isGeneratingPassword = false
                    }
                }
            }
        )
    }
}


// =====================================================
// SCHOOL CARD
// =====================================================

@Composable
private fun SchoolCard(
    school: SchoolItem,
    onClick: () -> Unit
) {

    Card(

        onClick = onClick,

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(20.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(

                modifier =
                    Modifier
                        .size(55.dp)
                        .clip(
                            RoundedCornerShape(16.dp)
                        )
                        .background(
                            PathOrange.copy(
                                alpha = 0.12f
                            )
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Icon(
                    imageVector =
                        Icons.Default.School,
                    contentDescription = null,
                    tint =
                        PathOrangeDark,
                    modifier =
                        Modifier.size(30.dp)
                )
            }

            Spacer(
                modifier =
                    Modifier.width(14.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text =
                        school.schoolName.ifBlank {
                            "Unnamed School"
                        },
                    color = PathBlue,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(
                    text =
                        "Owner: ${school.ownerName}",
                    color = Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(3.dp)
                )

                Text(
                    text =
                        school.email,
                    color = Color.Gray
                )
            }
        }
    }
}


// =====================================================
// SCHOOL DETAILS DIALOG
// =====================================================

@Composable
private fun SchoolDetailsDialog(
    school: SchoolItem,
    onDismiss: () -> Unit,
    onGeneratePassword: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                text = school.schoolName,
                color = PathBlue,
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column {

                DetailRow(
                    icon = Icons.Default.School,
                    label = "Owner",
                    value = school.ownerName
                )

                DetailRow(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = school.email
                )

                DetailRow(
                    icon = Icons.Default.Phone,
                    label = "Phone",
                    value = school.phone
                )

                DetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "Address",
                    value = school.address
                )

                if (
                    school.latitude != null &&
                    school.longitude != null
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Latitude: ${school.latitude}",
                        color = Color.Gray
                    )

                    Text(
                        text =
                            "Longitude: ${school.longitude}",
                        color = Color.Gray
                    )
                }
            }
        },

        confirmButton = {

            Button(

                onClick =
                    onGeneratePassword,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            PathOrange
                    )
            ) {

                Text(
                    text =
                        "First-Time Password"
                )
            }
        },

        dismissButton = {

            OutlinedButton(
                onClick = onDismiss
            ) {

                Text(
                    text = "Close",
                    color = PathBlue
                )
            }
        }
    )
}


// =====================================================
// DETAIL ROW
// =====================================================

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 5.dp
                ),

        verticalAlignment =
            Alignment.Top
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PathOrangeDark,
            modifier =
                Modifier.size(20.dp)
        )

        Spacer(
            modifier =
                Modifier.width(10.dp)
        )

        Column {

            Text(
                text = label,
                color = PathBlue,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text =
                    value.ifBlank {
                        "Not available"
                    },
                color = Color.Gray
            )
        }
    }
}


// =====================================================
// FIRST PASSWORD DIALOG
// =====================================================

@Composable
private fun FirstPasswordDialog(
    school: SchoolItem,
    password: String?,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onGenerate: () -> Unit
) {

    AlertDialog(

        onDismissRequest = {
            if (!isLoading) {
                onDismiss()
            }
        },

        title = {

            Text(
                text =
                    "School Admin Password",
                color = PathBlue,
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column {

                Text(
                    text =
                        "School: ${school.schoolName}",
                    color = PathBlue,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Email: ${school.email}",
                    color = Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                if (password == null) {

                    Text(
                        text =
                            "Generate a first-time password for this school's admin account.",
                        color = Color.Gray
                    )

                } else {

                    Text(
                        text =
                            "First-Time Password",
                        color = PathBlue,
                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Box(

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(12.dp)
                                )
                                .background(
                                    PathOrange.copy(
                                        alpha = 0.12f
                                    )
                                )
                                .padding(14.dp)
                    ) {

                        Text(
                            text = password,
                            color =
                                PathOrangeDark,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(
                        text =
                            "Give this password to the School Admin. The admin should change it after the first login.",
                        color = Color.Gray
                    )
                }
            }
        },

        confirmButton = {

            Button(

                onClick = onGenerate,

                enabled =
                    !isLoading &&
                            password == null,

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            PathOrange
                    )
            ) {

                if (isLoading) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )

                } else {

                    Text(
                        text =
                            if (password == null)
                                "Generate Password"
                            else
                                "Generated"
                    )
                }
            }
        },

        dismissButton = {

            OutlinedButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {

                Text(
                    text = "Close",
                    color = PathBlue
                )
            }
        }
    )
}


// =====================================================
// TEMPORARY PASSWORD
// =====================================================

private fun generateTemporaryPassword(): String {

    val chars =
        "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789"

    return (1..10)
        .map {
            chars.random()
        }
        .joinToString("")
}