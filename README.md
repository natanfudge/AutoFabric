# AutoFabric

Currently, AutoFabric has one simple feature:
```java
@Entrypoint("main")
class MyMod implements ModInitializer {
 // [...]
}
```

This will generate an entrypoint in your fabric.mod.json, for MyMod, with the type `main`, so you don't need to specify it yourself.  
Obviously, any arbitrary value for `EntryPoint`, such as `client` or `server` will work.

## Gradle
Add AutoFabric as a gradle plugin:
```groovy
plugins {
    // [...]
    id "com.github.fudge.autofabric" version "1.1.0"
}
```
Despite being quite a complex arrangement, involving a Gradle plugin, annotation processor, and a compile-time dependency, you only need to add the plugin. Magic!

If you're using Kotlin, don't forget to add `kapt` to your project. The AutoFabric plugin will handle the rest.
```groovy
plugins {
    // [...]
    id "org.jetbrains.kotlin.jvm" version "1.3.72"
    id "org.jetbrains.kotlin.kapt" version "1.3.72"
}
```

## Entrypoints

You can also specify entrypoints for fields, methods, and kotlin `Object`s:

```java

// Field
class MyMod implements ModInitializer {
     @Entrypoint("main")
     public static final Instance = new MyMod();
    // [...]
}
// Method
@Entrypoint("main")
public static void myInitializer() {
    // [...]
}
```

```kotlin
// object
@Entrypoint("main")
object MyMod : ModInitializer {
    // [...]
}
```

You can specify multiple entrypoints for a single target as well.

```java
@Entrypoint({"main", "client"})
class MyMod implements ModInitializer, ClientModInitializer {
 // [...]
}
```

