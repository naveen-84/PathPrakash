package com.app.pathprakash.navigation

sealed class Routes(
    val route: String
) {

    // =====================================================
    // SPLASH
    // =====================================================

    data object Splash :
        Routes("splash")


    // =====================================================
    // LOGIN
    // =====================================================

    data object Login :
        Routes("login")


    // =====================================================
    // SUPER ADMIN
    // =====================================================

    data object SuperAdminDashboard :
        Routes("super_admin_dashboard")

    data object AddSchool :
        Routes("add_school")

    data object Schools :
        Routes("schools")

    data object AdminSettings :
        Routes("admin_settings")

    data object AdminProfile :
        Routes("admin_profile")


    // =====================================================
    // SYSTEM SETTINGS
    // =====================================================

    data object SystemSettings :
        Routes("system_settings")

    data object GeneralSettings :
        Routes("general_settings")

    data object UserRoleSettings :
        Routes("user_role_settings")

    data object SchoolManagementSettings :
        Routes("school_management_settings")

    data object NotificationSettings :
        Routes("notification_settings")

    data object SecuritySettings :
        Routes("security_settings")

    data object StorageSettings :
        Routes("storage_settings")

    data object AcademicDefaults :
        Routes("academic_defaults")

    data object SubscriptionSettings :
        Routes("subscription_settings")

    data object MaintenanceSettings :
        Routes("maintenance_settings")

    data object AuditLogs :
        Routes("audit_logs")


    // =====================================================
    // SCHOOL ADMIN
    // =====================================================

    data object SchoolAdminDashboard :
        Routes("school_admin_dashboard")


    // =====================================================
    // TEACHER
    // =====================================================

    data object TeacherDashboard :
        Routes("teacher_dashboard")


    // =====================================================
    // PARENT
    // =====================================================

    data object ParentDashboard :
        Routes("parent_dashboard")


    // =====================================================
    // STUDENT
    // =====================================================

    data object StudentDashboard :
        Routes("student_dashboard")
}