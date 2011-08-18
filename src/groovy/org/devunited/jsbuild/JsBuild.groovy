package org.devunited.jsbuild

import org.devunited.jsbuild.messages.ConsolePosters

import org.devunited.jsbuild.messages.MessageTemplate
import org.devunited.jsbuild.enricher.CommandLineUserInterfaceReady
import org.devunited.jsbuild.builders.JsCommentBuilder
import org.devunited.jsbuild.builders.JsPackageBuilder
import org.devunited.utils.DevUnitedBeanBagUser

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 7/28/11
 * Time: 11:01 PM
 */

class JsBuild implements CommandLineUserInterfaceReady, DevUnitedBeanBagUser {

    public static String baseDir = ""

    public static String targetFilePath = ""

    public static boolean isFileCommentsEnabled = true

    public static Integer filesScanned = 0

    public static Integer commentsFound = 0

    public static Integer totalProperties = 0

    public static Integer totalPackages = 0

    public static Integer totalLoc = 0

    public static Integer totalBlankLines = 0

    public static Integer totalLinesInBuild = 0



    public JsBuild() {
        baseDir = ""
        targetFilePath = ""
        isFileCommentsEnabled = true
        filesScanned = 0
        commentsFound = 0
        totalProperties = 0
        totalPackages = 0
        totalLoc = 0
        totalBlankLines = 0
        totalLinesInBuild = 0
    }


    public void build(String packageName) {

        Long startTime = System.currentTimeMillis()

        isFileCommentsEnabled = readFromBeanBag("commentsEnabled") as Boolean

        File sourceDir = new File(readFromBeanBag("sourceDir") + File.separatorChar + packageName)
        File targetDir = new File(readFromBeanBag("targetDir") as String)

        File targetFile = new File(targetDir.getCanonicalPath() + File.separatorChar + readFromBeanBag(packageName))

        baseDir = sourceDir.getCanonicalPath()

        try {
            targetFile.createNewFile()
            targetFilePath = targetFile.getCanonicalPath()
        } catch (Exception e) {
            showToUser e.getStackTrace()
            exitWithError "Error Creating Target File"
        }

        String targetFileContents = ""

        targetFileContents += new JsCommentBuilder(sourceDir).comments

        targetFileContents += new JsPackageBuilder([recursionLevel: 1, recursionSibling: 1]).build(sourceDir)

        targetFile.write targetFileContents

        targetFileContents.eachLine {totalLinesInBuild++}

        showToUser ConsolePosters.summaryPoster()

        showToUser "[JsBuild] Time To Build: ${System.currentTimeMillis() - startTime}ms"

    }


}
