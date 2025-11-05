# AI News (Android - Java + XML)

Frontend-only Android app showcasing an AI-driven news experience with Home, Search, Trending, and Profile screens. Clean repository/API layer is ready for backend integration.

## Features
- Bottom navigation across Home, Search, Trending, Profile
- RecyclerView article lists with thumbnails
- Search bar with in-UI results
- Personalization screen (mock preferences)
- Backend-connectable via repository and Retrofit placeholders

## Open in Android Studio
1. File → Open → select this folder (`madlabproj`).
2. Let Gradle sync finish. If prompted to upgrade Gradle/AGP, accept.
3. Run on an emulator (Pixel API 30+ recommended) or a device.

## Project Structure
- `app/src/main/java/com/example/ainews/` core app package
  - `MainActivity` with bottom navigation
  - `ui/` fragments and adapter
  - `model/` `Article` POJO
  - `data/` `NewsRepository` with sample data, ready to connect to backend
- `res/layout/` XML screens and item layouts
- `res/menu/` bottom nav menu
- `res/drawable/` vector icons

## Connect a Backend (Retrofit example)
- Add your API base URL in a new `ApiClient` and service interface.
- Replace `NewsRepository` demo methods to call the service (e.g., `getPersonalizedFeed`, `searchArticles`, `getTrendingHighlights`).
- Map DTOs to `Article` model.

## Notes
- Internet permission is enabled.
- Uses Glide for image loading.
- UI is Material 3 themed.


