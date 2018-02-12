package rmi;

import test.Test;
import test.TestFailed;

import java.net.InetSocketAddress;

public class SerializerUnitTest extends Test {

    @Override
    protected void perform() throws TestFailed {
        testSimpleCall();
        testRMICall();
    }

    /**
     * Checks that serialization/deserialization
     * of a simple arbitrary method call works as expected,
     * restoring given class, method, and argument values.
     */
    private void testSimpleCall() throws TestFailed {
        byte[] bytes = new byte[0];
        try {
            bytes = Serializer.toBytes(
                    String.class,
                    "concat",
                    new Object[]{"argument-string"});
        } catch (RMIException e) {
            throw new TestFailed("Serializable call must be converted to bytes");
        }

        RMICallInfo unserialized = null;
        try {
            unserialized = Serializer.fromBytes(bytes);
        } catch (RMIException e) {
            throw new TestFailed("Serialization should be successful");
        }

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

    /**
     * Checks that serialization/deserialization
     * of an RMI stub as an argument works
     */
    private void testRMICall() throws TestFailed {
        PingPongServerInterface stubAsArgument = Stub.create(
                PingPongServerInterface.class,
                InetSocketAddress.createUnresolved("localhost", 123));

        byte[] bytes = new byte[0];
        try {
            bytes = Serializer.toBytes(
                    rmi.PingPongConsumerInterface.class,
                    "play",
                    new Object[]{stubAsArgument});
        } catch (RMIException e) {
            throw new TestFailed("Serializable call must be converted to bytes");
        }

        RMICallInfo unserialized = null;
        try {
            unserialized = Serializer.fromBytes(bytes);
        } catch (RMIException e) {
            throw new TestFailed("Deserialization should be successful");
        }

        if (unserialized == null) {
            throw new TestFailed("unserialized object must not be null");
        }

        if (unserialized.args.length != 1) {
            throw new TestFailed("Unexpected unserialized arguments");
        }

        Object unserializedStub = unserialized.args[0];

        if (!(unserializedStub instanceof PingPongServerInterface)) {
            throw new TestFailed("Unserialized stub has invalid class");
        }
    }
}
