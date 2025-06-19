plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

tasks.withType<JavaCompile> {
    dependsOn("spotlessApply")
}
