package org.denevell.droidnatch.interfaces;

public interface ResponseConverter {
    
    <T> T convert(String s, Class<T> t);

}
