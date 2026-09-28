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

    private val auth = FirebaseAuth.getInstance()

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

            // Firebase Authentication
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


            // Get user profile from Firestore
            val userDocument = firestore
                .collection("users")
                .document(uid)
                .get()
                .await()


            // User document does not exist
            if (!userDocument.exists()) {

                auth.signOut()

                return Result.failure(
                    Exception(
                        "Your PathPrakash account is not registered."
                    )
                )
            }


            // Convert Firestore document
            // into UserProfile
            val profile =
                userDocument.toObject(
                    UserProfile::class.java
                )
                    ?: return Result.failure(
                        Exception(
                            "Invalid user profile."
                        )
                    )


            // Check account status
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


            // Google ID option
            val googleIdOption =
                GetGoogleIdOption.Builder()
//
//                    .setServerClientId(
//                        context.getString(
//                            R.string.default_web_client_id
//                        )
//                    )

                    .setFilterByAuthorizedAccounts(false)

                    .build()


            // Credential request
            val request =
                GetCredentialRequest.Builder()

                    .addCredentialOption(
                        googleIdOption
                    )

                    .build()


            // Show Google account selector
            val result =
                credentialManager.getCredential(
                    context,
                    request
                )


            val credential =
                result.credential


            // Check Google credential
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


                // Convert Google credential
                // into Firebase credential
                val firebaseCredential =
                    GoogleAuthProvider.getCredential(
                        googleCredential.idToken,
                        null
                    )


                // Firebase login
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


                // Get Firestore profile
                val userDocument =
                    firestore
                        .collection("users")
                        .document(uid)
                        .get()
                        .await()


                // Google account authenticated,
                // but not registered in PathPrakash
                if (!userDocument.exists()) {

                    auth.signOut()

                    return Result.failure(
                        Exception(
                            "This Google account is not registered in PathPrakash."
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


                // Check account status
                if (!profile.isActive) {

                    auth.signOut()

                    return Result.failure(
                        Exception(
                            "Your PathPrakash account is inactive."
                        )
                    )
                }


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
    // SIGN OUT
    // =====================================================

    fun signOut() {

        auth.signOut()
    }
}