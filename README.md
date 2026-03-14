# CartPulse

CartPulse is an AI-powered shopping assistant built with Kotlin Multiplatform and Jetpack Compose. Users can chat with the assistant, ask product-related questions, and receive quick recommendations with short explanations and price guidance.

## Features

- Chat-based shopping assistant UI built with Jetpack Compose
- Shared business logic with Kotlin Multiplatform
- Gemini API integration through Ktor Client
- Clean separation between shared logic and Android UI
- Material 3 chat interface with loading state and assistant replies

## Tech Stack

### Shared

- Kotlin Multiplatform
- Ktor Client
- Coroutines
- Kotlinx Serialization

### Android

- Jetpack Compose
- ViewModel
- StateFlow
- Material 3

### AI

- Gemini API

## Project Structure

```text
composeApp/
  src/
    commonMain/
      kotlin/com/seenubommisetti/app/cartpulse/
        data/
        domain/
        models/
        network/
        prompt/
    androidMain/
      kotlin/com/seenubommisetti/app/cartpulse/
        ui/
        viewmodel/
```

## Architecture

CartPulse keeps provider logic and business logic in the shared KMP layer, while Android only owns UI and Android-specific wiring.

- `models/` contains shared chat message models
- `prompt/` builds shopping assistant instructions and conversation context
- `network/` contains the provider-agnostic AI contract and Gemini client
- `data/` contains the repository layer
- `domain/` contains the use case for sending shopping queries
- `androidMain/ui/` contains the Compose chat screen
- `androidMain/viewmodel/` contains the Android `ViewModel` and UI state

## Setup

Add your Gemini API key to `local.properties`:

```properties
GEMINI_API_KEY=your_gemini_api_key
```

The project also checks `GOOGLE_API_KEY` as a fallback.

## Run the Android App

### Windows

```powershell
.\gradlew.bat :composeApp:assembleDebug
```

### macOS/Linux

```bash
./gradlew :composeApp:assembleDebug
```

Then run the generated Android app from Android Studio or install the debug APK on a device/emulator.

## Package Name

The app package name is:

```text
com.seenubommisetti.app.cartpulse
```

## Current Status

- Android chat experience is implemented and working
- Shared KMP business logic is in place
- Gemini-based shopping recommendations are wired through the shared layer
- iOS entry point is present, but the full chat UI is currently focused on Android

## Future Improvements

- Product cards with images and links
- Conversation history persistence
- Better recommendation formatting
- Category filters and budget chips
- Cross-platform UI parity for iOS
