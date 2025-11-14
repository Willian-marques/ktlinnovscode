# 🗄️ Diagrama de Banco de Dados

## Visão Geral da Arquitetura de Dados

O sistema utiliza uma arquitetura **híbrida** com dois bancos de dados:

1. **Firebase Firestore** (Nuvem) - Fonte da verdade (Source of Truth)
2. **Room Database** (Local) - Cache para acesso offline

---

## 📊 Diagrama Entidade-Relacionamento (ER)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          BANCO DE DADOS ROOM                            │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────┐
│      Usuario        │
├─────────────────────┤
│ PK id (Long)        │◄─────────────────────┐
│    firebaseUid      │                      │
│    nome             │                      │ Foreign Key
│    email            │                      │ 1:1 Relationship
│    tipo             │                      │
│    dataCriacao      │                      │
└──────────┬──────────┘                      │
           │                                 │
           │ 1:1                             │
    ┌──────┴───────┐                         │
    │              │                         │
    ▼              ▼                         │
┌───────────┐  ┌──────────────┐             │
│Restaurante│  │   Motoboy    │             │
├───────────┤  ├──────────────┤             │
│PK id      │  │PK id         │             │
│FK usuarioId│ │FK usuarioId  │             │
│  cnpj     │  │  cnh         │             │
│  endereco │  │  veiculo     │             │
│  telefone │  │  experienciaAnos           │
└─────┬─────┘  │  raioAtuacao │             │
      │        │  telefone    │             │
      │        └───────┬──────┘             │
      │                │                    │
      │ 1:N            │ 1:N                │
      ▼                ▼                    │
┌─────────────┐  ┌─────────────────┐       │
│    Vaga     │  │  Candidatura    │       │
├─────────────┤  ├─────────────────┤       │
│PK id        │  │PK (vagaId,      │       │
│  firestoreId│◄─┤    motoboyId)   │       │
│  titulo     │  │                 │       │
│  descricao  │  │FK vagaId        │───────┘
│  salario    │  │FK motoboyId     │
│  status     │  │  id             │
│  requisitos │  │  dataCandidatura│
│  dataPublicacao│  status         │
│FK restauranteId│  firestoreId    │
└─────────────┘  │  motoboyNome    │
                 │  motoboyEmail   │
                 │  motoboyTelefone│
                 └─────────────────┘

┌─────────────────────────────────────────────────────────────────────────┐
│                       FIREBASE FIRESTORE (NUVEM)                        │
└─────────────────────────────────────────────────────────────────────────┘

Collection: /vagas
Document ID: {firestoreId}
├─ titulo: String
├─ descricao: String
├─ salario: Double
├─ status: String
├─ requisitos: String
├─ dataPublicacao: String
└─ restauranteId: String (Firebase UID)

Collection: /candidaturas
Document ID: {firestoreId}
├─ vagaId: Long
├─ motoboyId: Long
├─ dataCandidatura: String
├─ status: String
├─ motoboyNome: String
├─ motoboyEmail: String
└─ motoboyTelefone: String

Collection: /motoboys
Document ID: {firebaseUid}
├─ nome: String
├─ email: String
├─ cnh: String
├─ veiculo: String
├─ experienciaAnos: Int
├─ raioAtuacao: Double
└─ telefone: String

