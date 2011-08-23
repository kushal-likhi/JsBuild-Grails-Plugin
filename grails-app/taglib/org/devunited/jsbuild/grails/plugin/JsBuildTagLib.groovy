package org.devunited.jsbuild.grails.plugin

import org.devunited.utils.DevUnitedBeanBagUser
import org.devunited.jsbuild.grails.plugin.utils.FileNameBuilder
import org.devunited.jsbuild.JsBuild


class JsBuildTagLib implements DevUnitedBeanBagUser {

    static namespace = "jsbuild"

    def include = {attrs ->

        String packageName = attrs['packageName']
        String fileName

        if (!beanBagContains(packageName)) {
            fileName = FileNameBuilder.fileName(packageName)
            storeInBeanBag packageName, fileName
            new JsBuild().build(packageName)
        } else if (readFromBeanBag("rebuild")) {
            fileName = readFromBeanBag(packageName)
            new JsBuild().build(packageName)
        } else {
            fileName = readFromBeanBag(packageName)
        }

        String file = "jsbuild/${fileName}"

        if (readFromBeanBag("minify")) {
            file = file.replaceAll(/.js$/, "") + ".min.js"
        }

        out << "<script type=\"text/javascript\" src=\"${g.resource(dir: 'js', file: file)}\"></script>"

    }
}
