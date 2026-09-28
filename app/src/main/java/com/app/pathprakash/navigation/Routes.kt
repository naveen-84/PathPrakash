package com.app.pathprakash.navigation

sealed class Routes(
    val route: String
) {

    data object Splash :
        Routes("splash")

    data object Login :
        Routes("login")

    // Super Admin
    data object SuperAdminDashboard :
        Routes("super_admin_dashboard")

    data object AddSchool :
        Routes("add_school")

    data object Schools :
        Routes("schools")

    data object AdminSettings :
        Routes("admin_settings")

    // School Admin
    data object SchoolAdminDashboard :
        Routes("school_admin_dashboard")

    // Teacher
    data object TeacherDashboard :
        Routes("teacher_dashboard")

    // Parent
    data object ParentDashboard :
        Routes("parent_dashboard")

    // Student
    data object StudentDashboard :
        Routes("student_dashboard")
}