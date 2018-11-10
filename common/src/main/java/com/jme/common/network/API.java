package com.jme.common.network;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import retrofit2.Call;


/**
 * Created by Yanmin on 2016/3/15.
 */

public abstract class API<T> {

    private String _name;
    private TypeToken token;
    private boolean _responseIsJson;

    public API(String name, boolean responseIsJson) {
        this._name = name;
        this._responseIsJson = responseIsJson;
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type type = parameterized.getActualTypeArguments()[0];

        token = TypeToken.get(type);
    }

    public API(String name) {
        this(name, true);
    }

    public abstract Call request(HashMap<String, String> params);

    public Type getEntryType() {
        return token.getType();
    }

    public String getName() {
        return _name;
    }

    public boolean isResponseJson() {
        return _responseIsJson;
    }
}
