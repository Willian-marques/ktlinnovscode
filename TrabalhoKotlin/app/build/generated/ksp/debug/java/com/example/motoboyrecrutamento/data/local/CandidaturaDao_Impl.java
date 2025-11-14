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
public final class CandidaturaDao_Impl implements CandidaturaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Candidatura> __insertionAdapterOfCandidatura;

  private final EntityDeletionOrUpdateAdapter<Candidatura> __updateAdapterOfCandidatura;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CandidaturaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCandidatura = new EntityInsertionAdapter<Candidatura>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `candidaturas` (`id`,`vagaId`,`motoboyId`,`dataCandidatura`,`status`,`firestoreId`,`motoboyNome`,`motoboyEmail`,`motoboyTelefone`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Candidatura entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getVagaId());
        statement.bindLong(3, entity.getMotoboyId());
        statement.bindString(4, entity.getDataCandidatura());
        statement.bindString(5, entity.getStatus());
        if (entity.getFirestoreId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFirestoreId());
        }
        if (entity.getMotoboyNome() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMotoboyNome());
        }
        if (entity.getMotoboyEmail() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMotoboyEmail());
        }
        if (entity.getMotoboyTelefone() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getMotoboyTelefone());
        }
      }
    };
    this.__updateAdapterOfCandidatura = new EntityDeletionOrUpdateAdapter<Candidatura>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `candidaturas` SET `id` = ?,`vagaId` = ?,`motoboyId` = ?,`dataCandidatura` = ?,`status` = ?,`firestoreId` = ?,`motoboyNome` = ?,`motoboyEmail` = ?,`motoboyTelefone` = ? WHERE `vagaId` = ? AND `motoboyId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Candidatura entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getVagaId());
        statement.bindLong(3, entity.getMotoboyId());
        statement.bindString(4, entity.getDataCandidatura());
        statement.bindString(5, entity.getStatus());
        if (entity.getFirestoreId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFirestoreId());
        }
        if (entity.getMotoboyNome() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMotoboyNome());
        }
        if (entity.getMotoboyEmail() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getMotoboyEmail());
        }
        if (entity.getMotoboyTelefone() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getMotoboyTelefone());
        }
        statement.bindLong(10, entity.getVagaId());
        statement.bindLong(11, entity.getMotoboyId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM candidaturas";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Candidatura candidatura,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCandidatura.insertAndReturnId(candidatura);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<Candidatura> candidaturas,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCandidatura.insert(candidaturas);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Candidatura candidatura,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCandidatura.handle(candidatura);
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
  public Flow<List<Candidatura>> getCandidaturasByMotoboy(final long motoboyId) {
    final String _sql = "SELECT * FROM candidaturas WHERE motoboyId = ? ORDER BY dataCandidatura DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, motoboyId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"candidaturas"}, new Callable<List<Candidatura>>() {
      @Override
      @NonNull
      public List<Candidatura> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVagaId = CursorUtil.getColumnIndexOrThrow(_cursor, "vagaId");
          final int _cursorIndexOfMotoboyId = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyId");
          final int _cursorIndexOfDataCandidatura = CursorUtil.getColumnIndexOrThrow(_cursor, "dataCandidatura");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfFirestoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreId");
          final int _cursorIndexOfMotoboyNome = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyNome");
          final int _cursorIndexOfMotoboyEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyEmail");
          final int _cursorIndexOfMotoboyTelefone = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyTelefone");
          final List<Candidatura> _result = new ArrayList<Candidatura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Candidatura _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpVagaId;
            _tmpVagaId = _cursor.getLong(_cursorIndexOfVagaId);
            final long _tmpMotoboyId;
            _tmpMotoboyId = _cursor.getLong(_cursorIndexOfMotoboyId);
            final String _tmpDataCandidatura;
            _tmpDataCandidatura = _cursor.getString(_cursorIndexOfDataCandidatura);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpFirestoreId;
            if (_cursor.isNull(_cursorIndexOfFirestoreId)) {
              _tmpFirestoreId = null;
            } else {
              _tmpFirestoreId = _cursor.getString(_cursorIndexOfFirestoreId);
            }
            final String _tmpMotoboyNome;
            if (_cursor.isNull(_cursorIndexOfMotoboyNome)) {
              _tmpMotoboyNome = null;
            } else {
              _tmpMotoboyNome = _cursor.getString(_cursorIndexOfMotoboyNome);
            }
            final String _tmpMotoboyEmail;
            if (_cursor.isNull(_cursorIndexOfMotoboyEmail)) {
              _tmpMotoboyEmail = null;
            } else {
              _tmpMotoboyEmail = _cursor.getString(_cursorIndexOfMotoboyEmail);
            }
            final String _tmpMotoboyTelefone;
            if (_cursor.isNull(_cursorIndexOfMotoboyTelefone)) {
              _tmpMotoboyTelefone = null;
            } else {
              _tmpMotoboyTelefone = _cursor.getString(_cursorIndexOfMotoboyTelefone);
            }
            _item = new Candidatura(_tmpId,_tmpVagaId,_tmpMotoboyId,_tmpDataCandidatura,_tmpStatus,_tmpFirestoreId,_tmpMotoboyNome,_tmpMotoboyEmail,_tmpMotoboyTelefone);
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
  public Flow<List<Candidatura>> getCandidaturasByVaga(final long vagaId) {
    final String _sql = "SELECT * FROM candidaturas WHERE vagaId = ? ORDER BY dataCandidatura DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, vagaId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"candidaturas"}, new Callable<List<Candidatura>>() {
      @Override
      @NonNull
      public List<Candidatura> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVagaId = CursorUtil.getColumnIndexOrThrow(_cursor, "vagaId");
          final int _cursorIndexOfMotoboyId = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyId");
          final int _cursorIndexOfDataCandidatura = CursorUtil.getColumnIndexOrThrow(_cursor, "dataCandidatura");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfFirestoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreId");
          final int _cursorIndexOfMotoboyNome = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyNome");
          final int _cursorIndexOfMotoboyEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyEmail");
          final int _cursorIndexOfMotoboyTelefone = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyTelefone");
          final List<Candidatura> _result = new ArrayList<Candidatura>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Candidatura _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpVagaId;
            _tmpVagaId = _cursor.getLong(_cursorIndexOfVagaId);
            final long _tmpMotoboyId;
            _tmpMotoboyId = _cursor.getLong(_cursorIndexOfMotoboyId);
            final String _tmpDataCandidatura;
            _tmpDataCandidatura = _cursor.getString(_cursorIndexOfDataCandidatura);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpFirestoreId;
            if (_cursor.isNull(_cursorIndexOfFirestoreId)) {
              _tmpFirestoreId = null;
            } else {
              _tmpFirestoreId = _cursor.getString(_cursorIndexOfFirestoreId);
            }
            final String _tmpMotoboyNome;
            if (_cursor.isNull(_cursorIndexOfMotoboyNome)) {
              _tmpMotoboyNome = null;
            } else {
              _tmpMotoboyNome = _cursor.getString(_cursorIndexOfMotoboyNome);
            }
            final String _tmpMotoboyEmail;
            if (_cursor.isNull(_cursorIndexOfMotoboyEmail)) {
              _tmpMotoboyEmail = null;
            } else {
              _tmpMotoboyEmail = _cursor.getString(_cursorIndexOfMotoboyEmail);
            }
            final String _tmpMotoboyTelefone;
            if (_cursor.isNull(_cursorIndexOfMotoboyTelefone)) {
              _tmpMotoboyTelefone = null;
            } else {
              _tmpMotoboyTelefone = _cursor.getString(_cursorIndexOfMotoboyTelefone);
            }
            _item = new Candidatura(_tmpId,_tmpVagaId,_tmpMotoboyId,_tmpDataCandidatura,_tmpStatus,_tmpFirestoreId,_tmpMotoboyNome,_tmpMotoboyEmail,_tmpMotoboyTelefone);
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
  public Object getCandidaturaByVagaAndMotoboy(final long vagaId, final long motoboyId,
      final Continuation<? super Candidatura> $completion) {
    final String _sql = "SELECT * FROM candidaturas WHERE vagaId = ? AND motoboyId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, vagaId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, motoboyId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Candidatura>() {
      @Override
      @Nullable
      public Candidatura call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVagaId = CursorUtil.getColumnIndexOrThrow(_cursor, "vagaId");
          final int _cursorIndexOfMotoboyId = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyId");
          final int _cursorIndexOfDataCandidatura = CursorUtil.getColumnIndexOrThrow(_cursor, "dataCandidatura");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfFirestoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "firestoreId");
          final int _cursorIndexOfMotoboyNome = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyNome");
          final int _cursorIndexOfMotoboyEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyEmail");
          final int _cursorIndexOfMotoboyTelefone = CursorUtil.getColumnIndexOrThrow(_cursor, "motoboyTelefone");
          final Candidatura _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpVagaId;
            _tmpVagaId = _cursor.getLong(_cursorIndexOfVagaId);
            final long _tmpMotoboyId;
            _tmpMotoboyId = _cursor.getLong(_cursorIndexOfMotoboyId);
            final String _tmpDataCandidatura;
            _tmpDataCandidatura = _cursor.getString(_cursorIndexOfDataCandidatura);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpFirestoreId;
            if (_cursor.isNull(_cursorIndexOfFirestoreId)) {
              _tmpFirestoreId = null;
            } else {
              _tmpFirestoreId = _cursor.getString(_cursorIndexOfFirestoreId);
            }
            final String _tmpMotoboyNome;
            if (_cursor.isNull(_cursorIndexOfMotoboyNome)) {
              _tmpMotoboyNome = null;
            } else {
              _tmpMotoboyNome = _cursor.getString(_cursorIndexOfMotoboyNome);
            }
            final String _tmpMotoboyEmail;
            if (_cursor.isNull(_cursorIndexOfMotoboyEmail)) {
              _tmpMotoboyEmail = null;
            } else {
              _tmpMotoboyEmail = _cursor.getString(_cursorIndexOfMotoboyEmail);
            }
            final String _tmpMotoboyTelefone;
            if (_cursor.isNull(_cursorIndexOfMotoboyTelefone)) {
              _tmpMotoboyTelefone = null;
            } else {
              _tmpMotoboyTelefone = _cursor.getString(_cursorIndexOfMotoboyTelefone);
            }
            _result = new Candidatura(_tmpId,_tmpVagaId,_tmpMotoboyId,_tmpDataCandidatura,_tmpStatus,_tmpFirestoreId,_tmpMotoboyNome,_tmpMotoboyEmail,_tmpMotoboyTelefone);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
