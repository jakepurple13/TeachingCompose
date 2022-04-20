package com.programmersbox.teachingcompose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.programmersbox.teachingcompose.ui.theme.TeachingComposeTheme
import kotlin.random.Random

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Animations") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) } },
            )
        }
    ) { p ->
        LazyColumn(
            contentPadding = p,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                Text("AnimateColorAsState")

                val colors = MaterialTheme.colors

                var color by remember { mutableStateOf(colors.primary) }

                Button(
                    onClick = { color = Random.nextColor() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = animateColorAsState(color).value),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Click to change Color") }

            }

            item {
                Divider()
                Text("AnimatedVisibility")

                var showOrHide by remember { mutableStateOf(true) }
                val density = LocalDensity.current

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.toggleable(
                        value = showOrHide,
                        role = Role.Checkbox,
                        onValueChange = { showOrHide = !showOrHide }
                    )
                ) {
                    Checkbox(checked = showOrHide, onCheckedChange = null)
                    Text("Animate Visibility")
                }

                AnimatedVisibility(
                    visible = showOrHide,
                    enter = slideInVertically {
                        // Slide in from 40 dp from the top.
                        with(density) { -40.dp.roundToPx() }
                    } + expandVertically(
                        // Expand from the top.
                        expandFrom = Alignment.Top
                    ) + fadeIn(
                        // Fade in with the initial alpha of 0.3f.
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) { Text(text = "Edit") }

                AnimatedVisibility(
                    visible = showOrHide,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    // Fade in/out the background and the foreground.
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray)
                    ) {
                        Box(
                            Modifier
                                .align(Alignment.Center)
                                .animateEnterExit(
                                    // Slide in/out the inner box.
                                    enter = slideInVertically(),
                                    exit = slideOutVertically()
                                )
                                .sizeIn(minWidth = 256.dp, minHeight = 64.dp)
                                .background(Color.Red)
                        ) {
                            // Content of the notification…
                            Text("More Animation!")
                        }
                    }
                }
            }

            item {
                Divider()
                Text("AnimatedContent")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var count by remember { mutableStateOf(0) }
                    IconButton(onClick = { count-- }) { Icon(Icons.Default.RemoveCircle, null) }
                    AnimatedContent(
                        targetState = count,
                        transitionSpec = {
                            // Compare the incoming number with the previous number.
                            if (targetState > initialState) {
                                // If the target number is larger, it slides up and fades in
                                // while the initial (smaller) number slides up and fades out.
                                slideInVertically { height -> height } + fadeIn() with
                                        slideOutVertically { height -> -height } + fadeOut()
                            } else {
                                // If the target number is smaller, it slides down and fades in
                                // while the initial number slides down and fades out.
                                slideInVertically { height -> -height } + fadeIn() with
                                        slideOutVertically { height -> height } + fadeOut()
                            }.using(
                                // Disable clipping since the faded slide-in/out should
                                // be displayed out of bounds.
                                SizeTransform(clip = false)
                            )
                        }
                    ) { targetCount ->
                        // Make sure to use `targetCount`, not `count`.
                        Text("Count: $targetCount")
                    }
                    IconButton(onClick = { count++ }) { Icon(Icons.Default.AddCircle, null) }
                }
            }

            item {
                Divider()
                Text("AnimateContentSize")
                var message by remember { mutableStateOf("") }

                Button(
                    onClick = { message += "!!!!!!!!!!" },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Add !") }

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(Color.Blue)
                        .animateContentSize()
                ) { Text(stringResource(id = R.string.string_test, message)) }

            }

        }
    }
}

@Composable
@Preview(device = Devices.PIXEL_4_XL)
@Preview(device = Devices.PIXEL_4_XL, uiMode = UI_MODE_NIGHT_YES)
fun AnimationPreview() {
    TeachingComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            AnimationScreen(navController = rememberNavController())
        }
    }
}