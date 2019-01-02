package com.ahmetkilic.ophelia.ea_utilities.tools;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class ObjectUtils {

    @SuppressWarnings("All")
    /**
     * Clone an object with a new instance.Object should implemets Serializable
     *
     * @param object object to clone
     */
    public static <T extends Serializable> T cloneObject(T object) {
        Gson gson = new Gson();
        Class<T> clazz = null;
        if (object instanceof Serializable) {
            clazz = (Class<T>) object.getClass();
        }
        if (clazz != null)
            return gson.fromJson(gson.toJson(object), clazz);
        else return null;
    }

    /**
     * check if an object is an array
     *
     * @param object object to check
     */
    public static boolean isArray(Object object) {
        return object instanceof Collection && !(object instanceof Map);
    }


    /**
     * Check if an array is not empty or null
     *
     * @param array array to check
     */
    public static boolean arrayIsNotEmpty(Collection array) {
        return array != null && array.size() > 0;
    }

    /**
     * Check if a class is primitive
     *
     * @param clazz class to check
     */
    private static boolean isPrimitive(Class<?> clazz) {
        return getWrapperTypes().contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(String.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(Date.class);
        return ret;
    }
}
