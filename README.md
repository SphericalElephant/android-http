# android-http

## Introduction
The android-http framework was inspired by the Google I/O 2010 - Android REST client application talk that can be found [here](http://www.youtube.com/watch?v=xHXn3Kg2IQE).
The framework is designed to send, receive, process and publish webrequests and their corresponding replies.

## Features
* WebRequest / WebReply API: enables developers to conveniently send web requests and handle their replies concurrently
* Processor: allows post processing of web replies
* Several abstract processors that facilitate development of JSON and XML (SOAP) processors
* Processors that ease every day development (Image processing)
* In memory (LRU) and file based caching
* SSL support
* Automatic selection of web clients based on the Android version running on individual devices

## Planned
* Chaining multiple processors
* Blocking web requests (developers need to manage concurrency manually)

## License
Apache License, Version 2.0. Please refer to LICENSE and NOTICE for additional information.
