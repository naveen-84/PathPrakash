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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
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
// ADMIN MENU ITEM
// =====================================================

data class AdminMenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)


// =====================================================
// ADMIN DASHBOARD
// =====================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminName: String = "Admin",
    onAddSchool: () -> Unit,
    onSchools: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit
) {

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }

    // =================================================
    // REAL FIRESTORE COUNTS
    // =================================================

    var schoolCount by remember {
        mutableStateOf(0L)
    }

    var teacherCount by remember {
        mutableStateOf(0L)
    }

    var studentCount by remember {
        mutableStateOf(0L)
    }

    var isLoadingCounts by remember {
        mutableStateOf(true)
    }


    // =================================================
    // LOAD REAL COUNTS
    // =================================================

    LaunchedEffect(Unit) {

        isLoadingCounts = true

        try {

            // ---------------------------------------------
            // TOTAL SCHOOLS
            // ---------------------------------------------

            val schoolsResult =
                firestore
                    .collection("schools")
                    .count()
                    .get(AggregateSource.SERVER)
                    .await()

            schoolCount =
                schoolsResult.count


            // ---------------------------------------------
            // TOTAL TEACHERS
            // ---------------------------------------------

            val teachersResult =
                firestore
                    .collection("users")
                    .whereEqualTo(
                        "role",
                        "teacher"
                    )
                    .count()
                    .get(AggregateSource.SERVER)
                    .await()

            teacherCount =
                teachersResult.count


            // ---------------------------------------------
            // TOTAL STUDENTS
            // ---------------------------------------------

            val studentsResult =
                firestore
                    .collection("users")
                    .whereEqualTo(
                        "role",
                        "student"
                    )
                    .count()
                    .get(AggregateSource.SERVER)
                    .await()

            studentCount =
                studentsResult.count

        } catch (e: Exception) {

            // Keep previous values if loading fails.
            // Do not crash the dashboard.

        } finally {

            isLoadingCounts = false
        }
    }


    // =================================================
    // MENU ITEMS
    // =================================================

    val menuItems = listOf(

        AdminMenuItem(
            title = "Add School",
            subtitle = "Register a new school",
            icon = Icons.Default.AddBusiness
        ),

        AdminMenuItem(
            title = "Schools",
            subtitle = "Manage registered schools",
            icon = Icons.Default.School
        ),

        AdminMenuItem(
            title = "Users",
            subtitle = "Manage platform users",
            icon = Icons.Default.Groups
        ),

        AdminMenuItem(
            title = "Admins",
            subtitle = "Manage administrators",
            icon = Icons.Default.SupervisedUserCircle
        ),

        AdminMenuItem(
            title = "System Settings",
            subtitle = "Configure PathPrakash",
            icon = Icons.Default.Settings
        ),

        AdminMenuItem(
            title = "Admin Profile",
            subtitle = "View admin account",
            icon = Icons.Default.AdminPanelSettings
        )
    )


    // =================================================
    // UI
    // =================================================

    Scaffold(

        containerColor = PathBackground,

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "PathPrakash",
                            color = PathBlue,
                            fontWeight = FontWeight.Bold,
                            style =
                                MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Admin Dashboard",
                            color = Color.Gray,
                            style =
                                MaterialTheme.typography.bodySmall
                        )
                    }
                },

                actions = {

                    IconButton(
                        onClick = onLogout
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Logout,
                            contentDescription =
                                "Logout",
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

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),

            verticalArrangement =
                Arrangement.Top
        ) {

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )


            // =================================================
            // WELCOME CARD
            // =================================================

            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(24.dp)
                    )
                    .background(
                        brush =
                            Brush.linearGradient(
                                colors =
                                    listOf(
                                        PathBlue,
                                        PathBlueLight,
                                        PathOrange
                                    )
                            )
                    )
                    .padding(22.dp)
            ) {

                Column {

                    Text(
                        text = "Welcome back,",
                        color =
                            Color.White.copy(
                                alpha = 0.85f
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .bodyLarge
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(
                        text = adminName,
                        color = Color.White,
                        fontWeight =
                            FontWeight.Bold,
                        style =
                            MaterialTheme
                                .typography
                                .headlineSmall
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Manage schools, teachers and students.",
                        color =
                            Color.White.copy(
                                alpha = 0.90f
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )


            // =================================================
            // QUICK STATS
            // =================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                DashboardStatCard(
                    title = "Schools",
                    value =
                        if (isLoadingCounts) {
                            null
                        } else {
                            schoolCount.toString()
                        },
                    modifier =
                        Modifier.weight(1f)
                )

                DashboardStatCard(
                    title = "Teachers",
                    value =
                        if (isLoadingCounts) {
                            null
                        } else {
                            teacherCount.toString()
                        },
                    modifier =
                        Modifier.weight(1f)
                )

                DashboardStatCard(
                    title = "Students",
                    value =
                        if (isLoadingCounts) {
                            null
                        } else {
                            studentCount.toString()
                        },
                    modifier =
                        Modifier.weight(1f)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            // =================================================
            // MANAGEMENT
            // =================================================

            Text(
                text = "Management",
                color = PathBlue,
                fontWeight =
                    FontWeight.Bold,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Manage your PathPrakash platform",
                color = Color.Gray,
                style =
                    MaterialTheme.typography.bodyMedium
            )


            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )


            // =================================================
            // MENU GRID
            // =================================================

            LazyVerticalGrid(

                columns =
                    GridCells.Fixed(2),

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)

            ) {

                items(menuItems) { item ->

                    AdminMenuCard(

                        item = item,

                        onClick = {

                            when (item.title) {

                                "Add School" ->
                                    onAddSchool()

                                "Schools" ->
                                    onSchools()

                                "System Settings" ->
                                    onSettings()
                            }
                        }
                    )
                }
            }
        }
    }
}


