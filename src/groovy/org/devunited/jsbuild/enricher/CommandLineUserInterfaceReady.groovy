package org.devunited.jsbuild.enricher

import org.devunited.jsbuild.templates.TemplateBuilder

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 7/29/11
 * Time: 8:31 PM
 */
public interface CommandLineUserInterfaceReady {

    public static showToUser = {delegate ->
        println delegate
    }

    public static builderState = {String state ->
        println "----[BUILDER] ${state.toUpperCase()}"
    }

    public static showToUserFromTemplate = {template, model ->
        println TemplateBuilder.buildTemplate(template, model)
    }

    public static putLineBreakWithHeight = {Integer num ->
        num.times {
            println ""
        }
    }

    def exitWithError = {error ->
        println "ERROR: ${error}"
        System.exit 1
    }

    def exitWithMessage = {message ->
        println message
        System.exit 0
    }

}