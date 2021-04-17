</br>
<p 
  align="center">
  <img 
    src="https://raw.githubusercontent.com/maxhvesser/showroom/master/images/ic_showroom.svg" 
    height="100">
</p>
</br>

## Showroom

[![Build Status](https://travis-ci.com/MaxHvesser/showroom.svg?branch=master)](https://travis-ci.com/MaxHvesser/showroom)

Showroom is an image gallery library built for Android. The aim of this library is to provide a single view component that provides an image gallery experience almost completely out-of-the-box with as little configuration from the consumer as possible. The project utilizes Kotlin and is very much in active development.

Currently the project is using a Release Canditate version of the [AndroidX Core KTX library](https://developer.android.com/jetpack/androidx/releases/core), this is to get access to the latest compatibility APIs that the library has to offer due to issues with immersive mode pre version 1.5.0.

### Features

The project makes use of the following features: 

- Highly customizable
- Pre-defined customizable gallery UI 
- Optional infinite image scrolling
- Immersion toggling
- Support for API versions 21 to 30
- Simple design

### Third Party Libraries

Currently, the project also makes use the following open source libraries: 

- [Coil](https://github.com/coil-kt/coil) used for image loading
- [GestureViews](https://github.com/alexvasilkov/GestureViews) used for image manipulation

### Show me it



### Dependency

This library is currently accessible via [JitPack](https://jitpack.io/#maxhvesser/showroom) and can therefore be used in your project by adding the following to your `build.gradle` file:

```Gradle
dependencies {
    implementation 'com.github.maxhvesser:showroom:1.1.3-alpha'
}
```

### Releases

The current release is [v1.1.3-alpha](https://github.com/maxhvesser/showroom/releases/tag/1.1.3-alpha) found at the respective link otherwise visit [releases](https://github.com/maxhvesser/showroom/releases) for the complete list.

### License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
