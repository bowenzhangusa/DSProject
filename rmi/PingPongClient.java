package rmi;

/**
 * Ping pong client that tests RMI for PingPongServer
 */
public class PingPongClient {
    public static void main(String[] args) {
        int successes = 0;
        int fails = 0;

        // hopefully this port will be available,
        // otherwise please specify it in the arguments
        int port = 7778;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        PingServerFactory factory = new PingServerFactory("localhost", port);
        PingPongServer pingPong;

        try {
            pingPong = factory.makePingServer();
        } catch (RMIException e) {
            System.out.println("Could not create PingPong server");
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < 4; i++) {
            try {
                String pong = pingPong.ping(i);

                if (!pong.equals("Pong " + i)) {
                    throw new RuntimeException("Unexpected response from ping pong server");
                }

                successes++;
            } catch (Exception e) {
                e.printStackTrace();
                fails++;
            }
        }

        System.out.printf(
                "%d Tests completed, %d Tests failed\n",
                successes,
                fails);
    }
}
