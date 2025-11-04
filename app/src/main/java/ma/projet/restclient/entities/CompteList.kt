package ma.projet.restclient.entities

import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "List", strict = false)
data class CompteList(
    // 'field:' est nécessaire pour cibler la propriété en Kotlin
    // entry="item" correspond au nom de la classe Compte annotée avec @Root(name="item")
    @field:ElementList(inline = true, entry = "item")
    var comptes: List<Compte>? = null
) {
    // Constructeur par défaut nécessaire pour SimpleXML
    @Suppress("unused")
    private constructor() : this(null)
}