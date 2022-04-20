package com.programmersbox.teachingcompose

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

sealed class Screen(val route: String, val name: String) {
    object MainScreen : Screen("mainscreen", "Playground")
    object StateScreen : Screen("state", "State")
    object ModifierScreen : Screen("modifier", "Modifiers")
    object SideEffectsScreen : Screen("sideeffects", "Side Effects")
    object AnimationScreen : Screen("animation", "Animation")
    object OtherScreen : Screen("other", "Other")

    companion object {
        val items = arrayOf(
            StateScreen,
            ModifierScreen,
            SideEffectsScreen,
            AnimationScreen,
            OtherScreen
        )
    }
}

fun Random.nextColor(
    r: Int = nextInt(0, 255),
    g: Int = nextInt(0, 255),
    b: Int = nextInt(0, 255)
) = Color(r, g, b, 255)