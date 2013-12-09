package org.denevell.droidnatch.app.interfaces;

public interface ObjectStringConverter {
    
    <T> T convert(String s, Class<T> t);
    String encode(Object t);

}
