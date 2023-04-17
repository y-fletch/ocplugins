import okhttp3.OkHttpClient
import okhttp3.Request
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.get
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.nio.file.Paths
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*


open class BootstrapTask : DefaultTask() {

    private fun formatDate(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("yyyy-MM-dd").format(this)
    }

    private fun hash(file: ByteArray): String {
        return MessageDigest.getInstance("SHA-512").digest(file).fold("", { str, it -> str + "%02x".format(it) })
            .toUpperCase()
    }

    private fun getBootstrap(): JSONArray? {
        val client = OkHttpClient()

        val url = "https://raw.githubusercontent.com/y-fletch/ocplugins/main/plugins.json"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute()
            .use { response -> return JSONObject("{\"plugins\":${response.body!!.string()}}").getJSONArray("plugins") }
    }

    @TaskAction
    fun boostrap() {
        if (project == project.rootProject) {
            val bootstrapDir = File("${project.buildDir}")
            val bootstrapReleaseDir = File("${project.buildDir}/release")

            val finalDir = File(".")
            val finalReleaseDir = File("release")

            bootstrapDir.mkdirs()
            bootstrapReleaseDir.mkdirs()

            val plugins = ArrayList<JSONObject>()
            val baseBootstrap = getBootstrap() ?: throw RuntimeException("Base bootstrap is null!")

            project.subprojects.forEach {
                if (it.project.properties.containsKey("PluginName") && it.project.properties.containsKey("PluginDescription")) {
                    var pluginAdded = false
                    val plugin = it.project.tasks.get("jar").outputs.files.singleFile

                    val releases = ArrayList<JsonBuilder>()

                    releases.add(
                        JsonBuilder(
                            "version" to it.project.version,
                            "requires" to ProjectVersions.apiVersion,
                            "date" to formatDate(Date()),
                            "url" to "https://github.com/y-fletch/ocplugins/blob/main/release/${it.project.name}-${it.project.version}.jar?raw=true",
                            "sha512sum" to hash(plugin.readBytes())
                        )
                    )

                    val pluginObject = JsonBuilder(
                        "name" to it.project.extra.get("PluginName"),
                        "id" to nameToId(it.project.extra.get("PluginName") as String),
                        "description" to it.project.extra.get("PluginDescription"),
                        "provider" to "yfletch",
                        "projectUrl" to "https://github.com/y-fletch/ocplugins",
                        "releases" to releases.toTypedArray()
                    ).jsonObject()

                    println(pluginObject)

                    for (i in 0 until baseBootstrap.length()) {
                        val item = baseBootstrap.getJSONObject(i)

                        if (!item.get("id").equals(nameToId(it.project.extra.get("PluginName") as String))) {
                            continue
                        }

                        pluginAdded = true

                        val fileName = "${it.project.name}-${it.project.version}"

                        // version already exists
                        if (fileName in item.getJSONArray("releases").toString()) {
                            plugins.add(item)
                            break
                        }

                        // new version
                        plugins.add(
                            JsonMerger(arrayMergeMode = JsonMerger.ArrayMergeMode.MERGE_ARRAY).merge(
                                item,
                                pluginObject
                            )
                        )
                        plugin.copyTo(
                            Paths.get(
                                finalReleaseDir.toString(),
                                "${it.project.name}-${it.project.version}.jar"
                            ).toFile()
                        )
                    }

                    if (!pluginAdded) {
                        plugins.add(pluginObject)
                        plugin.copyTo(
                            Paths.get(
                                finalReleaseDir.toString(),
                                "${it.project.name}-${it.project.version}.jar"
                            ).toFile()
                        )
                    }
                }
            }

            File(finalDir, "plugins.json").printWriter().use { out ->
                out.println(plugins.toString())
            }
        }

    }
}
