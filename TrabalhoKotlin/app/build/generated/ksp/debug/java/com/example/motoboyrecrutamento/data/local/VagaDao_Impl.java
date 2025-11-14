package com.example.motoboyrecrutamento.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class VagaDao_Impl implements VagaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Vaga> __insertionAdapterOfVaga;

  private final EntityDeletionOrUpdateAdapter<Vaga> __deletionAdapterOfVaga;

  private final EntityDeletionOrUpdateAdapter<Vaga> __updateAdapterOfVaga;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOrphanVagas;

  private final SharedSQLiteStatement __preparedStmtOfDeleteVagasWithoutRestaurant;

  public VagaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVaga = new EntityInsertionAdapter<Vaga>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `vagas` (`id`,`firestoreId`,`titulo`,`descricao`,`salario`,`status`,`requisitos`,`dataPublicacao`,`restauranteId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Vaga entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFirestoreId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFirestoreId());
        }
        statement.bindString(3, entity.getTitulo());
        statement.bindString(4, entity.getDescricao());
        statement.bindDouble(5, entity.getSalario());
        statement.bindString(6, entity.getStatus());
        statement.bindString(7, entity.getRequisitos());
        statement.bindString(8, entity.getDataPublicacao());
        statement.bindString(9, entity.getRestauranteId());
      }
    };
    this.__deletionAdapterOfVaga = new EntityDeletionOrUpdateAdapter<Vaga>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `vagas` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Vaga entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfVaga = new EntityDeletionOrUpdateAdapter<Vaga>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `vagas` SET `id` = ?,`firestoreId` = ?,`titulo` = ?,`descricao` = ?,`salario` = ?,`status` = ?,`requisitos` = ?,`dataPublicacao` = ?,`restauranteId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Vaga entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFirestoreId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getFirestoreId());
        }
        statement.bindString(3, entity.getTitulo());
        statement.bindString(4, entity.getDescricao());
        statement.bindDouble(5, entity.getSalario());
        statement.bindString(6, entity.getStatus());
        statement.bindString(7, entity.getRequisitos());
        statement.bindString(8, entity.getDataPublicacao());
        statement.bindString(9, entity.getRestauranteId());
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM vagas";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOrphanVagas = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM vagas WHERE restauranteId NOT IN (SELECT id FROM restaurantes)";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteVagasWithoutRestaurant = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM vagas WHERE restauranteId = 0 OR restauranteId IS NULL";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Vaga vaga, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfVaga.insertAndReturnId(vaga);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<Vaga> vagas, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVaga.insert(vagas);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Vaga vaga, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfVaga.handle(vaga);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Vaga vaga, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfVaga.handle(vaga);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOrphanVagas(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOrphanVagas.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOrphanVagas.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteVagasWithoutRestaurant(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteVagasWithoutRestaurant.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteVagasWithoutRestaurant.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Vaga>> getVagasByRestaurante(final String restauranteId) {
    final String _sql = "SELECT * FROM vagas WHERE restauranteId = ? ORDER BY dataPublicacao DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, restauranteId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vagas"}, new Callable<List<Vaga>>() {
      @Override
      @NonNull
      public List<Vaga> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFirestoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreId");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfSalario = CursorUtil.getColumnIndexOrThrow(_cursor, "salario");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequisitos = CursorUtil.getColumnIndexOrThrow(_cursor, "requisitos");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfRestauranteId = CursorUtil.getColumnIndexOrThrow(_cursor, "restauranteId");
          final List<Vaga> _result = new ArrayList<Vaga>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Vaga _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFirestoreId;
            if (_cursor.isNull(_cursorIndexOfFirestoreId)) {
              _tmpFirestoreId = null;
            } else {
              _tmpFirestoreId = _cursor.getString(_cursorIndexOfFirestoreId);
            }
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final double _tmpSalario;
            _tmpSalario = _cursor.getDouble(_cursorIndexOfSalario);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpRequisitos;
            _tmpRequisitos = _cursor.getString(_cursorIndexOfRequisitos);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpRestauranteId;
            _tmpRestauranteId = _cursor.getString(_cursorIndexOfRestauranteId);
            _item = new Vaga(_tmpId,_tmpFirestoreId,_tmpTitulo,_tmpDescricao,_tmpSalario,_tmpStatus,_tmpRequisitos,_tmpDataPublicacao,_tmpRestauranteId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Vaga>> getVagasAbertas() {
    final String _sql = "SELECT * FROM vagas WHERE status = 'aberta' ORDER BY dataPublicacao DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vagas"}, new Callable<List<Vaga>>() {
      @Override
      @NonNull
      public List<Vaga> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFirestoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreId");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfSalario = CursorUtil.getColumnIndexOrThrow(_cursor, "salario");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequisitos = CursorUtil.getColumnIndexOrThrow(_cursor, "requisitos");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfRestauranteId = CursorUtil.getColumnIndexOrThrow(_cursor, "restauranteId");
          final List<Vaga> _result = new ArrayList<Vaga>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Vaga _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFirestoreId;
            if (_cursor.isNull(_cursorIndexOfFirestoreId)) {
              _tmpFirestoreId = null;
            } else {
              _tmpFirestoreId = _cursor.getString(_cursorIndexOfFirestoreId);
            }
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final double _tmpSalario;
            _tmpSalario = _cursor.getDouble(_cursorIndexOfSalario);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpRequisitos;
            _tmpRequisitos = _cursor.getString(_cursorIndexOfRequisitos);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpRestauranteId;
            _tmpRestauranteId = _cursor.getString(_cursorIndexOfRestauranteId);
            _item = new Vaga(_tmpId,_tmpFirestoreId,_tmpTitulo,_tmpDescricao,_tmpSalario,_tmpStatus,_tmpRequisitos,_tmpDataPublicacao,_tmpRestauranteId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getVagaByIdSync(final long vagaId, final Continuation<? super Vaga> $completion) {
    final String _sql = "SELECT * FROM vagas WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, vagaId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Vaga>() {
      @Override
      @Nullable
      public Vaga call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFirestoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreId");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfSalario = CursorUtil.getColumnIndexOrThrow(_cursor, "salario");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequisitos = CursorUtil.getColumnIndexOrThrow(_cursor, "requisitos");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfRestauranteId = CursorUtil.getColumnIndexOrThrow(_cursor, "restauranteId");
          final Vaga _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFirestoreId;
            if (_cursor.isNull(_cursorIndexOfFirestoreId)) {
              _tmpFirestoreId = null;
            } else {
              _tmpFirestoreId = _cursor.getString(_cursorIndexOfFirestoreId);
            }
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final double _tmpSalario;
            _tmpSalario = _cursor.getDouble(_cursorIndexOfSalario);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpRequisitos;
            _tmpRequisitos = _cursor.getString(_cursorIndexOfRequisitos);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpRestauranteId;
            _tmpRestauranteId = _cursor.getString(_cursorIndexOfRestauranteId);
            _result = new Vaga(_tmpId,_tmpFirestoreId,_tmpTitulo,_tmpDescricao,_tmpSalario,_tmpStatus,_tmpRequisitos,_tmpDataPublicacao,_tmpRestauranteId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllVagasSync(final Continuation<? super List<Vaga>> $completion) {
    final String _sql = "SELECT * FROM vagas";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Vaga>>() {
      @Override
      @NonNull
      public List<Vaga> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFirestoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreId");
          final int _cursorIndexOfTitulo = CursorUtil.getColumnIndexOrThrow(_cursor, "titulo");
          final int _cursorIndexOfDescricao = CursorUtil.getColumnIndexOrThrow(_cursor, "descricao");
          final int _cursorIndexOfSalario = CursorUtil.getColumnIndexOrThrow(_cursor, "salario");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfRequisitos = CursorUtil.getColumnIndexOrThrow(_cursor, "requisitos");
          final int _cursorIndexOfDataPublicacao = CursorUtil.getColumnIndexOrThrow(_cursor, "dataPublicacao");
          final int _cursorIndexOfRestauranteId = CursorUtil.getColumnIndexOrThrow(_cursor, "restauranteId");
          final List<Vaga> _result = new ArrayList<Vaga>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Vaga _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpFirestoreId;
            if (_cursor.isNull(_cursorIndexOfFirestoreId)) {
              _tmpFirestoreId = null;
            } else {
              _tmpFirestoreId = _cursor.getString(_cursorIndexOfFirestoreId);
            }
            final String _tmpTitulo;
            _tmpTitulo = _cursor.getString(_cursorIndexOfTitulo);
            final String _tmpDescricao;
            _tmpDescricao = _cursor.getString(_cursorIndexOfDescricao);
            final double _tmpSalario;
            _tmpSalario = _cursor.getDouble(_cursorIndexOfSalario);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpRequisitos;
            _tmpRequisitos = _cursor.getString(_cursorIndexOfRequisitos);
            final String _tmpDataPublicacao;
            _tmpDataPublicacao = _cursor.getString(_cursorIndexOfDataPublicacao);
            final String _tmpRestauranteId;
            _tmpRestauranteId = _cursor.getString(_cursorIndexOfRestauranteId);
            _item = new Vaga(_tmpId,_tmpFirestoreId,_tmpTitulo,_tmpDescricao,_tmpSalario,_tmpStatus,_tmpRequisitos,_tmpDataPublicacao,_tmpRestauranteId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
