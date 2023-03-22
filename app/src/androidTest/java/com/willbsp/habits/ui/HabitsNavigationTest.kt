package com.willbsp.habits.ui

// TODO need to reimplement

/*@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HabitsNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun init() {
        hiltRule.inject()
        setupHabitsNavHost()
    }

    private fun setupHabitsNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            HabitsApp(navController = navController)
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_clickAddHabit_navigatesToAddHabitScreen() {
        navigateToAddHabitScreen()
        navController.assertCurrentRouteName(HabitsNavigationDestination.ADD.route)
    }

    @Test
    fun navHost_verifyBackNavigationNotShownOnHome() {
        val backText = composeTestRule.activity.getString(R.string.topbar_back)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun navHost_verifyBackNavigationShownOnAddScreen() {
        navigateToAddHabitScreen()
        val backText = composeTestRule.activity.getString(R.string.topbar_back)
        composeTestRule.onNodeWithContentDescription(backText).assertExists()
    }

    @Test
    fun navHost_verifyBackNavigationNavigatesBackFromAddScreen() {
        navigateToAddHabitScreen()
        val backText = composeTestRule.activity.getString(R.string.topbar_back)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    @Test
    fun navHost_verifyAddHabitNavigatesBackToHome() {
        navigateToAddHabitScreen()
        val nameText = composeTestRule.activity.getString(R.string.modify_habit_name)
        composeTestRule.onNodeWithText(nameText).performClick().performTextInput("Swimming")
        val doneText = composeTestRule.activity.getString(R.string.add_habit_add_habit)
        composeTestRule.onNodeWithContentDescription(doneText).performClick()
        navController.assertCurrentRouteName(HabitsNavigationDestination.HOME.route)
    }

    private fun navigateToAddHabitScreen() {
        val addText = composeTestRule.activity.getString(R.string.home_add_habit)
        composeTestRule.onNodeWithContentDescription(addText).performClick()
    }

}*/