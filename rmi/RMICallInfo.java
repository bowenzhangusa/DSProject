package rmi;

import java.io.Serializable;

/**
 * An object sent from client to host
 * that contains class, method, and arguments to be invoked
 */
class RMICallInfo implements Serializable {
    public String className;
    public String methodName;
    public Object[] args;

    RMICallInfo(String className, String methodName, Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }
}

