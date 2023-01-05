package com.willbsp.habits.ui.home

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.ui.HabitsAppTopBar
import com.willbsp.habits.ui.theme.HabitsTheme
import com.willbsp.habits.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToAddHabit: () -> Unit
) {

    Scaffold(
        topBar = {
            HabitsAppTopBar(
                title = stringResource(R.string.app_name),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddHabit,
                shape = CircleShape,
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(10.dp)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HabitsTheme() {
        HomeScreen() {}
    }
}