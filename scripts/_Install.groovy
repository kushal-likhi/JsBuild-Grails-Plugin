//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/grails-app/jobs")
//

ant.mkdir(dir: "${basedir}/grails-app/jsbuild")
ant.mkdir(dir: "${basedir}/grails-app/jsbuild/.jsbuild")

String to = "${basedir}/grails-app/jsbuild/.jsbuild"

String from = "${pluginBasedir}/grails-app/jsbuild/.jsbuild"

println "[JSBUILD PLUGIN] Copying Component Files"

ant.copy(toDir: to, filtering: true, overwrite: true) {
    fileset(dir: from)
}

println "[JSBUILD PLUGIN] Copied Files To: ${to}"
