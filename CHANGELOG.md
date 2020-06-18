## 1.0.0

Released

## 1.1.0

- You can now pass multiple values to `@Entrypoint`, allowing a class to serve as multiple entrypoints, provided it implements all the necessary interfaces.
- An `entrypoints` field is no longer required to be present in your `fabric.mod.json`. 
- You can now place `@Entrypoint`s on fields and methods, allowing a specific static field to be the receiver of an entrypoint,
 or to make a specific method be called as an entrypoint.
  (Or multiple entrypoints, provided the field's class implements the multiple interfaces, and the method fits all these entrypoints).
  You can do the same for Kotlin top-level methods.
- You can now place `@Entrypoints` on Kotlin `object`s, and the generated INSTANCE will be used for the entrypoint(s). The Kotlin language adapter is not required.
- Fixed a gradle plugin exception when having 2 or more auto entrypoints on a key when there are no manual entrypoints for that key.

### 1.1.1
- Made the handling of objects a bit more precise