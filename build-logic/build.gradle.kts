plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.pluginartifact.android)
    implementation(libs.pluginartifact.kotlin)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
