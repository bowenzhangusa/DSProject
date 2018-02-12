package rmi;

class PingPongConsumerImplementation implements rmi.PingPongConsumerInterface {
    @Override
    public int play(PingPongServerInterface game) throws RMIException {
        return game.ping(1).hashCode();
    }
}
