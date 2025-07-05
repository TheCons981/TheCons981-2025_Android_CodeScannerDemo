# Code Scanner Demo

**Code Scanner Demo**, a lightweight Android (Kotlin+Jetpack Compose) demo project created to refresh some details before going on with the refactoring of my prduction project after almost two years of inactivity in native development. The app allows users to **scan, select, or generate various types of QR codes and barcodes**, with the ability to save them locally. The project demonstrates best practices for modern Android photo and data handling.
(The focus was not on the UI, so no particular attention was given to details).

## ✨ Features

### 📷 Code Scanner

- **Scan or select QR/barcodes:** Scan a code directly using the Camera Mlkit API or select an image from your photo library.
- **Preview:** Preview scanned or selected QR/barcodes before saving them to the "Code repository" or performing other actions.

### 🏷️ Code Generator

- **Choose code type:** Select from various types to create and share your own code:
  - **Text/Link:** Generate a QR code from custom text or a link.
  - **Contact:** Create a QR code with contact information (vCard).
  - **Email:** Generate a QR code with an email address, subject, and body for direct email sending.
  - **SMS:** Generate a QR code with a phone number for quick messaging.
  - **Call:** QR code for direct phone calls.
  - **Event:** QR code for quick calendar event setup.
  - **Location:** QR code based on your current or selected location to start navigation.
  - **Barcode:** Generate various types of barcodes.
- **Preview:** Preview the generated code before saving or sharing.

### 🗂️ Code Repository

- **Saved codes list:** Browse all the codes you have chosen to save while using the app.
- **Code details:** View code details, enlarge the image, and access actions like "copy", "search", and more, depending on the code type.

## 🛠️ Built With

- **Kotlin with Jetpack Compose**
- **MlKit**
- **Room Database** for data persistence
- **View/Insert/Dial... Intents** (Calendar Event, Mail, SMS, Contact, Location)
