package ma.projet.restclient


import android.os.Bundle

import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ma.projet.restclient.adapter.CompteAdapter
import ma.projet.restclient.databinding.ActivityMainBinding
import ma.projet.restclient.entities.Compte
import ma.projet.restclient.repository.CompteRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Utilisation du View Binding pour accéder aux vues de manière sécurisée
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CompteAdapter
    private var currentFormat: String = "JSON" // Démarre en JSON

    // Repository sera initialisé au besoin (ou comme propriété si on utilise une architecture MVVM)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialisation du View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFormatSelection()
        setupAddButton()

        // Chargement initial des données
        loadData(currentFormat)
    }

    private fun setupRecyclerView() {
        // Définition des callbacks (Supprimer et Modifier) pour l'adapter
        adapter = CompteAdapter(
            onDeleteClickListener = { compte -> showDeleteConfirmationDialog(compte) },
            onUpdateClickListener = { compte -> showUpdateCompteDialog(compte) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupFormatSelection() {
        binding.formatGroup.setOnCheckedChangeListener { _, checkedId ->
            currentFormat = if (checkedId == R.id.radioJson) "JSON" else "XML"
            loadData(currentFormat)
        }
    }

    private fun setupAddButton() {
        binding.fabAdd.setOnClickListener {
            showAddCompteDialog()
        }
    }

    // --- 1. FONCTIONS DE LECTURE (READ) ---

    private fun loadData(format: String) {
        val compteRepository = CompteRepository(format)

        // Utilisation de Coroutine pour l'appel asynchrone
        lifecycleScope.launch {
            val comptes = compteRepository.getAllComptes()
            if (comptes != null) {
                adapter.updateData(comptes)
            } else {
                showToast("Erreur lors du chargement des données en $format.")
            }
        }
    }

    // --- 2. FONCTIONS D'AJOUT (CREATE) ---

    private fun showAddCompteDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_compte, null)
        val etSolde = dialogView.findViewById<EditText>(R.id.etSolde)
        val typeGroup = dialogView.findViewById<RadioGroup>(R.id.typeGroup)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Ajouter un compte")
            .setPositiveButton("Ajouter") { _, _ ->
                val solde = etSolde.text.toString().toDoubleOrNull()
                if (solde == null) {
                    showToast("Solde invalide")
                    return@setPositiveButton
                }
                val type = if (typeGroup.checkedRadioButtonId == R.id.radioCourant) "COURANT" else "EPARGNE"

                val formattedDate = getCurrentDateFormatted()
                val nouveauCompte = Compte(id = null, solde = solde, type = type, dateCreation = formattedDate)
                addCompte(nouveauCompte)
            }
            .setNegativeButton("Annuler", null)
            .create()
            .show()
    }

    private fun addCompte(compte: Compte) {
        val compteRepository = CompteRepository(currentFormat)
        lifecycleScope.launch {
            val result = compteRepository.addCompte(compte)
            if (result != null) {
                showToast("Compte ajouté (ID: ${result.id})")
                loadData(currentFormat) // Recharger les données pour actualiser l'ID
            } else {
                showToast("Erreur lors de l'ajout")
            }
        }
    }

    // --- 3. FONCTIONS DE MODIFICATION (UPDATE) ---

    private fun showUpdateCompteDialog(compte: Compte) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_compte, null)
        val etSolde = dialogView.findViewById<EditText>(R.id.etSolde)
        val typeGroup = dialogView.findViewById<RadioGroup>(R.id.typeGroup)

        // Pré-remplir les données
        etSolde.setText(compte.solde.toString())
        if (compte.type.equals("COURANT", ignoreCase = true)) {
            typeGroup.check(R.id.radioCourant)
        } else {
            typeGroup.check(R.id.radioEpargne)
        }

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Modifier un compte (ID: ${compte.id})")
            .setPositiveButton("Modifier") { _, _ ->
                val solde = etSolde.text.toString().toDoubleOrNull()
                if (solde == null) {
                    showToast("Solde invalide")
                    return@setPositiveButton
                }

                val type = if (typeGroup.checkedRadioButtonId == R.id.radioCourant) "COURANT" else "EPARGNE"
                // Appliquer les modifications à l'objet Compte
                val compteModifie = compte.copy(solde = solde, type = type)

                updateCompte(compteModifie)
            }
            .setNegativeButton("Annuler", null)
            .create()
            .show()
    }

    private fun updateCompte(compte: Compte) {
        val compteRepository = CompteRepository(currentFormat)
        lifecycleScope.launch {
            val result = compteRepository.updateCompte(compte.id!!, compte)
            if (result != null) {
                showToast("Compte modifié (ID: ${compte.id})")
                loadData(currentFormat)
            } else {
                showToast("Erreur lors de la modification")
            }
        }
    }

    // --- 4. FONCTIONS DE SUPPRESSION (DELETE) ---

    private fun showDeleteConfirmationDialog(compte: Compte) {
        AlertDialog.Builder(this)
            .setTitle("Confirmation de Suppression")
            .setMessage("Voulez-vous vraiment supprimer le compte ID ${compte.id} ?")
            .setPositiveButton("Oui") { _, _ ->
                deleteCompte(compte.id!!)
            }
            .setNegativeButton("Non", null)
            .show()
    }

    private fun deleteCompte(id: Long) {
        val compteRepository = CompteRepository(currentFormat)
        lifecycleScope.launch {
            val success = compteRepository.deleteCompte(id)
            if (success) {
                showToast("Compte supprimé (ID: $id)")
                loadData(currentFormat)
            } else {
                showToast("Erreur lors de la suppression du compte ID $id")
            }
        }
    }

    // --- 5. FONCTIONS UTILITAIRES ---

    private fun getCurrentDateFormatted(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}