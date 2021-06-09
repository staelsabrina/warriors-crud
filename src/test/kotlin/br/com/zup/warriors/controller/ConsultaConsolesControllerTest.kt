package br.com.zup.warriors.controller

import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.model.NovoConsole
import br.com.zup.warriors.repository.ConsoleRepository
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import javax.inject.Inject

@MicronautTest(transactional = false)
internal class ConsultaConsolesControllerTest(
    @Inject private val repository: ConsoleRepository,
    @Inject private val consultaController: ConsultaConsolesController
){

    /**
     * Canários de teste
     * 1 - deve encontrar um console pelo id
     * 2 - deve retornar todos os consoles cadastrados
     * 3 - deve retornar uma lista vazia quando nao houverem consoles cadastrados
     * 4 - nao deve retornar um console quando o id nao estiver cadastrado
     */

    @BeforeEach
    fun setUp(){
        repository.deleteAll()
    }

    @Test
    fun `deve encontrar um console pelo id`(){
        //cenário
        val consoleCadastrado = repository.save(
            NovoConsole(
                nome = "Playstation One",
                marca = "Sony",
                dataLancamento = LocalDate.of(1994, 12, 3)
            )
        )

        //ação
        val consultaId = consultaController.consultaId(consoleCadastrado.id!!)

        //validação
        println(consultaId.body())
        assertTrue(consultaId != null)
        assertEquals(HttpStatus.OK.code, consultaId.status.code)
        assertEquals(ConsoleResponse(id=1, nome="Playstation One", marca="Sony", dataLancamento="1994-12-03", dataCadastro="2021-06-09").toString(), consultaId.body().toString())
    }

    @Test
    fun `deve retornar todos os consoles cadastrados`(){
        //canário
        repository.save(NovoConsole(nome = "Playstation 2", marca = "Sony", dataLancamento = null))
        repository.save(NovoConsole(nome = "Sega Saturn", marca = "Sega", dataLancamento = null))
        repository.save(NovoConsole(nome = "Atari 2600", marca = "Atari VSC", dataLancamento = null))
        repository.save(NovoConsole(nome = "Dreamcast", marca = "Sega", dataLancamento = null))

        //ação
        val consultaTodos = consultaController.consultaTodos()
        val listaConsolesRetornados = consultaTodos.body.get().map {
            Pair(it.nome, it.marca)
        }

        //validação
        val bancoDados = repository.findAll()

        assertEquals(bancoDados.size, consultaTodos.body().size)
        assertTrue(listaConsolesRetornados.contains(Pair("Playstation 2", "Sony")))
        assertTrue(listaConsolesRetornados.contains(Pair("Sega Saturn", "Sega")))
        assertTrue(listaConsolesRetornados.contains(Pair("Atari 2600", "Atari VSC")))
        assertTrue(listaConsolesRetornados.contains(Pair("Dreamcast", "Sega")))
    }

    @Test
    fun `deve retornar uma lista vazia quando nao houverem consoles cadastrados`(){
        //cenário

        //ação
        val listaVazia = consultaController.consultaTodos().body()

        //validações
        assertTrue(listaVazia.isEmpty())
    }

    @Test
    fun `nao deve retornar um console quando o id nao estiver cadastrado`(){
        //cenário
        val idInexistente = 1L

        //ação
        val vazio = consultaController.consultaId(idInexistente)

        //validação
        with(vazio){
            assertEquals(HttpStatus.NOT_FOUND.code, status.code)
            assertEquals("Console inexistente no banco de dados", body())
        }
    }

}