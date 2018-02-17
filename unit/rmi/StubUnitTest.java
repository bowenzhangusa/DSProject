package rmi;

import rmi.SampleClassUnderTest;
import test.Test;
import test.TestFailed;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

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

        skeleton.stop();
    }
}
