[English](README.md) | [ქართული](README.ka.md)

# Open Wheel Regulations (OWR)

**An offline Android reference for the technical-regulation history of top-tier open-wheel racing.**

OWR lets you explore 27 seasons (2000–2026) of single-seater motorsport season by season — what the rules were, what changed each year and why it mattered, and how the cars evolved — including interactive before/after comparisons for the major regulation eras. Everything is bundled in the app, so it works completely offline with no ads, no accounts, and no data collection.

---

## Download

📥 **[Download the latest APK from Releases](https://github.com/h0taa/OpenWheelRegulations_App/releases/latest)** — install it directly on your Android phone (Android 8.0+).
On install, Android may ask you to allow installs from your browser/file manager — that's normal for apps outside Google Play.

---

## Screenshots

| Seasons list | Season detail | Visual comparison | Settings |
|:---:|:---:|:---:|:---:|
| ![Seasons](screenshots/home.png) | ![Detail](screenshots/detail.png) | ![Comparison](screenshots/comparison.png) | ![Settings](screenshots/settings.png) |


---

## Features

- **27 seasons (2000–2026)** — each with a plain-language summary of the key changes and why they mattered.
- **Major-era highlights** — the seasons that reshaped the sport are flagged.
- **Expandable rule categories** per season — power unit, aerodynamics, tyres, car dimensions & weight, and more.
- **Interactive visual comparison** — a guided before/after walkthrough of how the car changed for the big rule resets.
- **Bookmarks & search** — save the seasons you care about and find them fast.
- **Light & dark themes**, plus Material You dynamic color.
- **100% offline** — no network access, no ads, no sign-up, no tracking, and no runtime permissions.

---

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3), with shared-element transitions
- **Architecture:** MVVM
- **Local storage:** Room (database) and DataStore (preferences)
- **Dependency injection:** Hilt
- **Navigation:** Navigation-Compose

---

## Requirements

- Android Studio (latest stable release)
- An Android device or emulator running **Android 8.0 (API 26)** or higher

---

## Build & run

```bash
git clone https://github.com/h0taa/OpenWheelRegulations_App.git
```

1. Open the cloned folder in **Android Studio**.
2. Let Gradle finish syncing.
3. Select a device or emulator (API 26+) and press **Run**.

No API keys, accounts, or network connection are required — the app is fully self-contained.

---

## Project structure

```
app/src/main/java/com/openwheel/regulations/
├── data/            # Room database, data models, repository
│   ├── db/
│   ├── model/
│   └── repository/
├── di/              # Hilt dependency-injection modules
├── ui/              # Jetpack Compose screens & UI
│   ├── home/        # Seasons list
│   ├── detail/      # Season detail + visual comparison
│   ├── bookmarks/
│   ├── settings/
│   ├── about/
│   ├── splash/
│   ├── components/  # Reusable composables
│   ├── navigation/
│   └── theme/       # Colours, typography, theming
└── util/
```

---

## Data

All regulation content is bundled offline in `app/src/main/assets/regulations_data.json` and seeded into a local Room database on first launch. If the database is ever empty, it re-seeds itself from the bundled file, so the app always has its data without any network connection.

---

## Privacy

OWR collects no personal data, requests no permissions, and makes no network calls.
Privacy policy: https://h0taa.github.io/owr-privacy/

---

## Disclaimer

Open Wheel Regulations is an independent reference application. It is not affiliated with, endorsed by, or associated with any motorsport series, team, sponsor, or governing body, and it uses no official names or logos.
