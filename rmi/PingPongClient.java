package rmi;

import java.net.InetSocketAddress;

/**
 * Ping pong client that tests RMI for PingPongServer.
 * For successfull execution, PingServerFactory should be started first
 * at corresponding host and port.
 */
public class PingPongClient {
    public static void main(String[] args) {
        int successes = 0;
        int fails = 0;

        String host = RMIHelper.DEFAULT_HOST;
        int port = RMIHelper.DEFAULT_PORT;

        if (args.length > 0) {
            host = args[0];
        }

        if (args.length > 1) {
            port = Integer.parseInt(args[1]);
        }

        PingPongServer server = Stub.create(PingPongServer.class, new InetSocketAddress(host, port));

        for (int i = 0; i < 4; i++) {
            try {
                String pong = server.ping(i);

                if (!pong.equals("Pong " + i)) {
                    throw new RuntimeException("Unexpected response from ping pong server");
                }

                successes++;
            } catch (Exception e) {
                // uncomment to see error reason
                //e.printStackTrace();
                fails++;
            }
        }

        System.out.printf(
                "%d Tests completed, %d Tests failed\n",
                successes,
                fails);
    }
}
