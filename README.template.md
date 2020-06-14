# AutoFabric

Currently, AutoFabric has one simple feature:
```java
@EntryPoint("main")
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
    id "com.github.fudge.autofabric" version "${version}"
}
```
Despite being quite a complex arrangement, involving a Gradle plugin, annotation processor, and a compile-time dependency, you only need to add the plugin. Magic!
 
If you're using Kotlin, don't forget to add `kapt` to your project. The AutoFabric plugin will handle the rest.
```groovy
plugins {
    // [...]
    id "org.jetbrains.kotlin.jvm" version "${kotlin_version}"
    id "org.jetbrains.kotlin.kapt" version "${kotlin_version}"
}
```