Firebase Storage: /documentos/{userId}/{filename}
├─ CNH.pdf
├─ RG.pdf
└─ outros documentos...
```

---

## 📋 Descrição Detalhada das Entidades

### 1. Usuario (Room)

**Tabela**: `usuarios`  
**Propósito**: Entidade base para todos os usuários do sistema

| Campo | Tipo | Constraints | Descrição |
|-------|------|-------------|-----------|
| id | Long | PK, AUTO_INCREMENT | Identificador único local |
| firebaseUid | String | NOT NULL, UNIQUE | UID do Firebase Authentication |
| nome | String | NOT NULL | Nome completo do usuário |
| email | String | NOT NULL, UNIQUE | Email de autenticação |
| tipo | String | NOT NULL | "motoboy" ou "restaurante" |
| dataCriacao | String | NOT NULL | ISO 8601 format (yyyy-MM-dd'T'HH:mm:ss) |

**Relacionamentos**:
- 1:1 com Restaurante (se tipo = "restaurante")
- 1:1 com Motoboy (se tipo = "motoboy")

**Código Kotlin**:
```kotlin
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firebaseUid: String,
    val nome: String,
    val email: String,
    val tipo: String, // "motoboy" ou "restaurante"
    val dataCriacao: String
)
```

---

### 2. Restaurante (Room)

**Tabela**: `restaurantes`  
**Propósito**: Dados específicos do perfil Restaurante/Empresa

| Campo | Tipo | Constraints | Descrição |
|-------|------|-------------|-----------|
| id | Long | PK, AUTO_INCREMENT | Identificador único |
| usuarioId | Long | FK → Usuario.id, NOT NULL | Referência ao usuário base |
| cnpj | String | NOT NULL, UNIQUE | CNPJ da empresa (14 dígitos) |
| endereco | String | NOT NULL | Endereço completo |
| telefone | String | NOT NULL | Telefone comercial |

**Relacionamentos**:
- N:1 com Usuario (um restaurante pertence a um usuário)
- 1:N com Vaga (um restaurante pode ter várias vagas)

**Código Kotlin**:
```kotlin
@Entity(tableName = "restaurantes")
data class Restaurante(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: Long, // Foreign key para Usuario
    val cnpj: String,
    val endereco: String,
    val telefone: String
)
```

---

### 3. Motoboy (Room)

**Tabela**: `motoboys`  
**Propósito**: Dados específicos do perfil Motoboy/Profissional

| Campo | Tipo | Constraints | Descrição |
|-------|------|-------------|-----------|
| id | Long | PK, AUTO_INCREMENT | Identificador único |
| usuarioId | Long | FK → Usuario.id, NOT NULL | Referência ao usuário base |
| cnh | String | NOT NULL | Número da CNH |
| veiculo | String | NOT NULL | Tipo de veículo (moto/bicicleta) |
| experienciaAnos | Int | NOT NULL | Anos de experiência como entregador |
| raioAtuacao | Double | NOT NULL | Distância máxima de atuação (km) |
| telefone | String | NOT NULL | Telefone pessoal |

**Relacionamentos**:
- N:1 com Usuario (um motoboy pertence a um usuário)
- N:M com Vaga através de Candidatura

**Código Kotlin**:
```kotlin
@Entity(tableName = "motoboys")
data class Motoboy(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val usuarioId: Long, // Foreign key para Usuario
    val cnh: String,
    val veiculo: String,
    val experienciaAnos: Int,
    val raioAtuacao: Double,
    val telefone: String
)
```

---

### 4. Vaga (Room + Firestore)

**Tabela Room**: `vagas`  
**Coleção Firestore**: `/vagas`  
**Propósito**: Oportunidades de trabalho publicadas por restaurantes

| Campo | Tipo | Constraints | Descrição |
|-------|------|-------------|-----------|
| id | Long | PK, AUTO_INCREMENT | Identificador único local |
| firestoreId | String? | NULLABLE | ID do documento no Firestore |
| titulo | String | NOT NULL | Título da vaga (ex: "Entregador Noturno") |
| descricao | String | NOT NULL | Descrição detalhada |
| salario | Double | NOT NULL | Valor oferecido (R$) |
| status | String | NOT NULL | "aberta" ou "fechada" |
| requisitos | String | NOT NULL | Lista JSON de requisitos |
| dataPublicacao | String | NOT NULL | Data de criação (yyyy-MM-dd HH:mm:ss) |
| restauranteId | String | FK, NOT NULL | Firebase UID do restaurante |

**Relacionamentos**:
- N:1 com Restaurante (várias vagas pertencem a um restaurante)
- N:M com Motoboy através de Candidatura

**Exemplo de Requisitos (JSON)**:
```json
["CNH categoria A", "Moto própria", "Experiência mínima 1 ano", "Disponibilidade noturna"]
```

**Código Kotlin**:
```kotlin
@Entity(tableName = "vagas")
data class Vaga(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firestoreId: String? = null,
    val titulo: String,
    val descricao: String,
    val salario: Double,
    val status: String, // "aberta", "fechada"
    val requisitos: String, // JSON array
    val dataPublicacao: String,
    val restauranteId: String // Firebase UID
)
```

---

### 5. Candidatura (Room + Firestore)

**Tabela Room**: `candidaturas`  
**Coleção Firestore**: `/candidaturas`  
**Propósito**: Registro de candidaturas de motoboys em vagas

| Campo | Tipo | Constraints | Descrição |
|-------|------|-------------|-----------|
| vagaId | Long | PK1, FK → Vaga.id | Parte da chave primária composta |
| motoboyId | Long | PK2, FK → Motoboy.id | Parte da chave primária composta |
| id | Long | NOT NULL | ID interno (não é PK) |
| dataCandidatura | String | NOT NULL | Data/hora (yyyy-MM-dd HH:mm:ss) |
| status | String | NOT NULL | "pendente", "aceita", "rejeitada" |
| firestoreId | String? | NULLABLE | ID do documento no Firestore |
| motoboyNome | String? | NULLABLE | Nome (desnormalizado para performance) |
| motoboyEmail | String? | NULLABLE | Email (desnormalizado) |
| motoboyTelefone | String? | NULLABLE | Telefone (desnormalizado) |

**Chave Primária Composta**: `(vagaId, motoboyId)`  
**Motivo**: Previne que um motoboy se candidate duas vezes na mesma vaga

**Relacionamentos**:
- N:1 com Vaga (várias candidaturas para uma vaga)
- N:1 com Motoboy (várias candidaturas de um motoboy)

**Código Kotlin**:
```kotlin
@Entity(
    tableName = "candidaturas",
    primaryKeys = ["vagaId", "motoboyId"]
)
data class Candidatura(
    val id: Long = 0,
    val vagaId: Long,
    val motoboyId: Long,
    val dataCandidatura: String,
    val status: String,
    val firestoreId: String? = null,
    val motoboyNome: String? = null,
    val motoboyEmail: String? = null,
    val motoboyTelefone: String? = null
)
```

---

## 🔄 Estratégia de Sincronização

### Padrão: Offline-First (Single Source of Truth)

```
┌──────────────────────────────────────────────────────────────┐
│                    FLUXO DE SINCRONIZAÇÃO                    │
└──────────────────────────────────────────────────────────────┘

