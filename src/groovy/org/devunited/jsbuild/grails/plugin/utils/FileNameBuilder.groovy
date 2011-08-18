package org.devunited.jsbuild.grails.plugin.utils

import org.codehaus.groovy.grails.commons.ConfigurationHolder

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 8/15/11
 * Time: 7:09 PM
 * To change this template use File | Settings | File Templates.
 */
class FileNameBuilder {

    def static config = ConfigurationHolder.config

    public static String fileName(String packageName) {
        String namePrefix = config.jsbuild.prefix ?: "jsb"
        namePrefix += ".${packageName}.js"
        namePrefix
    }


}
