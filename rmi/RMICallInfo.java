package rmi;

import java.io.Serializable;

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

