# Invoices

A local Android app for creating, tracking, exporting, and sharing simple business invoices. All data stays on the device. There is no backend, ads, analytics, or account system.

This project is **native Kotlin + Jetpack Compose** (not Flutter). Flutter was not available in this environment, and a native package is the most direct way to produce a Play-ready Android App Bundle with `gradlew bundleRelease`.

## What the app does

- Create, list, view, edit, and delete invoices
- Fields: client name, date, amount ($ or ₪), item description, status (Paid / Pending)
- Empty home state when nothing is saved yet
- Generate a one-page PDF and share it through the system share sheet
- Room database on-device (`invoices.db`)
- Settings → **Privacy Policy** opens an in-app WebView (`app/src/main/assets/privacy_policy.html`)
- Target SDK **36**, min SDK **26**, no dangerous permissions

Bottom navigation: **Invoices** | **Settings**.

## Prerequisites

1. **JDK 17 or 21** (this machine has `C:\Program Files\Java\jdk-21`)
2. **Android SDK** with API 36 platform and Build-Tools  
   Install [Android Studio](https://developer.android.com/studio) and in SDK Manager install:
   - Android SDK Platform 36
   - Android SDK Build-Tools 36.x
   - Android SDK Command-line Tools
3. Set environment variables (PowerShell example):

```powershell
setx JAVA_HOME "C:\Program Files\Java\jdk-21"
setx ANDROID_HOME "$env:LOCALAPPDATA\Android\Sdk"
```

Close and reopen the terminal after `setx`.

4. Create `local.properties` in the project root (Android Studio does this automatically):

```
sdk.dir=C:\\Users\\YOUR_USER\\AppData\\Local\\Android\\Sdk
```

Use double backslashes on Windows, or a single-quoted path with forward slashes: `C:/Users/YOUR_USER/AppData/Local/Android/Sdk`.

## Run a debug build

```powershell
cd C:\work\gitProjects\invoicesApp
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\gradlew.bat assembleDebug
```

Install the APK from `app\build\outputs\apk\debug\app-debug.apk`, or open the folder in Android Studio and click **Run**.

## Build the Play Store App Bundle (.aab)

Google Play accepts an **Android App Bundle**, not a raw APK, for new apps.

### 1. Create an upload keystore (once)

From the project root, in PowerShell:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
& "$env:JAVA_HOME\bin\keytool.exe" -genkeypair -v `
  -keystore upload-keystore.jks `
  -keyalg RSA -keysize 2048 -validity 10000 `
  -alias upload
```

You will be asked for a store password, key password, and certificate name. **Back up `upload-keystore.jks` and the passwords.** If you lose them and are not using Play App Signing recovery, you cannot update the app.

Do not commit `.jks` or `keystore.properties` to git (they are gitignored).

### 2. Point Gradle at the keystore

```powershell
copy keystore.properties.example keystore.properties
```

Edit `keystore.properties`:

```
storeFile=upload-keystore.jks
storePassword=YOUR_STORE_PASSWORD
keyAlias=upload
keyPassword=YOUR_KEY_PASSWORD
```

`storeFile` is relative to the **project root**.

### 3. Assemble the release bundle

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
.\gradlew.bat clean bundleRelease
```

The file to upload is:

```
app\build\outputs\bundle\release\app-release.aab
```

If signing is missing, Gradle still compiles, but Play will reject an unsigned bundle. Confirm `keystore.properties` exists and `bundleRelease` prints that the release signing config was used.

### 4. Upload to Play Console

1. Create the app named **Invoices** with package name `com.mealplanner1234.app`.
2. Host a **public Privacy Policy URL**. Copy `app/src/main/assets/privacy_policy.html` to GitHub Pages, your site, or any HTTPS host. Paste that URL into Play Console → App content → Privacy policy. The in-app WebView works offline; Play still requires a public URL.
3. Complete Data safety: this app does **not** collect or share user data with a developer server. Invoices are stored only on device.
4. Target audience: business / productivity, not children.
5. Upload `app-release.aab` to a testing track, then promote to production when policy checks pass.
6. Upload a 512×512 store icon (Image Asset Studio in Android Studio can export one from the existing adaptive icon).

## Play policy notes (keep the listing honest)

- The app is a real invoicing utility (CRUD + PDF). Do not describe it as a template, webview shell, or “test app”.
- No ads, no misleading store graphics, no unused `INTERNET` permission.
- If you add networking later, update the privacy policy and Data safety form before shipping.

## Project layout

```
app/src/main/java/com/mealplanner1234/app/
  data/          Room entity, DAO, database, repository
  pdf/           A4 PDF writer + share sheet
  ui/            Compose screens (home, form, detail, settings)
```

Change `namespace` / `applicationId` together if you republish under another package name.
