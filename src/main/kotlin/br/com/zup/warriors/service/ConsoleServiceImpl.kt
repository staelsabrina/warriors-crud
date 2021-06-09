package br.com.zup.warriors.service

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.NovoConsoleRequest
import br.com.zup.warriors.dto.NovosDadosRequest
import br.com.zup.warriors.model.NovoConsole
import br.com.zup.warriors.repository.ConsoleRepository
import br.com.zup.warriors.utils.ConsoleNaoEncontradoException
import io.micronaut.http.HttpResponse
import io.micronaut.validation.Validated
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Transactional
@Validated
@Singleton
class ConsoleServiceImpl(
    @Inject private val repository: ConsoleRepository
): ConsoleService {

    override fun atualiza(id: Long, novosDados: NovosDadosRequest): ConsoleResponse {
        val possivelConsole = repository.findById(id)
        if(possivelConsole.isEmpty){
            throw ConsoleNaoEncontradoException("Console inexistente no banco de dados")
        }
        val console = possivelConsole.get()
        console.atualiza(novosDados)
        return repository.update(console).toDto()
    }

    override fun cadastra(@Valid request: NovoConsoleRequest): ConsoleResponse {
        val consoleCadastrado = repository.save(request.paraNovoConsole())
        return consoleCadastrado.toDto()
    }

    private fun location(idConsole: Long) = HttpResponse
        .uri(("/api/consoles/${idConsole}"))

    override fun consultaId(id: Long): ConsoleResponse{
        val possivelConsole = repository.findById(id)
        if(possivelConsole.isEmpty){
            throw ConsoleNaoEncontradoException("Console inexistente no banco de dados")
        }
        return possivelConsole.get().toDto()
    }

    override fun consultaTodos(): List<ConsoleResponse> {
        val map = repository.findAll().map { console ->
            console.toDto() }
        return map
    }

    override fun deleta(id: Long) {
        val possivelConsole = repository.findById(id)
        if(possivelConsole.isEmpty){
            throw ConsoleNaoEncontradoException("Console inexistente no banco de dados")
        }
        val console = possivelConsole.get()

        return repository.delete(console)
    }

}