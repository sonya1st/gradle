plugins {
    java
}
version = "1.0"

// tag::add-to-manifest[]
tasks.getByName<Jar>("jar") {
    manifest {
        attributes(
            "Implementation-Title" to "Gradle",
            "Implementation-Version" to version
        )
    }
}
// end::add-to-manifest[]

// tag::custom-manifest[]
val sharedManifest = the<JavaPluginConvention>().manifest {
    attributes (
        "Implementation-Title" to "Gradle",
        "Implementation-Version" to version
    )
}

task<Jar>("fooJar") {
    manifest = project.the<JavaPluginConvention>().manifest {
        from(sharedManifest)
    }
}
// end::custom-manifest[]

// tag::merge[]
task<Jar>("barJar") {
    manifest {
        attributes("key1" to "value1")
        from(sharedManifest, "src/config/basemanifest.txt")
        from(listOf("src/config/javabasemanifest.txt", "src/config/libbasemanifest.txt")) {
            eachEntry(Action<ManifestMergeDetails> {
                if (baseValue != mergeValue) {
                    value = baseValue
                }
                if (key == "foo") {
                    exclude()
                }
            })
        }
    }
}
// end::merge[]

// tag::write[]
tasks.getByName<Jar>("jar").manifest.writeTo("$buildDir/mymanifest.mf")
// end::write[]