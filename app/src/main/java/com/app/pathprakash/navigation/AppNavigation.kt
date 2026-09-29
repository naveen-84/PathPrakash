package com.app.pathprakash.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.app.pathprakash.ui.admin.AddSchoolScreen
import com.app.pathprakash.ui.admin.AdminDashboardScreen
import com.app.pathprakash.ui.admin.AdminProfileScreen
import com.app.pathprakash.ui.admin.SchoolsScreen
import com.app.pathprakash.ui.admin.system.SystemSettingsScreen
import com.app.pathprakash.ui.admin.system.AcademicDefaultsScreen
import com.app.pathprakash.ui.admin.system.AuditLogsScreen
import com.app.pathprakash.ui.admin.system.GeneralSettingsScreen
import com.app.pathprakash.ui.admin.system.MaintenanceSettingsScreen
import com.app.pathprakash.ui.admin.system.NotificationSettingsScreen
import com.app.pathprakash.ui.admin.system.SchoolManagementSettingsScreen
import com.app.pathprakash.ui.admin.system.SecuritySettingsScreen
import com.app.pathprakash.ui.admin.system.StorageSettingsScreen
import com.app.pathprakash.ui.admin.system.SubscriptionSettingsScreen
import com.app.pathprakash.ui.admin.system.UserRoleSettingsScreen

import com.app.pathprakash.ui.auth.LoginScreen
import com.app.pathprakash.viewmodel.AuthViewModel


