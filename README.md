# Habit Garden Desktop

Plant named habits, check in once per day, track streaks, and grow a small illustrated garden from completed days.

[![Build and verify](https://github.com/netcodermike/habit-garden-desktop/actions/workflows/verify.yml/badge.svg)](https://github.com/netcodermike/habit-garden-desktop/actions/workflows/verify.yml)

**Language:** Kotlin · **Platform:** Windows, macOS, Linux desktop · **App type:** Desktop habit tracker with procedural garden

First-release learning implementation, with explicit scope below. This is one independently runnable app, not a placeholder in a combined repository.

## Features

- Up to 100 unique habit names and date-set check-ins.
- Current streaks with yesterday grace until today is checked in.
- Growth stages driven by total completed days.
- Garden illustration, today undo, and persistent local state.

## Requirements

JDK 17 and Gradle 8.9. The first build downloads Kotlin 2.0.21 and dependencies from Gradle/Maven repositories. No binary Gradle wrapper is bundled.

## Quick start

```sh
git clone https://github.com/netcodermike/habit-garden-desktop.git
cd habit-garden-desktop
gradle run
# Separate garden:
gradle run --args="my-garden.properties"
gradle installDist
```

Commands run from the repository root. Lines marked for a particular operating system apply only there. Language tools are prerequisites, not installed by the app. Close desktop windows normally; terminal/server apps stop with Ctrl+C unless their usage notes say otherwise.

## Example workflow

Plant Read and Walk. Check in Read twice today: it should count once. Undo today, check in again, and reopen the app to verify persistence. Selecting a habit highlights its illustrated plant when among the first ten.

## Architecture, configuration, and data

`Main.kt` contains Habit data, streak/stage functions, Properties persistence, a custom-painted garden, and Swing UI. Dates use the local calendar. A set prevents duplicate check-ins. Streaks end at today when checked in, otherwise yesterday; a gap before that resets the streak. Total day thresholds 3/7/14/30 advance growth stages without punishing a missed day. The garden paints the first ten habits while the list retains up to 100. Default storage is garden.properties, with same-directory atomic replacement and fallback. Model mutations roll back if persistence fails. Tests check streak boundaries, growth stages, and save/load.

## Verification

Build with the quick-start command where needed, then run:

```sh
gradle run --args="--self-test" --no-daemon
```

Manual acceptance: Plant multiple habits, check in twice, undo today, reopen, select plants, and inspect behavior around a day boundary using a development fixture.

See [validation record](docs/VALIDATION.md) for observed checks and their limits. CI results verify only what the workflow runs; they do not establish native GUI correctness or production readiness.

## Limitations and next steps

No historical-date entry UI, reminders, cloud sync, deletion/rename, weekly targets, analytics, or clinical/health claims. Dates follow local system time, so clock/timezone changes can affect streaks. Illustrations show only the first ten habits. Plaintext stores are intended for a single writer. Growth depends on total days rather than consecutive days.

## Troubleshooting

Use the pinned Gradle/JDK combination. Headless environments can run --self-test but not Swing. Names are unique case-insensitively and limited to 80 characters. Corrupt stores fail explicitly; preserve them before resetting. Select a habit before checking in or undoing.

## Source map

- `build.gradle.kts`
- `settings.gradle.kts`
- `src/main/kotlin/Main.kt`

## Development and license

Read [contributing](CONTRIBUTING.md) before changing the app, and [security/privacy](SECURITY.md) before supplying private data. Use synthetic fixtures for tests. No telemetry, paid API key, or account is required by the app itself. Dependencies, when present, are stated in Requirements.

[MIT license](LICENSE), copyright 2026 netcodermike.
