package rmi;

import java.io.*;
import java.util.HashMap;

/**
 * Converts method call information to byte array,
 * and vice versa.
 */
class Serializer {
    static class RMICallInfo {
        public String className;
        public String methodName;
        public Object[] args;
    }

    public static byte[] toBytes(Class c, String method, Object[] args) throws RMIException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("class", c.getCanonicalName());
        data.put("method", method);
        data.put("args", args);

        ObjectOutput out = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(data);
            out.flush();
            return bos.toByteArray();
        } catch (Throwable e) {
            throw new RMIException("Could not serialize object to bytes");
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static RMICallInfo fromBytes(byte[] bytes) throws RMIException  {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        RMICallInfo data = new RMICallInfo();

        try {
            in = new ObjectInputStream(bis);
            HashMap<String, Object> map = (HashMap<String, Object>) in.readObject();
            data.args = (Object[]) map.get("args");
            data.className = (String) map.get("class");
            data.methodName = (String) map.get("method");

            return data;
        } catch (Throwable e) {
            throw new RMIException("Could not deserialize object from bytes");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
    }
}