// =====================================================
// APP NAVIGATION
// =====================================================

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel = viewModel()
) {

    // =================================================
    // NAV CONTROLLER
    // =================================================

    val navController = rememberNavController()


    // =================================================
    // AUTH STATE
    // =================================================

    val isLoading by
    authViewModel.isLoading.collectAsState()

    val errorMessage by
    authViewModel.errorMessage.collectAsState()

    val userProfile by
    authViewModel.userProfile.collectAsState()

    val isCheckingSession by
    authViewModel.isCheckingSession.collectAsState()


    // =================================================
    // SESSION RESTORE
    // =================================================

    LaunchedEffect(
        isCheckingSession,
        userProfile
    ) {

        // Session check complete hone ke baad hi
        // navigation perform hogi.

        if (!isCheckingSession) {

            // =================================================
            // USER LOGGED IN
            // =================================================

            if (userProfile != null) {

                val profile = userProfile!!


                // =================================================
                // ROLE BASED NAVIGATION
                // =================================================

                when (
                    profile.role
                        .trim()
                        .lowercase()
                ) {

                    // =========================================
                    // SUPER ADMIN
                    // =========================================

                    "admin",
                    "super_admin" -> {

                        navController.navigate(
                            Routes.SuperAdminDashboard.route
                        ) {

                            popUpTo(
                                Routes.Splash.route
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }


                    // =========================================
                    // SCHOOL ADMIN
                    // =========================================

                    "school_admin" -> {

                        navController.navigate(
                            Routes.SchoolAdminDashboard.route
                        ) {

                            popUpTo(
                                Routes.Splash.route
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }


                    // =========================================
                    // TEACHER
                    // =========================================

                    "teacher" -> {

                        navController.navigate(
                            Routes.TeacherDashboard.route
                        ) {

                            popUpTo(
                                Routes.Splash.route
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }


                    // =========================================
                    // PARENT
                    // =========================================

                    "parent" -> {

                        navController.navigate(
                            Routes.ParentDashboard.route
                        ) {

                            popUpTo(
                                Routes.Splash.route
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }


                    // =========================================
                    // STUDENT
                    // =========================================

                    "student" -> {

                        navController.navigate(
                            Routes.StudentDashboard.route
                        ) {

                            popUpTo(
                                Routes.Splash.route
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }


                    // =========================================
                    // UNKNOWN ROLE
                    // =========================================

                    else -> {

                        authViewModel.logout()

                        navController.navigate(
                            Routes.Login.route
                        ) {

                            popUpTo(
                                Routes.Splash.route
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                }

            } else {

                // =================================================
                // USER NOT LOGGED IN
                // =================================================

                navController.navigate(
                    Routes.Login.route
                ) {

                    popUpTo(
                        Routes.Splash.route
                    ) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            }
        }
    }


    // =====================================================
    // NAVIGATION HOST
    // =====================================================

    NavHost(

        navController = navController,

        startDestination = Routes.Splash.route

    ) {


        // =================================================
        // SPLASH
        // =================================================

        composable(
            Routes.Splash.route
        ) {

            SessionLoadingScreen()
        }


        // =================================================
        // LOGIN
        // =================================================

        composable(
            Routes.Login.route
        ) {

            LoginScreen(

                onLogin = {
                        email,
                        password,
                        _ ->

                    /*
                     * Login screen se selected role
                     * authorization ke liye use nahi ho raha.
                     *
                     * Actual role Firestore users/{uid}
                     * se liya jayega.
                     */

                    authViewModel.loginWithEmail(

                        email = email,

                        password = password
                    )
                },


                onGoogleLogin = {

                    authViewModel.loginWithGoogle()
                },


                onForgotPassword = {

                    // Firebase password reset
                    // later implement karenge.

                },


                isLoading =
                    isLoading,


                errorMessage =
                    errorMessage
            )
        }


        // =================================================
        // SUPER ADMIN DASHBOARD
        // =================================================

        composable(
            Routes.SuperAdminDashboard.route
        ) {

            AdminDashboardScreen(

                // ---------------------------------------------
                // ADMIN NAME
                // ---------------------------------------------

                adminName =
                    userProfile
                        ?.name
                        ?.ifBlank {
                            "Admin"
                        }
                        ?: "Admin",


                // ---------------------------------------------
                // ADD SCHOOL
                // ---------------------------------------------

                onAddSchool = {

                    navController.navigate(
                        Routes.AddSchool.route
                    )
                },


                // ---------------------------------------------
                // SCHOOLS
                // ---------------------------------------------

                onSchools = {

                    navController.navigate(
                        Routes.Schools.route
                    )
                },


                // ---------------------------------------------
                // SETTINGS
                // ---------------------------------------------

                onSettings = {

                    navController.navigate(
                        Routes.SystemSettings.route
                    )
                },


                // ---------------------------------------------
                // USERS
                // ---------------------------------------------

                onUsers = {

                    /*
                     * Users screen baad mein implement karenge.
                     */

                },


                // ---------------------------------------------
                // REPORTS
                // ---------------------------------------------

                onReports = {

                    /*
                     * Reports screen baad mein implement karenge.
                     */

                },


                // ---------------------------------------------
                // PROFILE
                // ---------------------------------------------

                onProfile = {

                    navController.navigate(
                        Routes.AdminProfile.route
                    ) {

                        launchSingleTop = true
                    }
                },


                // ---------------------------------------------
                // LOGOUT
                // ---------------------------------------------

                onLogout = {

                    authViewModel.logout()

                    navController.navigate(
                        Routes.Login.route
                    ) {

                        popUpTo(0) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }


        // =================================================
        // ADD SCHOOL
        // =================================================

        composable(
            Routes.AddSchool.route
        ) {

            AddSchoolScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // SCHOOLS
        // =================================================

        composable(
            Routes.Schools.route
        ) {

            SchoolsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // ADMIN PROFILE
        // =================================================

        composable(
            Routes.AdminProfile.route
        ) {

            AdminProfileScreen(

                // ---------------------------------------------
                // NAME
                // ---------------------------------------------

                adminName =
                    userProfile
                        ?.name
                        ?.ifBlank {
                            "PathPrakash Admin"
                        }
                        ?: "PathPrakash Admin",


                // ---------------------------------------------
                // EMAIL
                // ---------------------------------------------

                adminEmail =
                    userProfile
                        ?.email
                        ?: "",


                // ---------------------------------------------
                // FIREBASE UID
                // ---------------------------------------------

                adminUid =
                    userProfile
                        ?.uid
                        ?: "",


                // ---------------------------------------------
                // BACK
                // ---------------------------------------------

                onBack = {

                    navController.popBackStack()
                },


                // ---------------------------------------------
                // LOGOUT
                // ---------------------------------------------

                onLogout = {

                    authViewModel.logout()

                    navController.navigate(
                        Routes.Login.route
                    ) {

                        popUpTo(0) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            )
        }


        // =================================================
        // SYSTEM SETTINGS
        // =================================================

        composable(
            Routes.SystemSettings.route
        ) {

            SystemSettingsScreen(

                // ---------------------------------------------
                // BACK
                // ---------------------------------------------

                onBack = {

                    navController.popBackStack()
                },


                // ---------------------------------------------
                // GENERAL
                // ---------------------------------------------

                onGeneral = {

                    navController.navigate(
                        Routes.GeneralSettings.route
                    )
                },


                // ---------------------------------------------
                // USERS & ROLES
                // ---------------------------------------------

                onUsersRoles = {

                    navController.navigate(
                        Routes.UserRoleSettings.route
                    )
                },


                // ---------------------------------------------
                // SCHOOL MANAGEMENT
                // ---------------------------------------------

                onSchoolManagement = {

                    navController.navigate(
                        Routes.SchoolManagementSettings.route
                    )
                },


                // ---------------------------------------------
                // NOTIFICATIONS
                // ---------------------------------------------

                onNotifications = {

                    navController.navigate(
                        Routes.NotificationSettings.route
                    )
                },


                // ---------------------------------------------
                // SECURITY
                // ---------------------------------------------

                onSecurity = {

                    navController.navigate(
                        Routes.SecuritySettings.route
                    )
                },


                // ---------------------------------------------
                // STORAGE
                // ---------------------------------------------

                onStorage = {

                    navController.navigate(
                        Routes.StorageSettings.route
                    )
                },


                // ---------------------------------------------
                // ACADEMIC DEFAULTS
                // ---------------------------------------------

                onAcademicDefaults = {

                    navController.navigate(
                        Routes.AcademicDefaults.route
                    )
                },


                // ---------------------------------------------
                // SUBSCRIPTION
                // ---------------------------------------------

                onSubscription = {

                    navController.navigate(
                        Routes.SubscriptionSettings.route
                    )
                },


                // ---------------------------------------------
                // MAINTENANCE
                // ---------------------------------------------

                onMaintenance = {

                    navController.navigate(
                        Routes.MaintenanceSettings.route
                    )
                },


                // ---------------------------------------------
                // AUDIT LOGS
                // ---------------------------------------------

                onAuditLogs = {

                    navController.navigate(
                        Routes.AuditLogs.route
                    )
                }
            )
        }


        // =================================================
        // GENERAL SETTINGS
        // =================================================

        composable(
            Routes.GeneralSettings.route
        ) {

            GeneralSettingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // USER ROLE SETTINGS
        // =================================================

        composable(
            Routes.UserRoleSettings.route
        ) {

            UserRoleSettingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // SCHOOL MANAGEMENT SETTINGS
        // =================================================

        composable(
            Routes.SchoolManagementSettings.route
        ) {

            SchoolManagementSettingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // NOTIFICATION SETTINGS
        // =================================================

        composable(
            Routes.NotificationSettings.route
        ) {

            NotificationSettingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // SECURITY SETTINGS
        // =================================================

        composable(
            Routes.SecuritySettings.route
        ) {

            SecuritySettingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // STORAGE SETTINGS
        // =================================================

        composable(
            Routes.StorageSettings.route
        ) {

            StorageSettingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // ACADEMIC DEFAULTS
        // =================================================

        composable(
            Routes.AcademicDefaults.route
        ) {

            AcademicDefaultsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // SUBSCRIPTION SETTINGS
        // =================================================

        composable(
            Routes.SubscriptionSettings.route
        ) {

            SubscriptionSettingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // MAINTENANCE SETTINGS
        // =================================================

        composable(
            Routes.MaintenanceSettings.route
        ) {

            MaintenanceSettingsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // AUDIT LOGS
        // =================================================

        composable(
            Routes.AuditLogs.route
        ) {

            AuditLogsScreen(

                onBack = {

                    navController.popBackStack()
                }
            )
        }


        // =================================================
        // SCHOOL ADMIN DASHBOARD
        // =================================================

        composable(
            Routes.SchoolAdminDashboard.route
        ) {

            SimpleAdminScreen(
                title = "School Admin Dashboard"
            )
        }


        // =================================================
        // TEACHER DASHBOARD
        // =================================================

        composable(
            Routes.TeacherDashboard.route
        ) {

            SimpleAdminScreen(
                title = "Teacher Dashboard"
            )
        }


        // =================================================
        // PARENT DASHBOARD
        // =================================================

        composable(
            Routes.ParentDashboard.route
        ) {

            SimpleAdminScreen(
                title = "Parent Dashboard"
            )
        }


        // =================================================
        // STUDENT DASHBOARD
        // =================================================

        composable(
            Routes.StudentDashboard.route
        ) {

            SimpleAdminScreen(
                title = "Student Dashboard"
            )
        }
    }
}


// =====================================================
// SESSION LOADING SCREEN
// =====================================================

@Composable
private fun SessionLoadingScreen() {

    Column(

        modifier =
            Modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center

    ) {

        CircularProgressIndicator()
    }
}


// =====================================================
// TEMPORARY SCREEN
// =====================================================

@Composable
private fun SimpleAdminScreen(
    title: String
) {

    Column(

        modifier =
            Modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center

    ) {

        Text(

            text = title,

            style =
                MaterialTheme
                    .typography
                    .headlineMedium
        )
    }
}