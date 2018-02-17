package rmi;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.*;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * RMI stub factory.
 * <p>
 * <p>
 * RMI stubs hide network communication with the remote server and provide a
 * simple object-like interface to their users. This class provides methods for
 * creating stub objects dynamically, when given pre-defined interfaces.
 * <p>
 * <p>
 * The network address of the remote server is set when a stub is created, and
 * may not be modified afterwards. Two stubs are equal if they implement the
 * same interface and carry the same remote server address - and would
 * therefore connect to the same skeleton. Stubs are serializable.
 */
public abstract class Stub {
    /**
     * Creates a stub, given a skeleton with an assigned adress.
     * <p>
     * <p>
     * The stub is assigned the address of the skeleton. The skeleton must
     * either have been created with a fixed address, or else it must have
     * already been started.
     * <p>
     * <p>
     * This method should be used when the stub is created together with the
     * skeleton. The stub may then be transmitted over the network to enable
     * communication with the skeleton.
     *
     * @param c        A <code>Class</code> object representing the interface
     *                 implemented by the remote object.
     * @param skeleton The skeleton whose network address is to be used.
     * @return The stub created.
     * @throws IllegalStateException If the skeleton has not been assigned an
     *                               address by the user and has not yet been
     *                               started.
     * @throws UnknownHostException  When the skeleton address is a wildcard and
     *                               a port is assigned, but no address can be
     *                               found for the local host.
     * @throws NullPointerException  If any argument is <code>null</code>.
     * @throws Error                 If <code>c</code> does not represent a remote interface
     *                               - an interface in which each method is marked as throwing
     *                               <code>RMIException</code>, or if an object implementing
     *                               this interface cannot be dynamically created.
     */
    public static <T> T create(Class<T> c, Skeleton<T> skeleton)
    throws UnknownHostException {
        if (c == null || skeleton == null) {
            throw new NullPointerException("All agruments are required");
        }

        if (skeleton.getAddress() == null) {
            throw new IllegalStateException("Skeleton must have an address");
        }

        return createObject(c, skeleton.getAddress());
    }

    /**
     * Creates a stub, given a skeleton with an assigned address and a hostname
     * which overrides the skeleton's hostname.
     * <p>
     * <p>
     * The stub is assigned the port of the skeleton and the given hostname.
     * The skeleton must either have been started with a fixed port, or else
     * it must have been started to receive a system-assigned port, for this
     * method to succeed.
     * <p>
     * <p>
     * This method should be used when the stub is created together with the
     * skeleton, but firewalls or private networks prevent the system from
     * automatically assigning a valid externally-routable address to the
     * skeleton. In this case, the creator of the stub has the option of
     * obtaining an externally-routable address by other means, and specifying
     * this hostname to this method.
     *
     * @param c        A <code>Class</code> object representing the interface
     *                 implemented by the remote object.
     * @param skeleton The skeleton whose port is to be used.
     * @param hostname The hostname with which the stub will be created.
     * @return The stub created.
     * @throws IllegalStateException If the skeleton has not been assigned a
     *                               port.
     * @throws NullPointerException  If any argument is <code>null</code>.
     * @throws Error                 If <code>c</code> does not represent a remote interface
     *                               - an interface in which each method is marked as throwing
     *                               <code>RMIException</code>, or if an object implementing
     *                               this interface cannot be dynamically created.
     */
    public static <T> T create(Class<T> c, Skeleton<T> skeleton,
            String hostname) {
        if (c == null || skeleton == null || hostname == null) {
            throw new NullPointerException("All agruments are required");
        }

        if (skeleton.getAddress() == null) {
            throw new IllegalStateException("Skeleton does not have an address");
        }

        return createObject(c, InetSocketAddress.createUnresolved(hostname, skeleton.getAddress().getPort()));
    }

