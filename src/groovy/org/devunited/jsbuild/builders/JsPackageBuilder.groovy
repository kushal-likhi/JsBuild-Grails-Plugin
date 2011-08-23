package org.devunited.jsbuild.builders

import org.devunited.jsbuild.enricher.CommandLineUserInterfaceReady

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 7/30/11
 * Time: 11:56 AM
 */
class JsPackageBuilder implements CommandLineUserInterfaceReady {

    File basePackage

    def mainContext

    Integer recursionLevel = 1
    Integer recursionSibling = 1

    JsPackageBuilder(Integer recursionLevel, Integer recursionSibling) {
        this.recursionLevel = recursionLevel
        this.recursionSibling = recursionSibling
    }

    JsPackageBuilder(Map options, mainContext) {
        this.recursionLevel = options.recursionLevel
        this.recursionSibling = options.recursionSibling
        this.mainContext = mainContext
    }

    public String build(File base) {
        basePackage = base

        mainContext.totalPackages++



        String contentBuffer
        if (recursionLevel == 1) {
            contentBuffer = "var ${basePackage.getCanonicalPath().split(File.separatorChar.toString()).last()} = "
        } else {
            contentBuffer = "${basePackage.getCanonicalPath().split(File.separatorChar.toString()).last()}: "
        }


        return (contentBuffer + new JsNamespaceBuilder(
                [
                        recursionLevel: recursionLevel,
                        recursionSibling: recursionSibling
                ],
                mainContext
        ).build(basePackage) + (recursionLevel == 1 ? ";" : ""))
    }

    public static String determinePackage(File packageLocation, mainContext) {
        String basePath = new File((mainContext.baseDir + File.separatorChar + "..")).getCanonicalPath() + File.separatorChar
        String packageDir = packageLocation.getCanonicalPath()
        String packageName = packageDir - basePath
        packageName = packageName.trim().replace('/', '.').replace(File.separator, '.')
        if (mainContext.modeRemoteBuild) {
            packageName = (mainContext.basePackage + "." + packageName)
        }
        return packageName
    }
}
