# Light News - Frontend Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture & Design](#architecture--design)
3. [Features & Functionality](#features--functionality)
4. [Technical Stack](#technical-stack)
5. [Project Structure](#project-structure)
6. [Component Details](#component-details)
7. [Authentication Flow](#authentication-flow)
8. [API Integration](#api-integration)
9. [UI/UX Design](#uiux-design)
10. [Text-to-Speech (TTS) Implementation](#text-to-speech-tts-implementation)
11. [Swipe Gesture Handling](#swipe-gesture-handling)
12. [Data Flow](#data-flow)
13. [Setup & Installation](#setup--installation)
14. [Frequently Asked Questions (FAQ)](#frequently-asked-questions-faq)
15. [Code Examples](#code-examples)
16. [Design Decisions](#design-decisions)
17. [Future Enhancements](#future-enhancements)

---

## Project Overview

**Light News** is an AI-powered news aggregation Android application that provides a personalized news reading experience through swipe-based interactions. The app leverages machine learning algorithms to curate news articles based on user preferences and reading patterns.

### Key Highlights
- **Swipe-based Interface**: Users swipe left (dislike) or right (like) on news cards
- **AI Personalization**: Backend uses Thompson Sampling to learn user preferences
- **Accessibility**: Built-in Text-to-Speech for visually impaired users
- **Real-time Updates**: Progressive news feed that adapts to user behavior
- **Category Filtering**: Filter news by topics (business, technology, sports, etc.)

---

## Architecture & Design

### Architecture Pattern
The app follows **MVVM (Model-View-ViewModel) architecture** with the following layers:

1. **View Layer**: Activities, Fragments, Adapters
2. **ViewModel/Repository Layer**: Data management and business logic
3. **Model Layer**: Data classes and DTOs
4. **Network Layer**: Retrofit for API communication

### Design Principles
- **Single Responsibility**: Each class has one clear purpose
- **Separation of Concerns**: UI, business logic, and data access are separated
- **Dependency Injection**: Using singleton pattern for repository
- **Reactive Programming**: Callbacks for async operations

### App Flow
```
AuthActivity (Login/Register) 
    ↓
MainActivity (Container)
    ↓
HomeFragment (News Cards)
    ↓
Swipe → Backend API → More Articles
```

---

## Features & Functionality

### 1. Authentication System
- **Login Screen**: Username and password authentication
- **Registration Screen**: New user signup
- **Tab-based Navigation**: Swipe between Login and Register screens
- **JWT Token Management**: Secure token storage and automatic header injection

### 2. News Feed
- **Card-based Display**: Full-screen news cards with images
- **Swipe Gestures**: 
  - Swipe Right → Like article
  - Swipe Left → Dislike article
- **Progressive Loading**: After every 5 swipes, fetch 5 new articles
- **Category Filtering**: Filter articles by 7 categories

### 3. Text-to-Speech (TTS)
- **Accessibility**: Reads article title and summary aloud
- **Toggle Control**: Enable/disable TTS via switch
- **Auto-read**: Automatically reads next card after swipe
- **Manual Read**: Button to read current card
- **Smart Stopping**: Prevents duplicate reads and stops previous reads

### 4. User Personalization
- **Category Preferences**: Select preferred news categories
- **AI Learning**: Backend learns from swipe patterns
- **Persistent Preferences**: Saved in SharedPreferences

---

## Technical Stack

### Core Technologies
- **Language**: Java
- **Platform**: Android (API 24+)
- **Build Tool**: Gradle
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

### Libraries & Dependencies
```gradle
// UI Components
implementation 'androidx.appcompat:appcompat:1.7.0'
implementation 'com.google.android.material:material:1.12.0'
implementation 'androidx.constraintlayout:constraintlayout:2.2.0'
implementation 'androidx.recyclerview:recyclerview:1.3.2'
implementation 'androidx.cardview:cardview:1.0.0'

// Architecture Components
implementation 'androidx.lifecycle:lifecycle-livedata:2.8.6'
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.8.6'

// Networking
implementation 'com.squareup.retrofit2:retrofit:2.11.0'
implementation 'com.squareup.retrofit2:converter-gson:2.11.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'

// Image Loading
implementation 'com.github.bumptech.glide:glide:4.16.0'
```

---

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/ainews/
│   │   │   ├── MainActivity.java              # Main container activity
│   │   │   ├── auth/
│   │   │   │   ├── AuthActivity.java          # Login/Register screen
│   │   │   │   ├── AuthPagerAdapter.java      # Tab adapter
│   │   │   │   ├── LoginFragment.java         # Login UI
│   │   │   │   └── RegisterFragment.java      # Register UI
│   │   │   ├── ui/
│   │   │   │   ├── HomeFragment.java          # News feed screen
│   │   │   │   └── adapter/
│   │   │   │       └── ArticleAdapter.java    # RecyclerView adapter
│   │   │   ├── model/
│   │   │   │   └── Article.java               # Article data model
│   │   │   ├── data/
│   │   │   │   ├── NewsRepository.java        # Data repository
│   │   │   │   ├── ApiClient.java             # Retrofit client
│   │   │   │   ├── TokenStore.java             # JWT token storage
│   │   │   │   ├── api/
│   │   │   │   │   └── NewsApiService.java    # API endpoints
│   │   │   │   └── dto/
│   │   │   │       ├── ArticleDto.java        # API response DTOs
│   │   │   │       ├── AuthRequest.java
│   │   │   │       ├── AuthResponse.java
│   │   │   │       ├── InitRequest.java
│   │   │   │       ├── InitResponse.java
│   │   │   │       ├── SwipeRequest.java
│   │   │   │       ├── SwipeResponse.java
│   │   │   │       └── FeedResponse.java
│   │   │   └── ...
│   │   └── res/
│   │       ├── layout/                         # XML layouts
│   │       ├── drawable/                       # Icons and images
│   │       ├── values/                         # Strings, colors, themes
│   │       └── menu/                           # Bottom navigation menu
│   └── AndroidManifest.xml
├── build.gradle
└── ...
```

---

## Component Details

### 1. MainActivity
**Purpose**: Main container that hosts the HomeFragment

**Responsibilities**:
- Fragment container management
- Navigation between screens (currently only Home)

**Key Methods**:
- `onCreate()`: Initialize fragment container
- `replaceFragment()`: Replace current fragment

### 2. AuthActivity
**Purpose**: Handles user authentication (Login/Register)

**Features**:
- ViewPager2 with TabLayout for switching between Login/Register
- JWT token retrieval and storage
- Automatic navigation to MainActivity on success

**Key Components**:
- `LoginFragment`: Login UI and logic
- `RegisterFragment`: Registration UI and logic
- `AuthPagerAdapter`: Manages tab pages

### 3. HomeFragment
**Purpose**: Main news feed screen with swipeable cards

**Features**:
- RecyclerView with swipe gestures
- TTS integration
- Category filtering
- Progressive article loading

**Key Methods**:
- `onCreateView()`: Initialize UI and data
- `onDestroyView()`: Cleanup TTS resources
- Swipe callbacks: Handle like/dislike gestures

### 4. ArticleAdapter
**Purpose**: RecyclerView adapter for news cards

**Responsibilities**:
- Bind article data to card views
- Handle card click (opens article URL)
- Image loading with Glide

### 5. NewsRepository
**Purpose**: Central data repository for API calls

**Responsibilities**:
- User authentication (login/register)
- Fetching news articles
- Sending swipe feedback
- Managing user preferences

**Key Methods**:
- `bootstrapUser()`: Authenticate and initialize user
- `initFeed()`: Get initial 10 articles
- `swipe()`: Send feedback and get 5 new articles
- `feed()`: Get articles by category

### 6. ApiClient
**Purpose**: Retrofit client configuration

**Features**:
- Base URL configuration (http://10.0.2.2:3000 for emulator)
- JWT token interceptor (automatic header injection)
- HTTP logging interceptor
- Gson converter for JSON parsing

### 7. TokenStore
**Purpose**: Secure token storage

**Implementation**:
- Uses SharedPreferences for persistence
- Static methods for global access
- Automatic token injection in API requests

---

## Authentication Flow

### Registration Flow
```
1. User enters username and password
2. POST /api/auth/register
3. Backend creates user → Returns JWT token
4. Store token in TokenStore
5. POST /api/init (with token) → Get 10 initial articles
6. Navigate to MainActivity (HomeFragment)
```

### Login Flow
```
1. User enters username and password
2. POST /api/auth/login
3. Backend validates → Returns JWT token
4. Store token in TokenStore
5. POST /api/init (with token) → Get 10 initial articles
6. Navigate to MainActivity (HomeFragment)
```

### Token Management
- **Storage**: SharedPreferences (persistent across app restarts)
- **Injection**: Automatic via OkHttp interceptor
- **Format**: `Authorization: Bearer <token>`
- **Scope**: All API requests after login

### Error Handling
- **401 Unauthorized**: Token expired or invalid → Redirect to login
- **400 Bad Request**: User already exists (register) → Auto-login
- **Network Errors**: Show toast messages, allow retry

---

## API Integration

### Base Configuration
- **Base URL**: `http://10.0.2.2:3000/` (Android emulator)
- **Physical Device**: Use PC's IP address (e.g., `http://192.168.x.x:3000/`)
- **Protocol**: HTTP (cleartext enabled for development)
- **Timeout**: Default OkHttp timeout (10 seconds)

### API Endpoints

#### 1. Authentication
```java
POST /api/auth/register
Body: { "userId": "string", "password": "string" }
Response: { "token": "jwt_token" }

POST /api/auth/login
Body: { "userId": "string", "password": "string" }
Response: { "token": "jwt_token" }
```

#### 2. News Feed
```java
POST /api/init
Headers: Authorization: Bearer <token>
Body: { "userId": "string", "filters": ["string"], "diversify": true }
Response: { "articles": [...], "forcedDiversify": false }

POST /api/swipe
Headers: Authorization: Bearer <token>
Body: { 
  "userId": "string",
  "events": [
    { "category": "string", "articleUrl": "string", "reaction": "like|dislike" }
  ]
}
Response: { "articles": [...] }

GET /api/feed?userId=string&category=string&pageSize=10
Headers: Authorization: Bearer <token>
Response: { "articles": [...] }
```

### DTOs (Data Transfer Objects)

**ArticleDto**: Represents article from API
```java
- title: String
- description: String
- url: String
- urlToImage: String
- category: String
- source: SourceDto (or String)
- publishedAt: String
```

**Custom Deserializer**: Handles `source` as both string and object
```java
// Backend may return:
// "source": "CNN" (string)
// OR
// "source": { "name": "CNN" } (object)
```

### Error Handling
- **Network Errors**: Logged, returns empty list
- **HTTP Errors**: Status codes logged, graceful degradation
- **JSON Parsing**: Custom deserializers handle variations
- **Timeout**: OkHttp timeout, user notified

---

## UI/UX Design

### Design Philosophy
- **Minimalist**: Clean, simple interface
- **Card-based**: Large, swipeable cards for easy interaction
- **Accessible**: TTS support for visually impaired users
- **Responsive**: Adapts to different screen sizes

### Color Scheme
```xml
Primary: #111111 (Dark Gray)
Secondary: #0A84FF (Blue)
Background: #FFFFFF (White)
Text: #111111 (Dark Gray)
Accent: #0A7E07 (Green for Like), #B00020 (Red for Dislike)
```

### Layout Structure

#### Auth Screen
- **Top**: TabLayout (Login/Register tabs)
- **Center**: ViewPager2 with form fields
- **Bottom**: Action button (Login/Register)

#### Home Screen
- **Top Right**: TTS toggle, Read Article button, Filter button
- **Center**: RecyclerView with full-screen cards
- **Cards**: Image (top 2/3), Title and Summary (bottom 1/3)

### Animations
- **Swipe**: Smooth card removal animation
- **Feedback**: "Liked"/"Disliked" overlay fade in/out
- **Loading**: Empty state shows "Loading..."

---

## Text-to-Speech (TTS) Implementation

### Overview
TTS provides accessibility for visually impaired users by reading article content aloud.

### Features
1. **Onboarding**: First launch reads "Swipe left if you don't like the news, swipe right if you do."
2. **Auto-read**: After swipe, automatically reads next card
3. **Manual Read**: Button to read current card
4. **Toggle Control**: Enable/disable TTS via switch

### Implementation Details

#### Initialization
```java
tts = new TextToSpeech(context, status -> {
    ttsReady = status == TextToSpeech.SUCCESS && 
               tts.setLanguage(Locale.getDefault()) >= 0;
});
```

#### Reading Article
```java
String text = article.getTitle() + ". " + article.getSummary();
tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "article_id");
```

#### Preventing Duplicates
- Track `currentlyReadingArticle` to prevent re-reading same article
- Use `tts.stop()` before starting new read
- Check `tts.isSpeaking()` before manual read

#### Cleanup
```java
@Override
public void onDestroyView() {
    if (tts != null) {
        tts.stop();
        tts.shutdown();
    }
}
```

### TTS States
- **Ready**: TTS engine initialized and language set
- **Speaking**: Currently reading an article
- **Stopped**: TTS stopped, ready for next read
- **Disabled**: User toggled TTS off

---

## Swipe Gesture Handling

### Implementation
Uses Android's `ItemTouchHelper` with `SwipeCallback`:

```java
ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
    0, // No drag support
    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT // Swipe directions
) {
    @Override
    public void onSwiped(ViewHolder holder, int direction) {
        // direction == RIGHT → Like
        // direction == LEFT → Dislike
    }
};
```

### Swipe Actions
1. **Right Swipe (Like)**:
   - Remove card from list
   - Add to feedback buffer
   - Show "Liked" overlay
   - Read "Liked" via TTS
   - After 5 swipes: Send feedback to backend, get 5 new articles

2. **Left Swipe (Dislike)**:
   - Remove card from list
   - Add to feedback buffer
   - Show "Disliked" overlay
   - Read "Disliked" via TTS
   - After 5 swipes: Send feedback to backend, get 5 new articles

### Progressive Loading
- **Batch Size**: 5 articles per request
- **Trigger**: After every 5 swipes
- **Feedback**: Sends all 5 swipe events in one API call
- **Response**: Receives 5 new personalized articles

### Feedback Buffer
- Stores swipe events temporarily
- Flushed after every 5 swipes
- Contains: category, articleUrl, reaction (like/dislike)

---

## Data Flow

### Initial Load Flow
```
1. User logs in → Get JWT token
2. POST /api/init → Get 10 articles
3. Map ArticleDto → Article model
4. Display in RecyclerView
5. User swipes cards
```

### Swipe Feedback Flow
```
1. User swipes card (left/right)
2. Create SwipeEvent → Add to buffer
3. Remove card from list
4. After 5 swipes:
   a. POST /api/swipe with 5 events
   b. Backend processes feedback
   c. Returns 5 new personalized articles
   d. Add to list
```

### Category Filter Flow
```
1. User taps filter button
2. Show category dialog
3. User selects category
4. GET /api/feed?category=X
5. Replace articles in list
6. Display filtered articles
```

### Token Flow
```
1. Login/Register → Get token
2. Store in SharedPreferences
3. OkHttp interceptor adds to all requests:
   Authorization: Bearer <token>
4. Backend validates token
5. Returns data or 401 if invalid
```

---

## Setup & Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+
- Java 11+
- Gradle 7.0+

### Backend Setup
1. Ensure backend is running on `localhost:3000`
2. For emulator: Use `http://10.0.2.2:3000`
3. For physical device: Use PC's IP address

### Installation Steps

1. **Clone Repository**
```bash
git clone <repository-url>
cd madlabproj
```

2. **Open in Android Studio**
- File → Open → Select project folder
- Wait for Gradle sync

3. **Configure Backend URL**
- Edit `app/src/main/java/.../ApiClient.java`
- Update `baseUrl` if needed

4. **Run App**
- Connect device or start emulator
- Click Run button (Shift+F10)
- App installs and launches

### Configuration

#### Network Configuration
```java
// ApiClient.java
baseUrl("http://10.0.2.2:3000/") // Emulator
// OR
baseUrl("http://192.168.x.x:3000/") // Physical device
```

#### AndroidManifest
```xml
<uses-permission android:name="android.permission.INTERNET" />
<application android:usesCleartextTraffic="true">
```

---

## Frequently Asked Questions (FAQ)

### General Questions

**Q: What is Light News?**
A: Light News is an AI-powered news aggregation app that personalizes content based on user swipe patterns.

**Q: What platforms does it support?**
A: Android 7.0 (API 24) and above.

**Q: Is it free to use?**
A: Yes, the app is free. Backend uses NewsAPI free tier (limited to 100 articles).

### Technical Questions

**Q: Why Java instead of Kotlin?**
A: Project requirements specified Java. The architecture supports easy migration to Kotlin.

**Q: Why use Retrofit instead of Volley?**
A: Retrofit provides better type safety, automatic JSON parsing, and cleaner code.

**Q: How is data persisted?**
A: User ID and JWT token stored in SharedPreferences. Articles are not persisted (real-time from API).

**Q: Why cleartext traffic enabled?**
A: Development backend uses HTTP. For production, use HTTPS and remove `usesCleartextTraffic`.

### Feature Questions

**Q: How does the AI personalization work?**
A: Backend uses Thompson Sampling algorithm. Frontend sends swipe feedback, backend learns preferences, returns personalized articles.

**Q: Why progressive loading (5 articles at a time)?**
A: Reduces API calls, improves performance, and provides fresh content based on recent feedback.

**Q: How does TTS work?**
A: Android's TextToSpeech engine reads article title and summary. Can be toggled on/off.

**Q: Can I read articles manually?**
A: Yes, tap the "Read Article" button in the top-right corner.

### UI/UX Questions

**Q: Why card-based design?**
A: Cards are easier to swipe, provide better visual focus, and match modern mobile UX patterns.

**Q: Why only one screen (Home)?**
A: Initial design focused on core swipe functionality. Additional screens can be added later.

**Q: How do I filter by category?**
A: Tap the filter icon (top-right), select a category, tap "Apply".

**Q: What happens if I swipe all cards?**
A: After every 5 swipes, 5 new articles are automatically loaded.

### API Questions

**Q: Why does init sometimes return 500?**
A: Backend hits NewsAPI's 100-article limit. Backend needs to reset pagination. Frontend handles gracefully.

**Q: How are API errors handled?**
A: Network errors logged, user sees empty list. HTTP errors logged with status codes. User can retry.

**Q: Is the token stored securely?**
A: Token stored in SharedPreferences (not encrypted). For production, use EncryptedSharedPreferences.

**Q: What if token expires?**
A: Backend returns 401, app should redirect to login (not currently implemented).

### Development Questions

**Q: How to test on physical device?**
A: 
1. Find PC's IP: `ipconfig` (Windows) or `ifconfig` (Linux/Mac)
2. Update `ApiClient.java` baseUrl to PC's IP
3. Ensure phone and PC on same network
4. Run app

**Q: How to debug API calls?**
A: Check Logcat filtered by "NewsRepository" or "OkHttpClient" for HTTP logs.

**Q: How to add new features?**
A: 
1. Add UI components in layouts
2. Add logic in fragments/activities
3. Add API endpoints in NewsApiService
4. Update DTOs if needed

**Q: How to change backend URL?**
A: Edit `app/src/main/java/.../ApiClient.java`, update `baseUrl()` method.

---

## Code Examples

### Making an API Call
```java
// In NewsRepository
public void initFeed(String userId, List<String> filters, boolean diversify, 
                     ArticlesCallback cb) {
    InitRequest req = new InitRequest();
    req.userId = userId;
    req.filters = filters;
    req.diversify = diversify;
    
    api.init(req).enqueue(new Callback<InitResponse>() {
        @Override
        public void onResponse(Call<InitResponse> call, Response<InitResponse> response) {
            if (response.isSuccessful()) {
                List<Article> articles = map(response.body().articles);
                cb.onResult(articles);
            }
        }
        @Override
        public void onFailure(Call<InitResponse> call, Throwable t) {
            cb.onResult(new ArrayList<>());
        }
    });
}
```

### Handling Swipe Gesture
```java
@Override
public void onSwiped(ViewHolder holder, int direction) {
    Article swiped = articles.get(position);
    String reaction = direction == RIGHT ? "like" : "dislike";
    
    // Create event
    SwipeEvent event = new SwipeEvent();
    event.category = swiped.getCategory();
    event.articleUrl = swiped.getUrl();
    event.reaction = reaction;
    
    // Add to buffer
    feedbackBuffer.add(event);
    
    // Remove card
    articles.remove(position);
    adapter.notifyItemRemoved(position);
    
    // Send after 5 swipes
    if (swipeCount % 5 == 0) {
        sendFeedbackToBackend();
    }
}
```

### Using TTS
```java
// Initialize
tts = new TextToSpeech(context, status -> {
    ttsReady = (status == TextToSpeech.SUCCESS && 
                tts.setLanguage(Locale.getDefault()) >= 0);
});

// Read text
if (ttsReady && ttsEnabled) {
    tts.stop(); // Stop previous
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id");
}

// Cleanup
@Override
public void onDestroyView() {
    if (tts != null) {
        tts.stop();
        tts.shutdown();
    }
}
```

### Storing JWT Token
```java
// Store
TokenStore.setToken(token);

// Retrieve
String token = TokenStore.getToken();

// Automatic injection via OkHttp interceptor
// No manual header setting needed
```

---

## Design Decisions

### Why MVVM?
- **Separation**: UI logic separate from business logic
- **Testability**: Easy to unit test ViewModels
- **Maintainability**: Clear structure for future developers

### Why Singleton Repository?
- **Single Source of Truth**: One instance manages all data
- **Thread Safety**: Synchronized access
- **Memory Efficiency**: One instance shared across app

### Why Retrofit over Volley?
- **Type Safety**: Compile-time type checking
- **Less Boilerplate**: Automatic JSON parsing
- **Modern**: Better callback handling
- **Industry Standard**: Widely used in Android

### Why Card-based UI?
- **Mobile-first**: Natural swipe gestures
- **Focus**: One article at a time
- **Visual**: Large images improve engagement
- **Accessible**: Easier for TTS to read

### Why Progressive Loading?
- **Performance**: Don't load all articles at once
- **Freshness**: New articles based on recent feedback
- **Network**: Reduce API calls
- **UX**: Smooth continuous experience

### Why TTS?
- **Accessibility**: Support visually impaired users
- **Hands-free**: Users can listen while doing other tasks
- **Inclusive**: Makes app usable by broader audience

---

## Future Enhancements

### Planned Features
1. **Search Functionality**: Search articles by keywords
2. **Article Details**: Full article view with full text
3. **Bookmarks**: Save articles for later
4. **Share Feature**: Share articles via social media
5. **Offline Mode**: Cache articles for offline reading
6. **Push Notifications**: Breaking news alerts
7. **Dark Mode**: Theme switching
8. **Multiple Languages**: Support for multiple languages

### Technical Improvements
1. **Migration to Kotlin**: Modern Android development
2. **Room Database**: Local article caching
3. **WorkManager**: Background article fetching
4. **Encrypted Storage**: Secure token storage
5. **Unit Tests**: Comprehensive test coverage
6. **CI/CD**: Automated testing and deployment

---

## Feature-to-File Mapping

### Quick Reference: Where Each Feature Lives

#### Authentication Features

| Feature | File | Key Methods/Lines |
|---------|------|------------------|
| Login Screen UI | `auth/LoginFragment.java` | `onCreateView()` (line 21-52) |
| Register Screen UI | `auth/RegisterFragment.java` | `onCreateView()` (line 21-52) |
| Login/Register Tab Switcher | `auth/AuthActivity.java` | `onCreate()` with TabLayoutMediator (line 23-27) |
| Tab Layout | `res/layout/activity_auth.xml` | TabLayout + ViewPager2 (line 8-15) |
| Auth Form Layout | `res/layout/fragment_auth_simple.xml` | EditText fields + Button (line 17-38) |
| Login API Call | `data/NewsRepository.java` | `bootstrapUser()` (line 59-151) |
| Register API Call | `data/NewsRepository.java` | `bootstrapUser()` with `isSignup=true` |
| JWT Token Storage | `data/TokenStore.java` | `setToken()`, `getToken()` (line 12-22) |
| Token Auto-injection | `data/ApiClient.java` | OkHttp interceptor (line 18-25) |

#### News Feed Features

| Feature | File | Key Methods/Lines |
|---------|------|------------------|
| News Cards Display | `ui/HomeFragment.java` | `onCreateView()` (line 42-195) |
| Card Layout | `res/layout/item_article.xml` | CardView with Image + Text (line 1-38) |
| RecyclerView Setup | `ui/HomeFragment.java` | `onCreateView()` line 43-44, 60-61 |
| Article List Adapter | `ui/adapter/ArticleAdapter.java` | `onBindViewHolder()` (line 35-49) |
| Image Loading | `ui/adapter/ArticleAdapter.java` | Glide.load() (line 39-42) |
| Article Click (Open URL) | `ui/adapter/ArticleAdapter.java` | `setOnClickListener()` (line 44-48) |

#### Swipe Gesture Features

| Feature | File | Key Methods/Lines |
|---------|------|------------------|
| Swipe Detection | `ui/HomeFragment.java` | `ItemTouchHelper.SimpleCallback` (line 63-133) |
| Swipe Left (Dislike) | `ui/HomeFragment.java` | `onSwiped()` direction == LEFT (line 70-132) |
| Swipe Right (Like) | `ui/HomeFragment.java` | `onSwiped()` direction == RIGHT (line 70-132) |
| Swipe Feedback Overlay | `ui/HomeFragment.java` | `tvSwipeFeedback` (line 94-98) |
| Swipe Event Creation | `ui/HomeFragment.java` | `SwipeEvent` object (line 75-79) |
| Feedback Buffer | `ui/HomeFragment.java` | `feedbackBuffer` ArrayList (line 35) |
| Progressive Loading | `ui/HomeFragment.java` | `swipeCount % 5 == 0` (line 100-108) |
| Send Swipe to Backend | `data/NewsRepository.java` | `swipe()` method (line 153-169) |

#### TTS (Text-to-Speech) Features

| Feature | File | Key Methods/Lines |
|---------|------|------------------|
| TTS Initialization | `ui/HomeFragment.java` | `onCreateView()` TTS setup (line 176-183) |
| TTS Toggle Switch | `ui/HomeFragment.java` | `switchTTS` (line 47, 52-56) |
| TTS Toggle UI | `res/layout/fragment_home.xml` | Switch component (line 33-36) |
| Read Article Button | `ui/HomeFragment.java` | `btnReadArticle` click (line 137-152) |
| Read Article Button UI | `res/layout/fragment_home.xml` | ImageButton (line 38-45) |
| Auto-read Next Card | `ui/HomeFragment.java` | After swipe (line 115-129) |
| Read on Swipe | `ui/HomeFragment.java` | `onSwiped()` TTS call (line 86-91) |
| Stop TTS | `ui/HomeFragment.java` | `tts.stop()` (line 88, 123, 148) |
| TTS Cleanup | `ui/HomeFragment.java` | `onDestroyView()` (line 198-203) |
| Prevent Duplicate Reads | `ui/HomeFragment.java` | `currentlyReadingArticle` tracking (line 39, 119-129, 145-151) |
| TTS Preferences | `ui/HomeFragment.java` | SharedPreferences "tts_enabled" (line 51, 84, 115) |

#### Category Filtering Features

| Feature | File | Key Methods/Lines |
|---------|------|------------------|
| Filter Button UI | `res/layout/fragment_home.xml` | ImageButton (line 47-54) |
| Filter Button Click | `ui/HomeFragment.java` | `btnFilter.setOnClickListener()` (line 154-180) |
| Category Dialog | `ui/HomeFragment.java` | `AlertDialog.Builder` (line 160-179) |
| Category Selection | `ui/HomeFragment.java` | Single choice dialog (line 163-167) |
| Apply Filter | `ui/HomeFragment.java` | `setPositiveButton()` (line 168-177) |
| Fetch by Category | `data/NewsRepository.java` | `feed()` method (line 171-189) |
| Filter Icon | `res/drawable/ic_filter.xml` | Vector drawable |

#### API Integration Features

| Feature | File | Key Methods/Lines |
|---------|------|------------------|
| Retrofit Client | `data/ApiClient.java` | `getClient()` (line 14-29) |
| API Endpoints | `data/api/NewsApiService.java` | Interface with annotations (line 16-36) |
| Init Feed API | `data/NewsRepository.java` | `initFeed()` (line 153-169) |
| Swipe API | `data/NewsRepository.java` | `swipe()` (line 171-189) |
| Feed by Category API | `data/NewsRepository.java` | `feed()` (line 171-189) |
| DTO Mapping | `data/NewsRepository.java` | `map()` method (line 191-207) |
| Error Handling | `data/NewsRepository.java` | Callback error handlers (all methods) |
| Logging | `data/NewsRepository.java` | `android.util.Log` calls throughout |

#### Data Models

| Feature | File | Key Properties |
|---------|------|----------------|
| Article Model | `model/Article.java` | title, summary, source, timeAgo, imageUrl, url, category |
| Article DTO | `data/dto/ArticleDto.java` | API response format with source deserializer |
| Auth Request | `data/dto/AuthRequest.java` | userId, password |
| Auth Response | `data/dto/AuthResponse.java` | token (with getToken() helper) |
| Swipe Event | `data/dto/SwipeEvent.java` | category, articleUrl, reaction |
| Init Request | `data/dto/InitRequest.java` | userId, filters, diversify |
| Init Response | `data/dto/InitResponse.java` | articles, forcedDiversify |

#### UI Components & Layouts

| Feature | File | Description |
|---------|------|-------------|
| Main Activity Layout | `res/layout/activity_main.xml` | FrameLayout container for fragments |
| Home Fragment Layout | `res/layout/fragment_home.xml` | RecyclerView + TTS controls + Filter button |
| Article Card Layout | `res/layout/item_article.xml` | CardView with ImageView and TextViews |
| Auth Activity Layout | `res/layout/activity_auth.xml` | TabLayout + ViewPager2 |
| Auth Form Layout | `res/layout/fragment_auth_simple.xml` | Username, Password, Button |
| Bottom Nav Menu | `res/menu/bottom_nav_menu.xml` | Navigation menu (not currently used) |
| App Theme | `res/values/themes.xml` | Material3 theme |
| Colors | `res/values/colors.xml` | App color palette |
| Strings | `res/values/strings.xml` | All text resources |

---

## Animation Implementation Details

### 1. Card Swipe Animation

**Location**: `ui/HomeFragment.java` + Android's ItemTouchHelper

**Code**:
```java
// Line 63-133 in HomeFragment.java
ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
    0, // No drag support
    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT // Swipe directions
) {
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, 
                         @NonNull RecyclerView.ViewHolder viewHolder, 
                         @NonNull RecyclerView.ViewHolder target) {
        return false; // No drag support
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Card automatically slides out (built-in animation)
        int position = viewHolder.getBindingAdapterPosition();
        articles.remove(position);
        adapter.notifyItemRemoved(position); // Triggers removal animation
    }
};

new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
```

**How it works**:
- Android's `ItemTouchHelper` provides built-in swipe animations
- When `notifyItemRemoved()` is called, RecyclerView automatically:
  - Slides the card out in swipe direction
  - Fades out opacity
  - Animates remaining cards to fill space
- Animation duration: ~300ms (default RecyclerView animation)

### 2. Swipe Feedback Overlay Animation

**Location**: `ui/HomeFragment.java` line 94-98

**Code**:
```java
// Show overlay feedback text
tvSwipeFeedback.setText(feedback.equals("like") ? "Liked" : "Disliked");
tvSwipeFeedback.setTextColor(feedback.equals("like") ? 0xFF0A7E07 : 0xFFB00020);
tvSwipeFeedback.setVisibility(View.VISIBLE); // Fade in (default animation)

// Auto-hide after 700ms
tvSwipeFeedback.postDelayed(() -> 
    tvSwipeFeedback.setVisibility(View.GONE), // Fade out (default animation)
700);
```

**How it works**:
- `setVisibility(View.VISIBLE)` triggers default fade-in animation
- `setVisibility(View.GONE)` triggers default fade-out animation
- Duration: ~200ms (Android default)
- Uses `postDelayed()` to schedule auto-hide

**Visual Effect**:
- Text appears centered with semi-transparent background
- Fades in smoothly
- Displays for 700ms
- Fades out smoothly

### 3. RecyclerView Item Animations

**Location**: Default RecyclerView animations (no custom code needed)

**How it works**:
- When items are added: `notifyItemRangeInserted()` triggers slide-up animation
- When items are removed: `notifyItemRemoved()` triggers slide-out animation
- When items change: `notifyDataSetChanged()` triggers cross-fade animation

**Code Example** (Adding new articles):
```java
// Line 104-106 in HomeFragment.java
int start = articles.size();
articles.addAll(more);
adapter.notifyItemRangeInserted(start, more.size()); // Animates new items sliding up
```

### 4. Tab Switch Animation (Auth Screen)

**Location**: `auth/AuthActivity.java` + ViewPager2

**Code**:
```java
// Line 23-27 in AuthActivity.java
ViewPager2 pager = findViewById(R.id.pager);
TabLayout tabs = findViewById(R.id.tabLayout);
pager.setAdapter(new AuthPagerAdapter(this));

new TabLayoutMediator(tabs, pager, (tab, position) -> {
    tab.setText(position == 0 ? "Login" : "Signup");
}).attach();
```

**How it works**:
- ViewPager2 provides built-in page transition animations
- Swipe left/right triggers smooth page slide
- Tab indicator animates to selected tab
- Default transition: slide with ~300ms duration

**Layout**: `res/layout/activity_auth.xml`
```xml
<com.google.android.material.tabs.TabLayout
    android:id="@+id/tabLayout" ... />

<androidx.viewpager2.widget.ViewPager2
    android:id="@+id/pager" ... />
```

### 5. Button State Animations

**Location**: `auth/LoginFragment.java` and `RegisterFragment.java`

**Code**:
```java
// Line 34-36 in LoginFragment.java
btn.setEnabled(false); // Disables button (visual feedback)
btn.setText("Logging in..."); // Shows loading state

// After response:
btn.setEnabled(true); // Re-enables (smooth state change)
btn.setText("Login");
```

**How it works**:
- Android automatically animates button state changes
- Enabled → Disabled: opacity fade to 0.5
- Text change: instant (no animation)
- Visual feedback helps user understand state

### 6. Loading State Animation

**Location**: `res/layout/fragment_home.xml` line 11-16

**Code**:
```xml
<TextView
    android:id="@+id/tvEmpty"
    android:text="Loading..."
    android:layout_gravity="center" />
```

**Programmatic Control**:
```java
// Line 111-113, 166-167 in HomeFragment.java
if (articles.isEmpty()) {
    tvEmpty.setVisibility(View.VISIBLE); // Fade in
} else {
    tvEmpty.setVisibility(View.GONE); // Fade out
}
```

**How it works**:
- Visibility changes trigger fade animations
- Default duration: ~200ms
- Provides visual feedback when loading/empty

### 7. Image Loading Animation (Glide)

**Location**: `ui/adapter/ArticleAdapter.java` line 39-42

**Code**:
```java
Glide.with(holder.thumb.getContext())
    .load(article.getImageUrl())
    .placeholder(R.mipmap/ic_launcher) // Shows while loading
    .into(holder.thumb); // Fade-in animation when loaded
```

**How it works**:
- Glide automatically provides fade-in animation when image loads
- Placeholder shows immediately
- When image loads, cross-fades from placeholder to image
- Duration: ~300ms (Glide default)

### 8. Card View Elevation Animation

**Location**: `res/layout/item_article.xml` (CardView automatically handles)

**Code**:
```xml
<androidx.cardview.widget.CardView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_margin="16dp"
    app:cardElevation="4dp"> <!-- Creates shadow effect -->
```

**How it works**:
- CardView provides elevation-based shadows
- Shadow appears/disappears with visibility changes
- Creates depth perception (3D effect)
- No additional code needed

---

## Complete Code Snippets for Quick Reference

### Show: Login Implementation
**File**: `auth/LoginFragment.java`
```java
// Lines 21-52
@Override
public View onCreateView(@NonNull LayoutInflater inflater, 
                        @Nullable ViewGroup container, 
                        @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_auth_simple, container, false);
    EditText etUser = v.findViewById(R.id.etUsername);
    EditText etPass = v.findViewById(R.id.etPassword);
    Button btn = v.findViewById(R.id.btnAction);
    btn.setText("Login");
    
    btn.setOnClickListener(view -> {
        String userId = etUser.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        
        NewsRepository.getInstance().setUserId(requireContext(), userId);
        btn.setEnabled(false);
        btn.setText("Logging in...");
        
        NewsRepository.getInstance().bootstrapUser(userId, password, false, 
            new BootstrapCallback() {
                @Override
                public void onSuccess(InitResponse response) {
                    requireActivity().runOnUiThread(() -> 
                        ((AuthActivity) requireActivity()).onAuthSuccess()
                    );
                }
                @Override
                public void onError() {
                    requireActivity().runOnUiThread(() -> {
                        btn.setEnabled(true);
                        btn.setText("Login");
                        Toast.makeText(requireContext(), "Login failed", 
                                     Toast.LENGTH_SHORT).show();
                    });
                }
            }
        );
    });
    return v;
}
```

### Show: Swipe Gesture Implementation
**File**: `ui/HomeFragment.java`
```java
// Lines 63-133
ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
) {
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getBindingAdapterPosition();
        if (position < 0 || position >= articles.size()) return;
        
        Article swiped = articles.get(position);
        String feedback = direction == ItemTouchHelper.RIGHT ? "like" : "dislike";
        
        // Create swipe event
        SwipeEvent event = new SwipeEvent();
        event.category = swiped.getCategory();
        event.articleUrl = swiped.getUrl();
        event.reaction = feedback;
        feedbackBuffer.add(event);
        
        swipeCount++;
        articles.remove(position);
        adapter.notifyItemRemoved(position); // Triggers slide-out animation
        
        // TTS feedback
        if (ttsReady && prefs.getBoolean("tts_enabled", true)) {
            tts.stop();
            tts.speak(feedback.equals("like") ? "Liked" : "Disliked", 
                     TextToSpeech.QUEUE_FLUSH, null, "swipe_feedback");
        }
        
        // Show overlay
        tvSwipeFeedback.setText(feedback.equals("like") ? "Liked" : "Disliked");
        tvSwipeFeedback.setVisibility(View.VISIBLE);
        tvSwipeFeedback.postDelayed(() -> tvSwipeFeedback.setVisibility(View.GONE), 700);
        
        // Progressive loading
        if (swipeCount % 5 == 0) {
            NewsRepository.getInstance().swipe(userId, 
                new ArrayList<>(feedbackBuffer), 
                more -> {
                    int start = articles.size();
                    articles.addAll(more);
                    adapter.notifyItemRangeInserted(start, more.size());
                }
            );
            feedbackBuffer.clear();
        }
        
        // Auto-read next card
        if (!articles.isEmpty() && ttsReady && prefs.getBoolean("tts_enabled", true)) {
            Article nextArticle = articles.get(0);
            if (currentlyReadingArticle != nextArticle) {
                currentlyReadingArticle = nextArticle;
                tts.stop();
                tvSwipeFeedback.postDelayed(() -> {
                    String nextText = nextArticle.getTitle() + ". " + nextArticle.getSummary();
                    tts.speak(nextText, TextToSpeech.QUEUE_FLUSH, null, "next_article");
                }, 500);
            }
        }
    }
};
new ItemTouchHelper(callback).attachToRecyclerView(recyclerView);
```

### Show: TTS Implementation
**File**: `ui/HomeFragment.java`
```java
// Initialization (Lines 176-183)
tts = new TextToSpeech(requireContext(), status -> {
    ttsReady = status == TextToSpeech.SUCCESS && 
               tts.setLanguage(Locale.getDefault()) >= 0;
    if (ttsReady && !firstLaunchDone && prefs.getBoolean("tts_enabled", true)) {
        tts.speak("Swipe left if you don't like the news, swipe right if you do.", 
                 TextToSpeech.QUEUE_FLUSH, null, "onboarding");
        prefs.edit().putBoolean("first_launch_done", true).apply();
    }
});

// Read Article Button (Lines 137-152)
btnReadArticle.setOnClickListener(v -> {
    if (articles.isEmpty() || !ttsReady || !prefs.getBoolean("tts_enabled", true)) {
        Toast.makeText(requireContext(), "No article to read", Toast.LENGTH_SHORT).show();
        return;
    }
    Article currentArticle = articles.get(0);
    if (currentlyReadingArticle != currentArticle || !tts.isSpeaking()) {
        currentlyReadingArticle = currentArticle;
        tts.stop(); // Stop previous read
        String text = currentArticle.getTitle() + ". " + currentArticle.getSummary();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "read_article");
    }
});

// Cleanup (Lines 198-203)
@Override
public void onDestroyView() {
    super.onDestroyView();
    if (tts != null) {
        tts.stop();
        tts.shutdown();
    }
}
```

### Show: API Call Implementation
**File**: `data/NewsRepository.java`
```java
// Init Feed (Lines 153-169)
public void initFeed(String userId, @Nullable List<String> filters, 
                    boolean diversify, ArticlesCallback cb) {
    InitRequest req = new InitRequest();
    req.userId = userId;
    req.filters = filters;
    req.diversify = diversify;
    
    api.init(req).enqueue(new Callback<InitResponse>() {
        @Override
        public void onResponse(Call<InitResponse> call, Response<InitResponse> response) {
            if (response.isSuccessful()) {
                List<Article> mapped = map(response.body() != null ? 
                                          response.body().articles : null);
                cb.onResult(mapped);
            } else {
                android.util.Log.e("NewsRepository", "Init failed: " + response.code());
                cb.onResult(new ArrayList<>());
            }
        }
        @Override
        public void onFailure(Call<InitResponse> call, Throwable t) {
            android.util.Log.e("NewsRepository", "Init network error", t);
            cb.onResult(new ArrayList<>());
        }
    });
}
```

### Show: Token Auto-injection
**File**: `data/ApiClient.java`
```java
// Lines 18-25
OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(chain -> {
        okhttp3.Request original = chain.request();
        String token = TokenStore.getToken();
        okhttp3.Request.Builder builder = original.newBuilder();
        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        }
        return chain.proceed(builder.build());
    })
    .addInterceptor(logging)
    .build();
```

---

## Conclusion

Light News frontend provides a modern, accessible, and user-friendly news reading experience. The architecture is scalable, maintainable, and follows Android best practices. The integration with the AI-powered backend enables personalized content delivery based on user behavior.

For questions or contributions, please refer to the project repository or contact the development team.

---

**Document Version**: 1.1  
**Last Updated**: November 2025  
**Author**: Development Team


