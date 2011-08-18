package org.devunited.utils

import org.devunited.utils.BeanBag

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 8/15/11
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DevUnitedBeanBagUser {

    public def storeInBeanBag = {String key, Object value ->
        BeanBag.put(key, value)
    }

    public def readFromBeanBag = {String key ->
        return BeanBag.fetch(key)
    }

    public def beanBagContains = {key ->
        return BeanBag.check(key)
    }

}