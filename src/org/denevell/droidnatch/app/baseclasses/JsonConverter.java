package org.denevell.droidnatch.app.baseclasses;

import org.denevell.droidnatch.app.interfaces.ObjectStringConverter;

import com.google.gson.Gson;

public class JsonConverter implements ObjectStringConverter {
    @Override
    public <T> T convert(String s, Class<T> t) {
        return new Gson().fromJson(s, t);
    }

    @Override
    public String encode(Object t) {
        return new Gson().toJson(t);
    }
}