
# Caesar [![Build Status](https://travis-ci.org/vbauer/caesar.svg)](https://travis-ci.org/vbauer/caesar) [![Coverage Status](https://coveralls.io/repos/vbauer/caesar/badge.svg?branch=master)](https://coveralls.io/r/vbauer/caesar?branch=master) [![Maven](https://img.shields.io/github/tag/vbauer/caesar.svg?label=maven)](https://jitpack.io/#vbauer/caesar)

<img align="right" style="margin-left: 15px" width="250" height="268" src="misc/caesar.png">

> I came, I saw, I conquered. - Julius Caesar

**Caesar** is a tiny Java library that allows to create an asynchronous proxy-version of some synchronous bean. It means
that you can still think in terms of your service/bean/object and use its methods instead of wiring concurrency code.

**Use-cases:**

* You already have got some 3-rd party library that works synchronously, but it is necessary to use it asynchronously.
* You need to use both ways (sync & async) in different parts of your applications.

Caesar will help you to solve this problems.


## Main features:

* Flexible describing a method signatures:
    * Using standard Java [Futures](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Future.html)
    * or using [AsyncCallback](src/main/java/com/github/vbauer/caesar/callback/AsyncCallback.java) / [AsyncCallbackAdapter](src/main/java/com/github/vbauer/caesar/callback/AsyncCallbackAdapter.java)
* Small size of library
* Compact and very simple API
* Compatibility:
    * Java 5+
    * Android

## Setup

Maven:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.vbauer</groupId>
    <artifactId>caesar</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle:
```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    compile 'com.github.vbauer:caesar:1.0.0'
}
```


## Example

Lets make an async-proxy for the following bean:
```java
public class Sync {

    public String hello(final String name) {
        // It's just an example.
        return String.format("Hello, %s", name);
    }

}
```

First of all, we need to create an async-interface for this bean:
```java
// It is just an example, no needs to write both methods.
// Choose the most appropriate way for you.
public interface Async {

    // Future<T> will be the new return type.
    Future<String> hello(String name);

    // AsyncCallback<T> should be added as the last parameter.
    void hello(String name, AsyncCallback<String> callback);

}
```

After that we can create an async-proxy using `AsyncProxyCreator`:
```java
final AsyncBean asyncBean = AsyncProxyCreator.create(
    new Sync(), Async.class, Executors.newFixedThreadPool(5));
```

That's all. Now you can use you bean asynchronously. All methods will be invoked in threads from thread pool.

```java
// Retrieve result using Future:
final Future future = asyncBean.hello("World");
final String text = future.get(); // text is "Hello, World"

// Retrieve result using callback:
asyncBean.hello("World", new AsyncCallbackAdapter<String>() {
     @Override
     public void onSuccess(final String text) {
         // text is "Hello, World"
     }
});
```


## Might also like

* [houdini](https://github.com/vbauer/houdini) - Type conversion system for Spring framework.
* [herald](https://github.com/vbauer/herald) - Logging annotation for Spring framework.
* [commons-vfs2-cifs](https://github.com/vbauer/commons-vfs2-cifs) - SMB/CIFS provider for Commons VFS.
* [avconv4java](https://github.com/vbauer/avconv4java) - Java interface to avconv tool.


## License

Copyright 2015 Vladislav Bauer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
