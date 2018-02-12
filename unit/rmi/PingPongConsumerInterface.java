package rmi;

/**
 * An interface for a class that consumes a stub,
 * for doing RMI within RMI
 */
interface PingPongConsumerInterface {
    public int play(PingPongServerInterface game) throws RMIException;
}
