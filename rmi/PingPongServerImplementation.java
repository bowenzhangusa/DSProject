package rmi;

class PingPongServerImplementation implements PingPongServer {
    @Override
    public String ping(int idNumber) throws RMIException {
        return "Pong " + idNumber;
    }

    @Override
    public String ping(double idNumber) throws RMIException {
        return "Pong double " + idNumber;
    }
}
