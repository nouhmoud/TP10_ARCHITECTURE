package ma.projet.restclient.config

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

// Utilise un Kotlin 'object' pour un Singleton
object RetrofitClient {

    private var retrofit: Retrofit? = null
    private var currentFormat: String? = null

    // Assurez-vous que l'URL et le port correspondent à votre serveur REST
    private const val BASE_URL = "http://10.0.2.2:8082/"

    /**
     * Retourne une instance de Retrofit configurée avec le convertisseur spécifié (JSON ou XML).
     * @param converterType "JSON" ou "XML"
     * @return L'instance Retrofit configurée.
     */
    fun getClient(converterType: String): Retrofit {
        // Vérifie si l'instance Retrofit existante peut être réutilisée ou si le format a changé
        if (retrofit == null || converterType != currentFormat) {
            currentFormat = converterType // Met à jour le format actuel

            // Le Builder de base
            val builder = Retrofit.Builder()
                .baseUrl(BASE_URL)

            // Ajoute le convertisseur approprié
            when (converterType) {
                "JSON" -> {
                    // Pour le JSON (Gson)
                    builder.addConverterFactory(GsonConverterFactory.create())
                }
                "XML" -> {
                    // Pour le XML (SimpleXML) - Utilisation de createNonStrict() comme spécifié
                    builder.addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
                }
                else -> {
                    // Gère un cas par défaut ou lève une exception
                    throw IllegalArgumentException("Type de convertisseur non supporté: $converterType")
                }
            }

            retrofit = builder.build() // Construit la nouvelle instance
        }

        // Retourne l'instance configurée
        return retrofit!!
    }
}