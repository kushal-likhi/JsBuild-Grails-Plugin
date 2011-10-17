import org.devunited.jsbuild.grails.plugin.JsBuildWorkingDir
import org.devunited.jsbuild.grails.plugin.JsBuildTargetDir
import org.devunited.utils.DevUnitedBeanBagUser
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class JsbuildGrailsPlugin implements DevUnitedBeanBagUser {

    // the plugin version
    def version = "1.3.1.8"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.5 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Kushal Likhi"
    def authorEmail = "kushal.likhi@gmail.com"
    def title = "Javascript Building Framework"
    def description = '''\\
Implements www.devunited.org JsBuild utility in grails with added features
'''

    // URL to the plugin's documentation
    def documentation = "http://www.devunited.org/jsbuild"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)

        //Creating Directories
        storeInBeanBag "sourceDir", new JsBuildWorkingDir(applicationContext).toString()
        storeInBeanBag "targetDir", new JsBuildTargetDir(applicationContext).toString()
        storeInBeanBag "rebuild", false
        storeInBeanBag "commentsEnabled", false
        storeInBeanBag "buildConsole", false
        storeInBeanBag "minify", false
        if (ConfigurationHolder.config.jsbuild.rebuild) {
            storeInBeanBag "rebuild", true
        }
        if (ConfigurationHolder.config.jsbuild.commentsEnabled) {
            storeInBeanBag "commentsEnabled", true
        }
        if (ConfigurationHolder.config.jsbuild.console) {
            storeInBeanBag "buildConsole", true
        }
        if (ConfigurationHolder.config.jsbuild.minify) {
            storeInBeanBag "minify", true
        }
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.

        if (ConfigurationHolder.config.jsbuild.rebuild) {
            storeInBeanBag "rebuild", true
        } else {
            storeInBeanBag "rebuild", false
        }

        if (ConfigurationHolder.config.jsbuild.commentsEnabled) {
            storeInBeanBag "commentsEnabled", true
        } else {
            storeInBeanBag "commentsEnabled", false
        }

        if (ConfigurationHolder.config.jsbuild.minify) {
            storeInBeanBag "minify", true
        } else {
            storeInBeanBag "minify", false
        }

        if (ConfigurationHolder.config.jsbuild.console) {
            storeInBeanBag "buildConsole", true
        } else {
            storeInBeanBag "buildConsole", false
        }
    }
}
