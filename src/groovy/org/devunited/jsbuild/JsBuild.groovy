package org.devunited.jsbuild

import org.devunited.jsbuild.messages.ConsolePosters

import org.devunited.jsbuild.enricher.CommandLineUserInterfaceReady
import org.devunited.jsbuild.builders.JsCommentBuilder
import org.devunited.jsbuild.builders.JsPackageBuilder
import org.devunited.jsbuild.builders.JsAnnotationEngine
import org.devunited.utils.DevUnitedBeanBagUser

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 7/28/11
 * Time: 11:01 PM
 */

class JsBuild implements CommandLineUserInterfaceReady, DevUnitedBeanBagUser {

    public String baseDir = ""

    public String baseDirBackup = ""

    public String homeDir = ""

    public String targetFilePath = ""

    public String minFilePath = "N/A"

    public boolean isFileCommentsEnabled = true

    public boolean modeRemoteBuild = false

    public boolean buildConsole = false

    public String basePackage = ""

    public Integer filesScanned = 0

    public Integer commentsFound = 0

    public Integer totalProperties = 0

    public Integer totalPackages = 0

    public Integer totalConstructors = 0

    public Integer totalLoc = 0

    public Integer totalBlankLines = 0

    public Integer totalLinesInBuild = 0

    public Map exportedProperties = [:]

    public Map aliasedProperties = [:]

    public Map overrideProperties = [:]

    public Map eventRegistry = [:]

    public Map intervalRegistry = [:]

    public Map injectProperties = [:]

    public List errors = []

    public List<String> constructors = []



    public void build(String packageName) {

        Long startTime = System.currentTimeMillis()



        isFileCommentsEnabled = readFromBeanBag("commentsEnabled") as Boolean
        buildConsole = readFromBeanBag("buildConsole") as Boolean



        File sourceDir = new File(readFromBeanBag("sourceDir") + File.separatorChar + packageName)
        File home = new File(readFromBeanBag("sourceDir") + File.separatorChar + ".jsbuild")
        File targetDir = new File(readFromBeanBag("targetDir") as String)
        File targetFile = new File(targetDir.getCanonicalPath() + File.separatorChar + readFromBeanBag(packageName))



        baseDir = sourceDir.getCanonicalPath()
        baseDirBackup = sourceDir.getCanonicalPath()
        homeDir = home.getCanonicalPath()



        try {
            targetFile.createNewFile()
            targetFilePath = targetFile.getCanonicalPath()
        } catch (Exception e) {
            showToUser e.getStackTrace()
            exitWithError "Error Creating Target File"
        }



        String targetFileContents = ""
        targetFileContents += new JsCommentBuilder(sourceDir, this).comments



        basePackage = JsPackageBuilder.determinePackage(sourceDir, this)
        targetFileContents += new JsPackageBuilder([recursionLevel: 1, recursionSibling: 1], this).build(sourceDir)
        JsAnnotationEngine annotationEngine = new JsAnnotationEngine(targetFileContents)
        annotationEngine.processExports(exportedProperties)
        targetFileContents = annotationEngine.contents
        targetFile.write targetFileContents
        targetFileContents.eachLine {totalLinesInBuild++}



        boolean minify = readFromBeanBag("minify") as Boolean
        if (minify) {
            minFilePath = targetFilePath.replaceAll(/.js$/, "") + ".min.js"
            String[] arg = [targetFilePath, "-o", minFilePath]
            try {
                com.yahoo.platform.yui.compressor.YUICompressor.main(arg)
            } catch (Exception e) {
                minFilePath = "ERROR OCCOURED WHILE BUILDING"
            }
        }



        showToUser ConsolePosters.summaryPoster(this)



        if (!errors.isEmpty()) {
            showToUser "NOTE: There Were Few Errors/Warnings During Build"
            showToUser "-------------------------------------------------"
            errors.each {
                showToUser it
            }
            putLineBreakWithHeight 2
        }



        showToUser "[JsBuild] Time To Build: ${System.currentTimeMillis() - startTime}ms"

    }


}
