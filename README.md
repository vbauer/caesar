
# Caesar

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Caesar-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1598)
[![Build Status](https://travis-ci.org/vbauer/caesar.svg)](https://travis-ci.org/vbauer/caesar)
[![Coverage Status](https://coveralls.io/repos/vbauer/caesar/badge.svg?branch=master)](https://coveralls.io/r/vbauer/caesar?branch=master)
[![Maven](https://img.shields.io/github/tag/vbauer/caesar.svg?label=maven)](https://jitpack.io/#vbauer/caesar)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/defb1d30427c4220aa8cf8fc9fa7830c)](https://www.codacy.com/app/bauer-vlad/caesar)

<img align="right" style="margin-left: 15px" width="250" height="268" src="misc/caesar.png">

> I came, I saw, I conquered. - Julius Caesar

**Caesar** is a tiny Java library that allows to create an asynchronous proxy-version of some synchronous bean. It means
that you can still think in terms of your service/bean/object and use its methods instead of writing concurrency code.

**Use cases:**

* You have already got some 3-rd party library that works synchronously, but it is necessary to use it asynchronously.
* You need to use both ways (sync & async) in different parts of your applications.

Caesar will help you to solve these problems.

**Online documentation:**

* [Maven site](https://vbauer.github.io/caesar)
* [Javadoc](https://vbauer.github.io/caesar/apidocs)


## Main features:

* Flexible describing of method signatures:
    * using standard Java [Future](http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Future.html)
    * or using [RxJava](https://github.com/ReactiveX/RxJava) ([Observable](https://github.com/ReactiveX/RxJava/wiki/Observable))
    * or using [Guava](https://github.com/google/guava) ([ListenableFuture](https://github.com/google/guava/blob/master/guava/src/com/google/common/util/concurrent/ListenableFuture.java), [FutureCallback](https://github.com/google/guava/blob/master/guava/src/com/google/common/util/concurrent/FutureCallback.java), [FutureCallbackAdapter](src/main/java/com/github/vbauer/caesar/callback/FutureCallbackAdapter.java))
    * or using custom callbacks ([AsyncCallback](src/main/java/com/github/vbauer/caesar/callback/AsyncCallback.java), [AsyncCallbackAdapter](src/main/java/com/github/vbauer/caesar/callback/AsyncCallbackAdapter.java))
* Small library size with zero dependencies
* Compact and very simple API
* Configurable timeouts
* Compatibility:
    * Java 8+
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
    <version>1.7.0</version>
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
    compile 'com.github.vbauer:caesar:1.7.0'
}
```


## Async-proxy creation

To make async-proxy for some bean, you need to use `AsyncProxyCreator`:

```java
public static <SYNC, ASYNC> ASYNC create(
    final SYNC bean,
    final Class<ASYNC> asyncInterface,
    final ExecutorService executor,
    final boolean validate
)
```

**Parameters:**

<dl>
    <dt>bean</dt>
    <dd>Not-null object which will be wrapped by async-proxy.</dd>
    <dt>asyncInterface</dt>
    <dd>Class which represents async-proxy. See an example section for more details.</dd>
    <dt>executor</dt>
    <dd>Executor service for running background operations. The usual choice is `ThreadPoolExecutor`.</dd>
    <dt>validate</dt>
    <dd>Validate mapping between bean and asyncInterface during proxy creation (otherwise it will be checked in runtime). It is an optional parameter (default value is true).</dd>
</dl>


## Mapping, Naming conventions

To make correct mapping between object and async-proxy, it is necessary to perform some conventions.
Asynchronous proxy method signature must match the signature of the object method, except several points:

* To use **Future** as result value, return class must be `Future<T>`
* To use **Guava**:
    * return class should be `ListenableFuture<T>`
    * or add new parameter `FutureCallback<T>` at the first place of the method signature and change result type to `void`
* To use **RxJava**, return class must be `Observable<T>`
* To use **AsyncCallback**, you need to:
    * add new parameter `AsyncCallback<T>` at the first place of the method signature
    * change result type to `void`

If this conventions are not complied, than the corresponding sync-method with the same signature should be invoked.
It will be still invoked in the separate thread to allow to use `@Timeout` annotation.


## Example

Lets make an async-proxy for the following bean:
```java
public class Sync {

    public String hello(final String name) {
        // Just a simple code for an example.
        return String.format("Hello, %s", name);
    }

}
```

First of all, we need to create an async-interface for this bean:
```java
// IMPORTANT: It is just an example. Choose the most appropriate way for you.
// All methods could not be presented at the same time in the real code.
public interface Async {

    // Future<T> will be the new return type.
    Future<String> hello(String name);

    // Future<T> will be the new return type.
    ListenableFuture<String> hello(String name);

    // Observable<T> will be also the new return type.
    Observable<String> hello(String name);

    // AsyncCallback<T> should be added as the first parameter.
    void hello(AsyncCallback<String> callback, String name);

    // FutureCallback<T> should be also added as the first parameter.
    void hello(FutureCallback<String> callback, String name);

}
```

After that we can create an async-proxy using `AsyncProxyCreator`:
```java
final AsyncBean asyncBean = AsyncProxyCreator.create(
    new Sync(), Async.class, Executors.newFixedThreadPool(5));
```

That's all. Now you can use your bean asynchronously. All methods will be invoked in threads from thread pool.

```java
// Retrieve result using Future:
final Future future = asyncBean.hello("John");
final String text = future.get(); // text is "Hello, John"

// Retrieve result using ListenableFuture:
final ListenableFuture listenableFuture = asyncBean.hello("George");
final String text = listenableFuture.get(); // text is "Hello, George"

// Retrieve result using RxJava and Observable:
final Observable<String> observable = asyncBean.hello("Paul");
final String text = observable.toBlocking().first(); // text is "Hello, Paul"

// Retrieve result using custom callback:
asyncBean.hello(new AsyncCallbackAdapter<String>() {
     @Override
     public void onSuccess(final String text) {
         // text is "Hello, Ringo"
     }
}, "Ringo");

// Retrieve result using FutureCallback:
asyncBean.hello(new FutureCallback<String>() {
    @Override
    public void onSuccess(final String text) {
        // text is "Hello, guys"
    }
    @Override
    public void onFailure(final Throwable t) {
        // it will not be executed
    }
}, "guys");
```


## @Timeout

Sometimes it is useful to setup timeout value to cancel operation (ex: REST API call which takes a lot of time).
You can use `@Timeout` annotation to do it (cancel operation after 3 seconds):

```java
public interface Async {

    @Timeout(value = 3, unit = TimeUnit.SECONDS)
    Future<String> hello(String name);

```

It is also possible to configure timeouts for all methods of async-proxy putting annotation on class:

```java
@Timeout(5000)
public interface Async {
    Future<Long> foo1();
    Future<Long> foo2();
}
```

**IMPORTANT:** `ScheduledExecutorService` should be used to switch on this feature.

## Development

To build project in strict mode with tests, you can use your local Maven:

```bash
mvn -P strict clean package
```


## Might also like

* [jconditions](https://github.com/vbauer/jconditions) - Extra conditional annotations for JUnit.
* [jackdaw](https://github.com/vbauer/jackdaw) - Java Annotation Processor which allows to simplify development.
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

See [LICENSE](LICENSE) file for details.
