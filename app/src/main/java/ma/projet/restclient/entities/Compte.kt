package ma.projet.restclient.entities

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
// Note: Les annotations javax.xml.bind.annotation.XmlElement ne sont généralement pas utilisées
// avec SimpleXML et sont moins pratiques en Kotlin pour un data class simple,
// nous nous concentrerons sur les annotations SimpleXML et JSON/Gson implicites.

@Root(name = "item", strict = false)
data class Compte(
    @field:Element(name = "id") // 'field:' est nécessaire pour cibler les propriétés en Kotlin
    var id: Long? = null,

    @field:Element(name = "solde")
    var solde: Double,

    @field:Element(name = "type")
    var type: String,

    @field:Element(name = "dateCreation")
    var dateCreation: String
) {
    // Le data class fournit automatiquement:
    // - Les Getters/Setters (via les propriétés 'var')
    // - toString(), equals(), hashCode()

    // Constructeur par défaut nécessaire pour SimpleXML
    @Suppress("unused")
    private constructor() : this(null, 0.0, "", "")
}