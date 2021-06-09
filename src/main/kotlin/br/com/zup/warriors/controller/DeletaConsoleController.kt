package br.com.zup.warriors.controller

import br.com.zup.warriors.service.ConsoleService
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import javax.inject.Inject

@Controller("/api/consoles")
class DeletaConsoleController(@Inject private val service: ConsoleService) {

    @Delete("/{id}")
    fun deleta(@PathVariable id: Long): HttpResponse<Any> {
        val response = try {
            service.deleta(id)
        } catch (e: RuntimeException){
            if(e.message == "Console inexistente no banco de dados"){
                return HttpResponse.notFound(HttpStatus.NOT_FOUND).body(e.message)
            } else
                return HttpResponse.serverError(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
        return HttpResponse.ok(HttpStatus.OK).body(response)
    }
}