package com.app.pathprakash.navigation

sealed class Routes(
    val route: String
) {

    data object Splash : Routes("splash")

    data object Login : Routes("login")

    data object SuperAdminDashboard :
        Routes("super_admin_dashboard")

    data object SchoolAdminDashboard :
        Routes("school_admin_dashboard")

    data object TeacherDashboard :
        Routes("teacher_dashboard")

    data object ParentDashboard :
        Routes("parent_dashboard")

    data object StudentDashboard :
        Routes("student_dashboard")
}