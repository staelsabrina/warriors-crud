package br.com.zup.warriors.controller

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.NovoConsoleRequest
import br.com.zup.warriors.service.ConsoleService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/consoles/cadastro")
class CadastroConsolesController(@Inject private val service: ConsoleService) {

    @Post
    fun cadastro(@Valid @Body request: NovoConsoleRequest): HttpResponse<ConsoleResponse> {
        return HttpResponse.created(HttpStatus.CREATED).body(service.cadastra(request))
    }

}