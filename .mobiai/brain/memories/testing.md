# Testing Patterns

<!--
Reusable testing patterns discovered for this project.
Append entries with: mobiai brain save testing.
Include the problem, the pattern that solved it and a minimal example.
-->

## Compose TV Key Navigation & Focus Verification

- id: tv-focus-testing
- type: testing_pattern
- status: active
- platform: android
- area: ui-testing
- date: 2026-05-27

### Decision / Problem / Pattern
In TV apps, navigation is entirely driven by remote controls (D-Pad). Focus navigation (UP, DOWN, LEFT, RIGHT, ENTER) must be tested programmatically to ensure focus doesn't get lost, especially when grids dynamically load icons or show overlays.

### Reason / Root Cause / Solution
Traditional touch clicks do not simulate TV remote actions. In Compose TV, we must request focus on an element and dispatch key events (`performKeyInput`) to verify that the focus shifts to the correct adjacent element.

### Minimal Example
```kotlin
@Test
fun testDpadNavigationOnAppList() {
    composeTestRule.setContent {
        SamiBoxTVTheme {
            HomeScreen(viewModel = mockViewModel) { /* ... */ }
        }
    }

    // 1. Assert initial focus is on the first App Card
    composeTestRule.onNodeWithTag("AppCard_0").assertIsFocused()

    // 2. Navigate RIGHT with D-Pad
    composeTestRule.onRoot().performKeyInput {
        pressKey(Key.DirectionRight)
    }

    // 3. Verify focus moved to the second item
    composeTestRule.onNodeWithTag("AppCard_1").assertIsFocused()
}
```

### Files
- [AppCard.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/ui/AppCard.kt)
- [HomeScreen.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/ui/HomeScreen.kt)
