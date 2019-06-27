import com.android.build.gradle.internal.dsl.BaseFlavor
import com.android.build.gradle.internal.dsl.DecoratedBaseFlavor
import com.glovo.mobile.release.internal.PropertiesFile
import org.gradle.api.Project
import java.io.File

fun Project.propertiesFile(path: String): PropertiesFile = PropertiesFile(file(path))

fun BaseFlavor.persistedVersions(from: PropertiesFile) {
    val decorated = DecoratedBaseFlavor(this)
    decorated.setPersistedVersionsFrom(from)
}

fun BaseFlavor.persistedVersions(from: File) {
    val decorated = DecoratedBaseFlavor(this)
    decorated.setPersistedVersionsFrom(from)
}

fun BaseFlavor.persistedVersionName(from: PropertiesFile) {
    val decorated = DecoratedBaseFlavor(this)
    decorated.setPersistedVersionName(from)
}

fun BaseFlavor.persistedVersionName(from: File) {
    val decorated = DecoratedBaseFlavor(this)
    decorated.setPersistedVersionName(from)
}

fun BaseFlavor.persistedVersionCode(from: PropertiesFile) {
    val decorated = DecoratedBaseFlavor(this)
    decorated.setPersistedVersionCode(from)
}

fun BaseFlavor.persistedVersionCode(from: File) {
    val decorated = DecoratedBaseFlavor(this)
    decorated.setPersistedVersionCode(from)
}
