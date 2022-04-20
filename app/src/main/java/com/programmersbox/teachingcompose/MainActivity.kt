package com.programmersbox.teachingcompose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.programmersbox.teachingcompose.ui.theme.TeachingComposeTheme
import io.reactivex.subjects.BehaviorSubject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TeachingComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
                        composable(Screen.MainScreen.route) { TeachingScreen(navController) }
                        composable(Screen.StateScreen.route) { State(navController) }
                        composable(Screen.ModifierScreen.route) { ModifierScreen(navController) }
                        composable(Screen.SideEffectsScreen.route) { SideEffectsScreen(navController) }
                        composable(Screen.AnimationScreen.route) { AnimationScreen(navController) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TeachingScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teaching Compose") },
                navigationIcon = {
                    Icon(
                        painterResource(R.drawable.ic_baseline_adb_24),
                        null,
                        tint = colorResource(id = R.color.teal_200)
                    )
                }
            )
        }
    ) { p ->
        LazyColumn(
            contentPadding = p,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(Screen.items) {
                OutlinedButton(
                    onClick = { navController.navigate(it.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp)
                ) { Text(it.name) }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TeachingComposeTheme {
        TeachingScreen(rememberNavController())
    }
}