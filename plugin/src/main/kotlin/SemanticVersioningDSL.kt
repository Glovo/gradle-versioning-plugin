import com.glovoapp.gradle.plugin.PLUGIN_ID
import org.gradle.plugin.use.PluginDependenciesSpec

val PluginDependenciesSpec.`semantic-versioning`
    get() = id(PLUGIN_ID)
