package ma.projet.restclient.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ma.projet.restclient.R
import ma.projet.restclient.entities.Compte

// Utilisation des fonctions de haut niveau (lambdas) pour les listeners en Kotlin
class CompteAdapter(
    private val onDeleteClickListener: (Compte) -> Unit, // (Compte) -> Unit remplace l'interface OnDeleteClickListener
    private val onUpdateClickListener: (Compte) -> Unit  // (Compte) -> Unit remplace l'interface OnUpdateClickListener
) : RecyclerView.Adapter<CompteAdapter.CompteViewHolder>() {

    private var comptes: MutableList<Compte> = mutableListOf()

    // --- 1. ViewHolder : Contient les références des vues ---
    inner class CompteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Déclaration des vues de item_compte.xml
        private val tvId: TextView = itemView.findViewById(R.id.tvId)
        private val tvSolde: TextView = itemView.findViewById(R.id.tvSolde)
        private val tvType: TextView = itemView.findViewById(R.id.tvType)
        // Note: L'attribut tvDate n'existe pas dans item_compte.xml, je l'ai omis.
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        // Note: Le bouton d'édition (btnEdit) n'existe pas dans item_compte.xml, j'utilise un clic sur l'élément entier.

        fun bind(compte: Compte) {
            // Liaison des données
            tvId.text = "ID: ${compte.id ?: "N/A"}"
            tvSolde.text = "Solde: %.2f €".format(compte.solde)
            tvType.text = "Type: ${compte.type}"

            // Gestion du clic sur le bouton Supprimer
            btnDelete.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteClickListener(comptes[adapterPosition])
                }
            }

            // Utilisation du clic sur l'élément entier pour la Modification/Mise à jour
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onUpdateClickListener(comptes[adapterPosition])
                }
            }
        }
    }

    // --- 2. Création du ViewHolder ---
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compte, parent, false)
        return CompteViewHolder(view)
    }

    // --- 3. Liaison des données ---
    override fun onBindViewHolder(holder: CompteViewHolder, position: Int) {
        holder.bind(comptes[position])
    }

    // --- 4. Nombre total d'éléments ---
    override fun getItemCount(): Int = comptes.size

    /**
     * Met à jour la liste des comptes et notifie le RecyclerView.
     */
    fun updateData(newComptes: List<Compte>) {
        this.comptes.clear()
        this.comptes.addAll(newComptes)
        notifyDataSetChanged()
    }
}