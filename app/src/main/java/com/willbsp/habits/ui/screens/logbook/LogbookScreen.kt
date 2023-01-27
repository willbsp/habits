package com.willbsp.habits.ui.screens.logbook

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.willbsp.habits.R
import com.willbsp.habits.ui.theme.Typography

@Composable
fun LogbookScreen(
    modifier: Modifier = Modifier,
    viewModel: LogbookViewModel,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit
) {


    Logbook(
        modifier = modifier,
        navigateToHome = navigateToHome,
        navigateToSettings = navigateToSettings
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Logbook(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.logbook_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                modifier = modifier,
                navigationIcon = {
                    IconButton(onClick = navigateToHome) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(onClick = navigateToSettings) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.settings)
                        )
                    }
                }
            )
        }
    ) { innerPadding -> // TODO


        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .fillMaxSize()
        ) {

            Text( // TODO could have title area change colour when list is scrolled, e.g timers in google clock
                text = stringResource(R.string.home_screen_today),
                style = Typography.titleLarge,
                modifier = Modifier.padding(horizontal = 10.dp) // keep inline with habit titles
            )

            Spacer(modifier = Modifier.height(10.dp))

            /*HabitsList(
                habitUiStateList = homeUiState.todayState,
                completedOnClick = completedOnClick,
                navigateToEditHabit = navigateToEditHabit,
                modifier = Modifier
            )*/

        }


    }
}

@Preview
@Composable
fun LogbookPreview() {
    Logbook(
        navigateToSettings = {},
        navigateToHome = {}
    )
}