package com.example.motoboyrecrutamento.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * FASE 2: DAOs (Data Access Objects) do Room
 *
 * Interfaces que definem as operações de acesso ao banco de dados local.
 * O Room gera automaticamente a implementação dessas interfaces.
 */

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE firebaseUid = :firebaseUid LIMIT 1")
    suspend fun getByFirebaseUid(firebaseUid: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE id = :usuarioId LIMIT 1")
    suspend fun getUsuarioByIdSync(usuarioId: Long): Usuario?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario): Long

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)
}

/**
 * MEMBRO 2: DAO para Restaurantes
 */
@Dao
interface RestauranteDao {
    @Query("SELECT * FROM restaurantes WHERE usuarioId = :usuarioId LIMIT 1")
    suspend fun getByUsuarioId(usuarioId: Long): Restaurante?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurante: Restaurante): Long

    @Update
    suspend fun update(restaurante: Restaurante)
}

/**
 * MEMBRO 3: DAO para Motoboys
 */
@Dao
interface MotoboyDao {
    @Query("SELECT * FROM motoboys WHERE usuarioId = :usuarioId LIMIT 1")
    suspend fun getByUsuarioId(usuarioId: Long): Motoboy?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(motoboy: Motoboy): Long

    @Update
    suspend fun update(motoboy: Motoboy)

    @Query("SELECT * FROM motoboys WHERE id = :motoboyId LIMIT 1")
    suspend fun getMotoboyByIdSync(motoboyId: Long): Motoboy?
}

/**
 * MEMBRO 2: DAO para Vagas
 */
@Dao
interface VagaDao {
    @Query("SELECT * FROM vagas WHERE restauranteId = :restauranteId ORDER BY dataPublicacao DESC")
    fun getVagasByRestaurante(restauranteId: String): Flow<List<Vaga>>

    @Query("SELECT * FROM vagas WHERE status = 'aberta' ORDER BY dataPublicacao DESC")
    fun getVagasAbertas(): Flow<List<Vaga>>

    @Query("SELECT * FROM vagas WHERE id = :vagaId LIMIT 1")
    suspend fun getVagaByIdSync(vagaId: Long): Vaga?

    @Query("SELECT * FROM vagas")
    suspend fun getAllVagasSync(): List<Vaga>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vaga: Vaga): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vagas: List<Vaga>)

    @Update
    suspend fun update(vaga: Vaga)

    @Delete
    suspend fun delete(vaga: Vaga)

    @Query("DELETE FROM vagas")
    suspend fun deleteAll()

    @Query("DELETE FROM vagas WHERE restauranteId NOT IN (SELECT id FROM restaurantes)")
    suspend fun deleteOrphanVagas()

    @Query("DELETE FROM vagas WHERE restauranteId = 0 OR restauranteId IS NULL")
    suspend fun deleteVagasWithoutRestaurant()

}

/**
 * MEMBRO 3: DAO para Candidaturas
 */
// app/src/main/java/com/example/motoboyrecrutamento/data/local/Daos.kt

// ... (código omitido)

/**
 * MEMBRO 3: DAO para Candidaturas
 */
@Dao
interface CandidaturaDao {
    @Query("SELECT * FROM candidaturas WHERE motoboyId = :motoboyId ORDER BY dataCandidatura DESC")
    fun getCandidaturasByMotoboy(motoboyId: Long): Flow<List<Candidatura>>

    @Query("SELECT * FROM candidaturas WHERE vagaId = :vagaId ORDER BY dataCandidatura DESC")
    fun getCandidaturasByVaga(vagaId: Long): Flow<List<Candidatura>>

    @Query("SELECT * FROM candidaturas WHERE vagaId = :vagaId AND motoboyId = :motoboyId LIMIT 1")
    suspend fun getCandidaturaByVagaAndMotoboy(vagaId: Long, motoboyId: Long): Candidatura?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(candidatura: Candidatura): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(candidaturas: List<Candidatura>)

    @Update
    suspend fun update(candidatura: Candidatura)

    @Query("DELETE FROM candidaturas")
    suspend fun deleteAll()
}
