package com.nordstrom.ds.autoerroretry.converters;


import java.io.*;
import java.util.Base64;

public class ObjectStringConverter {

    private ObjectStringConverter(){}

    private static ObjectStringConverter objectStringConverter;

    public static ObjectStringConverter getObjectStringConverter(){
        if(objectStringConverter == null){
            objectStringConverter = new ObjectStringConverter();
        }
        return objectStringConverter;
    }

    /**
     * Converts any given class that implements Serializable into a base64 encoded string
     * @param o
     * @return
     * @throws IOException
     */
    public String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.flush();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /**
     * Converts a string into a object.
     * @param s
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public  Object fromString( String s ) throws IOException, ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

}
