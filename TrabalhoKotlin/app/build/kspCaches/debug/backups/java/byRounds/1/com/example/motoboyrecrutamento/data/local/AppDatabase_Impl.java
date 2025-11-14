package com.example.motoboyrecrutamento.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UsuarioDao _usuarioDao;

  private volatile RestauranteDao _restauranteDao;

  private volatile MotoboyDao _motoboyDao;

  private volatile VagaDao _vagaDao;

  private volatile CandidaturaDao _candidaturaDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `usuarios` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `firebaseUid` TEXT NOT NULL, `nome` TEXT NOT NULL, `email` TEXT NOT NULL, `tipo` TEXT NOT NULL, `dataCriacao` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `restaurantes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `usuarioId` INTEGER NOT NULL, `cnpj` TEXT NOT NULL, `endereco` TEXT NOT NULL, `telefone` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `motoboys` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `usuarioId` INTEGER NOT NULL, `cnh` TEXT NOT NULL, `veiculo` TEXT NOT NULL, `experienciaAnos` INTEGER NOT NULL, `raioAtuacao` REAL NOT NULL, `telefone` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `vagas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `firestoreId` TEXT, `titulo` TEXT NOT NULL, `descricao` TEXT NOT NULL, `salario` REAL NOT NULL, `status` TEXT NOT NULL, `requisitos` TEXT NOT NULL, `dataPublicacao` TEXT NOT NULL, `restauranteId` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `candidaturas` (`id` INTEGER NOT NULL, `vagaId` INTEGER NOT NULL, `motoboyId` INTEGER NOT NULL, `dataCandidatura` TEXT NOT NULL, `status` TEXT NOT NULL, `firestoreId` TEXT, `motoboyNome` TEXT, `motoboyEmail` TEXT, `motoboyTelefone` TEXT, PRIMARY KEY(`vagaId`, `motoboyId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3d05cd79fed5d9ca498c0da07d5cad72')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `usuarios`");
        db.execSQL("DROP TABLE IF EXISTS `restaurantes`");
        db.execSQL("DROP TABLE IF EXISTS `motoboys`");
        db.execSQL("DROP TABLE IF EXISTS `vagas`");
        db.execSQL("DROP TABLE IF EXISTS `candidaturas`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsUsuarios = new HashMap<String, TableInfo.Column>(6);
        _columnsUsuarios.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("firebaseUid", new TableInfo.Column("firebaseUid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("nome", new TableInfo.Column("nome", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("tipo", new TableInfo.Column("tipo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsuarios.put("dataCriacao", new TableInfo.Column("dataCriacao", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsuarios = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsuarios = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsuarios = new TableInfo("usuarios", _columnsUsuarios, _foreignKeysUsuarios, _indicesUsuarios);
        final TableInfo _existingUsuarios = TableInfo.read(db, "usuarios");
        if (!_infoUsuarios.equals(_existingUsuarios)) {
          return new RoomOpenHelper.ValidationResult(false, "usuarios(com.example.motoboyrecrutamento.data.local.Usuario).\n"
                  + " Expected:\n" + _infoUsuarios + "\n"
                  + " Found:\n" + _existingUsuarios);
        }
        final HashMap<String, TableInfo.Column> _columnsRestaurantes = new HashMap<String, TableInfo.Column>(5);
        _columnsRestaurantes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurantes.put("usuarioId", new TableInfo.Column("usuarioId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurantes.put("cnpj", new TableInfo.Column("cnpj", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurantes.put("endereco", new TableInfo.Column("endereco", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRestaurantes.put("telefone", new TableInfo.Column("telefone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRestaurantes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRestaurantes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRestaurantes = new TableInfo("restaurantes", _columnsRestaurantes, _foreignKeysRestaurantes, _indicesRestaurantes);
        final TableInfo _existingRestaurantes = TableInfo.read(db, "restaurantes");
        if (!_infoRestaurantes.equals(_existingRestaurantes)) {
          return new RoomOpenHelper.ValidationResult(false, "restaurantes(com.example.motoboyrecrutamento.data.local.Restaurante).\n"
                  + " Expected:\n" + _infoRestaurantes + "\n"
                  + " Found:\n" + _existingRestaurantes);
        }
        final HashMap<String, TableInfo.Column> _columnsMotoboys = new HashMap<String, TableInfo.Column>(7);
        _columnsMotoboys.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMotoboys.put("usuarioId", new TableInfo.Column("usuarioId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMotoboys.put("cnh", new TableInfo.Column("cnh", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMotoboys.put("veiculo", new TableInfo.Column("veiculo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMotoboys.put("experienciaAnos", new TableInfo.Column("experienciaAnos", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMotoboys.put("raioAtuacao", new TableInfo.Column("raioAtuacao", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMotoboys.put("telefone", new TableInfo.Column("telefone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMotoboys = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMotoboys = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMotoboys = new TableInfo("motoboys", _columnsMotoboys, _foreignKeysMotoboys, _indicesMotoboys);
        final TableInfo _existingMotoboys = TableInfo.read(db, "motoboys");
        if (!_infoMotoboys.equals(_existingMotoboys)) {
          return new RoomOpenHelper.ValidationResult(false, "motoboys(com.example.motoboyrecrutamento.data.local.Motoboy).\n"
                  + " Expected:\n" + _infoMotoboys + "\n"
                  + " Found:\n" + _existingMotoboys);
        }
        final HashMap<String, TableInfo.Column> _columnsVagas = new HashMap<String, TableInfo.Column>(9);
        _columnsVagas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVagas.put("firestoreId", new TableInfo.Column("firestoreId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVagas.put("titulo", new TableInfo.Column("titulo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVagas.put("descricao", new TableInfo.Column("descricao", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVagas.put("salario", new TableInfo.Column("salario", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVagas.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVagas.put("requisitos", new TableInfo.Column("requisitos", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVagas.put("dataPublicacao", new TableInfo.Column("dataPublicacao", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVagas.put("restauranteId", new TableInfo.Column("restauranteId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVagas = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVagas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVagas = new TableInfo("vagas", _columnsVagas, _foreignKeysVagas, _indicesVagas);
        final TableInfo _existingVagas = TableInfo.read(db, "vagas");
        if (!_infoVagas.equals(_existingVagas)) {
          return new RoomOpenHelper.ValidationResult(false, "vagas(com.example.motoboyrecrutamento.data.local.Vaga).\n"
                  + " Expected:\n" + _infoVagas + "\n"
                  + " Found:\n" + _existingVagas);
        }
        final HashMap<String, TableInfo.Column> _columnsCandidaturas = new HashMap<String, TableInfo.Column>(9);
        _columnsCandidaturas.put("id", new TableInfo.Column("id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCandidaturas.put("vagaId", new TableInfo.Column("vagaId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCandidaturas.put("motoboyId", new TableInfo.Column("motoboyId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCandidaturas.put("dataCandidatura", new TableInfo.Column("dataCandidatura", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCandidaturas.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCandidaturas.put("firestoreId", new TableInfo.Column("firestoreId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCandidaturas.put("motoboyNome", new TableInfo.Column("motoboyNome", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCandidaturas.put("motoboyEmail", new TableInfo.Column("motoboyEmail", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCandidaturas.put("motoboyTelefone", new TableInfo.Column("motoboyTelefone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCandidaturas = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCandidaturas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCandidaturas = new TableInfo("candidaturas", _columnsCandidaturas, _foreignKeysCandidaturas, _indicesCandidaturas);
        final TableInfo _existingCandidaturas = TableInfo.read(db, "candidaturas");
        if (!_infoCandidaturas.equals(_existingCandidaturas)) {
          return new RoomOpenHelper.ValidationResult(false, "candidaturas(com.example.motoboyrecrutamento.data.local.Candidatura).\n"
                  + " Expected:\n" + _infoCandidaturas + "\n"
                  + " Found:\n" + _existingCandidaturas);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "3d05cd79fed5d9ca498c0da07d5cad72", "a5f0d96aa4c76abb6be732b7816de5ee");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "usuarios","restaurantes","motoboys","vagas","candidaturas");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `usuarios`");
      _db.execSQL("DELETE FROM `restaurantes`");
      _db.execSQL("DELETE FROM `motoboys`");
      _db.execSQL("DELETE FROM `vagas`");
      _db.execSQL("DELETE FROM `candidaturas`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UsuarioDao.class, UsuarioDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RestauranteDao.class, RestauranteDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MotoboyDao.class, MotoboyDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VagaDao.class, VagaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CandidaturaDao.class, CandidaturaDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UsuarioDao usuarioDao() {
    if (_usuarioDao != null) {
      return _usuarioDao;
    } else {
      synchronized(this) {
        if(_usuarioDao == null) {
          _usuarioDao = new UsuarioDao_Impl(this);
        }
        return _usuarioDao;
      }
    }
  }

  @Override
  public RestauranteDao restauranteDao() {
    if (_restauranteDao != null) {
      return _restauranteDao;
    } else {
      synchronized(this) {
        if(_restauranteDao == null) {
          _restauranteDao = new RestauranteDao_Impl(this);
        }
        return _restauranteDao;
      }
    }
  }

  @Override
  public MotoboyDao motoboyDao() {
    if (_motoboyDao != null) {
      return _motoboyDao;
    } else {
      synchronized(this) {
        if(_motoboyDao == null) {
          _motoboyDao = new MotoboyDao_Impl(this);
        }
        return _motoboyDao;
      }
    }
  }

  @Override
  public VagaDao vagaDao() {
    if (_vagaDao != null) {
      return _vagaDao;
    } else {
      synchronized(this) {
        if(_vagaDao == null) {
          _vagaDao = new VagaDao_Impl(this);
        }
        return _vagaDao;
      }
    }
  }

  @Override
  public CandidaturaDao candidaturaDao() {
    if (_candidaturaDao != null) {
      return _candidaturaDao;
    } else {
      synchronized(this) {
        if(_candidaturaDao == null) {
          _candidaturaDao = new CandidaturaDao_Impl(this);
        }
        return _candidaturaDao;
      }
    }
  }
}
