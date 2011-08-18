package org.devunited.jsbuild.grails.plugin.utils

import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 8/15/11
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
class RealPathFinder {

    def applicationContext
    def grailsApplication
    Boolean warDeployed = false

    public RealPathFinder(applicationContext) {

        this.applicationContext = applicationContext

        grailsApplication = applicationContext.getBean("grailsApplication")

        this.warDeployed = grailsApplication.isWarDeployed()
    }

    public RealPathFinder() {}

    public String pathToJsBuildDir() {
        String path
        if (warDeployed) {
            path = ApplicationHolder.application.parentContext.servletContext.getRealPath('WEB-INF/grails-app/jsbuild/')
        } else {
            path = 'grails-app/jsbuild/'
        }
        return path
    }

    public String pathToJsBuildTargetDir() {
      return ApplicationHolder.application.parentContext.servletContext.getRealPath("js/jsbuild") as String
    }

}
