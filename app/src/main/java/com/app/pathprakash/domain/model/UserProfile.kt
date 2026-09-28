package com.app.pathprakash.domain.model

data class UserProfile(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val role: String = "",
    val schoolId: String? = null,
    val profileId: String = "",
    val isActive: Boolean = true
)