ESCRITA (Criar/Atualizar):
    App → ViewModel → Repository → Firestore (nuvem)
                          ↓
                     Room Database (cache local)
                          ↓
                    UI atualizada (StateFlow)

LEITURA (Buscar):
    App → ViewModel → Repository → Room Database (cache)
                          ↓
                    Se cache vazio/desatualizado:
                          ↓
                     Firestore (nuvem)
                          ↓
                     Room Database (atualiza cache)
                          ↓
                    UI atualizada (StateFlow)
```

### Exemplo: Publicar Vaga

```kotlin
// 1. Salvar no Firestore
val firestoreId = firestoreVagaService.saveVaga(vaga)

// 2. Atualizar objeto com ID do Firestore
val vagaComId = vaga.copy(firestoreId = firestoreId)

// 3. Salvar no cache local (Room)
vagaDao.insert(vagaComId)

// 4. Emitir estado atualizado
_vagas.emit(getAllVagas())
```

### Exemplo: Listar Vagas

```kotlin
// 1. Buscar do cache local primeiro
var vagas = vagaDao.getAllVagas()

// 2. Se cache vazio, buscar da nuvem
if (vagas.isEmpty()) {
    vagas = firestoreVagaService.getAllVagas()
    
    // 3. Salvar no cache
    vagaDao.insertAll(vagas)
}

