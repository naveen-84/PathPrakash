package com.app.pathprakash.ui.admin

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.app.pathprakash.ui.theme.PathPrakashOrange
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale


// =====================================================
// PATHPRAKASH COLORS
// =====================================================

private val PathBlue = Color(0xFF0D47A1)
private val PathOrange = Color(0xFFFF9800)
private val PathOrangeDark = Color(0xFFF57C00)
private val PathBackground = Color(0xFFF7F9FC)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSchoolScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scrollState = rememberScrollState()


    // =====================================================
    // FORM STATES
    // =====================================================

    var schoolName by remember {
        mutableStateOf("")
    }

    var ownerName by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var address by remember {
        mutableStateOf("")
    }

    var latitude by remember {
        mutableStateOf<Double?>(null)
    }

    var longitude by remember {
        mutableStateOf<Double?>(null)

    }
        // =====================================================
        // LOADING STATES
        // =====================================================

        var isGettingLocation by remember {
            mutableStateOf(false)
        }

        var isSaving by remember {
            mutableStateOf(false)
        }


        // =====================================================
        // VALIDATION
        // =====================================================

        val isEmailValid =
            email.isNotBlank() &&
                    Patterns.EMAIL_ADDRESS
                        .matcher(email.trim())
                        .matches()

        val isPhoneValid =
            phone.length == 10 &&
                    phone.all {
                        it.isDigit()
                    }

        val isFormValid =
            schoolName.isNotBlank() &&
                    ownerName.isNotBlank() &&
                    isEmailValid &&
                    isPhoneValid &&
                    address.isNotBlank()


        // =====================================================
        // TEXT FIELD COLORS
        // =====================================================

        val textFieldColors =
            OutlinedTextFieldDefaults.colors(

                // Input text
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,

                // Cursor
                cursorColor = PathBlue,

                // Border
                focusedBorderColor = PathBlue,
                unfocusedBorderColor = Color.Gray,

                // Label
                focusedLabelColor = PathBlue,
                unfocusedLabelColor = Color.Gray,

                // Placeholder
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,

                // Icons
                focusedLeadingIconColor = PathBlue,
                unfocusedLeadingIconColor = PathBlue
            )


        // =====================================================
        // GET CURRENT LOCATION
        // =====================================================

        fun getCurrentLocation() {

            val fineLocationGranted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

            val coarseLocationGranted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

            if (!fineLocationGranted && !coarseLocationGranted) {
                return
            }

            isGettingLocation = true

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->

                    if (location != null) {

                        latitude = location.latitude
                        longitude = location.longitude

                        try {

                            val geocoder =
                                Geocoder(
                                    context,
                                    Locale.getDefault()
                                )

                            val addresses =
                                geocoder.getFromLocation(
                                    location.latitude,
                                    location.longitude,
                                    1
                                )

                            if (!addresses.isNullOrEmpty()) {

                                val result =
                                    addresses[0]

                                address =
                                    result.getAddressLine(0)
                                        ?: ""

                            } else {

                                address =
                                    "Location: ${location.latitude}, ${location.longitude}"
                            }

                        } catch (e: Exception) {

                            address =
                                "Location: ${location.latitude}, ${location.longitude}"
                        }

                    } else {

                        scope.launch {

                            snackbarHostState.showSnackbar(
                                "Unable to get current location. Please try again."
                            )
                        }
                    }

                    isGettingLocation = false
                }
                .addOnFailureListener {

                    isGettingLocation = false

                    scope.launch {

                        snackbarHostState.showSnackbar(
                            "Unable to get current location."
                        )
                    }
                }
        }


        // =====================================================
        // LOCATION PERMISSION
        // =====================================================

        val locationPermissionLauncher =
            rememberLauncherForActivityResult(
                contract =
                    ActivityResultContracts
                        .RequestMultiplePermissions()
            ) { permissions ->

                val granted =
                    permissions[
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ] == true ||
                            permissions[
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ] == true

                if (granted) {

                    getCurrentLocation()

                } else {

                    scope.launch {

                        snackbarHostState.showSnackbar(
                            "Location permission is required."
                        )
                    }
                }
            }


        // =====================================================
        // SCREEN
        // =====================================================

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
                                text = "Add School",
                                color = PathBlue,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Register a new school",
                                color = Color.Gray,
                                style =
                                    MaterialTheme.typography.bodySmall
                            )
                        }
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = onBack,
                            enabled = !isSaving
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

                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        )
                )
            }

        ) { paddingValues ->


            // =================================================
            // SCROLLABLE CONTENT
            // =================================================

            Column(

                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp),

                verticalArrangement =
                    Arrangement.Top
            ) {

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )


                // =================================================
                // HEADER CARD
                // =================================================

                Card(

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(22.dp),

                    colors =
                        CardDefaults.cardColors(
                            containerColor = Color.White
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
                                .padding(18.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(

                            modifier =
                                Modifier
                                    .size(58.dp)
                                    .clip(
                                        RoundedCornerShape(17.dp)
                                    )
                                    .background(
                                        PathOrange.copy(
                                            alpha = 0.12f
                                        )
                                    ),

                            horizontalAlignment =
                                Alignment.CenterHorizontally,

                            verticalArrangement =
                                Arrangement.Center
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Default.School,

                                contentDescription =
                                    null,

                                tint =
                                    PathOrangeDark,

                                modifier =
                                    Modifier.size(32.dp)
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.width(14.dp)
                        )

                        Column {

                            Text(
                                text =
                                    "School Information",

                                color =
                                    PathBlue,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(3.dp)
                            )

                            Text(
                                text =
                                    "Enter school and owner details.",

                                color =
                                    Color.Gray,

                                style =
                                    MaterialTheme
                                        .typography
                                        .bodySmall
                            )
                        }
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )


                // =================================================
                // SCHOOL NAME
                // =================================================

                Text(
                    text = "School Name",
                    color = PathBlue,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                OutlinedTextField(

                    value = schoolName,

                    onValueChange = {
                        schoolName = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    placeholder = {
                        Text(
                            "Enter school name"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.School,

                            contentDescription =
                                null,

                            tint =
                                PathBlue
                        )
                    },

                    singleLine = true,

                    shape =
                        RoundedCornerShape(16.dp),

                    enabled =
                        !isSaving,

                    colors =
                        textFieldColors
                )


                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )


                // =================================================
                // SCHOOL OWNER NAME
                // =================================================

                Text(
                    text = "School Owner Name",
                    color = PathBlue,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                OutlinedTextField(

                    value = ownerName,

                    onValueChange = {
                        ownerName = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    placeholder = {
                        Text(
                            "Enter school owner name"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Person,

                            contentDescription =
                                "School Owner",

                            tint =
                                PathBlue
                        )
                    },

                    singleLine = true,

                    shape =
                        RoundedCornerShape(16.dp),

                    enabled =
                        !isSaving,

                    colors =
                        textFieldColors
                )


                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )


                // =================================================
                // CONTACT NUMBER
                // =================================================

                Text(
                    text = "Contact Number",
                    color = PathBlue,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                OutlinedTextField(

                    value = phone,

                    onValueChange = { newValue ->

                        // Only numbers
                        // Maximum 10 digits

                        if (
                            newValue.length <= 10 &&
                            newValue.all {
                                it.isDigit()
                            }
                        ) {

                            phone = newValue
                        }
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    placeholder = {
                        Text(
                            "Enter 10 digit contact number"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Phone,

                            contentDescription =
                                null,

                            tint =
                                PathBlue
                        )
                    },

                    singleLine = true,

                    shape =
                        RoundedCornerShape(16.dp),

                    enabled =
                        !isSaving,

                    isError =
                        phone.isNotEmpty() &&
                                !isPhoneValid,

                    supportingText = {

                        if (
                            phone.isNotEmpty() &&
                            !isPhoneValid
                        ) {

                            Text(
                                text =
                                    "Contact number must be exactly 10 digits",

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
                                KeyboardType.Phone
                        ),

                    colors =
                        textFieldColors
                )


                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )


                // =================================================
                // EMAIL
                // =================================================

                Text(
                    text = "School Email",
                    color = PathBlue,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                OutlinedTextField(

                    value = email,

                    onValueChange = {
                        email = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    placeholder = {
                        Text(
                            "Enter school email"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.Email,

                            contentDescription =
                                null,

                            tint =
                                PathBlue
                        )
                    },

                    singleLine = true,

                    shape =
                        RoundedCornerShape(16.dp),

                    enabled =
                        !isSaving,

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
                        textFieldColors
                )


                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )


                // =================================================
                // ADDRESS + LOCATION
                // =================================================

                Row(

                    modifier =
                        Modifier.fillMaxWidth(),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text =
                            "School Address",

                        color =
                            PathBlue,

                        fontWeight =
                            FontWeight.SemiBold,

                        modifier =
                            Modifier.weight(1f)
                    )


                    OutlinedButton(

                        onClick = {

                            locationPermissionLauncher
                                .launch(
                                    arrayOf(
                                        Manifest.permission
                                            .ACCESS_FINE_LOCATION,

                                        Manifest.permission
                                            .ACCESS_COARSE_LOCATION
                                    )
                                )
                        },

                        enabled =
                            !isGettingLocation &&
                                    !isSaving,

                        shape =
                            RoundedCornerShape(12.dp)
                    ) {

                        if (isGettingLocation) {

                            CircularProgressIndicator(

                                modifier =
                                    Modifier.size(18.dp),

                                strokeWidth = 2.dp,

                                color =
                                    PathOrangeDark
                            )

                        } else {

                            Icon(
                                imageVector =
                                    Icons.Default.MyLocation,

                                contentDescription =
                                    "Use current location",

                                tint =
                                    PathOrangeDark
                            )

                            Spacer(
                                modifier =
                                    Modifier.width(5.dp)
                            )

                            Text(
                                text =
                                    "Use Location",

                                color =
                                    PathOrangeDark,

                                fontWeight =
                                    FontWeight.SemiBold
                            )
                        }
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )


                // =================================================
                // ADDRESS FIELD
                // =================================================

                OutlinedTextField(

                    value = address,

                    onValueChange = {
                        address = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    placeholder = {

                        Text(
                            "Tap Use Location to get address"
                        )
                    },

                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Default.LocationOn,

                            contentDescription =
                                null,

                            tint =
                                PathBlue
                        )
                    },

                    minLines = 2,

                    maxLines = 3,

                    shape =
                        RoundedCornerShape(16.dp),

                    enabled =
                        !isSaving,

                    colors =
                        textFieldColors
                )


                // =================================================
                // COORDINATES
                // =================================================

                if (
                    latitude != null &&
                    longitude != null
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "Location: $latitude, $longitude",

                        color =
                            Color.Gray,

                        style =
                            MaterialTheme
                                .typography
                                .bodySmall
                    )
                }


                Spacer(
                    modifier =
                        Modifier.height(24.dp)
                )


                // =================================================
                // ADD SCHOOL BUTTON
                // =================================================

                Button(

                    onClick = {

                        if (!isFormValid) {
                            return@Button
                        }

                        isSaving = true

                        scope.launch {

                            try {

                                val schoolData =
                                    hashMapOf(

                                        "schoolName" to
                                                schoolName.trim(),

                                        "ownerName" to
                                                ownerName.trim(),

                                        "email" to
                                                email.trim(),

                                        "phone" to
                                                phone.trim(),

                                        "address" to
                                                address.trim(),

                                        "latitude" to
                                                latitude,

                                        "longitude" to
                                                longitude,

                                        "isActive" to
                                                true,

                                        "createdAt" to
                                                FieldValue
                                                    .serverTimestamp()
                                    )


                                firestore
                                    .collection("schools")
                                    .add(schoolData)
                                    .await()


                                // =================================
                                // CLEAR FORM
                                // =================================

                                schoolName = ""
                                ownerName = ""
                                phone = ""
                                email = ""
                                address = ""

                                latitude = null
                                longitude = null


                                // =================================
                                // SUCCESS MESSAGE
                                // =================================

                                snackbarHostState
                                    .showSnackbar(
                                        "School added successfully"
                                    )

                            } catch (e: Exception) {

                                snackbarHostState
                                    .showSnackbar(
                                        e.message
                                            ?: "Failed to add school."
                                    )

                            } finally {

                                isSaving = false
                            }
                        }
                    },

                    enabled =
                        isFormValid &&
                                !isSaving,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(54.dp),

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        ButtonDefaults.buttonColors(

                            containerColor =
                                PathOrange,

                            contentColor =
                                Color.White,

                            disabledContainerColor =
                                PathOrange.copy(
                                    alpha = 0.40f
                                ),

                            disabledContentColor =
                                Color.White
                        )
                ) {

                    if (isSaving) {

                        CircularProgressIndicator(

                            modifier =
                                Modifier.size(22.dp),

                            strokeWidth = 2.dp,

                            color =
                                Color.White
                        )

                        Spacer(
                            modifier =
                                Modifier.width(10.dp)
                        )

                        Text(
                            text =
                                "Adding School...",

                            fontWeight =
                                FontWeight.Bold
                        )

                    } else {

                        Text(
                            text =
                                "Add School",

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


                // =================================================
                // CANCEL BUTTON
                // =================================================

                OutlinedButton(

                    onClick =
                        onBack,

                    enabled =
                        !isSaving,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(50.dp),

                    shape =
                        RoundedCornerShape(16.dp),

                    colors =
                        ButtonDefaults
                            .outlinedButtonColors(
                                contentColor =
                                    PathBlue
                            )
                ) {

                    Text(
                        text =
                            "Cancel",

                        fontWeight =
                            FontWeight.SemiBold
                    )
                }


                Spacer(
                    modifier =
                        Modifier.height(24.dp)
                )
            }
        }
    }