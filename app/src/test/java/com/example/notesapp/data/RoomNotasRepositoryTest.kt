package com.example.notesapp.data

import com.example.notesapp.model.Nota
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException

class RoomNotasRepositoryTest {
    lateinit var instance: RoomNotasRepository

    @MockK
    lateinit var notaDao: NotaDAO

    @Before
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    @ExperimentalCoroutinesApi
    fun `se prueba RoomNotasRepository`() {
        runBlockingTest{
            instance = RoomNotasRepository(notaDao)
            coEvery { notaDao.getAll() } returns listOf(
                NotaEntity(1,"descripción 1","./foto1"),
                NotaEntity(2,"descripción 2","./foto2")
            )
            val result = instance.getAll()
            assertThat(result.size).isEqualTo(2)
            assertThat(result[0].id).isEqualTo(1)
            assertThat(result[1].id).isEqualTo(2)
            assertThat(result[0].description).isEqualTo("descripción 1")
            assertThat(result[1].description).isEqualTo("descripción 2")
            assertThat(result[0].srcImagen).isEqualTo("./foto1")
            assertThat(result[1].srcImagen).isEqualTo("./foto2")
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `el dao devuelve error al insertar`(){
        instance = RoomNotasRepository((notaDao))
        coEvery{ notaDao.insertarNota(any()) } throws RuntimeException("test_exception")
        assertThatThrownBy {
            runBlockingTest {
                instance.insertarNota(Nota(1,"test","./testFoto"))
            }
        }.isInstanceOf(RuntimeException::class.java)
            .hasMessage(("test_exception"))
    }
}
