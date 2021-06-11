package br.com.zup.warriors.controller

import br.com.zup.warriors.dto.ConsoleRequest
import br.com.zup.warriors.dto.ConsoleResponse
import br.com.zup.warriors.dto.DadosRequest
import br.com.zup.warriors.model.Console
import br.com.zup.warriors.repository.ConsoleRepository
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import javax.inject.Inject
import javax.validation.ConstraintViolationException

@MicronautTest
internal class ConsolesControllerTest {

    @Inject
    lateinit var repository: ConsoleRepository
    @Inject
    lateinit var consoleController: ConsolesController

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    @Nested
    inner class Atualizacao {
        @Test
        fun `deve atualizar os dados de um console com sucesso`(){
            //cenário
            val idCadastrado = repository.save(
                Console(
                    nome = "Mega Drive",
                    marca = "nintendo",
                    dataLancamento = LocalDate.of(1998, 10, 29)
                )
            ).id

            val novosDados = DadosRequest(
                nome = "Sega Genesis",
                "Sega",
                dataLancamento = LocalDate.of(1988, 10, 29)
            )

            //ação
            val atualizacao = consoleController.atualizaConsole(idCadastrado!!, novosDados)

            //validação
            val bancoDados = repository.findAll()
            assertEquals(1, bancoDados.size)
            assertEquals(novosDados.nome, bancoDados.get(0).nome)
            assertEquals(novosDados.marca, bancoDados.get(0).marca)
            assertEquals(novosDados.dataLancamento, bancoDados.get(0).dataLancamento)

        }

        @Test
        fun `nao deve alterar dados do console quando nao forem passados dados novos`(){
            //cenário
            val idCadastrado = repository.save(
                Console(
                    nome = "Mega Drive",
                    marca = "Sega",
                    dataLancamento = LocalDate.of(1988, 10, 29)
                )
            ).id

            val novosDados = DadosRequest(
                nome = "",
                "",
                dataLancamento = null
            )

            //ação
            consoleController.atualizaConsole(idCadastrado!!, novosDados)

            //validação
            val bancoDados = repository.findAll()
            assertEquals(1, bancoDados.size)
            assertEquals("Mega Drive", bancoDados.get(0).nome)
            assertEquals("Sega", bancoDados.get(0).marca)
            assertEquals("1988-10-29", bancoDados.get(0).dataLancamento.toString())
        }

        @Test
        fun `deve retornar not found quando id passado nao estiver cadastrado no banco de dados`(){
            //cenário
            val novosDados = DadosRequest(
                nome = "Mega Drive",
                marca = "Sega",
                dataLancamento = LocalDate.of(1988, 10, 29)
            )

            //ação
            val erro = consoleController.atualizaConsole(1, novosDados)

            //validação
            val bancoDados = repository.findAll()

            assertTrue(bancoDados.isEmpty())
            assertEquals(HttpStatus.NOT_FOUND.code, erro.status.code)
        }
    }

    @Nested
    inner class Cadastro {
        @Test
        fun `deve cadastrar um novo console`(){
            //cenário
            val consoleRequest = ConsoleRequest(
                nome = "Super Nintendo",
                marca = "Nintendo",
                dataLancamento = LocalDate.of(1990, 11, 21)
            )

            //ação
            val respostaCadastro = consoleController.cadastraConsole(consoleRequest)

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
            val consoleRequest = ConsoleRequest(
                nome = "",
                marca = "",
                dataLancamento = null
            )

            //ação
            val respostaComErro = org.junit.jupiter.api.assertThrows<ConstraintViolationException> {
                consoleController.cadastraConsole(consoleRequest)
            }

            //validação
            val bancoDados = repository.findAll()
            val map = respostaComErro.constraintViolations.map { erro ->
                Pair(erro.propertyPath.toString().substring(24), erro.message)
            }

            assertTrue(map.contains(Pair("nome", "must not be blank")))
            assertTrue(map.contains(Pair("marca", "must not be blank")))
            assertTrue(bancoDados.isEmpty())
        }

        @Test
        fun `nao deve cadastrar quando data de lancamento for uma data no futuro`(){
            val consoleRequest = ConsoleRequest(
                nome = "Playstation2000",
                marca = "Sony",
                dataLancamento = LocalDate.of(
                    LocalDate.now().year,
                    LocalDate.now().month,
                    LocalDate.now().dayOfMonth+1)
            )

            val respostaComErro = org.junit.jupiter.api.assertThrows<ConstraintViolationException> {
                consoleController.cadastraConsole(consoleRequest)
            }

            val map = respostaComErro.constraintViolations.map { erro ->
                Pair(erro.propertyPath.toString().substring(24), erro.message)
            }
            val bancoDados = repository.findAll()

            assertTrue(map.contains(Pair("dataLancamento", "must be a date in the past or in the present")))
            assertTrue(bancoDados.isEmpty())
        }
    }

    @Nested
    inner class Consulta {
        @Test
        fun `deve encontrar um console pelo id`(){
            //cenário
            val consoleCadastrado = repository.save(
                Console(
                    nome = "Playstation One",
                    marca = "Sony",
                    dataLancamento = LocalDate.of(1994, 12, 3)
                )
            )

            //ação
            val consultaId = consoleController.consultaConsole(consoleCadastrado.id!!)

            //validação
            println(consultaId.body())
            assertTrue(consultaId != null)
            assertEquals(HttpStatus.OK.code, consultaId.status.code)
            assertEquals(ConsoleResponse(consoleCadastrado.id, nome="Playstation One", marca="Sony", dataLancamento="1994-12-03", dataCadastro= LocalDate.now().toString()).toString(), consultaId.body().toString())
        }

        @Test
        fun `deve retornar todos os consoles cadastrados`(){
            //canário
            repository.save(Console(nome = "Playstation 2", marca = "Sony", dataLancamento = null))
            repository.save(Console(nome = "Sega Saturn", marca = "Sega", dataLancamento = null))
            repository.save(Console(nome = "Atari 2600", marca = "Atari VSC", dataLancamento = null))
            repository.save(Console(nome = "Dreamcast", marca = "Sega", dataLancamento = null))

            //ação
            val consultaTodos = consoleController.listaConsoles()
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
            val listaVazia = consoleController.listaConsoles().body()

            //validações
            assertTrue(listaVazia.isEmpty())
        }

        @Test
        fun `nao deve retornar um console quando o id nao estiver cadastrado`(){
            //cenário
            val idInexistente = 1L

            //ação
            val vazio = consoleController.consultaConsole(idInexistente)

            //validação
            with(vazio){
                assertEquals(HttpStatus.NOT_FOUND.code, status.code)
                assertEquals("Console inexistente no banco de dados", body())
            }
        }
    }

    @Nested
    inner class Remocao {
        @Test
        fun `deve deletar um console pelo id indicado`(){
            //cenário
            val idCadastrado = repository.save(Console(
                nome = "GameCube",
                marca = "Nintendo",
                dataLancamento = null
            )).id

            //validação1
            val bancoDadosAntesDeletar = repository.findAll()
            assertTrue(bancoDadosAntesDeletar.isNotEmpty())

            //ação
            consoleController.deletaConsole(idCadastrado!!)

            //validação2
            val bancoDadosDepoisDeletar = repository.findAll()
            assertTrue(bancoDadosDepoisDeletar.isEmpty())

        }
    }

}