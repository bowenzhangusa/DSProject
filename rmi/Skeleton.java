package rmi;

import java.lang.Error;
import java.lang.NullPointerException;
import java.net.*;
import java.io.*;
import java.rmi.Remote;
import java.lang.reflect.*;

/**
 * RMI skeleton
 * <p>
 * <p>
 * A skeleton encapsulates a multithreaded TCP server. The server's clients are
 * intended to be RMI stubs created using the <code>Stub</code> class.
 * <p>
 * <p>
 * The skeleton class is parametrized by a type variable. This type variable
 * should be instantiated with an interface. The skeleton will accept from the
 * stub requests for calls to the methods of this interface. It will then
 * forward those requests to an object. The object is specified when the
 * skeleton is constructed, and must implement the remote interface. Each
 * method in the interface should be marked as throwing
 * <code>RMIException</code>, in addition to any other exceptions that the user
 * desires.
 * <p>
 * <p>
 * Exceptions may occur at the top level in the listening and service threads.
 * The skeleton's response to these exceptions can be customized by deriving
 * a class from <code>Skeleton</code> and overriding <code>listen_error</code>
 * or <code>service_error</code>.
 */
public class Skeleton<T> {
    /**
     * Creates a <code>Skeleton</code> with no initial server address. The
     * address will be determined by the system when <code>start</code> is
     * called. Equivalent to using <code>Skeleton(null)</code>.
     * <p>
     * <p>
     * This constructor is for skeletons that will not be used for
     * bootstrapping RMI - those that therefore do not require a well-known
     * port.
     *
     * @param c      An object representing the class of the interface for which the
     *               skeleton server is to handle method call requests.
     * @param server An object implementing said interface. Requests for method
     *               calls are forwarded by the skeleton to this object.
     * @throws Error                If <code>c</code> does not represent a remote interface -
     *                              an interface whose methods are all marked as throwing
     *                              <code>RMIException</code>.
     * @throws NullPointerException If either of <code>c</code> or
     *                              <code>server</code> is <code>null</code>.
     */

/** RMI skeleton

    <p>
    A skeleton encapsulates a multithreaded TCP server. The server's clients are
    intended to be RMI stubs created using the <code>Stub</code> class.

    <p>
    The skeleton class is parametrized by a type variable. This type variable
    should be instantiated with an interface. The skeleton will accept from the
    stub requests for calls to the methods of this interface. It will then
    forward those requests to an object. The object is specified when the
    skeleton is constructed, and must implement the remote interface. Each
    method in the interface should be marked as throwing
    <code>RMIException</code>, in addition to any other exceptions that the user
    desires.

    <p>
    Exceptions may occur at the top level in the listening and service threads.
    The skeleton's response to these exceptions can be customized by deriving
    a class from <code>Skeleton</code> and overriding <code>listen_error</code>
    or <code>service_error</code>.
*/
    private Class<T> klass;
    private T server;
    private InetSocketAddress address;
    /** Creates a <code>Skeleton</code> with no initial server address. The
        address will be determined by the system when <code>start</code> is
        called. Equivalent to using <code>Skeleton(null)</code>.

        <p>
        This constructor is for skeletons that will not be used for
        bootstrapping RMI - those that therefore do not require a well-known
        port.

        @param c An object representing the class of the interface for which the
                 skeleton server is to handle method call requests.
        @param server An object implementing said interface. Requests for method
                      calls are forwarded by the skeleton to this object.
        @throws Error If <code>c</code> does not represent a remote interface -
                      an interface whose methods are all marked as throwing
                      <code>RMIException</code>.
        @throws NullPointerException If either of <code>c</code> or
                                     <code>server</code> is <code>null</code>.
     */
    public Skeleton(Class<T> c, T server)
    {
        if (c == null || server == null) {
            throw new NullPointerException("All arguments are required");
        }

        this.klass = c;
        this.server = server;
        this.address = new InetSocketAddress(7000);
    }

    /**
     * Creates a <code>Skeleton</code> with the given initial server address.
     * <p>
     * <p>
     * This constructor should be used when the port number is significant.
     *
     * @param c       An object representing the class of the interface for which the
     *                skeleton server is to handle method call requests.
     * @param server  An object implementing said interface. Requests for method
     *                calls are forwarded by the skeleton to this object.
     * @param address The address at which the skeleton is to run. If
     *                <code>null</code>, the address will be chosen by the
     *                system when <code>start</code> is called.
     * @throws Error                If <code>c</code> does not represent a remote interface -
     *                              an interface whose methods are all marked as throwing
     *                              <code>RMIException</code>.
     * @throws NullPointerException If either of <code>c</code> or
     *                              <code>server</code> is <code>null</code>.
     */

    public Skeleton(Class<T> c, T server, InetSocketAddress address)
    {
        if (c == null || server == null || address == null) {
            throw new NullPointerException("All arguments are required");
        }

        if (!Remote.class.isAssignableFrom(c)) {
            throw new Error("class doesn't implement remote interface");
        }

        this.klass = c;
        this.server = server;
        this.address = address;
    }

