# Application Architecture

<details>
<summary>Relevant source files</summary>

The following files were used as context for generating this wiki page:

- [app/src/main/java/com/suzhe/playdemo/AppContext.kt](app/src/main/java/com/suzhe/playdemo/AppContext.kt)
- [app/src/main/java/com/suzhe/playdemo/component/main/MainActivity.kt](app/src/main/java/com/suzhe/playdemo/component/main/MainActivity.kt)
- [app/src/main/java/com/suzhe/playdemo/component/main/MainAdapter.kt](app/src/main/java/com/suzhe/playdemo/component/main/MainAdapter.kt)
- [app/src/main/res/drawable/sun.png](app/src/main/res/drawable/sun.png)
- [settings.gradle.kts](settings.gradle.kts)

</details>



This document explains the overall architecture of the PlayDemo Android application, including the
initialization flow, navigation patterns, and core architectural components. It covers the
high-level structure and relationships between major system components that form the foundation of
the application.

For specific information about BRVAH demo implementations, see [BRVAH Demo System](#4). For details
about individual UI components and patterns, see [UI Components and Patterns](#5).

## Application Initialization Flow

The PlayDemo application follows a structured initialization sequence that sets up global services,
handles permissions, and establishes the main navigation framework.

### Initialization Sequence

```mermaid
sequenceDiagram
    participant System as "Android System"
    participant AC as "AppContext"
    participant SA as "SplashActivity"
    participant MA as "MainActivity"
    participant Libs as "Third-party Libraries"
    
    System->>AC: "attachBaseContext()"
    AC->>Libs: "XCrash.init()"
    System->>AC: "onCreate()"
    AC->>AC: "initMMKV()"
    AC->>AC: "processCrashLogs()"
    AC->>Libs: "DynamicColors.applyToActivitiesIfAvailable()"
    AC->>Libs: "DialogX.init()"
    System->>SA: "Launch SplashActivity"
    SA->>SA: "Handle permissions & terms"
    SA->>MA: "startActivity(MainActivity)"
    MA->>MA: "initViews()"
    MA->>MA: "initTabs()"
    MA->>MA: "initPagers()"
```

The `AppContext` class serves as the global application entry point, responsible for initializing
core libraries and services. Key initialization components include:

| Component     | Purpose                            | Implementation       |
|---------------|------------------------------------|----------------------|
| XCrash        | Crash monitoring and reporting     | [AppContext.kt:35]() |
| MMKV          | High-performance key-value storage | [AppContext.kt:44]() |
| DialogX       | Dialog framework initialization    | [AppContext.kt:50]() |
| DynamicColors | Material Design dynamic theming    | [AppContext.kt:48]() |

Sources: [AppContext.kt:23-102](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/AppContext.kt#L23-L102)

### Global Context Management

```mermaid
graph TD
    AppContext["AppContext<br/>(Application)"]
    MMKV["MMKV<br/>(Key-Value Storage)"]
    XCrash["XCrash<br/>(Crash Monitoring)"]
    DialogX["DialogX<br/>(Dialog Framework)"]
    CrashLogs["Crash Log Processing<br/>(Background Coroutine)"]
    DynamicColors["DynamicColors<br/>(Material Theming)"]
    
    AppContext --> MMKV
    AppContext --> XCrash
    AppContext --> DialogX
    AppContext --> CrashLogs
    AppContext --> DynamicColors
    
    CrashLogs --> MediaStore["MediaStore<br/>(External Storage)"]
```

The `AppContext` maintains a singleton instance accessible throughout the application and manages
background crash log processing using coroutines with a 5-second delay to avoid interfering with app
startup performance.

Sources: [AppContext.kt:23-102](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/AppContext.kt#L23-L102)

## Main Navigation Architecture

The main navigation system is built around a tabbed interface using `ViewPager2` and `DslTabLayout`,
managed by the `MainActivity` class.

### Navigation Component Structure

```mermaid
graph TB
    MainActivity["MainActivity<br/>(BaseViewModelActivity)"]
    TabLayout["DslTabLayout<br/>(mTabs)"]
    ViewPager2["ViewPager2<br/>(mPager)"]
    MainAdapter["MainAdapter<br/>(FragmentStateAdapter)"]
    SkinButton["SkinFlotButton<br/>(Custom FloatingButton)"]
    
    subgraph "Tab Fragments"
        DiscoveryFragment["DiscoveryFragment<br/>(Position 0)"]
        LibraryFragment["LibraryFragment<br/>(Position 1)"]
        TestFragment1["TestFragment<br/>(Position 2)"]
        TestFragment2["TestFragment<br/>(Position 3)"]
    end
    
    MainActivity --> TabLayout
    MainActivity --> ViewPager2
    MainActivity --> SkinButton
    ViewPager2 --> MainAdapter
    MainAdapter --> DiscoveryFragment
    MainAdapter --> LibraryFragment
    MainAdapter --> TestFragment1
    MainAdapter --> TestFragment2
    
    TabLayout -.-> ViewPager2
```

The navigation system implements a four-tab interface with the following structure:

| Tab Index | Title      | Fragment            | Icon Resource                       |
|-----------|------------|---------------------|-------------------------------------|
| 0         | "Discover" | `DiscoveryFragment` | `R.drawable.selector_tab_discovery` |
| 1         | "Library"  | `LibraryFragment`   | `R.drawable.selector_tab_library`   |
| 2         | "Category" | `TestFragment`      | `R.drawable.selector_tab_category`  |
| 3         | "Me"       | `TestFragment`      | `R.drawable.selector_tab_me`        |

Sources: [MainActivity.kt:36-48](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/MainActivity.kt#L36-L48), [MainAdapter.kt:16-23](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/MainAdapter.kt#L16-L23)

### Tab and Pager Initialization

The `MainActivity` initializes its navigation components through a two-stage process:

```mermaid
flowchart TD
    onCreate["onCreate()"] --> initViews["initViews()"]
    initViews --> initTabs["initTabs()"]
    initViews --> initPagers["initPagers()"]
    
    initTabs --> CreateTabItems["Create tab items using<br/>ItemTabBinding.inflate()"]
    CreateTabItems --> AddToTabLayout["Add views to DslTabLayout"]
    
    initPagers --> SetOffscreenLimit["Set offscreenPageLimit<br/>to tabTitles.size"]
    SetOffscreenLimit --> SetAdapter["Set MainAdapter"]
    SetAdapter --> InstallDelegate["ViewPager2Delegate.install()"]
```

The `ViewPager2` is configured with `offscreenPageLimit` set to the total number of tabs to keep all
fragments in memory, and uses `ViewPager2Delegate` to synchronize with the tab layout.

Sources: [MainActivity.kt:57-81](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/MainActivity.kt#L57-L81)

## Core Architectural Components

### Base Activity Framework

The application uses a layered activity hierarchy that provides common functionality:

```mermaid
graph TD
    BaseViewModelActivity["BaseViewModelActivity<br/>(Abstract Base)"]
    BaseTitleActivity["BaseTitleActivity<br/>(Toolbar Support)"]
    MainActivity["MainActivity<br/>(Main Navigation)"]
    WebActivity["WebActivity<br/>(Web Content)"]
    DemoActivities["Demo Activities<br/>(BRVAH Examples)"]
    
    BaseViewModelActivity --> MainActivity
    BaseViewModelActivity --> BaseTitleActivity
    BaseTitleActivity --> WebActivity
    BaseTitleActivity --> DemoActivities
```

This hierarchy provides:

- View binding and lifecycle management (`BaseViewModelActivity`)
- Toolbar and title bar functionality (`BaseTitleActivity`)
- Navigation-specific features (`MainActivity`)

Sources: [MainActivity.kt:29](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/MainActivity.kt#L29)

### Custom UI Components

The `MainActivity` includes a custom draggable floating action button implemented as an inner class:

```mermaid
graph LR
    SkinFlotButton["SkinFlotButton<br/>(FrameLayout)"]
    FragmentContainer["FragmentContainerView<br/>(Content Area)"]
    GlobalButton["QMUIRadiusImageView2<br/>(Draggable Button)"]
    OffsetHelper["QMUIViewOffsetHelper<br/>(Position Management)"]
    
    SkinFlotButton --> FragmentContainer
    SkinFlotButton --> GlobalButton
    GlobalButton --> OffsetHelper
    
    GlobalButton -.-> PopupMenu["QMUIPopup<br/>(Global Actions)"]
```

The `SkinFlotButton` component features:

- Touch event interception and drag handling
- Boundary constraint enforcement
- Popup menu integration for global actions
- Dynamic positioning with offset helpers

Sources: [MainActivity.kt:131-306](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/MainActivity.kt#L131-L306)

### Fragment Management Pattern

The application uses `FragmentStateAdapter` for efficient fragment lifecycle management:

```mermaid
graph TD
    FragmentActivity["MainActivity<br/>(FragmentActivity)"]
    StateAdapter["MainAdapter<br/>(FragmentStateAdapter)"]
    FragmentManager["FragmentManager<br/>(System)"]
    
    subgraph "Fragment Instances"
        Fragment0["DiscoveryFragment.newInstance()"]
        Fragment1["LibraryFragment.newInstance()"]
        Fragment2["TestFragment.newInstance()"]
        Fragment3["TestFragment.newInstance()"]
    end
    
    FragmentActivity --> StateAdapter
    StateAdapter --> FragmentManager
    FragmentManager --> Fragment0
    FragmentManager --> Fragment1
    FragmentManager --> Fragment2
    FragmentManager --> Fragment3
```

The `MainAdapter` uses factory methods (`newInstance()`) to create fragment instances, ensuring
proper argument passing and state management.

Sources: [MainAdapter.kt:10-24](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/MainAdapter.kt#L10-L24)

## Component Interaction Patterns

### Global State Access

The application provides global context access through a singleton pattern:

```mermaid
graph TD
    AppContext["AppContext.instance<br/>(Singleton)"]
    Activities["Activities"]
    Fragments["Fragments"]
    Services["Background Services"]
    Utils["Utility Classes"]
    
    Activities --> AppContext
    Fragments --> AppContext
    Services --> AppContext
    Utils --> AppContext
    
    AppContext --> MMKV["MMKV Storage"]
    AppContext --> Preferences["Application Preferences"]
```

The `AppContext.instance` provides centralized access to global services and utilities throughout
the application lifecycle.

Sources: [AppContext.kt:96-101](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/AppContext.kt#L96-L101)

### Event Handling Architecture

The main activity implements a comprehensive event handling system for touch interactions:

```mermaid
stateDiagram-v2
    [*] --> TouchDown: "ACTION_DOWN"
    TouchDown --> Evaluating: "Check if touch in button area"
    Evaluating --> Dragging: "Movement > touchSlop"
    Evaluating --> Clicking: "No significant movement"
    Dragging --> Moving: "ACTION_MOVE"
    Moving --> Moving: "Update button position"
    Moving --> Released: "ACTION_UP/CANCEL"
    Clicking --> Released: "ACTION_UP"
    Released --> [*]: "Reset state"
```

The touch handling system uses `ViewConfiguration.scaledTouchSlop` to distinguish between drag and
click gestures, with boundary enforcement to keep the draggable button within screen bounds.

Sources: [MainActivity.kt:237-304](https://github.com/SuZhelevel6/PlayDemo/blob/a2338414/MainActivity.kt#L237-L304)