package rmi;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Contains common methods for skeleton and proxy
 */
public class RMIHelper {
    public final static String DEFAULT_HOST = "localhost";
    public final static int DEFAULT_PORT = 7778;

    /**
     * Checks whether all public methods of an interface
     * throw RMIException
     */
    public static <T> void validateInterface(Class<T> c) {
        if (!c.isInterface()) {
            throw new Error("Argument must be an interface");
        }

        for (Method m : c.getDeclaredMethods()) {
            if (!Modifier.isPublic(m.getModifiers())) {
                continue;
            }

            boolean throwsRMI = false;

            for (Class ex : m.getExceptionTypes()) {
                if (ex.equals(RMIException.class)) {
                    throwsRMI = true;
                    break;
                }
            }

            if (!throwsRMI) {
                throw new Error("Some methods of class do not throw RMI exception");
            }
        }
    }

    /**
     * Checks if two class types are compatible
     */
    public static boolean isCompatible(Class klass1, Object obj) {
        if (!klass1.isPrimitive()) {
            return klass1.isInstance(obj);
        }

        // We need to check primitive type because Java reflection API doesn't do autoboxing
        return (klass1 == Integer.TYPE && Integer.class.isInstance(obj)) ||
              (klass1 == Boolean.TYPE && Boolean.class.isInstance(obj)) ||
              (klass1 == Byte.TYPE && Byte.class.isInstance(obj)) ||
              (klass1 == Character.TYPE && Character.class.isInstance(obj)) ||
              (klass1 == Float.TYPE && Float.class.isInstance(obj)) ||
              (klass1 == Long.TYPE && Long.class.isInstance(obj)) ||
              (klass1 == Short.TYPE && Short.class.isInstance(obj)) ||
              (klass1 == Double.TYPE && Double.class.isInstance(obj));
    }
}
