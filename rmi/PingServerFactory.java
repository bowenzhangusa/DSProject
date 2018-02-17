package rmi;

import java.net.InetSocketAddress;

/**
 * A factory that creates remote references for PingPongServer implementation.
 * Must be started first before running the PingPongClient.
 */
public class PingServerFactory {
    protected int port;
    protected String host;

    public PingServerFactory(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Starts ping pong server.
     * Returns corresponding Stub as required in assignment handout,
     * although this is not used here.
     *
     * @return
     * @throws RMIException
     */
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

    /**
     * Runs PingPong server indefinitely, waiting for incoming connections
     *
     * @param args
     */
    public static void main(String[] args) {
        String host = RMIHelper.DEFAULT_HOST;
        int port = RMIHelper.DEFAULT_PORT;

        if (args.length > 0) {
            host = args[0];
        }

        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        PingServerFactory factory = new PingServerFactory(host, port);
        try {
            factory.makePingServer();
        } catch (RMIException e) {
            System.out.println("Cannot run the ping pong server on port " + port);
            e.printStackTrace();
            return;
        }

        System.out.println("Running the ping pong server on port " + port);
    }
}
