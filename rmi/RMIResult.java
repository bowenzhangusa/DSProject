package rmi;

import java.io.Serializable;

/**
 * Wrapper for RMI result
 */
public class RMIResult implements Serializable {
    public Object value;
    public boolean isVoid;
    public Exception exception;
}
