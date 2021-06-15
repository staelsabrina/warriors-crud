package br.com.zup.warriors.dto

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class ConsoleResponse(
    val id: UUID? = null,
    val nome: String = "",
    val marca: String = "",
    val dataLancamento: String?,
    val dataCadastro: String
) {

    override fun toString(): String {
        return "ConsoleResponse(id=$id, nome='$nome', marca='$marca', dataLancamento=$dataLancamento, dataCadastro='$dataCadastro')"
    }
}
