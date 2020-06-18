package fudge

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation

// :: is not valid as a file name
private const val FieldMethodSeparator = "___"
private const val ObjectInstanceName = "INSTANCE"

@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(EntrypointMetadataGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class EntrypointMetadataGenerator : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Entrypoint::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun process(set: Set<TypeElement>, roundEnvironment: RoundEnvironment): Boolean {
        if (roundEnvironment.processingOver()) return true
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "Auto Fabric Annotation processor running")
        roundEnvironment.getElementsAnnotatedWith(Entrypoint::class.java).forEach { element ->
            val annotation = element.getAnnotation(Entrypoint::class.java)
            val entrypointReceiver = when (element) {
                is TypeElement -> {
                    val objectInstance = element.enclosedElements
                        .find { it is VariableElement && it.simpleName.toString() == ObjectInstanceName }
                    if (objectInstance != null) element.qualifiedName.toString() + FieldMethodSeparator + objectInstance.simpleName
                    else element.qualifiedName
                }
                is VariableElement -> element.getEnclosingClassName() + FieldMethodSeparator + element.simpleName
                is ExecutableElement -> element.getEnclosingClassName() + FieldMethodSeparator + element.simpleName
                else -> error("Only expecting @Entrypoint on TypeElements, VariableElements, and ExecutableElements")
            }

            generateEntrypointMetadata(entrypointReceiver, element, annotation)

        }
        return true
    }

    private fun Element.getEnclosingClassName(): String {
        val enclosing = enclosingElement
        if (enclosing !is TypeElement) error("Expected parent of element $this to be a class but it is not")
        return enclosing.qualifiedName.toString()
    }


    private fun generateEntrypointMetadata(
        entrypointReceiver: CharSequence,
        originatingElement: Element,
        annotation: Entrypoint
    ) {
        processingEnv.filer
            .createResource(
                StandardLocation.SOURCE_OUTPUT,
                "autofabric.entrypoints",
                entrypointReceiver,
                originatingElement
            )
            .openWriter()
            .use { it.write(annotation.value.joinToString("\n")) }
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

}