package ma.projet.restclient.repository

import ma.projet.restclient.api.CompteService
import ma.projet.restclient.config.RetrofitClient
import ma.projet.restclient.entities.Compte
import ma.projet.restclient.entities.CompteList
import java.io.IOException

// Le Repository gère la logique de sélection du format (JSON/XML) et appelle l'API.
class CompteRepository(private val converterType: String) {

    private val compteService: CompteService =
        RetrofitClient.getClient(converterType).create(CompteService::class.java)

    // Les fonctions du Repository sont marquées 'suspend' pour être appelées dans une Coroutine.

    /**
     * Récupère tous les comptes et gère la conversion XML -> List<Compte>.
     * @return List<Compte> ou null en cas d'erreur.
     */
    suspend fun getAllComptes(): List<Compte>? {
        return try {
            if (converterType == "JSON") {
                // Appel JSON : retourne directement List<Compte>
                compteService.getAllCompteJson()
            } else {
                // Appel XML : retourne CompteList, nécessite une conversion
                val compteList: CompteList = compteService.getAllCompteXml()
                compteList.comptes
            }
        } catch (e: Exception) {
            // Loggez l'erreur pour le débogage
            // Log.e("CompteRepository", "Erreur lors de la récupération des comptes", e)
            null // Retourne null en cas d'échec
        }
    }

    /**
     * Récupère un compte par son ID.
     */
    suspend fun getCompteById(id: Long): Compte? {
        return try {
            compteService.getCompteById(id)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Ajoute un nouveau compte.
     */
    suspend fun addCompte(compte: Compte): Compte? {
        return try {
            compteService.addCompte(compte)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Met à jour un compte existant.
     */
    suspend fun updateCompte(id: Long, compte: Compte): Compte? {
        return try {
            compteService.updateCompte(id, compte)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Supprime un compte.
     * @return true si la suppression réussit (pas d'exception), false sinon.
     */
    suspend fun deleteCompte(id: Long): Boolean {
        return try {
            compteService.deleteCompte(id)
            true // La suppression a réussi
        } catch (e: Exception) {
            false // La suppression a échoué (erreur réseau/serveur)
        }
    }
}