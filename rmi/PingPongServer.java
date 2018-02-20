package rmi;

interface PingPongServer {
    // Ping pong method as described in assignment
    String ping(int idNumber) throws RMIException;

    // Similar method with different agrument type to test overloading behavior
    String ping(double idNumber) throws RMIException;
}
