import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

// Workaround for https://github.com/gradle/gradle/issues/15383
internal val Project.libs: LibrariesForLibs
    get() = the()
