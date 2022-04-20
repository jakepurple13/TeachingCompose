package com.programmersbox.teachingcompose

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.programmersbox.teachingcompose.ui.theme.TeachingComposeTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun SideEffectsScreen(navController: NavController) {
    val state = rememberScaffoldState()

    /**
     * CoroutineScope! Quickly allows you to get a CoroutineScope so you can run suspend functions!
     * Compose is very coroutine based!
     */
    val scope = rememberCoroutineScope()

    val listState = rememberLazyListState()

    /**
     * derivedStateOf: convert one or multiple state objects into another state
     */
    val listOnScreen by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    /**
     * A SideEffect is a change to the state of the app outside the scope of a composable function
     *
     * To restart a SideEffect:
     * EffectName(restartIfThisKeyChanges, orThisKey, orThisKey, ...) { block }
     */
    Scaffold(
        scaffoldState = state,
        topBar = {
            TopAppBar(
                title = { Text("Side Effects") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) } },
            )
        },
        drawerContent = { Text("Here's a Drawer!") },
        bottomBar = {
            BottomAppBar {
                Button(onClick = { scope.launch { state.drawerState.open() } }) { Text("Open Drawer") }
                Text("First Visible Item: $listOnScreen")
            }
        }
    ) { p ->
        /**
         * SideEffect: publish Compose state to non-compose code
         * To share Compose state with objects not managed by compose, use the SideEffect composable,
         * as it's invoked on every successful recomposition.
         */
        SideEffect { println("Hello!") }
        LazyColumn(
            state = listState,
            contentPadding = p,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                val currentTime by currentTime()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Text("Time: ${SimpleDateFormat.getDateTimeInstance().format(currentTime)}")
                } else {
                    Text("Time: ${java.text.SimpleDateFormat.getDateTimeInstance().format(currentTime)}")
                }
            }

            items(50) { Text("$it") }
        }
    }

    /**
     * LaunchedEffect: run suspend functions in the scope of a composable
     */
    LaunchedEffect(listState) {
        /**
         * snapshotFlow: convert Compose's State into Flows
         */
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { it.toString() }
            .collect { println(it) }
    }
}

@Composable
fun currentTime(): State<Long> {
    return broadcastReceiver(
        defaultValue = System.currentTimeMillis(),
        intentFilter = IntentFilter(Intent.ACTION_TIME_TICK),
        tick = { _, _ -> System.currentTimeMillis() }
    )
}

@Composable
fun <T : Any> broadcastReceiver(defaultValue: T, intentFilter: IntentFilter, tick: (context: Context, intent: Intent) -> T): State<T> {
    val item: MutableState<T> = remember { mutableStateOf(defaultValue) }
    val context = LocalContext.current
    /**
     * DisposableEffect: effects that require cleanup
     * This will dispose and re-register when context updates.
     */
    DisposableEffect(context) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                item.value = tick(context, intent)
            }
        }
        context.registerReceiver(receiver, intentFilter)
        onDispose { context.unregisterReceiver(receiver) }
    }
    return item
}

@Composable
@Preview(device = Devices.PIXEL_4_XL)
@Preview(device = Devices.PIXEL_4_XL, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun SideEffectsPreview() {
    TeachingComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            SideEffectsScreen(navController = rememberNavController())
        }
    }
}