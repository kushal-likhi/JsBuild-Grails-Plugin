package org.devunited.jsbuild.grails.plugin.base

import org.devunited.jsbuild.grails.plugin.interfaces.DirectoryCreatorReady

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 8/15/11
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class DirectoryCreator implements DirectoryCreatorReady {

    String canonicalPath
    String dirPath

    public File createDirIfNotExist() {
        File dir = new File(dirPath)
        if (!dir.exists()) {
            dir.mkdir()
        }
        dir
    }

    public String toString(){
        return canonicalPath
    }

}