// 4. Retornar dados
return vagas
```

---

## 🔐 Segurança e Regras

### Firebase Firestore Rules

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Vagas: Restaurantes podem criar/editar suas próprias
    match /vagas/{vagaId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update, delete: if request.auth.uid == resource.data.restauranteId;
    }
    
    // Candidaturas: Motoboys podem criar suas próprias
    match /candidaturas/{candidaturaId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update: if request.auth.uid == resource.data.motoboyId 
                      || request.auth.uid == resource.data.restauranteId;
    }
    
    // Perfis Motoboy: Apenas o próprio usuário pode editar
    match /motoboys/{motoboyId} {
      allow read: if request.auth != null;
      allow create, update: if request.auth.uid == motoboyId;
    }
  }
}
```

### Firebase Storage Rules

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    
    // Documentos: Apenas o próprio usuário pode fazer upload
    match /documentos/{userId}/{fileName} {
      allow read: if request.auth != null;
      allow write: if request.auth.uid == userId;
    }
  }
}
```

---

## 📊 Índices e Performance

### Room Database Indexes

```kotlin
@Entity(
    tableName = "vagas",
    indices = [
        Index(value = ["restauranteId"]),
        Index(value = ["status"]),
        Index(value = ["dataPublicacao"])
    ]
)
data class Vaga(...)

@Entity(
    tableName = "candidaturas",
    indices = [
        Index(value = ["vagaId"]),
        Index(value = ["motoboyId"]),
        Index(value = ["status"])
    ]
)
data class Candidatura(...)
```

### Firestore Composite Indexes

```yaml
# firestore.indexes.json
{
  "indexes": [
    {
      "collectionGroup": "vagas",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "status", "order": "ASCENDING" },
        { "fieldPath": "dataPublicacao", "order": "DESCENDING" }
      ]
    },
    {
      "collectionGroup": "candidaturas",
      "queryScope": "COLLECTION",
      "fields": [
        { "fieldPath": "vagaId", "order": "ASCENDING" },
        { "fieldPath": "status", "order": "ASCENDING" }
      ]
    }
  ]
}
```

---

## 🔍 Queries Comuns

### Room DAOs

```kotlin
// VagaDao.kt
@Dao
interface VagaDao {
    @Query("SELECT * FROM vagas WHERE status = 'aberta' ORDER BY dataPublicacao DESC")
    fun getVagasAbertas(): Flow<List<Vaga>>
    
    @Query("SELECT * FROM vagas WHERE restauranteId = :restauranteId")
    fun getVagasByRestaurante(restauranteId: String): Flow<List<Vaga>>
    
    @Query("SELECT COUNT(*) FROM vagas WHERE restauranteId = :restauranteId AND status = 'aberta'")
    suspend fun countVagasAbertasByRestaurante(restauranteId: String): Int
}

// CandidaturaDao.kt
@Dao
interface CandidaturaDao {
    @Query("SELECT * FROM candidaturas WHERE vagaId = :vagaId")
    fun getCandidaturasByVaga(vagaId: Long): Flow<List<Candidatura>>
    
    @Query("SELECT * FROM candidaturas WHERE motoboyId = :motoboyId")
    fun getCandidaturasByMotoboy(motoboyId: Long): Flow<List<Candidatura>>
    
    @Query("SELECT * FROM candidaturas WHERE vagaId = :vagaId AND motoboyId = :motoboyId")
    suspend fun getCandidaturaByVagaAndMotoboy(vagaId: Long, motoboyId: Long): Candidatura?
}
```

### Firestore Queries

```kotlin
// Buscar vagas abertas
firestore.collection("vagas")
    .whereEqualTo("status", "aberta")
    .orderBy("dataPublicacao", Query.Direction.DESCENDING)
    .get()

