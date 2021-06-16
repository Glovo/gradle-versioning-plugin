import com.glovoapp.gradle.plugin_android.PLUGIN_ID
import org.gradle.plugin.use.PluginDependenciesSpec

val PluginDependenciesSpec.`android-versioning`
    get() = id(PLUGIN_ID)
