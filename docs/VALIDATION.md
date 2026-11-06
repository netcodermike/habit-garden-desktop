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
