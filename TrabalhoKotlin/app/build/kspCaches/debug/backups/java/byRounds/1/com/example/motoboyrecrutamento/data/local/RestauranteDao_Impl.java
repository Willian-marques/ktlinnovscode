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
public final class RestauranteDao_Impl implements RestauranteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Restaurante> __insertionAdapterOfRestaurante;

  private final EntityDeletionOrUpdateAdapter<Restaurante> __updateAdapterOfRestaurante;

  public RestauranteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRestaurante = new EntityInsertionAdapter<Restaurante>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `restaurantes` (`id`,`usuarioId`,`cnpj`,`endereco`,`telefone`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Restaurante entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUsuarioId());
        statement.bindString(3, entity.getCnpj());
        statement.bindString(4, entity.getEndereco());
        statement.bindString(5, entity.getTelefone());
      }
    };
    this.__updateAdapterOfRestaurante = new EntityDeletionOrUpdateAdapter<Restaurante>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `restaurantes` SET `id` = ?,`usuarioId` = ?,`cnpj` = ?,`endereco` = ?,`telefone` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Restaurante entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUsuarioId());
        statement.bindString(3, entity.getCnpj());
        statement.bindString(4, entity.getEndereco());
        statement.bindString(5, entity.getTelefone());
        statement.bindLong(6, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Restaurante restaurante,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRestaurante.insertAndReturnId(restaurante);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Restaurante restaurante,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRestaurante.handle(restaurante);
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
      final Continuation<? super Restaurante> $completion) {
    final String _sql = "SELECT * FROM restaurantes WHERE usuarioId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, usuarioId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Restaurante>() {
      @Override
      @Nullable
      public Restaurante call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUsuarioId = CursorUtil.getColumnIndexOrThrow(_cursor, "usuarioId");
          final int _cursorIndexOfCnpj = CursorUtil.getColumnIndexOrThrow(_cursor, "cnpj");
          final int _cursorIndexOfEndereco = CursorUtil.getColumnIndexOrThrow(_cursor, "endereco");
          final int _cursorIndexOfTelefone = CursorUtil.getColumnIndexOrThrow(_cursor, "telefone");
          final Restaurante _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpUsuarioId;
            _tmpUsuarioId = _cursor.getLong(_cursorIndexOfUsuarioId);
            final String _tmpCnpj;
            _tmpCnpj = _cursor.getString(_cursorIndexOfCnpj);
            final String _tmpEndereco;
            _tmpEndereco = _cursor.getString(_cursorIndexOfEndereco);
            final String _tmpTelefone;
            _tmpTelefone = _cursor.getString(_cursorIndexOfTelefone);
            _result = new Restaurante(_tmpId,_tmpUsuarioId,_tmpCnpj,_tmpEndereco,_tmpTelefone);
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
