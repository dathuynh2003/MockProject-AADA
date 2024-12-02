<h1 align="center">Movies App</h1>

<img src='art/img.png' width='25%'/><img src = 'art/img_2.png' width='25%'/><img src='art/img_1.png' width='25%'/><img src ='art/img_3.png' width='25%'/>
<img src='art/img_4.png' width='25%'/><img src = 'art/img_5.png' width='25%'/><img src='art/img_6.png' width='25%'/>

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Tech Stacks](#tech-stacks)
- [Architectures](#architectures)
- [Package Structures](#package-structures)
- [Installation](#installation)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This project is an Android app using [The Movie DB API](https://www.themoviedb.org/) based on MVVM architecture. It showcases the latest Android tech stacks with clean architecture and best practices.

## Features

- Feature 1: Save User Profile into Firebase
- Feature 2: Get all movies from API
- Feature 3: Add or remove favorite movie
- Feature 4: Manage movie reminders
- Feature 5: Filter movies by category
- Feature 6: Sort movies by rating, release date
- Feature 7: Search favorite movies by title
- Feature 8: Paging
- Feature 9: Push notification
- Feature 10: Show movie details

## Tech Stacks
* [Retrofit](http://square.github.io/retrofit/) + [OkHttp](http://square.github.io/okhttp/) - RESTful API and networking client.
* [Dagger](https://github.com/google/dagger) - Dependency injection.
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - A collections of libraries that help you design robust, testable and maintainable apps.
    * [Room](https://developer.android.com/training/data-storage/room) - Local persistence database.
    * [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - Pagination loading for RecyclerView.
    * [ViewModel](https://developer.android.com/reference/androidx/lifecycle/ViewModel) - UI related data holder, lifecycle aware.
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Observable data holder that notify views when underlying data changes.
    * [Data Binding](https://developer.android.com/topic/libraries/data-binding) - Declarative way to bind data to UI layout.
    * [Navigation component](https://developer.android.com/guide/navigation) - Fragment routing handler. (Upcoming)
    * [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) - Tasks scheduler in background jobs. (Upcoming)
* [RxJava](https://github.com/ReactiveX/RxJava) - Asynchronous programming with observable streams.
* [Glide](https://github.com/bumptech/glide) - Image loading.
* [Firebase](https://firebase.google.com/) - Backend services and storage for mobile apps.

## Architectures

![MVVM](./art/MyMovieApp_Architecture.png)

I follow Google recommended [Guide to app architecture](https://developer.android.com/jetpack/guide) to structure our architecture based on MVVM, reactive UI using LiveData / RxJava observables and data binding.

* **View**: Activity/Fragment with UI-specific logics only.
* **ViewModel**: It keeps the logic away from View layer, provides data streams for UI and handle user interactions.
* **Model**: Repository pattern, data layers that provide interface to manipulate data from both the local and remote data sources. The local data sources will serve as [single source of truth](https://en.wikipedia.org/wiki/Single_source_of_truth).

## Package Structures

Data Module: Data modeling and access layer
```
com.example.data        # Root Package
├── mapper              # Convert data between data layer and domain layer.
├── repository          # Repository pattern combining local and remote data.
└── source              # Data source for local and remote.
    ├── local           # Local persistence database
    │   ├── dao         # Data Access Objects for Room database
    │   └── entity      # Entity classes for Room database tables
    └── remote          # Remote data source
        ├── model       # Data models for network responses
        ├── paging      # Paging sources for large data sets
        └── service     # Retrofit services for API calls
```

Domain Module: Core business logic and use cases
```
com.example.domain      # Root Package
├── model               # Business models used across the app
├── repository          # Interfaces for repository implementations
├── usecase             # Use cases for handling business logic
│  └── base             # Base use cases for common logic
└── utils               # Utility classes for domain-specific operations
```

Presentation Module: User interface and ViewModel layer
```
com.example.mymovieapp  # Root Package
├── di                  # Dependency injection setup using Dagger
├── listener            # Interface callbacks for UI interactions
├── ui                  # User interface screens and components
│   ├── about           # About screen UI
│   ├── adapters        # Adapters for RecyclerViews and other lists
│   ├── base            # Base classes for common UI logic like movies and favorites list fragments
│   ├── favorites       # Favorites screen UI
│   ├── main            # Main navigation and layout
│   ├── movies          # Movie-related UI components
│   │   ├── details     # Details screen for movies
│   │   └── list        # List screen for movies
│   ├── profile         # Profile screen UI
│   ├── reminder        # Reminder management UI
│   ├── settings        # Settings screen UI
│   └── MainActivity    # Single activity hosting fragments
├── utils               # Utility classes for presentation-specific operations
└── workers             # Background tasks and WorkManager workers
```

## Installation

To install and run this project, follow these steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/dathuynh2003/MockProject-AADA.git
    ```
2. Open the project in Android Studio.
3. Build the project and run it on an emulator or a physical device.

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit them (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
