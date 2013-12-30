package org.denevell.droidnatch.app.interfaces;

public interface ObjectToStringConverter {
    
    <T> T convert(String s, Class<T> t);
    String encode(Object t);

}
