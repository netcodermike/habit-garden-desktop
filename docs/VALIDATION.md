# Validation

Authoring date: November 6, 2026. Host: Windows. Target: Windows, macOS, Linux desktop.

## Local result

Kotlin/Gradle unavailable on authoring host. CI builds and tests date/growth/persistence behavior.

## Reproducible checks

The GitHub Actions workflow builds/runs the following on `ubuntu-latest`:

```sh
gradle build --no-daemon
gradle run --args="--self-test" --no-daemon
```

## Manual acceptance

Plant multiple habits, check in twice, undo today, reopen, select plants, and inspect behavior around a day boundary using a development fixture.

A workflow file is not evidence of a passing run. Observed CI run links and source commits are recorded after publication. Native GUI, sound, sleep/resume, and platform permissions require direct target-platform acceptance even when model tests pass.

## Observed CI result

Passed: [target-platform build/check](https://github.com/netcodermike/habit-garden-desktop/actions/runs/42204101585). Source commit: `0f81a2358f7187dd1be8205146d326d244ed4e3f`. Later documentation-only changes do not alter the tested implementation. The reproducible commands above identify test scope; native GUI/audio/permissions are not implied by compilation.
