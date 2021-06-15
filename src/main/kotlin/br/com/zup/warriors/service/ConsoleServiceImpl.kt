package br.com.zup.warriors.service

import br.com.zup.warriors.dto.ConsoleRequest
import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.DadosRequest
import br.com.zup.warriors.exception.ConsoleNaoEncontradoException
import br.com.zup.warriors.repository.ConsoleRepository
import br.com.zup.warriors.utils.ConsoleUtils
import io.micronaut.validation.Validated
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Validated
@Singleton
class ConsoleServiceImpl : ConsoleService {

    @Inject
    lateinit var repository: ConsoleRepository

    override fun atualizaConsole(id: UUID, novosDados: DadosRequest): ConsoleResponse {
        val possivelConsole = repository.findById(id)
        if(possivelConsole.isEmpty){
            throw ConsoleNaoEncontradoException("Console inexistente no banco de dados")
        }
        val console = possivelConsole.get()
        val atualizado = ConsoleUtils.atualiza(console, novosDados)
        return repository.update(atualizado).toDto()
    }

    override fun cadastraConsole(request: ConsoleRequest): ConsoleResponse {
        val consoleCadastrado = repository.save(request.paraNovoConsole())
        return consoleCadastrado.toDto()
    }

    override fun consultaConsole(id: UUID): ConsoleResponse{
        val possivelConsole = repository.findById(id)
        if(possivelConsole.isEmpty){
            throw ConsoleNaoEncontradoException("Console inexistente no banco de dados")
        }
        return possivelConsole.get().toDto()
    }

    override fun listaConsoles(): List<ConsoleResponse> {
        val map = repository.findAll().map { console ->
            console.toDto() }
        return map
    }

    override fun deletaConsole(id: UUID) {
        val console = repository.findById(id).get()
        return repository.delete(console)
    }

}