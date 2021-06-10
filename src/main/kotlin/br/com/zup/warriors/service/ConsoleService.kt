package br.com.zup.warriors.service

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.ConsoleRequest
import br.com.zup.warriors.dto.NovosDadosRequest
import javax.validation.Valid

interface ConsoleService {
    fun atualizaConsole(id: Long, novosDados: NovosDadosRequest): ConsoleResponse
    fun cadastraConsole(request: ConsoleRequest): ConsoleResponse
    fun consultaConsole(id: Long): ConsoleResponse
    fun listaConsoles(): List<ConsoleResponse>
    fun deletaConsole(id: Long)
}