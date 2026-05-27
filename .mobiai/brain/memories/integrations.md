# Integrations

<!--
Notes on third-party integrations (Firebase, analytics, push, payments,
etc.) and their project-specific configuration quirks.
-->

## Global Hardware Menu Key Hijacking via Accessibility Service

- id: menu-key-accessibility
- type: integration_note
- status: active
- platform: android
- area: accessibility-service
- date: 2026-05-27

### Decision / Problem / Pattern
To toggle the system performance overlay from any app (in the background or foreground), the launcher utilizes an Accessibility Service: `SamiBoxAccessibilityService`. It intercepts the TV remote's `KEYCODE_MENU` (key code 82) globally.

### Reason / Root Cause / Solution
Under normal conditions, Android applications cannot receive key events when they are not in focus or are in the background. The `AccessibilityService` is the standard, secure way in Android to hook into global hardware buttons (such as remote controls) and consume them:
```kotlin
override fun onKeyEvent(event: KeyEvent): Boolean {
    if (event.action == KeyEvent.ACTION_DOWN) {
        when (event.keyCode) {
            KeyEvent.KEYCODE_MENU -> {
                if (overlayManager.canDrawOverlays()) {
                    overlayManager.toggle()
                }
                return true // Consume the key event so it doesn't propagate
            }
        }
    }
    return false
}
```

### Files
- [SamiBoxAccessibilityService.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/SamiBoxAccessibilityService.kt)
- [AndroidManifest.xml](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/AndroidManifest.xml)

---

## TV System Metrics Overlay Window (TYPE_APPLICATION_OVERLAY)

- id: system-metrics-overlay
- type: integration_note
- status: active
- platform: android
- area: system-overlay
- date: 2026-05-27

### Decision / Problem / Pattern
Drawing views directly over other apps requires requesting `android.permission.SYSTEM_ALERT_WINDOW` (Settings.canDrawOverlays) and setting `WindowManager.LayoutParams.type` to `TYPE_APPLICATION_OVERLAY` (Android 8.0+) or legacy `TYPE_PHONE`.

### Reason / Root Cause / Solution
To render Jetpack Compose UI in a background/system service context without keeping an activity running, the view is inflated using `ComposeView` and added directly through `WindowManager.addView`.
The overlay window configuration flags are set to be completely transparent and pass through touch/focus:
```kotlin
WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
```
This is essential for a TV metrics overlay to remain visible in the top-right corner while the user navigates other apps or movies, without blocking remote control inputs or key events.

### Files
- [OverlayWindowManager.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/OverlayWindowManager.kt)
- [SystemInfoOverlay.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/ui/SystemInfoOverlay.kt)
