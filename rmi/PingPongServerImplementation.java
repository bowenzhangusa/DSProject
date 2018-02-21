package rmi;

import java.util.Map;

class PingPongServerImplementation implements PingPongServer {
    public String ping(int idNumber) throws RMIException {
        return "Pong " + idNumber;
    }

    public String ping(double idNumber) throws RMIException {
        return "Pong double " + idNumber;
    }

    public String methodThatAcceptsInterface(Map<String, Integer> m) throws RMIException {
        return "ok";
    }
}
