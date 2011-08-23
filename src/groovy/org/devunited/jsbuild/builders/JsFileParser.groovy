package org.devunited.jsbuild.builders

import org.devunited.jsbuild.enricher.CommandLineUserInterfaceReady

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 7/31/11
 * Time: 12:14 AM
 */
class JsFileParser implements CommandLineUserInterfaceReady {

    File targetFile

    String comments
    String property

    boolean ignored = false

    def mainContext


    JsFileParser(File target, mainContext) {
        targetFile = target
        this.mainContext = mainContext
        parse()
    }

    private void parse() {

        String commentBuffer
        String propertyBuffer = targetFile.getText()

        Map channels = splitFileContentToChannels(propertyBuffer)

        commentBuffer = channels.commentChannel
        propertyBuffer = channels.propertyChannel

        if (commentBuffer.trim().length() > 0) {
            comments = decorateComments(commentBuffer)
        } else {
            comments = commentBuffer.trim()
        }
        property = removeBlankLines(propertyBuffer)
    }

    public String getComments() {
        if (mainContext.isFileCommentsEnabled) {
            return comments
        } else {
            return ""
        }
    }

    private String removeBlankLines(String text) {
        String outBuffer = ""
        boolean isFirst = true
        text.eachLine {line ->
            if (line.trim() != "") {
                outBuffer += ((isFirst ? '' : '\n') + line)
                isFirst = false
            }
        }
        return outBuffer
    }

    private String decorateComments(String comments) {
        String decoratedComment = ""

        comments = comments.replace('/*', '').replace('*/', '').replace('//', '')

        comments.eachLine {line ->
            if (line.startsWith(" *")) {
                decoratedComment += (line + '\n')
            } else if (line.startsWith("*")) {
                decoratedComment += (' ' + line + '\n')
            } else {
                decoratedComment += (' *' + line + '\n')
            }
        }

        ('/* \n' + decoratedComment + ' */')
    }

    private Map splitFileContentToChannels(String text) {
        Map channels = [
                commentChannel: "",
                propertyChannel: ""
        ]

        boolean commentBlockLock = false
        boolean propertyLock = false
        boolean isFirst = true

        text.eachLine {line ->
            if (!ignored) {
                if (line.trim() != "") {
                    if (!propertyLock && !commentBlockLock && line.trim().startsWith("//")) {
                        channels.commentChannel += ((isFirst ? '' : '\n') + line)
                    } else if (!propertyLock && !commentBlockLock && line.trim().startsWith("/*")) {
                        commentBlockLock = true
                        channels.commentChannel += ((isFirst ? '' : '\n') + line)
                    } else if (!propertyLock && commentBlockLock) {
                        channels.commentChannel += ((isFirst ? '' : '\n') + line)
                        if (line.trim().endsWith("*/")) {
                            commentBlockLock = false
                        }
                    } else if (!propertyLock && !commentBlockLock && line.trim().startsWith("@")) {
                        new JsAnnotationProcessor(line, targetFile, mainContext, this)
                    } else {
                        channels.propertyChannel += ((isFirst ? '' : '\n') + line)
                        propertyLock = true
                    }
                    isFirst = false
                } else {
                    mainContext.totalBlankLines++
                }
                mainContext.totalLoc++
            }
        }

        if (ignored) {
            channels.commentChannel = "Property Ignored"
            channels.propertyChannel = "null"
        }

        channels
    }
}