// =====================================================
// STAT CARD
// =====================================================

@Composable
private fun DashboardStatCard(
    title: String,
    value: String?,
    modifier: Modifier = Modifier
) {

    Card(

        modifier = modifier,

        shape =
            RoundedCornerShape(18.dp),

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

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            horizontalAlignment =
                Alignment.Start
        ) {

            if (value == null) {

                CircularProgressIndicator(

                    modifier =
                        Modifier.size(28.dp),

                    strokeWidth = 3.dp,

                    color =
                        PathOrange
                )

            } else {

                Text(
                    text = value,
                    color =
                        PathOrangeDark,
                    fontWeight =
                        FontWeight.Bold,
                    style =
                        MaterialTheme
                            .typography
                            .headlineSmall
                )
            }

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text = title,
                color = PathBlue,
                fontWeight =
                    FontWeight.Medium,
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium
            )
        }
    }
}


// =====================================================
// ADMIN MENU CARD
// =====================================================

@Composable
private fun AdminMenuCard(
    item: AdminMenuItem,
    onClick: () -> Unit
) {

    Card(

        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth()
            .height(155.dp),

        shape =
            RoundedCornerShape(22.dp),

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

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),

            horizontalAlignment =
                Alignment.Start,

            verticalArrangement =
                Arrangement.Center
        ) {

            Box(

                modifier = Modifier
                    .size(50.dp)
                    .clip(
                        RoundedCornerShape(15.dp)
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
                        item.icon,
                    contentDescription =
                        item.title,
                    tint =
                        PathOrangeDark,
                    modifier =
                        Modifier.size(28.dp)
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text = item.title,
                color = PathBlue,
                fontWeight =
                    FontWeight.Bold,
                style =
                    MaterialTheme
                        .typography
                        .titleMedium
            )

            Spacer(
                modifier =
                    Modifier.height(3.dp)
            )

            Text(
                text = item.subtitle,
                color = Color.Gray,
                style =
                    MaterialTheme
                        .typography
                        .bodySmall
            )
        }
    }
}