/*
 * ConnectionNotFoundException.java
 *
 * Created on 2000/12/01, 23:09
 */

package javax.microedition.io;

/**
 *
 * @author  akito
 * @version 
 */
public class ConnectionNotFoundException extends java.io.IOException {

    /** Creates new ConnectionNotFoundException */
    public ConnectionNotFoundException() {
        super();
    }
    public ConnectionNotFoundException(String param){
        super(param);
    }

}