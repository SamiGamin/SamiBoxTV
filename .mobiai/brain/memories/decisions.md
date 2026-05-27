# Decisions

<!--
Architecture decisions specific to this project.
Append entries with: mobiai brain save decision.
Each entry should record: title, status (active|deprecated), platform,
area, date, decision, reason, files.
-->

## Clean Architecture and Package Modularization Standard

- id: clean-architecture-modularization
- type: architecture_decision
- status: active
- platform: android
- area: architecture
- date: 2026-05-27

### Decision / Problem / Pattern
The project has grown as a single-module `app` with a basic package structure (`data`, `ui`, and root classes). To comply with the strict project guidelines defined in `GEMINI.md`, the codebase must adopt a clean, highly modular architecture separating logic into layers:
- `core/`: Common utilities, themes, system level base classes.
- `domain/`: Business entities and Use Cases/Interactors (completely framework-independent).
- `data/`: Repositories, data sources (SharedPreferences, Package Manager, Local/Remote databases).
- `presentation/`: UI state, ViewModels (using StateFlow with immutable UI states), and Compose Screen components.
- `services/`: Accessibility services and background monitor tasks.
- `di/`: Central container for manual dependency injection.

### Reason / Root Cause / Solution
A flat hierarchy creates high coupling between UI components and background logic (e.g. `MainActivity` instantiating `MemoryUsageTester` and `OverlayWindowManager` directly). Transitioning to Clean Architecture ensures:
1. **Separation of Concerns:** Business logic (Use Cases) is completely independent of Android frameworks (Package Manager, Window Manager).
2. **Testability:** Business logic can be unit-tested without needing Android instrumentation or mock context.
3. **Cohesive State Management:** StateFlow states are fully immutable, driven by declarative UI.

### Files
- [MainActivity.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/MainActivity.kt)
- [MainViewModel.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/MainViewModel.kt)
- [AppRepository.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/data/AppRepository.kt)

---

## Manual Dependency Injection Strategy (No Hilt)

- id: dependency-injection-manual
- type: architecture_decision
- status: active
- platform: android
- area: di
- date: 2026-05-27

### Decision / Problem / Pattern
It was decided to completely avoid the Hilt DI framework for this project. Instead, the codebase will implement manual dependency injection using constructor injection, factories, or a simple Service Locator pattern where direct injection is not possible (such as in AccessibilityServices or system broadcast receivers).

### Reason / Root Cause / Solution
Avoiding Hilt simplifies build setup, reduces compilation overhead, avoids additional annotation processing dependencies (like KSP), and keeps the project lightweight and direct. Manual DI will be achieved by:
1. Instantiating singletons (such as `OverlayWindowManager` and `AppPreferences`) in a central application class (`SamiBoxApplication`) or a custom simple Service Locator / Container.
2. Injecting these dependencies via constructors in ViewModels using a custom `ViewModelProvider.Factory`.
3. Accessing the central application container where constructor injection is impossible.

### Files
- [MainActivity.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/MainActivity.kt)
- [MainViewModel.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/MainViewModel.kt)
- [SamiBoxAccessibilityService.kt](file:///E:/AndroidStudio/SamiBoxTV/app/src/main/java/com/launcher/samiboxtv/SamiBoxAccessibilityService.kt)
