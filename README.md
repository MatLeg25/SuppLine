# SuppLine

### Mobile Android app for managing daily supplements. 

## Light mode:
![SuppLine_light_mode](https://github.com/user-attachments/assets/50127f9b-9334-420b-b812-6de3e01e26bd)

## Dark mode:
![SuppLine_dark_mode](https://github.com/user-attachments/assets/8693ab2d-b262-4ec9-9e98-17ef1f155fda)

## Demo:
[SuppLine-demo.webm](https://github.com/user-attachments/assets/5977eed4-9aab-4516-b409-6919ca39ef6e)


## Check it out!
The app is not officially published in Google Play Store. If you want to check it out, you can do it using one of the following ways:

1) Download directly on Android device from: <b>[SuppLine v1.0](https://github.com/MatLeg25/SuppLine/releases/download/v1.0/SuppLine_v1.0.apk)</b> <br />
<i>You may need to enable Unknown Sources in your device settings to install apps outside of the Google Play Store.</i>  [[Allow Unknown Sources on Android](https://www.wikihow.com/Allow-Apps-from-Unknown-Sources-on-Android)]

2) Build the app on your computer:
   - a)  Clone repository.
   - b)  Open project in Android Studio.
   - c)  Build with Gradle.
   - d)  Run on your device or emulator


## Features:
 - create and modify supplements,
 - progress bar to monitor daily status,
 - recreate supplementation for every day,
 - set system notifications to remind when take supplements,
 - hanlde notification actions in app (Snooze, Done, Cancel),
 - reschedule notification after system reboot

## Tech stack:
- Android, Jetpack Compose
- Kotlin, Coroutines, Flow
- MVVM, DI with Dagger-Hilt
- SharedPreferences

## Sources:
 - App icon based on: https://adioma.com/icons/pill
 - Theme generated with: https://material-foundation.github.io/material-theme-builder/
