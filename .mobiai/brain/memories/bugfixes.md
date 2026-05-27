# Bugfixes

<!--
Bugfixes and workarounds worth remembering for this project.
Append entries with: mobiai brain save bugfix.
Mark temporary workarounds as status: temporary so the agent does not
treat them as permanent decisions.
-->

## Memory Leak: System Overlay Lifecycle is never Stopped (RESOLVED)

- id: overlay-lifecycle-leak
- type: platform_workaround
- status: deprecated
- platform: android
- area: system-overlay
- date: 2026-05-27

### Decision / Problem / Pattern
In `OverlayWindowManager.kt`, a custom `OverlayLifecycleOwner` was instantiated to manage a ComposeView's lifecycle when rendered outside of a standard Activity (via `WindowManager` directly).
However, the `stop()` method of the `OverlayLifecycleOwner` was never invoked, neither in `OverlayWindowManager.hide()` nor in `MainActivity.onDestroy()`. This kept the Compose recomposer, observers, and scope active indefinitely, leaking RAM.

### Reason / Root Cause / Solution
**Resolved:** Saved the `lifecycleOwner` as a private property of `OverlayWindowManager`, and invoked `lifecycleOwner?.stop()` in `hide()`, cleaning up recomposition context and observers perfectly.

### Files
- [OverlayWindowManager.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/OverlayWindowManager.kt)

---

## Coroutine Leak: MemoryUsageTester Loop is never Cancelled (RESOLVED)

- id: coroutine-leak-memory-tester
- type: platform_workaround
- status: deprecated
- platform: android
- area: background-services
- date: 2026-05-27

### Decision / Problem / Pattern
In `MemoryUsageTester.kt`, a background monitoring loop was started via a newly instantiated `CoroutineScope(Dispatchers.IO)` with no parent Job, meaning it was treated as a global scope. Since it was never cancelled, the `while(isActive)` loop ran forever in the background even after `MainActivity` was destroyed.

### Reason / Root Cause / Solution
**Resolved:** Added a private `Job` property inside `MemoryUsageTester` and exposed `stopMonitoring()` which calls `job?.cancel()`. This function is invoked from `MainActivity.onDestroy()` to ensure clean thread termination.

### Files
- [MemoryUsageTester.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/MemoryUsageTester.kt)
- [MainActivity.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/MainActivity.kt)