    /**
     * Called when the listening thread exits.
     * <p>
     * <p>
     * The listening thread may exit due to a top-level exception, or due to a
     * call to <code>stop</code>.
     * <p>
     * <p>
     * When this method is called, the calling thread owns the lock on the
     * <code>Skeleton</code> object. Care must be taken to avoid deadlocks when
     * calling <code>start</code> or <code>stop</code> from different threads
     * during this call.
     * <p>
     * <p>
     * The default implementation does nothing.
     *
     * @param cause The exception that stopped the skeleton, or
     *              <code>null</code> if the skeleton stopped normally.
     */
    protected void stopped(Throwable cause) {
    }

    /**
     * Called when an exception occurs at the top level in the listening
     * thread.
     * <p>
     * <p>
     * The intent of this method is to allow the user to report exceptions in
     * the listening thread to another thread, by a mechanism of the user's
     * choosing. The user may also ignore the exceptions. The default
     * implementation simply stops the server. The user should not use this
     * method to stop the skeleton. The exception will again be provided as the
     * argument to <code>stopped</code>, which will be called later.
     *
     * @param exception The exception that occurred.
     * @return <code>true</code> if the server is to resume accepting
     * connections, <code>false</code> if the server is to shut down.
     */
    protected boolean listen_error(Exception exception) {
        return false;
    }

    /**
     * Called when an exception occurs at the top level in a service thread.
     * <p>
     * <p>
     * The default implementation does nothing.
     *
     * @param exception The exception that occurred.
     */
    protected void service_error(RMIException exception) {
    }

    /**
     * Starts the skeleton server.
     * <p>
     * <p>
     * A thread is created to listen for connection requests, and the method
     * returns immediately. Additional threads are created when connections are
     * accepted. The network address used for the server is determined by which
     * constructor was used to create the <code>Skeleton</code> object.
     *
     * @throws RMIException When the listening socket cannot be created or
     *                      bound, when the listening thread cannot be created,
     *                      or when the server has already been started and has
     *                      not since stopped.
     */
    public synchronized void start() throws RMIException
    {
        new Thread(new Listener(this.address)).start();

    }

    /**
     * Stops the skeleton server, if it is already running.
     * <p>
     * <p>
     * The listening thread terminates. Threads created to service connections
     * may continue running until their invocations of the <code>service</code>
     * method return. The server stops at some later time; the method
     * <code>stopped</code> is called at that point. The server may then be
     * restarted.
     */
    public synchronized void stop() {

        //throw new UnsupportedOperationException("not implemented");
    }

    public InetSocketAddress getAddress() {
        return address;
    }


    private class Listener implements Runnable {
        private InetSocketAddress listeningAddress;

        public Listener(InetSocketAddress addr) {
            this.listeningAddress = addr;
        }

        public void run() {
            try{
                int serverPort = this.listeningAddress.getPort();
                ServerSocket listenSocket = new ServerSocket(serverPort);

                // Server will start listening on the port
                while(true) {
                    Socket clientSocket = listenSocket.accept();
                    Connection connection = new Connection(clientSocket);
                }
            }
            catch(IOException e) {
                System.out.println("Listen :"+e.getMessage());
            }
        }
    }
    private class Connection extends Thread {
        ObjectInputStream input;
        ObjectOutputStream output;
        Socket clientSocket;

        public Connection (Socket socket) {
            try {
                clientSocket = socket;
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                input = new ObjectInputStream(clientSocket.getInputStream());
                this.start();
            }
            catch(IOException e) {
                System.out.println("Connection:"+e.getMessage());
            }
        }

        public void run() {
            // This is the part we parse the arguments and make method call

            try {
                RMICallInfo info = (RMICallInfo) input.readObject();
                //Class<?> klass = Class.forName(info.className);

                Method[] allMethods = klass.getDeclaredMethods();
                for (Method m : allMethods) {
                    String mname = m.getName();
                    // check if the method name matches the current method name
                    if (!mname.equals(info.methodName)) continue;

                    // Check if the number of parameters match
                    Type[] pType = m.getGenericParameterTypes();
                    if (pType.length != info.args.length) continue;


                    try {
                        Object o = m.invoke(server, info.args);
                        output.writeObject(o);
                        output.flush();
                        // Handle any exceptions thrown by method to be invoked.
                    } catch (InvocationTargetException x) {
                        // we need to throw RMI exception
                        // TODO: throwing meaningful RMI Exception
                    }
                }
            }
            catch (IOException ex) {
                System.out.println("input read failed");
            }
            catch (ClassNotFoundException ex) {
                System.out.println("This should never happen since we write our own RMICallInfo class");
            }
            catch (IllegalAccessException ex) {
                System.out.println("cannot access the object");
            }
        }
    }
}
