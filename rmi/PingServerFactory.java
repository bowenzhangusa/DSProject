package rmi;

import java.net.InetSocketAddress;

/**
 * A factory that creates remote references for PingPongServer implementation
 */
public class PingServerFactory {
    protected int port;
    protected String host;

    public PingServerFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public PingPongServer makePingServer() throws RMIException {
        Skeleton<PingPongServer> skeleton = new Skeleton<PingPongServer>(
                PingPongServer.class,
                new PingPongServerImplementation(),
                new InetSocketAddress(host, port)
        );

        skeleton.start();

        return Stub.create(
                PingPongServer.class,
                skeleton.getAddress());
    }
}