// Buscar candidaturas de uma vaga
firestore.collection("candidaturas")
    .whereEqualTo("vagaId", vagaId)
    .whereEqualTo("status", "pendente")
    .get()

// Buscar perfil do motoboy
firestore.collection("motoboys")
    .document(firebaseUid)
    .get()
```

---

## 📈 Estatísticas e Agregações

### Contadores em Tempo Real

```kotlin
// Total de vagas por restaurante
SELECT COUNT(*) FROM vagas WHERE restauranteId = ?

// Total de candidaturas por vaga
SELECT COUNT(*) FROM candidaturas WHERE vagaId = ?

// Vagas abertas
SELECT COUNT(*) FROM vagas WHERE status = 'aberta'

// Candidaturas pendentes
SELECT COUNT(*) FROM candidaturas WHERE status = 'pendente' AND vagaId = ?
```

---

## 🛠️ Migrations e Versionamento

### Room Database Version

```kotlin
@Database(
    entities = [
        Usuario::class,
        Restaurante::class,
        Motoboy::class,
        Vaga::class,
        Candidatura::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun restauranteDao(): RestauranteDao
    abstract fun motoboyDao(): MotoboyDao
    abstract fun vagaDao(): VagaDao
    abstract fun candidaturaDao(): CandidaturaDao
}
```

### Estratégia de Migration

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Exemplo: adicionar nova coluna
        database.execSQL("ALTER TABLE vagas ADD COLUMN localidade TEXT")
    }
}
```

---

## 📝 Observações Importantes

### Desnormalização Intencional

Na tabela `Candidatura`, os campos `motoboyNome`, `motoboyEmail` e `motoboyTelefone` são **desnormalizados** para:
- ✅ Melhorar performance (evita JOINs complexos)
- ✅ Reduzir consultas ao banco
- ✅ Manter dados do motoboy mesmo se o perfil for alterado depois

### Sincronização Bidirecional

- **Firestore → Room**: Dados da nuvem são sincronizados para o cache local
- **Room → Firestore**: Alterações locais são enviadas para a nuvem
- **Conflitos**: Firestore sempre prevalece (Source of Truth)

### Tratamento de Duplicatas

A chave primária composta `(vagaId, motoboyId)` garante que:
- ❌ Um motoboy não pode se candidatar duas vezes na mesma vaga
- ✅ Room lança exceção se tentar inserir duplicata
- ✅ Código valida antes de inserir: `getCandidaturaByVagaAndMotoboy()`

---

## 🎯 Casos de Uso Práticos

### 1. Motoboy se Candidata a Vaga

```
UI → MotoboyViewModel → CandidaturaRepository
       ↓
   Verifica se já existe candidatura (Room)
       ↓
   Se não existe:
       ↓
   Cria candidatura no Firestore
       ↓
   Salva no Room com firestoreId
       ↓
   Emite estado de sucesso
```

### 2. Restaurante Publica Vaga

```
UI → RestauranteViewModel → VagaRepository
       ↓
   Cria vaga no Firestore
       ↓
   Recebe firestoreId
       ↓
   Salva no Room com firestoreId
       ↓
   Emite lista atualizada de vagas
```

### 3. Restaurante Visualiza Candidatos

```
UI → RestauranteViewModel → CandidaturaRepository
       ↓
   Busca candidaturas do Room (cache)
       ↓
   Se cache vazio:
       ↓
   Busca do Firestore
       ↓
   Salva no Room
       ↓
   Emite lista de candidaturas
       ↓
   UI exibe cards com dados desnormalizados
```

---

## 📚 Referências

- [Room Database Documentation](https://developer.android.com/training/data-storage/room)
- [Firebase Firestore Documentation](https://firebase.google.com/docs/firestore)
- [Offline Data with Firestore](https://firebase.google.com/docs/firestore/manage-data/enable-offline)
