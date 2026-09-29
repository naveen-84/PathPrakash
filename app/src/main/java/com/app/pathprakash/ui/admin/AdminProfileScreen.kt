package com.app.pathprakash.ui.admin

import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.VerifiedUser

import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

import coil.compose.AsyncImage

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


// =====================================================
// PATHPRAKASH COLORS
// =====================================================

private val PathBlue = Color(0xFF0D47A1)
private val PathBlueLight = Color(0xFF1565C0)

private val PathOrange = Color(0xFFFF9800)
private val PathOrangeDark = Color(0xFFF57C00)

private val PathBackground = Color(0xFFF7F9FC)


// =====================================================
// ADMIN PROFILE SCREEN
// =====================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(

    adminName: String = "PathPrakash Admin",

    adminEmail: String = "",

    adminUid: String = "",

    profilePhotoUrl: String = "",

    onProfilePhotoUpdated: (String) -> Unit = {},

    onBack: () -> Unit,

    onLogout: () -> Unit

) {

    // =================================================
    // FIREBASE
    // =================================================

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    val storage = remember {
        FirebaseStorage.getInstance()
    }

    // Coroutine scope for Firebase operations
    val scope = rememberCoroutineScope()


    // =================================================
    // STATES
    // =================================================

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var currentProfilePhotoUrl by remember {
        mutableStateOf(profilePhotoUrl)
    }

    var isUploadingPhoto by remember {
        mutableStateOf(false)
    }

    var showPasswordDialog by remember {
        mutableStateOf(false)
    }

    var currentPassword by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var isChangingPassword by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf<String?>(null)
    }


    // =================================================
    // UPDATE PHOTO URL
    // =================================================

    LaunchedEffect(profilePhotoUrl) {

        currentProfilePhotoUrl =
            profilePhotoUrl
    }


    // =================================================
    // IMAGE PICKER
    // =================================================

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {

                selectedImageUri = uri
            }
        }


    // =================================================
    // UPLOAD PROFILE PHOTO
    // =================================================

    LaunchedEffect(selectedImageUri) {

        val uri =
            selectedImageUri
                ?: return@LaunchedEffect

        if (adminUid.isBlank()) {

            message =
                "User ID not available."

            selectedImageUri = null

            return@LaunchedEffect
        }

        isUploadingPhoto = true
        message = null

        try {

            // -----------------------------------------
            // STORAGE REFERENCE
            // -----------------------------------------

            val storageReference =
                storage
                    .reference
                    .child("profile_photos")
                    .child("$adminUid.jpg")


            // -----------------------------------------
            // UPLOAD IMAGE
            // -----------------------------------------

            storageReference
                .putFile(uri)
                .await()


            // -----------------------------------------
            // GET DOWNLOAD URL
            // -----------------------------------------

            val downloadUrl =
                storageReference
                    .downloadUrl
                    .await()
                    .toString()


            // -----------------------------------------
            // SAVE URL IN FIRESTORE
            // -----------------------------------------

            firestore
                .collection("users")
                .document(adminUid)
                .update(
                    "profilePhotoUrl",
                    downloadUrl
                )
                .await()


            // -----------------------------------------
            // UPDATE LOCAL STATE
            // -----------------------------------------

            currentProfilePhotoUrl =
                downloadUrl

            onProfilePhotoUpdated(
                downloadUrl
            )


            message =
                "Profile photo updated successfully."

        } catch (e: Exception) {

            message =
                e.message
                    ?: "Unable to update profile photo."

        } finally {

            isUploadingPhoto = false

            selectedImageUri = null
        }
    }


    // =================================================
    // MAIN UI
    // =================================================

    Scaffold(

        containerColor =
            PathBackground,

        topBar = {

            TopAppBar(

                title = {

                    Text(

                        text =
                            "Admin Profile",

                        color =
                            PathBlue,

                        fontWeight =
                            FontWeight.Bold
                    )
                },

                navigationIcon = {

                    IconButton(

                        onClick =
                            onBack
                    ) {

                        Icon(

                            imageVector =
                                Icons.Outlined.ArrowBack,

                            contentDescription =
                                "Back",

                            tint =
                                PathBlue
                        )
                    }
                },

                colors =
                    TopAppBarDefaults
                        .topAppBarColors(
                            containerColor =
                                Color.White
                        )
            )
        }

    ) { paddingValues ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        paddingValues
                    )
                    .padding(
                        horizontal = 20.dp
                    )
                    .verticalScroll(
                        rememberScrollState()
                    ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =================================================
            // PROFILE PHOTO
            // =================================================

            Box(

                modifier =
                    Modifier.size(115.dp),

                contentAlignment =
                    Alignment.Center
            ) {

                if (
                    currentProfilePhotoUrl
                        .isNotBlank()
                ) {

                    AsyncImage(

                        model =
                            currentProfilePhotoUrl,

                        contentDescription =
                            "Admin Profile Photo",

                        modifier =
                            Modifier
                                .size(105.dp)
                                .clip(
                                    CircleShape
                                ),

                        contentScale =
                            ContentScale.Crop
                    )

                } else {

                    Box(

                        modifier =
                            Modifier
                                .size(105.dp)
                                .clip(
                                    CircleShape
                                )
                                .background(
                                    PathBlue
                                ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(

                            imageVector =
                                Icons.Outlined.Person,

                            contentDescription =
                                "Admin",

                            tint =
                                Color.White,

                            modifier =
                                Modifier.size(55.dp)
                        )
                    }
                }


                // =================================================
                // CAMERA BUTTON
                // =================================================

                Box(

                    modifier =
                        Modifier
                            .align(
                                Alignment.BottomEnd
                            )
                            .size(38.dp)
                            .clip(
                                CircleShape
                            )
                            .background(
                                PathOrangeDark
                            )
                            .clickable {

                                if (
                                    !isUploadingPhoto
                                ) {

                                    imagePickerLauncher
                                        .launch(
                                            "image/*"
                                        )
                                }
                            },

                    contentAlignment =
                        Alignment.Center
                ) {

                    if (isUploadingPhoto) {

                        CircularProgressIndicator(

                            modifier =
                                Modifier.size(
                                    20.dp
                                ),

                            color =
                                Color.White,

                            strokeWidth =
                                2.dp
                        )

                    } else {

                        Icon(

                            imageVector =
                                Icons.Outlined.PhotoCamera,

                            contentDescription =
                                "Change Profile Photo",

                            tint =
                                Color.White,

                            modifier =
                                Modifier.size(
                                    21.dp
                                )
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            // =================================================
            // ADMIN NAME
            // =================================================

            Text(

                text =
                    adminName.ifBlank {
                        "PathPrakash Admin"
                    },

                color =
                    PathBlue,

                fontWeight =
                    FontWeight.Bold,

                style =
                    MaterialTheme
                        .typography
                        .headlineSmall
            )


            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )


            Text(

                text =
                    "Super Administrator",

                color =
                    PathOrangeDark,

                fontWeight =
                    FontWeight.SemiBold,

                style =
                    MaterialTheme
                        .typography
                        .bodyLarge
            )


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =================================================
            // MESSAGE
            // =================================================

            if (
                !message
                    .isNullOrBlank()
            ) {

                Text(

                    text =
                        message!!,

                    color =
                        PathBlue,

                    fontWeight =
                        FontWeight.Medium,

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = 12.dp
                            )
                )
            }


            // =================================================
            // ACCOUNT INFORMATION
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                colors =
                    CardDefaults
                        .cardColors(
                            containerColor =
                                Color.White
                        ),

                elevation =
                    CardDefaults
                        .cardElevation(
                            defaultElevation =
                                2.dp
                        )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            20.dp
                        )
                ) {

                    Text(

                        text =
                            "Account Information",

                        color =
                            PathBlue,

                        fontWeight =
                            FontWeight.Bold,

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                18.dp
                            )
                    )


                    ProfileInfoRow(

                        icon =
                            Icons.Outlined.Person,

                        title =
                            "Name",

                        value =
                            adminName.ifBlank {
                                "PathPrakash Admin"
                            }
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )


                    ProfileInfoRow(

                        icon =
                            Icons.Outlined.Email,

                        title =
                            "Email",

                        value =
                            adminEmail.ifBlank {
                                "Not available"
                            }
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )


                    ProfileInfoRow(

                        icon =
                            Icons.Outlined.Security,

                        title =
                            "Role",

                        value =
                            "Super Admin"
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )


                    ProfileInfoRow(

                        icon =
                            Icons.Outlined.VerifiedUser,

                        title =
                            "Account Status",

                        value =
                            "Active"
                    )


                    if (
                        adminUid.isNotBlank()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    16.dp
                                )
                        )


                        ProfileInfoRow(

                            icon =
                                Icons.Outlined.Security,

                            title =
                                "User ID",

                            value =
                                adminUid
                        )
                    }
                }
            }


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )


            // =================================================
            // ACCOUNT SETTINGS
            // =================================================

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(22.dp),

                colors =
                    CardDefaults
                        .cardColors(
                            containerColor =
                                Color.White
                        ),

                elevation =
                    CardDefaults
                        .cardElevation(
                            defaultElevation =
                                2.dp
                        )
            ) {

                Column(

                    modifier =
                        Modifier.padding(
                            16.dp
                        )
                ) {

                    Text(

                        text =
                            "Account Settings",

                        color =
                            PathBlue,

                        fontWeight =
                            FontWeight.Bold,

                        style =
                            MaterialTheme
                                .typography
                                .titleLarge,

                        modifier =
                            Modifier.padding(
                                bottom = 10.dp
                            )
                    )


                    // =================================================
                    // CHANGE PROFILE PHOTO
                    // =================================================

                    AccountActionRow(

                        icon =
                            Icons.Outlined.PhotoCamera,

                        title =
                            "Change Profile Photo",

                        subtitle =
                            "Update your profile picture",

                        onClick = {

                            if (
                                !isUploadingPhoto
                            ) {

                                imagePickerLauncher
                                    .launch(
                                        "image/*"
                                    )
                            }
                        }
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )


                    // =================================================
                    // CHANGE PASSWORD
                    // =================================================

                    AccountActionRow(

                        icon =
                            Icons.Outlined.Key,

                        title =
                            "Change Password",

                        subtitle =
                            "Update your account password",

                        onClick = {

                            message = null

                            currentPassword = ""
                            newPassword = ""
                            confirmPassword = ""

                            showPasswordDialog =
                                true
                        }
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =================================================
            // LOGOUT BUTTON
            // =================================================

            Button(

                onClick =
                    onLogout,

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(52.dp),

                shape =
                    RoundedCornerShape(16.dp),

                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                PathOrangeDark,

                            contentColor =
                                Color.White
                        )
            ) {

                Icon(

                    imageVector =
                        Icons.Outlined.Logout,

                    contentDescription =
                        "Logout"
                )


                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )


                Text(

                    text =
                        "Logout",

                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )
        }
    }


    // =====================================================
    // CHANGE PASSWORD DIALOG
    // =====================================================

    if (showPasswordDialog) {

        AlertDialog(

            onDismissRequest = {

                if (
                    !isChangingPassword
                ) {

                    showPasswordDialog =
                        false
                }
            },


            // =================================================
            // TITLE
            // =================================================

            title = {

                Text(

                    text =
                        "Change Password",

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        PathBlue
                )
            },


            // =================================================
            // CONTENT
            // =================================================

            text = {

                Column {

                    Text(

                        text =
                            "Enter your current password and choose a new password.",

                        color =
                            Color.Gray,

                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium
                    )


                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )


                    // -----------------------------------------
                    // CURRENT PASSWORD
                    // -----------------------------------------

                    OutlinedTextField(

                        value =
                            currentPassword,

                        onValueChange = {
                            currentPassword = it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text(
                                "Current Password"
                            )
                        },

                        singleLine = true,

                        enabled =
                            !isChangingPassword,

                        visualTransformation =
                            PasswordVisualTransformation()
                    )


                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )


                    // -----------------------------------------
                    // NEW PASSWORD
                    // -----------------------------------------

                    OutlinedTextField(

                        value =
                            newPassword,

                        onValueChange = {
                            newPassword = it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text(
                                "New Password"
                            )
                        },

                        singleLine = true,

                        enabled =
                            !isChangingPassword,

                        visualTransformation =
                            PasswordVisualTransformation()
                    )


                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )


                    // -----------------------------------------
                    // CONFIRM PASSWORD
                    // -----------------------------------------

                    OutlinedTextField(

                        value =
                            confirmPassword,

                        onValueChange = {
                            confirmPassword = it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text(
                                "Confirm New Password"
                            )
                        },

                        singleLine = true,

                        enabled =
                            !isChangingPassword,

                        visualTransformation =
                            PasswordVisualTransformation()
                    )


                    // -----------------------------------------
                    // PASSWORD LENGTH ERROR
                    // -----------------------------------------

                    if (
                        newPassword.isNotEmpty() &&
                        newPassword.length < 6
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(5.dp)
                        )

                        Text(

                            text =
                                "Password must contain at least 6 characters.",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error,

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )
                    }


                    // -----------------------------------------
                    // PASSWORD MATCH ERROR
                    // -----------------------------------------

                    if (
                        confirmPassword.isNotEmpty() &&
                        newPassword != confirmPassword
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(5.dp)
                        )

                        Text(

                            text =
                                "Passwords do not match.",

                            color =
                                MaterialTheme
                                    .colorScheme
                                    .error,

                            style =
                                MaterialTheme
                                    .typography
                                    .bodySmall
                        )
                    }
                }
            },


            // =================================================
            // CONFIRM BUTTON
            // =================================================

            confirmButton = {

                Button(

                    onClick = {

                        // -------------------------------------
                        // VALIDATION
                        // -------------------------------------

                        if (
                            currentPassword.isBlank() ||
                            newPassword.isBlank() ||
                            confirmPassword.isBlank()
                        ) {

                            message =
                                "Please fill all password fields."

                            return@Button
                        }


                        if (
                            newPassword.length < 6
                        ) {

                            message =
                                "New password must contain at least 6 characters."

                            return@Button
                        }


                        if (
                            newPassword != confirmPassword
                        ) {

                            message =
                                "New passwords do not match."

                            return@Button
                        }


                        // -------------------------------------
                        // CURRENT FIREBASE USER
                        // -------------------------------------

                        val firebaseUser =
                            auth.currentUser

                        if (
                            firebaseUser == null
                        ) {

                            message =
                                "User session has expired."

                            return@Button
                        }


                        // -------------------------------------
                        // EMAIL
                        // -------------------------------------

                        val email =
                            firebaseUser.email
                                ?: adminEmail

                        if (
                            email.isBlank()
                        ) {

                            message =
                                "Admin email is not available."

                            return@Button
                        }


                        // -------------------------------------
                        // START LOADING
                        // -------------------------------------

                        isChangingPassword =
                            true

                        message = null


                        // -------------------------------------
                        // FIREBASE COROUTINE
                        // -------------------------------------

                        scope.launch {

                            try {

                                // ---------------------------------
                                // EMAIL CREDENTIAL
                                // ---------------------------------

                                val credential =
                                    EmailAuthProvider
                                        .getCredential(
                                            email,
                                            currentPassword
                                        )


                                // ---------------------------------
                                // RE-AUTHENTICATE
                                // ---------------------------------

                                firebaseUser
                                    .reauthenticate(
                                        credential
                                    )
                                    .await()


                                // ---------------------------------
                                // UPDATE PASSWORD
                                // ---------------------------------

                                firebaseUser
                                    .updatePassword(
                                        newPassword
                                    )
                                    .await()


                                // ---------------------------------
                                // CLEAR FIELDS
                                // ---------------------------------

                                currentPassword = ""
                                newPassword = ""
                                confirmPassword = ""


                                // ---------------------------------
                                // CLOSE DIALOG
                                // ---------------------------------

                                showPasswordDialog =
                                    false


                                // ---------------------------------
                                // SUCCESS MESSAGE
                                // ---------------------------------

                                message =
                                    "Password changed successfully."

                            } catch (e: Exception) {

                                // ---------------------------------
                                // ERROR
                                // ---------------------------------

                                val errorText =
                                    e.message
                                        ?: ""

                                message =
                                    when {

                                        errorText.contains(
                                            "password is invalid",
                                            ignoreCase = true
                                        ) ->
                                            "Current password is incorrect."


                                        errorText.contains(
                                            "INVALID_LOGIN_CREDENTIALS",
                                            ignoreCase = true
                                        ) ->
                                            "Current password is incorrect."


                                        errorText.contains(
                                            "requires-recent-login",
                                            ignoreCase = true
                                        ) ->
                                            "Please login again and try changing your password."


                                        else ->
                                            errorText.ifBlank {
                                                "Unable to change password."
                                            }
                                    }

                            } finally {

                                isChangingPassword =
                                    false
                            }
                        }
                    },

                    enabled =
                        !isChangingPassword,

                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    PathBlue
                            )
                ) {

                    if (
                        isChangingPassword
                    ) {

                        CircularProgressIndicator(

                            modifier =
                                Modifier.size(
                                    20.dp
                                ),

                            color =
                                Color.White,

                            strokeWidth =
                                2.dp
                        )

                    } else {

                        Text(
                            text =
                                "Change Password"
                        )
                    }
                }
            },


            // =================================================
            // CANCEL BUTTON
            // =================================================

            dismissButton = {

                OutlinedButton(

                    onClick = {

                        showPasswordDialog =
                            false
                    },

                    enabled =
                        !isChangingPassword
                ) {

                    Text(
                        text =
                            "Cancel"
                    )
                }
            }
        )
    }
}


