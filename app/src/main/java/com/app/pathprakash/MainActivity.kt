package com.app.pathprakash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.app.pathprakash.navigation.AppNavigation
import com.app.pathprakash.ui.theme.PathPrakashTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            PathPrakashTheme {
                AppNavigation()
            }
        }
    }
}