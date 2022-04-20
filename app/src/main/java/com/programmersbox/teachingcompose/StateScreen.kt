package com.programmersbox.teachingcompose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.rxjava2.subscribeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.programmersbox.teachingcompose.ui.theme.TeachingComposeTheme
import io.reactivex.subjects.BehaviorSubject

@Composable
fun State(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("State") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) } },
            )
        }
    ) { p ->
        Column(
            modifier = Modifier
                .padding(p)
                .verticalScroll(rememberScrollState())
        ) {
            /**When this changes, ONLY Checkbox will change*/
            var check by remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = check, onCheckedChange = { check = it })
                Text("Checkbox")
            }

            Button(onClick = { check = !check }) { Text("Change Me!") }

            /**You can ALSO not use delegates and you'll have a MutableState variable instead*/
            val checkState = remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checkState.value, onCheckedChange = { checkState.value = it })
                Text("State Checkbox")
            }

            /**You can also deconstruct a MutableState to give the value and when it changes*/
            val (checkState2, onCheckStateChange) = remember { mutableStateOf(false) }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checkState2, onCheckedChange = onCheckStateChange)
                Text("State Checkbox")
            }

            /**If you don't use a state, things won't update*/
            var check2 = remember { false }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = check2, onCheckedChange = { check2 = it })
                Text("No States Checkbox")
            }

            /**If you don't use remember, but use a mutableStateOf, things won't update either*/
            var check3 by mutableStateOf(false)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = check3, onCheckedChange = { check3 = it })
                Text("No Remember Checkbox")
            }

            /**
             * An example of using RxJava

            ```
            @Composable
            fun <R, T : R> Observable<T>.subscribeAsState(initial: R): State<R> = asState(initial) { subscribe(it) }

            @Composable
            private inline fun <T, S> S.asState(
                initial: T,
                crossinline subscribe: S.((T) -> Unit) -> Disposable
            ): State<T> {
                val state = remember { mutableStateOf(initial) }
                DisposableEffect(this) {
                    val disposable = subscribe {
                        state.value = it
                    }
                    onDispose { disposable.dispose() }
                }
                return state
            }
             ```
             */
            val behavior = remember { BehaviorSubject.create<Boolean>() }
            val behaviorCheck by behavior.subscribeAsState(initial = false)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = behaviorCheck, onCheckedChange = { behavior.onNext(it) })
                Text("RxJava Checkbox")
            }

            Divider()

            /**Using ViewModels follow the same idea, except we don't need to add the remember function*/
            val vm: StateViewModel = viewModel()

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = vm.check, onCheckedChange = { vm.check = it })
                Text("ViewModel Checkbox")
            }

            /**Similar to ViewModel except we use remember!*/
            val stateModel = remember { StateModel() }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = stateModel.check, onCheckedChange = { stateModel.check = it })
                Text("StateModel Checkbox")
            }

            /**
             * Taking the StateModel a step further, we can use remember's key to recompose when the key changes.
             * This is called State holders as source of truth
            */
            val stateModel2 = rememberStateModel(check)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = stateModel2.check, onCheckedChange = { stateModel2.check = it })
                Text("StateModel Checkbox")
            }
        }
    }
}

class StateViewModel : ViewModel() {
    var check by mutableStateOf(false)
}

@Composable
fun rememberStateModel(checkState: Boolean = false) = remember(checkState) { StateModel(checkState) }

class StateModel(checkState: Boolean = false) {
    var check by mutableStateOf(checkState)
}

@Composable
@Preview(device = Devices.PIXEL_4_XL)
@Preview(device = Devices.PIXEL_4_XL, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun StatePreview() {
    TeachingComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            State(navController = rememberNavController())
        }
    }
}