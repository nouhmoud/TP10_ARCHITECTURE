plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // LIGNE SUPPRIMÉE: id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "ma.projet.restclient"
    compileSdk = 34

    defaultConfig {
        applicationId = "ma.projet.restclient"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        // LIGNE SUPPRIMÉE: compose = true
        viewBinding = true // CONSERVÉE
    }
}

dependencies {

    // Dépendances Android de base (pour le système de Views)
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")

    // RecyclerView (Essentiel pour afficher la liste des comptes)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Coroutines (Nécessaire pour Retrofit avec 'suspend')
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // --- Dépendances Retrofit et Convertisseurs ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Convertisseur JSON (Gson)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Convertisseurs XML (SimpleXML)
    implementation("org.simpleframework:simple-xml:2.7.1")
    implementation("com.squareup.retrofit2:converter-simplexml:2.9.0")

    // Convertisseur JAXB (Si nécessaire, sinon retirez-le)
    implementation("com.squareup.retrofit2:converter-jaxb:2.9.0")

    // Dépendances de tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // Dépendances Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // 1. Convertisseur JSON (probablement déjà là)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // 2. Convertisseur XML (CRITIQUE pour le crash)
    implementation("com.squareup.retrofit2:converter-simplexml:2.9.0")
}