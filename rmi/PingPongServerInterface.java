package rmi;

interface PingPongServerInterface {
    String ping(int idNumber) throws RMIException;
}
