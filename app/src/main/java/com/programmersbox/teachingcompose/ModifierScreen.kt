package com.programmersbox.teachingcompose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.programmersbox.teachingcompose.ui.theme.TeachingComposeTheme

@Composable
fun ModifierScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modifier") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, null) } },
            )
        }
    ) { p ->
        /**
         * Modifiers are like attributes to the View class! Anything that takes in a modifier, can be clickable, have padding, and MUCH more!
         */
        LazyColumn(
            contentPadding = p,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            /**
             * There are a lot of built in modifiers, like View attributes!
             * This is DEFINITELY something to look into since there are FAR too many to go over.
             */
            item {
                Box(
                    Modifier
                        .background(Color.Blue)
                        .size(40.dp)
                ) { Text("I'm a Box!") }
            }

            /**
             * Pay close attention to the difference between these two. They look identical, except that clickable and padding are swapped.
             *
             * THAT IS IMPORTANT!
             *
             * When running it, you should notice that with the first one, the Card INCLUDING the padding is all clickable.
             * And in the second one, ONLY the Card is clickable.
             */
            item {
                Card(
                    Modifier
                        .clickable { }
                        .padding(4.dp)
                        .fillMaxWidth()
                ) { Text("ORDER MATTERS!", style = MaterialTheme.typography.h4) }
            }

            item {
                Card(
                    Modifier
                        .padding(4.dp)
                        .clickable { }
                        .fillMaxWidth()
                ) { Text("ORDER MATTERS!", style = MaterialTheme.typography.h4) }
            }
        }
    }
}

@Composable
@Preview(device = Devices.PIXEL_4_XL)
@Preview(device = Devices.PIXEL_4_XL, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun ModifierPreview() {
    TeachingComposeTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            ModifierScreen(navController = rememberNavController())
        }
    }
}