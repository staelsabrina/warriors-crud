package br.com.zup.warriors.controller

import br.com.zup.warriors.dto.NovoConsoleRequest
import br.com.zup.warriors.repository.ConsoleRepository
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import javax.inject.Inject
import javax.validation.ConstraintViolationException

@MicronautTest(transactional = false)
internal class CadastroConsolesTest(
    @Inject private val repository: ConsoleRepository,
    @Inject private val cadastraController: CadastroConsolesController
) {

    /**
     * Canários de teste
     * 1 - deve cadastrar um novo console
     * 2 - nao deve cadastrar um novo console quando campos obrigatorios estiverem vazios ou nulos
     * 3 - nao deve cadastrar quando data de lancamento for uma data no futuro
     */

    @BeforeEach
    fun setUp(){
        repository.deleteAll()
    }

    @Test
    fun `deve cadastrar um novo console`(){
        //cenário
        val consoleRequest = NovoConsoleRequest(
            nome = "Super Nintendo",
            marca = "Nintendo",
            dataLancamento = LocalDate.of(1990, 11, 21)
        )

        //ação
        val respostaCadastro = cadastraController.cadastro(consoleRequest)

        //validação
        val bancoDados = repository.findAll()

        assertEquals(HttpStatus.CREATED.code, respostaCadastro.status.code)
        assertTrue(bancoDados.isNotEmpty())
        assertEquals(1, bancoDados.size)
        assertEquals("Super Nintendo", bancoDados.get(0).nome)
        assertEquals("Nintendo", bancoDados.get(0).marca)
        assertEquals("1990-11-21", bancoDados.get(0).dataLancamento.toString())
    }

    @Test
    fun `nao deve cadastrar um novo console quando campos obrigatorios estiverem vazios ou nulos`(){
        //cenário
        val consoleRequest = NovoConsoleRequest(
            nome = "",
            marca = "",
            dataLancamento = null
        )

        //ação
        val respostaComErro =  assertThrows<ConstraintViolationException> {
            cadastraController.cadastro(consoleRequest)
        }

        //validação
        val bancoDados = repository.findAll()
        val map = respostaComErro.constraintViolations.map { erro ->
            Pair(erro.propertyPath.toString().substring(17), erro.message)
        }

        assertTrue(map.contains(Pair("nome", "must not be blank")))
        assertTrue(map.contains(Pair("marca", "must not be blank")))
        assertTrue(bancoDados.isEmpty())
    }

    @Test
    fun `nao deve cadastrar quando data de lancamento for uma data no futuro`(){
        val consoleRequest = NovoConsoleRequest(
            nome = "Playstation2000",
            marca = "Sony",
            dataLancamento = LocalDate.of(
                LocalDate.now().year,
                LocalDate.now().month,
                LocalDate.now().dayOfMonth+1)
        )

        val respostaComErro = assertThrows<ConstraintViolationException> {
            cadastraController.cadastro(consoleRequest)
        }

        val map = respostaComErro.constraintViolations.map { erro ->
            Pair(erro.propertyPath.toString().substring(17), erro.message)
        }
        val bancoDados = repository.findAll()

        assertTrue(map.contains(Pair("dataLancamento", "must be a date in the past or in the present")))
        assertTrue(bancoDados.isEmpty())
    }

}