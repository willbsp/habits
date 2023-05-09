package com.willbsp.habits.ui.screens.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.willbsp.habits.R
import com.willbsp.habits.ui.common.DefaultHabitsAppTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            DefaultHabitsAppTopBar(
                title = stringResource(R.string.about_title),
                canNavigateBack = true,
                navigateUp = navigateUp,
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            Text("this is the about screen")

        }

    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AboutScreenPreview() {
    AboutScreen({})
}