    /**
     * Creates a stub, given the address of a remote server.
     * <p>
     * <p>
     * This method should be used primarily when bootstrapping RMI. In this
     * case, the server is already running on a remote host but there is
     * not necessarily a direct way to obtain an associated stub.
     *
     * @param c       A <code>Class</code> object representing the interface
     *                implemented by the remote object.
     * @param address The network address of the remote skeleton.
     * @return The stub created.
     * @throws NullPointerException If any argument is <code>null</code>.
     * @throws Error                If <code>c</code> does not represent a remote interface
     *                              - an interface in which each method is marked as throwing
     *                              <code>RMIException</code>, or if an object implementing
     *                              this interface cannot be dynamically created.
     */
    public static <T> T create(Class<T> c, InetSocketAddress address) {
        if (c == null || address == null) {
            throw new NullPointerException("All arguments are required");
        }

        return createObject(c, address);
    }

    /**
     * Stub object proxy
     */
    private static class StubInvocationHandler
            implements InvocationHandler, Serializable {
        protected InetSocketAddress remoteAddress;
        protected Class originalClass;

        /**
         * Implementation of "equals" and "hashcode"
         * is based on an article
         * https://javaclippings.wordpress.com/2009/03/18/dynamic-proxies-equals-hashcode-tostring/
         */
        private static final Method OBJECT_EQUALS =
                getObjectMethod("equals", Object.class);

        private static final Method OBJECT_HASHCODE =
                getObjectMethod("hashCode");

        private static final Method OBJECT_TOSTRING =
                getObjectMethod("toString");


        public <T> StubInvocationHandler(InetSocketAddress addr, Class<T> c) {
            this.remoteAddress = addr;
            this.originalClass = c;
        }

        /**
         * Performs a method - either a local one (equals, hashcode, or toString),
         * or remote one, by sending method and arguments over the socket.
         */
        public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {
            if (method.equals(OBJECT_EQUALS)) {
                return equalsInternal(args[0], proxy);
            } else if (method.equals(OBJECT_HASHCODE)) {
                return hashcodeInternal();
            } else if (method.equals(OBJECT_TOSTRING)) {
                return toString();
            }

            RMIResult result = null;

            try {
                Socket sock = new Socket(
                        this.remoteAddress.getAddress(),
                        this.remoteAddress.getPort()
                );
                ObjectOutputStream socketOutputStream = new ObjectOutputStream(sock.getOutputStream());
                RMICallInfo data = new RMICallInfo(proxy.getClass().getCanonicalName(), method.getName(), args);
                socketOutputStream.writeObject(data);
                socketOutputStream.flush();

                ObjectInputStream inputStream = new ObjectInputStream(sock.getInputStream());
                result = (RMIResult) inputStream.readObject();

                inputStream.close();
                sock.close();
            } catch (Throwable ex) {
                throw new RMIException("Could not perform RMI", ex);
            }

            if (result == null) {
                return null;
            }

            if (result.exception != null) {
                throw result.exception.getCause();
            }

            return result.value;
        }

        private static Method getObjectMethod(String name, Class... types) {
            try {
                return Object.class.getMethod(name, types);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(e);
            }
        }

        /**
         * Implementation of "equals" for a stub
         */
        private boolean equalsInternal(Object other, Object proxy) {
            if (other == null) {
                return false;
            }

            if (other.getClass() != proxy.getClass()) {
                return false;
            }

            StubInvocationHandler otherProxy = (StubInvocationHandler) Proxy.getInvocationHandler(other);

            // if both addresses are not null, compare them
            if (otherProxy.remoteAddress != null && this.remoteAddress != null) {
                if (!otherProxy.remoteAddress.equals(this.remoteAddress)) {
                    return false;
                }
            } else if (otherProxy.remoteAddress != this.remoteAddress) {
                return false;
            }

            if (!otherProxy.originalClass.equals(this.originalClass)) {
                return false;
            }

            return true;
        }

        private int hashcodeInternal() {
            return Objects.hash(
                    this.remoteAddress != null ? this.remoteAddress.hashCode() : null,
                    this.originalClass.getCanonicalName());
        }

        @Override
        public String toString() {
            return this.originalClass.getCanonicalName() + " at " + this.remoteAddress;
        }
    }

    /**
     * Creates a stub object for given class
     */
    @SuppressWarnings("unchecked")
    private static <T> T createObject(Class<T> c, InetSocketAddress addr) {
        RMIHelper.validateInterface(c);
        return (T) Proxy.newProxyInstance(
                c.getClassLoader(),
                new Class[]{c},
                new StubInvocationHandler(addr, c));
    }
}
