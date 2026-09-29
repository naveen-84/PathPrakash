package com.app.pathprakash.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddBusiness
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
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

private val BottomPurple = Color(0xFF4C2BF5)


// =====================================================
// ADMIN MENU ITEM
// =====================================================

data class AdminMenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)


// =====================================================
// BOTTOM NAVIGATION ITEM
// =====================================================

enum class AdminBottomNavItem(
    val title: String,
    val icon: ImageVector
) {
    HOME(
        title = "Home",
        icon = Icons.Outlined.Home
    ),

    SCHOOLS(
        title = "Schools",
        icon = Icons.Outlined.School
    ),

    USERS(
        title = "Users",
        icon = Icons.Outlined.Groups
    ),

    REPORTS(
        title = "Reports",
        icon = Icons.Outlined.BarChart
    ),

    PROFILE(
        title = "Profile",
        icon = Icons.Outlined.Person
    )
}


// =====================================================
// ADMIN DASHBOARD SCREEN
// =====================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    adminName: String = "Admin",
    onAddSchool: () -> Unit,
    onSchools: () -> Unit,
    onSettings: () -> Unit,
    onLogout: () -> Unit,
    onUsers: () -> Unit = {},
    onReports: () -> Unit = {},
    onProfile: () -> Unit = {}
) {

    // =================================================
    // FIRESTORE
    // =================================================

    val firestore = remember {
        FirebaseFirestore.getInstance()
    }


    // =================================================
    // COUNTS
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
    // BOTTOM NAV SELECTED ITEM
    // =================================================

    var selectedBottomItem by remember {
        mutableStateOf(
            AdminBottomNavItem.HOME
        )
    }


    // =================================================
    // LOAD FIRESTORE COUNTS
    // =================================================

    LaunchedEffect(Unit) {

        isLoadingCounts = true

        try {

            // -----------------------------------------
            // SCHOOLS
            // -----------------------------------------

            val schoolsSnapshot =
                firestore
                    .collection("schools")
                    .count()
                    .get(AggregateSource.SERVER)
                    .await()

            schoolCount =
                schoolsSnapshot.count


            // -----------------------------------------
            // TEACHERS
            // -----------------------------------------

            val teachersSnapshot =
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
                teachersSnapshot.count


            // -----------------------------------------
            // STUDENTS
            // -----------------------------------------

            val studentsSnapshot =
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
                studentsSnapshot.count

        } catch (e: Exception) {

            // Do not crash dashboard
            // if count request fails.

        } finally {

            isLoadingCounts = false
        }
    }


    // =================================================
    // MANAGEMENT ITEMS
    // =================================================

    val menuItems = listOf(

        AdminMenuItem(
            title = "Add School",
            subtitle = "Register a new school",
            icon = Icons.Outlined.AddBusiness
        ),

        AdminMenuItem(
            title = "Schools",
            subtitle = "Manage registered schools",
            icon = Icons.Outlined.School
        ),

        AdminMenuItem(
            title = "Users",
            subtitle = "Manage platform users",
            icon = Icons.Outlined.Groups
        ),

        AdminMenuItem(
            title = "Admins",
            subtitle = "Manage administrators",
            icon = Icons.Outlined.AdminPanelSettings
        ),

        AdminMenuItem(
            title = "System Settings",
            subtitle = "Configure PathPrakash",
            icon = Icons.Outlined.Settings
        ),

        AdminMenuItem(
            title = "Admin Profile",
            subtitle = "View admin account",
            icon = Icons.Outlined.Person
        )
    )


    // =================================================
    // SCAFFOLD
    // =================================================

    Scaffold(

        containerColor = PathBackground,

        // =================================================
        // TOP APP BAR
        // =================================================

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "PathPrakash",
                            color = PathBlue,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme
                                .typography
                                .titleLarge
                        )

                        Text(
                            text = "Admin Dashboard",
                            color = Color.Gray,
                            style = MaterialTheme
                                .typography
                                .bodySmall
                        )
                    }
                },

                actions = {

                    IconButton(
                        onClick = onLogout
                    ) {

                        Icon(
                            imageVector =
                                Icons.Outlined.Logout,

                            contentDescription =
                                "Logout",

                            tint =
                                PathBlue
                        )
                    }
                },

                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            Color.White
                    )
            )
        },


        // =================================================
        // BOTTOM BAR
        // =================================================

        bottomBar = {

            AdminBottomNavigationBar(

                selectedItem =
                    selectedBottomItem,

                onItemSelected = { item ->

                    selectedBottomItem = item

                    when (item) {

                        AdminBottomNavItem.HOME -> {
                            // Already on dashboard
                        }

                        AdminBottomNavItem.SCHOOLS -> {
                            onSchools()
                        }

                        AdminBottomNavItem.USERS -> {
                            onUsers()
                        }

                        AdminBottomNavItem.REPORTS -> {
                            onReports()
                        }

                        AdminBottomNavItem.PROFILE -> {
                            onProfile()
                        }
                    }
                }
            )
        }

    ) { paddingValues ->


        // =================================================
        // MAIN CONTENT
        // =================================================

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(
                modifier =
                    Modifier.height(18.dp)
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
                        Brush.linearGradient(
                            colors = listOf(
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
                        color = Color.White.copy(
                            alpha = 0.85f
                        ),
                        style = MaterialTheme
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
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme
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
                        color = Color.White.copy(
                            alpha = 0.90f
                        ),
                        style = MaterialTheme
                            .typography
                            .bodyMedium
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(20.dp)
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
                    Modifier.height(22.dp)
            )


            // =================================================
            // MANAGEMENT TITLE
            // =================================================

            Text(
                text = "Management",
                color = PathBlue,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme
                    .typography
                    .titleLarge
            )

            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )

            Text(
                text =
                    "Manage your PathPrakash platform",
                color = Color.Gray,
                style = MaterialTheme
                    .typography
                    .bodyMedium
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )


            // =================================================
            // MANAGEMENT GRID
            // =================================================

            LazyVerticalGrid(

                columns =
                    GridCells.Fixed(2),

                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),

                horizontalArrangement =
                    Arrangement.spacedBy(12.dp),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp),

                contentPadding =
                    PaddingValues(
                        bottom = 12.dp
                    )
            ) {

                items(
                    items = menuItems
                ) { item ->

                    AdminMenuCard(

                        item = item,

                        onClick = {

                            when (item.title) {

                                "Add School" -> {
                                    onAddSchool()
                                }

                                "Schools" -> {
                                    onSchools()
                                }

                                "Users" -> {
                                    onUsers()
                                }

                                "Admins" -> {
                                    onUsers()
                                }

                                "System Settings" -> {
                                    onSettings()
                                }

                                "Admin Profile" -> {
                                    onProfile()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


// =====================================================
// BOTTOM NAVIGATION BAR
// =====================================================

@Composable
private fun AdminBottomNavigationBar(

    selectedItem: AdminBottomNavItem,

    onItemSelected:
        (AdminBottomNavItem) -> Unit

) {

    /*
     * 100dp total height.
     *
     * White navigation surface = 76dp.
     *
     * Remaining space allows the selected
     * purple circle to float above the white bar.
     */

    Box(

        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(100.dp)
    ) {


        // =================================================
        // WHITE SURFACE
        // =================================================

        Surface(

            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp)
                .align(
                    Alignment.BottomCenter
                ),

            color = Color.White,

            shape =
                RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ),

            shadowElevation = 10.dp
        ) {}


        // =================================================
        // NAVIGATION ROW
        // =================================================

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(
                    Alignment.BottomCenter
                )
                .padding(
                    horizontal = 8.dp
                ),

            horizontalArrangement =
                Arrangement.SpaceEvenly,

            verticalAlignment =
                Alignment.Bottom
        ) {

            AdminBottomNavItem.entries.forEach { item ->

                AdminBottomNavigationItem(

                    item = item,

                    selected =
                        item == selectedItem,

                    onClick = {
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}


// =====================================================
// BOTTOM NAVIGATION ITEM
// =====================================================

@Composable
private fun AdminBottomNavigationItem(

    item: AdminBottomNavItem,

    selected: Boolean,

    onClick: () -> Unit

) {

    Box(

        modifier = Modifier
            .width(72.dp)
            .height(100.dp)
            .clickable {
                onClick()
            },

        contentAlignment =
            Alignment.BottomCenter
    ) {

        if (selected) {

            // =================================================
            // SELECTED ITEM
            // =================================================

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = 5.dp
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Bottom
            ) {

                // ---------------------------------------------
                // PURPLE ACTIVE CIRCLE
                // ---------------------------------------------

                Box(

                    modifier = Modifier
                        .size(50.dp)
                        .offset(
                            y = (-5).dp
                        )
                        .clip(
                            CircleShape
                        )
                        .background(
                            BottomPurple
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
                            Color.White,

                        modifier =
                            Modifier.size(27.dp)
                    )
                }


                // ---------------------------------------------
                // ACTIVE TEXT
                // ---------------------------------------------

                Text(

                    text =
                        item.title,

                    color =
                        BottomPurple,

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        12.sp,

                    maxLines = 1
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )
            }

        } else {

            // =================================================
            // UNSELECTED ITEM
            // =================================================

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = 7.dp
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Bottom
            ) {

                Icon(

                    imageVector =
                        item.icon,

                    contentDescription =
                        item.title,

                    tint =
                        Color(0xFF64748B),

                    modifier =
                        Modifier.size(25.dp)
                )

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Text(

                    text =
                        item.title,

                    color =
                        Color(0xFF64748B),

                    fontWeight =
                        FontWeight.Medium,

                    fontSize =
                        11.sp,

                    maxLines = 1
                )
            }
        }
    }
}


// =====================================================
// DASHBOARD STAT CARD
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

                    text =
                        value,

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

                text =
                    title,

                color =
                    PathBlue,

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

    /*
     * IMPORTANT:
     *
     * We are NOT using:
     *
     * Card(onClick = onClick)
     *
     * because some Material3 versions can produce:
     *
     * "No value passed for parameter 'content'"
     *
     * Instead we use normal Card + clickable modifier.
     */

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .height(145.dp)
            .clickable {
                onClick()
            },

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

            // =================================================
            // ICON
            // =================================================

            Box(

                modifier = Modifier
                    .size(48.dp)
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
                        Modifier.size(27.dp)
                )
            }


            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )


            // =================================================
            // TITLE
            // =================================================

            Text(

                text =
                    item.title,

                color =
                    PathBlue,

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


            // =================================================
            // SUBTITLE
            // =================================================

            Text(

                text =
                    item.subtitle,

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