// =====================================================
// ACCOUNT ACTION ROW
// =====================================================

@Composable
private fun AccountActionRow(

    icon:
    androidx.compose.ui.graphics.vector.ImageVector,

    title: String,

    subtitle: String,

    onClick: () -> Unit

) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .clickable(
                    onClick = onClick
                )
                .padding(12.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        // =================================================
        // ICON
        // =================================================

        Box(

            modifier =
                Modifier
                    .size(46.dp)
                    .clip(
                        RoundedCornerShape(14.dp)
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
                    icon,

                contentDescription =
                    title,

                tint =
                    PathOrangeDark,

                modifier =
                    Modifier.size(24.dp)
            )
        }


        Spacer(
            modifier =
                Modifier.width(14.dp)
        )


        // =================================================
        // TEXT
        // =================================================

        Column(

            modifier =
                Modifier.weight(1f)
        ) {

            Text(

                text =
                    title,

                color =
                    PathBlue,

                fontWeight =
                    FontWeight.SemiBold,

                style =
                    MaterialTheme
                        .typography
                        .bodyLarge
            )


            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )


            Text(

                text =
                    subtitle,

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


// =====================================================
// PROFILE INFORMATION ROW
// =====================================================

@Composable
private fun ProfileInfoRow(

    icon:
    androidx.compose.ui.graphics.vector.ImageVector,

    title: String,

    value: String

) {

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        // =================================================
        // ICON
        // =================================================

        Box(

            modifier =
                Modifier
                    .size(44.dp)
                    .clip(
                        RoundedCornerShape(13.dp)
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
                    icon,

                contentDescription =
                    title,

                tint =
                    PathOrangeDark,

                modifier =
                    Modifier.size(23.dp)
            )
        }


        Spacer(
            modifier =
                Modifier.width(14.dp)
        )


        // =================================================
        // TEXT
        // =================================================

        Column(

            modifier =
                Modifier.weight(1f)
        ) {

            Text(

                text =
                    title,

                color =
                    Color.Gray,

                style =
                    MaterialTheme
                        .typography
                        .bodySmall
            )


            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )


            Text(

                text =
                    value,

                color =
                    PathBlue,

                fontWeight =
                    FontWeight.SemiBold,

                style =
                    MaterialTheme
                        .typography
                        .bodyLarge
            )
        }
    }
}