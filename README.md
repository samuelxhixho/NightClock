# NightClock

NightClock is a minimalist OLED-friendly Android clock and timer app built with Kotlin and Jetpack Compose.

It is designed to turn an Android phone, foldable, or tablet into a clean bedside or desk clock. NightClock focuses on a pure-black OLED interface, customizable clock layouts, flexible timers, reliable alarms, burn-in protection, and a distraction-free nighttime experience.

## Features

### Clock

- Fullscreen landscape clock
- Pure-black OLED-friendly background
- Keeps the screen awake while the app is open
- Burn-in protection with subtle periodic position shifting
- Responsive layouts for phones, foldables, and tablets
- Fold and unfold responsive behavior
- Date display
- Dim mode for softer night colors
- Scheduled Auto Dim

### Clock Styles

NightClock includes 8 clock styles:

- Classic
- Stacked
- Minimal
- Split
- Seconds
- Analog
- Focus
- Wide

Clock appearance can also be customized with:

- Multiple clock fonts
- Accent color presets
- Custom RGB accent colors

### Timers

- Three customizable quick timer presets
- Custom timer durations from 1 minute up to 23 hours 59 minutes
- Hours and minutes wheel picker
- Radial countdown timer
- Pause and resume
- Reset timer
- Animated quick timer controls
- Timer controls automatically hide when inactive
- Done screen when a timer finishes
- Add 5 minutes directly from the Done screen

### Alarms

Four selectable alarm sounds:

- Soft
- Digital
- Bell
- Pulse

Additional alarm controls include:

- Alarm sound preview
- Adjustable alarm volume
- Optional gradual alarm fade-in
- Vibration support
- Battery Saver warning

### Settings and Experience

- Persistent settings using Android DataStore
- First-launch onboarding
- Customizable timer presets
- Customizable clock appearance
- Responsive settings layout
- Dismissible modal overlays
- OLED-focused minimal interface
- Settings preserved between launches

## Built With

- Kotlin
- Jetpack Compose
- Jetpack ViewModel
- Kotlin Coroutines
- Android DataStore
- Android MediaPlayer
- Android vibration APIs

## Architecture

NightClock v2 uses a separated application structure instead of keeping the app inside a single activity.

Main areas include:

- `NightClockViewModel` for application and timer state
- `SettingsRepository` for persisted settings
- DataStore Preferences for local configuration
- Separate models for clocks, fonts, colors, and alarm sounds
- Reusable Compose screens and overlays
- Responsive layout information for different screen sizes
- Utility classes for alarm behavior

## App Purpose

NightClock is designed for everyday use as a:

- Bedside clock
- Desk clock
- Nap timer
- Focus timer
- Pomodoro-style timer
- Minimal nighttime display
- Foldable or tablet clock display

## Installation

NightClock requires Android 8.0 (API 26) or newer.

A signed NightClock v2.0 APK is provided with the GitHub release.

To install the APK:

1. Download the NightClock v2.0 APK from the GitHub release.
2. Open the downloaded APK on your Android device.
3. Allow installation from the selected source if Android asks for permission.
4. Install and open NightClock.

## Battery and Alarm Notes

For the most reliable timer alarm behavior, Battery Saver should be disabled for NightClock.

Some Android devices may restrict sound, vibration, or background behavior when Battery Saver or manufacturer-specific battery optimization is enabled. NightClock includes a Battery Saver warning to help identify this.

## Version

**NightClock v2.0**

Version 2 is a major expansion of the original NightClock app and adds:

- Custom timer durations
- Editable quick timer presets
- Multiple selectable alarm sounds
- Alarm volume control
- Gradual alarm fade-in
- Scheduled Auto Dim
- Three additional clock styles
- Analog clock support
- Clock font customization
- Expanded accent color customization
- Custom RGB colors
- First-launch onboarding
- Responsive foldable and tablet layouts
- Improved burn-in protection
- Timer control animations and auto-hide behavior
- Add 5 minutes from the timer completion screen
- Internal architecture and ViewModel refactor

## Status

NightClock v2.0 is feature complete and release ready.

## Developer

Built by Samuel Xhixho.