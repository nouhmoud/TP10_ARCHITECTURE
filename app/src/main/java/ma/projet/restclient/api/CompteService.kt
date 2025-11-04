package ma.projet.restclient.api

import ma.projet.restclient.entities.Compte
import ma.projet.restclient.entities.CompteList
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.Response // Utilisé pour gérer les réponses HTTP manuellement (si nécessaire)

// L'interface définit toutes les requêtes que l'application peut envoyer au serveur
interface CompteService {

    // --- Opérations de lecture (READ) ---

    // 1. Récupérer tous les comptes en JSON (Retourne une List de Compte)
    @GET("banque/comptes")
    @Headers("Accept: application/json")
    suspend fun getAllCompteJson(): List<Compte> // Utilisation de 'suspend' et d'une List directe

    // 2. Récupérer tous les comptes en XML (Retourne CompteList pour la désérialisation XML)
    @GET("banque/comptes")
    @Headers("Accept: application/xml")
    suspend fun getAllCompteXml(): CompteList // Utilisation de 'suspend' et de CompteList

    // 3. Récupérer un compte par son ID
    @GET("banque/comptes/{id}")
    suspend fun getCompteById(@Path("id") id: Long): Compte

    // --- Opérations de Création, Mise à Jour, Suppression (CRUD) ---

    // 4. Ajouter un nouveau compte (CREATE)
    @POST("banque/comptes")
    suspend fun addCompte(@Body compte: Compte): Compte // Retourne le Compte créé (avec son ID)

    // 5. Mettre à jour un compte existant (UPDATE)
    @PUT("banque/comptes/{id}")
    suspend fun updateCompte(@Path("id") id: Long, @Body compte: Compte): Compte

    // 6. Supprimer un compte (DELETE)
    @DELETE("banque/comptes/{id}")
    suspend fun deleteCompte(@Path("id") id: Long) // La suppression retourne souvent une réponse vide (pas besoin de Call<Void>)
}