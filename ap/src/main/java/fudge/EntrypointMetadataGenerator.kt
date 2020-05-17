package fudge

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation

//TODO: remember to modify services/ and gradle/ when changing the processor name

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
        roundEnvironment.getElementsAnnotatedWith(Entrypoint::class.java).forEach {
            if (it is TypeElement) {
                generateEntrypointMetadata(it,it.getAnnotation(Entrypoint::class.java))
            } else {
                error("Only expecting @Entrypoint on TypeElements")
            }

        }
        return true
    }


    private fun generateEntrypointMetadata(type: TypeElement, annotation : Entrypoint) {
        processingEnv.filer
            .createResource(StandardLocation.SOURCE_OUTPUT, "autofabric.entrypoints", type.qualifiedName, type)
            .openWriter()
            .use { it.write(annotation.value) }
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

}