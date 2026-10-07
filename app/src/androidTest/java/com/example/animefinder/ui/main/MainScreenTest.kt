package com.example.animefinder.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/** Instrumented UI tests for Anime XXI components. */
class MainScreenTest {

  @get:Rule val composeTestRule = createAndroidComposeRule<ComponentActivity>()

  @Before
  fun setup() {
    composeTestRule.setContent {
      Text("ANIME XXI")
    }
  }

  @Test
  fun appTitle_exists() {
    composeTestRule.onNodeWithText("ANIME XXI").assertExists()
  }
}
