package com.example.motoboyrecrutamento.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * FASE 2: Banco de Dados Room
 * 
 * Classe abstrata que representa o banco de dados local do aplicativo.
 * Responsável por fornecer acesso aos DAOs.
 */
@Database(
    entities = [
        Usuario::class,
        Restaurante::class,
        Motoboy::class,
        Vaga::class,
        Candidatura::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun usuarioDao(): UsuarioDao
    abstract fun restauranteDao(): RestauranteDao
    abstract fun motoboyDao(): MotoboyDao
    abstract fun vagaDao(): VagaDao
    abstract fun candidaturaDao(): CandidaturaDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "motoboy_recrutamento_db"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Limpa vagas órfãs ao criar o banco
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.vagaDao()?.deleteAll()
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
