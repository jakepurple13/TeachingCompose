package com.programmersbox.teachingcompose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtherScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Other") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) } },
            )
        }
    ) { p ->
        ConstraintLayout(modifier = Modifier.padding(p)) {

            val (image, button) = createRefs()
            val scope = rememberCoroutineScope()
            var refresh by remember { mutableStateOf(false) }

            AsyncImage(
                model = if (refresh) null else "https://picsum.photos/200.jpg",
                contentDescription = null,
                modifier = Modifier.constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(button.top)
                }
            )

            Button(
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        refresh = true
                        delay(1000)
                        refresh = false
                    }
                },
                modifier = Modifier.constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            ) { Text("Refresh Image") }

        }
    }
}