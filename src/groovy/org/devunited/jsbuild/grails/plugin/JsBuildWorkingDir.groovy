package org.devunited.jsbuild.grails.plugin

import org.devunited.jsbuild.grails.plugin.utils.RealPathFinder
import org.devunited.jsbuild.grails.plugin.base.DirectoryCreator

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 8/15/11
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
class JsBuildWorkingDir extends DirectoryCreator {

    public JsBuildWorkingDir(applicationContext) {

        dirPath = new RealPathFinder(applicationContext).pathToJsBuildDir()

        canonicalPath = createDirIfNotExist().getCanonicalPath()

    }

}
