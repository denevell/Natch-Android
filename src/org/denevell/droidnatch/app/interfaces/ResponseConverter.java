package org.denevell.droidnatch.app.interfaces;

public interface ResponseConverter {
    
    <T> T convert(String s, Class<T> t);

}
