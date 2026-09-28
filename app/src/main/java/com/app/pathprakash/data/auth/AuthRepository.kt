package com.app.pathprakash.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.app.pathprakash.R
import com.app.pathprakash.domain.model.UserProfile
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val context: Context
) {

    private val auth =
        FirebaseAuth.getInstance()

    private val firestore =
        FirebaseFirestore.getInstance()


    // =====================================================
    // EMAIL + PASSWORD LOGIN
    // =====================================================

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<UserProfile> {

        return try {

            auth.signInWithEmailAndPassword(
                email.trim(),
                password
            ).await()

            val firebaseUser =
                auth.currentUser
                    ?: return Result.failure(
                        Exception(
                            "Firebase user not found."
                        )
                    )

            val uid = firebaseUser.uid

            val userDocument =
                firestore
                    .collection("users")
                    .document(uid)
                    .get()
                    .await()

            if (!userDocument.exists()) {

                auth.signOut()

                return Result.failure(
                    Exception(
                        "Your PathPrakash account is not registered."
                    )
                )
            }

            val profile =
                userDocument.toObject(
                    UserProfile::class.java
                )
                    ?: return Result.failure(
                        Exception(
                            "Invalid user profile."
                        )
                    )

            if (!profile.isActive) {

                auth.signOut()

                return Result.failure(
                    Exception(
                        "Your PathPrakash account is inactive."
                    )
                )
            }

            Result.success(profile)

        } catch (e: Exception) {

            auth.signOut()

            Result.failure(
                Exception(
                    e.message ?: "Email login failed."
                )
            )
        }
    }


    // =====================================================
    // GOOGLE LOGIN
    // =====================================================

    suspend fun signInWithGoogle(): Result<UserProfile> {

        return try {

            val credentialManager =
                CredentialManager.create(context)

            val googleIdOption =
                GetGoogleIdOption.Builder()
                    .setServerClientId(
                        context.getString(
                            R.string.default_web_client_id
                        )
                    )
                    .setFilterByAuthorizedAccounts(false)
                    .build()

            val request =
                GetCredentialRequest.Builder()
                    .addCredentialOption(
                        googleIdOption
                    )
                    .build()

            val result =
                credentialManager.getCredential(
                    context,
                    request
                )

            val credential =
                result.credential

            if (
                credential is CustomCredential &&
                credential.type ==
                GoogleIdTokenCredential
                    .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {

                val googleCredential =
                    try {

                        GoogleIdTokenCredential
                            .createFrom(
                                credential.data
                            )

                    } catch (
                        e: GoogleIdTokenParsingException
                    ) {

                        return Result.failure(
                            Exception(
                                "Invalid Google credential."
                            )
                        )
                    }

                val firebaseCredential =
                    GoogleAuthProvider.getCredential(
                        googleCredential.idToken,
                        null
                    )

                // -----------------------------------------
                // Sign in with Google to Firebase
                // -----------------------------------------

                auth.signInWithCredential(
                    firebaseCredential
                ).await()

                val firebaseUser =
                    auth.currentUser
                        ?: return Result.failure(
                            Exception(
                                "Firebase user not found."
                            )
                        )

                val uid =
                    firebaseUser.uid

                // -----------------------------------------
                // Check PathPrakash users collection
                // -----------------------------------------

                val userDocument =
                    firestore
                        .collection("users")
                        .document(uid)
                        .get()
                        .await()

                // -----------------------------------------
                // Google account not registered
                // -----------------------------------------

                if (!userDocument.exists()) {

                    auth.signOut()

                    return Result.failure(
                        Exception(
                            "This email is not registered in PathPrakash."
                        )
                    )
                }

                // -----------------------------------------
                // Convert Firestore document to profile
                // -----------------------------------------

                val profile =
                    userDocument.toObject(
                        UserProfile::class.java
                    )

                if (profile == null) {

                    auth.signOut()

                    return Result.failure(
                        Exception(
                            "Invalid PathPrakash user profile."
                        )
                    )
                }

                // -----------------------------------------
                // Check account status
                // -----------------------------------------

                if (!profile.isActive) {

                    auth.signOut()

                    return Result.failure(
                        Exception(
                            "Your PathPrakash account is inactive."
                        )
                    )
                }

                // -----------------------------------------
                // Everything is valid
                // -----------------------------------------

                Result.success(profile)

            } else {

                Result.failure(
                    Exception(
                        "Google account credential not found."
                    )
                )
            }

        } catch (e: Exception) {

            auth.signOut()

            Result.failure(
                Exception(
                    e.message
                        ?: "Google login failed."
                )
            )
        }
    }


    // =====================================================
    // RESTORE EXISTING LOGIN SESSION
    // =====================================================

    suspend fun getCurrentUserProfile():
            Result<UserProfile?> {

        return try {

            val firebaseUser =
                auth.currentUser
                    ?: return Result.success(null)

            val uid =
                firebaseUser.uid

            val userDocument =
                firestore
                    .collection("users")
                    .document(uid)
                    .get()
                    .await()

            if (!userDocument.exists()) {

                auth.signOut()

                return Result.failure(
                    Exception(
                        "User profile not found."
                    )
                )
            }

            val profile =
                userDocument.toObject(
                    UserProfile::class.java
                )
                    ?: return Result.failure(
                        Exception(
                            "Invalid user profile."
                        )
                    )

            if (!profile.isActive) {

                auth.signOut()

                return Result.failure(
                    Exception(
                        "Your PathPrakash account is inactive."
                    )
                )
            }

            Result.success(profile)

        } catch (e: Exception) {

            Result.failure(
                Exception(
                    e.message
                        ?: "Unable to restore login session."
                )
            )
        }
    }


    // =====================================================
    // SIGN OUT
    // =====================================================

    fun signOut() {

        auth.signOut()
    }
}