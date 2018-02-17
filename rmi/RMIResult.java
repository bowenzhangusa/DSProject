package rmi;

import java.io.Serializable;

/**
 * Wrapper for RMI result that allows to easily distinguish between
 * regular operation result and en exception.
 */
public class RMIResult implements Serializable {
    public Object value;
    public Exception exception;
}
