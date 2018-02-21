package rmi;

import rmi.SampleClassUnderTest;
import test.Test;
import test.TestFailed;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

public class StubUnitTest extends Test {
    /**
     * End-to-end test of a skeleton and stub
     *
     * @throws TestFailed
     */
    @Override
    protected void perform() throws TestFailed {
        Skeleton<PingPongServer> skeleton = new Skeleton<PingPongServer>(
                PingPongServer.class,
                new PingPongServerImplementation());

        try {
            skeleton.start();
        } catch (RMIException e) {
            throw new TestFailed("this should not throw exceptions", e);
        }

        PingPongServer stub = null;
        try {
            stub = Stub.create(
                    PingPongServer.class,
                    skeleton);
        } catch (UnknownHostException e) {
            throw new TestFailed("this should not throw exceptions", e);
        }

        String result = null;

        try {
            result = stub.ping(14736);

        } catch (Throwable e) {
            throw new TestFailed("this should not throw exceptions", e);
        }
        if (!result.equals("Pong 14736")) {
            throw new TestFailed("Unexpected result from RMI: " + result);
        }

        // do the same with overloaded method
        try {
            result = stub.ping(14.736);

        } catch (Throwable e) {
            throw new TestFailed("this should not throw exceptions", e);
        }
        if (!result.equals("Pong double 14.736")) {
            throw new TestFailed("Unexpected result from RMI: " + result);
        }

        // pass a HashMap to a method that accepts Map interface
        try {
            HashMap<String, Integer> arg = new HashMap<String, Integer>();
            arg.put("a", 1);
            result = stub.methodThatAcceptsInterface(arg);
        } catch (Throwable e) {
            throw new TestFailed("this should not throw exceptions", e);
        }
        if (!result.equals("ok")) {
            throw new TestFailed("Unexpected result from RMI: " + result);
        }

        skeleton.stop();
    }
}
