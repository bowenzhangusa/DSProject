package rmi;

import java.util.Map;

interface PingPongServer {
    // Ping pong method as described in assignment
    String ping(int idNumber) throws RMIException;

    // Similar method with different agrument type to test overloading behavior
    String ping(double idNumber) throws RMIException;

    // To see what happens when argument is an interface, and not a specific class.
    String methodThatAcceptsInterface(Map<String, Integer> m) throws RMIException;
}
