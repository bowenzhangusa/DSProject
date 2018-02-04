package rmi;

class PingPongServerImplementation implements PingPongServerInterface {
    @Override
    public String ping(int idNumber) throws RMIException {
        return "Pong ".concat(Integer.toString(idNumber));
    }
}
