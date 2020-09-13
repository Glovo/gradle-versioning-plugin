import com.android.build.gradle.internal.dsl.BaseFlavor
import com.android.build.gradle.internal.dsl.DecoratedBaseFlavor
import com.glovo.mobile.release.internal.PropertiesFile
import org.gradle.api.Project
import java.io.File

fun Project.propertiesFile(path: String): PropertiesFile = PropertiesFile(file(path))

fun BaseFlavor.persistedVersions(from: PropertiesFile, project: Project) {
    val decorated = DecoratedBaseFlavor(this, project)
    decorated.setPersistedVersionsFrom(from)
}

fun BaseFlavor.persistedVersions(from: File, project: Project) {
    val decorated = DecoratedBaseFlavor(this, project)
    decorated.setPersistedVersionsFrom(from)
}

fun BaseFlavor.persistedVersionName(from: PropertiesFile, project: Project) {
    val decorated = DecoratedBaseFlavor(this, project)
    decorated.setPersistedVersionName(from)
}

fun BaseFlavor.persistedVersionName(from: File, project: Project) {
    val decorated = DecoratedBaseFlavor(this, project)
    decorated.setPersistedVersionName(from)
}

fun BaseFlavor.persistedVersionCode(from: PropertiesFile, project: Project) {
    val decorated = DecoratedBaseFlavor(this, project)
    decorated.setPersistedVersionCode(from)
}

fun BaseFlavor.persistedVersionCode(from: File, project: Project) {
    val decorated = DecoratedBaseFlavor(this, project)
    decorated.setPersistedVersionCode(from)
}
