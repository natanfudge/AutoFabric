package fudge

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.apache.groovy.json.internal.LazyMap
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.compile.JavaCompile
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

//TODO: work for kapt

class AutoFabricPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        ProjectContext(project).apply()
    }
}

private fun getVersion(): String = AutoFabricPlugin::class.java.classLoader.getResource("version.txt").readText()

class ProjectContext(private val project: Project) {
    fun apply() {
        project.tasks.getByName("classes").doLast {
            insertEntrypoints()
        }

        addAnnotationProcessorDep()
    }

    private fun addAnnotationProcessorDep() {
        project.repositories.jcenter()

        val dependency = "io.github.fudge:autofabric-ap:${getVersion()}"
        project.dependencies.add("compileOnly", dependency)

        for (sourceSet in getSourceSets()) {
            project.dependencies.add(sourceSet.annotationProcessorConfigurationName, dependency)
            // Gradle doesn't wanna pull kotlin and groovy for some reason
            project.dependencies.add(
                sourceSet.annotationProcessorConfigurationName,
                "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.72"
            )
            project.dependencies.add(
                sourceSet.annotationProcessorConfigurationName,
                "org.codehaus.groovy:groovy-all:2.4.15"
            )
        }
    }

    private fun insertEntrypoints() {
        for (sourceSet in getSourceSets()) {
            val entrypointsDir = Paths.get(
                sourceSet.compileJavaTask().options.annotationProcessorGeneratedSourcesDirectory.toString(),
                "autofabric",
                "entrypoints"
            )
            if (!entrypointsDir.exists()) continue

            val fabricModJson = Paths.get(sourceSet.output.resourcesDir.toString(), "fabric.mod.json")
            if (!Files.exists(fabricModJson)) {
                warn("Could not find fabric.mod.json that is expected to be at $fabricModJson")
                return
            }
            //TODo: switch to kotlinx serialization
            val json = JsonSlurper().parseText(fabricModJson.readText())
            if (json !is LazyMap) {
                warn("fabric.mod.json at $fabricModJson is not a json map and cannot be parsed")
                return
            }

            entrypointsDir.toFile().listFiles()!!.forEach {
                val key = it.readText()
                val value = it.name
                if (json.containsKey(key)) {
                    val entrypointList = json[key]
                    if (entrypointList !is MutableList<*>) {
                        warn("Value of $key is unexpectedly not a list")
                        return
                    }
                    @Suppress("UNCHECKED_CAST")
                    entrypointList as MutableList<String>
                    entrypointList.add(value)
                } else {
                    json[key] = listOf(value)
                }
            }

            fabricModJson.writeText(JsonBuilder(json).toPrettyString())
        }
    }

    private fun SourceSet.compileJavaTask(): JavaCompile = project.tasks.getByName(
        compileJavaTaskName
    ) as JavaCompile

    private fun getSourceSets() = project.convention.getPlugin(
        JavaPluginConvention::class.java
    ).sourceSets

    private fun warn(warning: String) = project.logger.warn(warning)


}

fun Path.exists() = Files.exists(this)
fun Path.readText() = Files.readAllBytes(this).toString(Charset.defaultCharset())
fun Path.writeText(text: String): Path = Files.write(this, text.toByteArray())
