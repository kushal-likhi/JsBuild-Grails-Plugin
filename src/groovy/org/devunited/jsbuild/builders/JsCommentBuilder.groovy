package org.devunited.jsbuild.builders

import org.devunited.jsbuild.enricher.CommandLineUserInterfaceReady
import org.devunited.jsbuild.BuildInfo
import org.devunited.jsbuild.messages.MessageTemplate
import org.devunited.jsbuild.templates.TemplateBuilder

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 7/30/11
 * Time: 12:44 AM
 */
class JsCommentBuilder implements CommandLineUserInterfaceReady {

    File directoryToBeScanned

    Integer filesScanned = 0
    Integer filesFound = 0

    public static String COMMENT_START = "/*" + '\n'
    public static String COMMENT_END = " */" + '\n\n'

    def mainContext

    public JsCommentBuilder(File file, mainContext) {
        directoryToBeScanned = file
        this.mainContext = mainContext
    }

    public String getComments() {
        String comments = ""
        def addComment = {comm ->
            comments += " * ${comm}" + '\n'
        }
        addComment TemplateBuilder.buildTemplate(MessageTemplate.COMMENT_FACTORY_HEADER_NOTICE, [project: BuildInfo.projectName, version: BuildInfo.version])

        addComment "\n"

        directoryToBeScanned.eachFileRecurse {file ->
            if (file.getName().endsWith(".comment")) {
                addComment "PACKAGE: " + JsPackageBuilder.determinePackage(file.parentFile, mainContext)
                file.eachLine {line ->
                    addComment line
                }
                addComment "\n"
                filesFound++
            }
            filesScanned++
        }

        mainContext.filesScanned = filesScanned
        mainContext.commentsFound = filesFound

        (COMMENT_START + comments + COMMENT_END)
    }
}
