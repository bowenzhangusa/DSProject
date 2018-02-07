package rmi;

import rmi.SampleClassUnderTest;
import test.Test;
import test.TestFailed;

import java.net.InetSocketAddress;

public class StubUnitTest extends Test {
    /**
     * This is an initial version of stub test
     *
     * @throws TestFailed
     */
    @Override
    protected void perform() throws TestFailed {
        PingPongServerInterface stub = Stub.create(
                PingPongServerInterface.class,
                InetSocketAddress.createUnresolved("localhost", 123));
        try {
            stub.ping(1);
            //TODO: check result
        } catch (RMIException e) {
            throw new TestFailed("this should not throw exceptions");
        }
    }
}
