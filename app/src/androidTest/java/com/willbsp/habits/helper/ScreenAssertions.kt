package com.willbsp.habits.helper

import androidx.navigation.NavController
import org.junit.Assert.assertEquals

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}

fun NavController.assertCurrentRouteName(expectedRouteName: String, args: String) {
    assertEquals(expectedRouteName + "{$args}", currentBackStackEntry?.destination?.route)
}
