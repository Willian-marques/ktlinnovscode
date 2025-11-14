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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MotoboyDao_Impl implements MotoboyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Motoboy> __insertionAdapterOfMotoboy;

  private final EntityDeletionOrUpdateAdapter<Motoboy> __updateAdapterOfMotoboy;

  public MotoboyDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMotoboy = new EntityInsertionAdapter<Motoboy>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `motoboys` (`id`,`usuarioId`,`cnh`,`veiculo`,`experienciaAnos`,`raioAtuacao`,`telefone`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Motoboy entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUsuarioId());
        statement.bindString(3, entity.getCnh());
        statement.bindString(4, entity.getVeiculo());
        statement.bindLong(5, entity.getExperienciaAnos());
        statement.bindDouble(6, entity.getRaioAtuacao());
        statement.bindString(7, entity.getTelefone());
      }
    };
    this.__updateAdapterOfMotoboy = new EntityDeletionOrUpdateAdapter<Motoboy>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `motoboys` SET `id` = ?,`usuarioId` = ?,`cnh` = ?,`veiculo` = ?,`experienciaAnos` = ?,`raioAtuacao` = ?,`telefone` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Motoboy entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUsuarioId());
        statement.bindString(3, entity.getCnh());
        statement.bindString(4, entity.getVeiculo());
        statement.bindLong(5, entity.getExperienciaAnos());
        statement.bindDouble(6, entity.getRaioAtuacao());
        statement.bindString(7, entity.getTelefone());
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Motoboy motoboy, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMotoboy.insertAndReturnId(motoboy);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Motoboy motoboy, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMotoboy.handle(motoboy);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getByUsuarioId(final long usuarioId,
      final Continuation<? super Motoboy> $completion) {
    final String _sql = "SELECT * FROM motoboys WHERE usuarioId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, usuarioId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Motoboy>() {
      @Override
      @Nullable
      public Motoboy call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfCnh = CursorUtil.getColumnIndexOrThrow(_cursor, "cnh");
          final int _cursorIndexOfVeiculo = CursorUtil.getColumnIndexOrThrow(_cursor, "veiculo");
          final int _cursorIndexOfExperienciaAnos = CursorUtil.getColumnIndexOrThrow(_cursor, "experienciaAnos");
          final int _cursorIndexOfRaioAtuacao = CursorUtil.getColumnIndexOrThrow(_cursor, "raioAtuacao");
          final int _cursorIndexOfTelefone = CursorUtil.getColumnIndexOrThrow(_cursor, "telefone");
          final Motoboy _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUsuarioId;
            _tmpUsuarioId = _cursor.getLong(_cursorIndexOfUsuarioId);
            final String _tmpCnh;
            _tmpCnh = _cursor.getString(_cursorIndexOfCnh);
            final String _tmpVeiculo;
            _tmpVeiculo = _cursor.getString(_cursorIndexOfVeiculo);
            final int _tmpExperienciaAnos;
            _tmpExperienciaAnos = _cursor.getInt(_cursorIndexOfExperienciaAnos);
            final double _tmpRaioAtuacao;
            _tmpRaioAtuacao = _cursor.getDouble(_cursorIndexOfRaioAtuacao);
            final String _tmpTelefone;
            _tmpTelefone = _cursor.getString(_cursorIndexOfTelefone);
            _result = new Motoboy(_tmpId,_tmpUsuarioId,_tmpCnh,_tmpVeiculo,_tmpExperienciaAnos,_tmpRaioAtuacao,_tmpTelefone);
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
  public Object getMotoboyByIdSync(final long motoboyId,
      final Continuation<? super Motoboy> $completion) {
    final String _sql = "SELECT * FROM motoboys WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, motoboyId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Motoboy>() {
      @Override
      @Nullable
      public Motoboy call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfCnh = CursorUtil.getColumnIndexOrThrow(_cursor, "cnh");
          final int _cursorIndexOfVeiculo = CursorUtil.getColumnIndexOrThrow(_cursor, "veiculo");
          final int _cursorIndexOfExperienciaAnos = CursorUtil.getColumnIndexOrThrow(_cursor, "experienciaAnos");
          final int _cursorIndexOfRaioAtuacao = CursorUtil.getColumnIndexOrThrow(_cursor, "raioAtuacao");
          final int _cursorIndexOfTelefone = CursorUtil.getColumnIndexOrThrow(_cursor, "telefone");
          final Motoboy _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUsuarioId;
            _tmpUsuarioId = _cursor.getLong(_cursorIndexOfUsuarioId);
            final String _tmpCnh;
            _tmpCnh = _cursor.getString(_cursorIndexOfCnh);
            final String _tmpVeiculo;
            _tmpVeiculo = _cursor.getString(_cursorIndexOfVeiculo);
            final int _tmpExperienciaAnos;
            _tmpExperienciaAnos = _cursor.getInt(_cursorIndexOfExperienciaAnos);
            final double _tmpRaioAtuacao;
            _tmpRaioAtuacao = _cursor.getDouble(_cursorIndexOfRaioAtuacao);
            final String _tmpTelefone;
            _tmpTelefone = _cursor.getString(_cursorIndexOfTelefone);
            _result = new Motoboy(_tmpId,_tmpUsuarioId,_tmpCnh,_tmpVeiculo,_tmpExperienciaAnos,_tmpRaioAtuacao,_tmpTelefone);
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
