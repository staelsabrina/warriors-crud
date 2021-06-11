package br.com.zup.warriors.utils

import br.com.zup.warriors.dto.DadosRequest
import br.com.zup.warriors.model.Console

class ConsoleUtils {

    companion object {

        fun atualiza(console: Console, novosDados: DadosRequest): Console {
            if (novosDados.nome != null && novosDados.nome != "") {
                console.nome = novosDados.nome
            }
            if (novosDados.marca != null && novosDados.marca != "") {
                console.marca = novosDados.marca
            }
            if (novosDados.dataLancamento != null) {
                console.dataLancamento = novosDados.dataLancamento
            }
            return console
        }

    }

}