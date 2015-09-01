# android-http

## Introduction
The android-http framework was inspired by the Google I/O 2010 - Android REST client application talk that can be found [here](http://www.youtube.com/watch?v=xHXn3Kg2IQE).
The framework is designed to send, receive, process and publish webrequests and their corresponding replies.

## Features
* WebRequest / WebReply API: enables developers to conveniently send web requests and handle their replies concurrently
* Supports Asynchronous and Synchronous web requests
* Processor: allows post processing of web replies
* Several abstract processors that facilitate development of JSON and XML (SOAP) processors
* Processors that ease every day development (Image processing)
* In memory (LRU) and file based caching
* SSL support
* Automatic selection of web clients based on the Android version running on individual devices
* Connectivity aware
* Convenient assisting API:
    * "fire-and-forget" API that enques web requests for later execution when service is not available
    * Ordered-conditional synchronous web request API
* Bundling and prioritization of non time critical web requests in order to save battery life

## Planned Features
* Stream Processor
* Service to save and repeat failed web requests
* REST assister
* Volley Support
* Chaining multiple processors
* OAuth 2.0
* Dynamically generated processor ids, deprecate getProcessorId()
* Automatic cookie handling
* Parser annotations for data object to create parsers during runtime

## Usage
Clone the project and run "./gradlew uploadArchives" to store the archives to your local maven repository

## Permissions
Make sure that all permissions listed below are granted by your manifest:

* android.permission.WRITE_EXTERNAL_STORAGE
* android.permission.INTERNET
* android.permission.ACCESS_NETWORK_STATE
* android.permission.CHANGE_NETWORK_STATE

## Provider
* at.diamonddogs.contentprovider.CacheContentProvider

## Services
* at.diamonddogs.service.CacheService
* at.diamonddogs.service.net.HttpService

## License
Apache License, Version 2.0. Please refer to LICENSE and NOTICE for additional information.

## Example
Example code illustrating a simple web request can be found in the at https://github.com/SphericalElephant/android-http-example.

## Opensource Software used:
- [ksoap2-android](http://code.google.com/p/ksoap2-android)
- [kXML](http://kxml.sourceforge.net/)
- [SLF4J](http://www.slf4j.org/)
- [XMLPULL](http://www.xmlpull.org/)
- [kObjects](http://kobjects.sourceforge.net/)
