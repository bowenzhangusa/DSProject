package rmi;

interface PingPongServer {
    String ping(int idNumber) throws RMIException;
}
