package com.willbsp.habits.ui.home

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.willbsp.habits.ui.theme.HabitsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToAddHabit: () -> Unit
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddHabit,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add item" // TODO strings.xml
                )
            }
        }
    ) { innerPadding -> // TODO

        Text(
            text = "hi",
            modifier = modifier.padding(innerPadding)
        )

    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HabitsTheme() {
        HomeScreen() {}
    }
}