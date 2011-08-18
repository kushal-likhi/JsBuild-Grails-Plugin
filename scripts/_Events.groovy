eventCreateWarStart = {name, stagingDir ->

    String from = "${basedir}/grails-app/jsbuild/"
    String to = "${stagingDir}/WEB-INF/grails-app/jsbuild"

    println "[JSBUILD PLUGIN] Copying Source JS Files From: ${from}"

    ant.copy(toDir: to, filtering: true, overwrite: true) {
        fileset(dir: from)
    }

    println "[JSBUILD PLUGIN] Copied Files To: ${to}"
}
