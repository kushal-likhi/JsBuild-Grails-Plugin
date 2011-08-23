package org.devunited.jsbuild.builders

import org.devunited.jsbuild.enricher.CommandLineUserInterfaceReady
import org.devunited.jsbuild.templates.TemplateBuilder

/**
 * Created by IntelliJ IDEA.
 * User: kushal
 * Date: 8/17/11
 * Time: 8:08 PM
 * To change this template use File | Settings | File Templates.
 */
class JsAnnotationEngine implements CommandLineUserInterfaceReady {

    public String contents

    JsAnnotationEngine(String contents) {
        this.contents = contents
    }

    def addLine = {line ->
        contents += ('\n' + line)
    }

    public void processExports(Map exportedProperties) {
        exportedProperties.each {key, value ->
            addLine "var ${key} = ${value};"
        }
    }

    public void processAlias(Map aliasedProperties) {
        aliasedProperties.each {key, value ->
            addLine "${key} = ${value};"
        }
    }

    public void processOverrides(Map overrideProperties) {
        overrideProperties.each {key, value ->
            addLine "${key} = ${value};"
        }
    }

    public String processIntervals(String identifier, Map args) {
        String data
        data = TemplateBuilder.buildTemplate(codeTemplates.createInterval,
                [
                        interval: args.interval,
                        target: args.target,
                        name: identifier
                ]
        )
        data
    }

    public String buildEventCode(String handler, Map args) {
        String code
        String selector = args.selector
        String event = args.event
        if (selector.startsWith("#")) {
            code = TemplateBuilder.buildTemplate(
                    codeTemplates.eventHandlerIdBasedTryCatch,
                    [
                            id: (selector - "#"),
                            event: event,
                            handler: handler
                    ]
            )
        } else if (selector.startsWith(".")) {
            code = TemplateBuilder.buildTemplate(
                    codeTemplates.eventHandlerClassBased,
                    [
                            className: (selector - "."),
                            event: event,
                            handler: handler
                    ]
            )
        } else {
            code = TemplateBuilder.buildTemplate(
                    codeTemplates.eventHandlerImplicit,
                    [
                            object: (selector),
                            event: event,
                            handler: handler
                    ]
            )
        }


        code
    }

    private Map codeTemplates = [
            eventHandlerIdBased: "if(document.getElementById('###id###')){document.getElementById('###id###').###event### = ###handler###}",
            eventHandlerIdBasedTryCatch: "try{document.getElementById('###id###').###event### = ###handler###}catch(c){}",
            eventHandlerImplicit: "###object###.###event### = ###handler###;",
            createInterval: "###name### = setInterval('###target###()',###interval###);",
            eventHandlerClassBased: "try{var aht = document.getElementsByTagName(\"*\");for(idx in aht){if(aht[idx].className == \"###className###\"){aht[idx].###event### = ###handler###}}}catch(c){}"
    ]

}