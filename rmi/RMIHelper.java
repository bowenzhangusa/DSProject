package rmi;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Contains common methods for skeleton and proxy
 */
public class RMIHelper {
    /**
     * Checks whether all public methods of interface
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
}
