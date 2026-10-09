# Crazy Basket

Legacy Java / libGDX arcade basketball game. Sources and Android assets are in `game - Android Studio`.

The menu supports English, Spanish, Portuguese, French and Italian. Tap a setting to cycle language, court, basket distance, basket height or difficulty. Choices are saved for the next launch. Desktop shortcuts: **L** changes language, **C** changes court and **F** changes difficulty.

There are eight courts: street, rooftop, beach, arena, park, desert, snow and space. Each court has a complete illustrated background and its own shot budget; space uses 65% of normal gravity. Championship mode progresses to the next court after a completed game. Practice allows unlimited shots.

| Difficulty | Balls relative to the court budget | Gravity multiplier | Trajectory guide |
| --- | --- | --- | --- |
| Easy | +5 | 0.85 | Yes |
| Normal | Unchanged | 1.00 | No |
| Hard | −3, minimum 4 | 1.15 | No |

Drag inside the area to the right of the basket and release to shoot. Easy mode previews the flight before collisions. Each successful basket displays an animated **GOOD!** sticker with an elastic entrance, rotating stars, expanding rings and confetti for 1.9 seconds and plays a short synthesized “Good!” voice. The sound toggle also mutes the voice; overlapping celebrations restart cleanly.

The opening screen displays the generated Crazy Basket logo and the existing Audela mark. Logo prompts are in [output/asset-prompts.md](output/asset-prompts.md); the eight court assets and their imagegen prompts are in [output/court-art-prompts.json](output/court-art-prompts.json).

## Verification

Use JDK 11 or newer to compile the Java 8 core without the legacy Android Gradle environment:

```sh
python3 tools/check_game.py
python3 tools/check_game.py --render
```

The first command checks language persistence, translation completeness, difficulty budgets, invalid saved-setting recovery, font coverage and the voice recording. The optional desktop check requires an X display and OpenGL, uses isolated temporary preferences and mock audio/platform services, exercises the touch selectors and real Box2D scoring across all 24 court/difficulty combinations, and captures actual game screenshots in `output/previews`. It also checks voice playback requests, retriggering and mute behavior.

The checker downloads the pinned libGDX/LWJGL dependencies to a temporary cache. Android packaging uses libGDX 1.9.10 and includes armeabi-v7a, arm64-v8a, x86 and x86_64 native libraries.

## Signed Android test release

Version 1.2.0 (version code 3) is built with:

```sh
cd 'game - Android Studio'
bash gradlew :android:assembleRelease
```

Gradle loads the dedicated RSA-4096 signing key from `signing/release.properties`. The private keystore is `signing/crazy-basket-release.jks`, with alias `crazybasket`; its passwords stay in the private properties file. Both are excluded by `.gitignore`. Keep a secure backup of both files to sign future updates with the same identity. The public certificate is `signing/crazy-basket-cert.pem`.

The test APK and QR code are in `output/test-download/`. Start or restart the LAN download server from the repository root with:

```sh
python3 tools/serve_test_apk.py
```

The server listens on all interfaces and refreshes the QR code when the current Wi-Fi/LAN address changes. The phone must use the same network and the computer must remain running. The exact current URL is in `output/test-download-url.txt`. Only the download directory is served; signing files are never published. The release metadata file records the APK checksum and certificate fingerprint.

If an older APK signed with a different key is installed, Android will not accept it as an update. Uninstall that older copy before installing this release; uninstalling removes its local progress.


When disk space is unavailable, build artifacts and preview captures use temporary RAM storage. The current test APK download requires the computer to remain running; rebuild after a restart. The source artwork and signing key remain in the workspace.
