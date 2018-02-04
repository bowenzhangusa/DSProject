package rmi;

import test.Test;
import test.TestFailed;

public class SerializerUnitTest extends Test {
    /**
     * Checks that serialization/deserialization
     * of a simple arbitrary method call works as expected,
     * restoring given class, method, and argument values.
     */
    @Override
    protected void perform() throws TestFailed {
        byte[] bytes = Serializer.toBytes(
                String.class,
                "concat",
                new Object[]{"argument-string"});

        Serializer.RMICallInfo unserialized = Serializer.fromBytes(bytes);

        if (unserialized == null) {
            throw new TestFailed("unserialized map must not be null");
        }

        if (!unserialized.className.equals(String.class.getCanonicalName())) {
            throw new TestFailed("Unexpected unserialized classname");
        }

        if (!unserialized.methodName.equals("concat")) {
            throw new TestFailed("Unexpected unserialized method");
        }

        if (unserialized.args.length != 1 || !unserialized.args[0].equals("argument-string")) {
            throw new TestFailed("Unexpected unserialized arguments");
        }
    }
}
