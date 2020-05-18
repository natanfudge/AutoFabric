package fudge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Automatically inserts the annotated class as an entrypoint in the fabric.mod.json.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Entrypoint {
    /**
     * The entry point type that will be inserted. Common entrypoints are MAIN, CLIENT, and SERVER.
     */
    String value();

    /**
     * Will be run first. For classes implementing ModInitializer.
     */
    public static String MAIN = "main";

    /**
     * Will be run second and only on the client side. For classes implementing ClientModInitializer.
     */
    public static String CLIENT = "client";

    /**
     * Will be run second and only on the server side. For classes implementing DedicatedServerModInitializer.
     */
    public static String SERVER = "server";

}