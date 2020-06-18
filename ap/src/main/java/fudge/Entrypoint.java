package fudge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Automatically inserts the annotated class, field, method, or Kotlin object as entrypoint(s) with the specified value(s)
 * in the fabric.mod.json.
 * <p>
 * A <b>class</b> or Kotlin <b>object</b> must implement the interfaces assigned for the entrypoints. <br>
 * A <b>field</b> must be a static instance of a class that implements the interfaces of the entrypoints. <br>
 * A <b>method</b> must have a signature that matches the method of the interfaces that the entrypoints are assigned to.
 * (Usually the interfaces have just one method, so you just declare a static method that matches it and annotate that)
 *
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface Entrypoint {
    /**
     * Entry point types that will be inserted. Common entrypoints are MAIN, CLIENT, and SERVER.
     * Can accept multiple values, in that case the annotated element will be used for multiple entrypoints.
     */
    String[] value();

    /**
     * Will be run first. For classes implementing ModInitializer.
     */
    public static final String MAIN = "main";

    /**
     * Will be run second and only on the client side. For classes implementing ClientModInitializer.
     */
    public static final String CLIENT = "client";

    /**
     * Will be run second and only on the server side. For classes implementing DedicatedServerModInitializer.
     */
    public static final String SERVER = "server";

}