# 🏍️ Motoboy Recrutamento - Sistema de Gestão de Vagas

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)

## 📝 Descrição do Projeto

O **Motoboy Recrutamento** é um aplicativo Android desenvolvido para conectar **restaurantes** e **motoboys**, facilitando o processo de publicação de vagas, candidaturas e gestão de perfis profissionais. O sistema oferece uma experiência moderna, intuitiva e completa tanto para empresas quanto para profissionais autônomos.

### 🎯 Objetivos Principais

- **Para Restaurantes**: Publicar vagas, gerenciar candidaturas e visualizar perfis de motoboys
- **Para Motoboys**: Buscar vagas disponíveis, candidatar-se e gerenciar perfil profissional
- **Para Ambos**: Interface moderna e fluida com Material Design 3

---

## 🚀 Tecnologias Utilizadas

### Stack Principal

| Tecnologia | Descrição | Versão |
|-----------|-----------|--------|
| **Kotlin** | Linguagem de programação moderna e concisa | 1.9+ |
| **Jetpack Compose** | Framework moderno para UI declarativa | - |
| **Material 3** | Sistema de design Google (Material You) | - |
| **Firebase Authentication** | Autenticação de usuários (email/senha) | - |
| **Firebase Firestore** | Banco de dados NoSQL em tempo real | - |
| **Firebase Storage** | Armazenamento de arquivos (documentos) | - |
| **Room Database** | Banco de dados local SQLite (cache offline) | - |
| **Navigation Compose** | Navegação entre telas | - |
| **Coroutines & Flow** | Programação assíncrona e reativa | - |
| **ViewModel** | Gerenciamento de estado e ciclo de vida | - |

### Arquitetura

- **MVVM** (Model-View-ViewModel)
- **Repository Pattern** (Single Source of Truth)
- **Clean Architecture** (Separação de camadas)
- **Offline-First** (Cache local com sincronização)

---

## 🧩 Funcionalidades Implementadas

### Requisitos Funcionais (RF)

| ID | Funcionalidade | Descrição | Responsável |
|----|---------------|-----------|-------------|
| **RF01** | Cadastro de Usuário | Registro com email, senha e tipo de perfil | 
| **RF02** | Login | Autenticação com Firebase e redirecionamento por perfil | 
| **RF03** | Recuperação de Senha | Envio de email de recuperação via Firebase | 
| **RF04** | Publicar Vaga | Restaurantes criam vagas com título, descrição e salário | 
| **RF05** | Candidatar-se | Motoboys enviam candidaturas para vagas | 
| **RF06** | Gerenciar Candidaturas | Restaurantes visualizam e analisam candidatos | 
| **RF07** | Anexar Documentos | Upload de CNH e documentos no Firebase Storage | 

### Funcionalidades Extras

- ✅ **Visualização de senha** (ícone de olho nos campos)
- ✅ **Design moderno** (Material 3 com cards elevados)
- ✅ **Estatísticas em tempo real** (contadores de vagas/candidaturas)
- ✅ **Sincronização bidirecional** (Firestore ↔ Room)
- ✅ **Prevenção de duplicatas** (chave composta em candidaturas)
- ✅ **Mensagens de erro em PT-BR** (tratamento completo de erros)

---

## 📂 Estrutura do Projeto

```
TrabalhoKotlin/app/src/main/java/com/example/motoboyrecrutamento/
│
├── data/
│   ├── local/
│   │   ├── Entities.kt                 # Entidades Room (Usuario, Vaga, Candidatura, etc)
│   │   ├── AppDatabase.kt              # Configuração do banco Room
│   │   └── DAOs.kt                     # Data Access Objects
│   │
│   ├── repository/
│   │   ├── VagaRepository.kt           # Lógica de vagas (Firestore + Room)
│   │   ├── CandidaturaRepository.kt    # Lógica de candidaturas
│   │   └── MotoboyRepository.kt        # Lógica de perfil motoboy
│   │
│   └── firebase/
│       ├── FirestoreVagaService.kt     # Serviço Firestore para vagas
│       ├── FirestoreCandidaturaService.kt  # Serviço Firestore para candidaturas
│       └── FirestoreMotoboyService.kt  # Serviço Firestore para perfis
│
├── viewmodel/
│   ├── LoginViewModel.kt               # RF01, RF02, RF03
│   ├── RestauranteViewModel.kt         # RF04, RF06
│   └── MotoboyViewModel.kt             # RF05, RF07
│
├── ui/
│   ├── login/
│   │   ├── LoginScreen.kt              # Tela de login
│   │   ├── RegisterScreen.kt           # Tela de cadastro
│   │   └── ForgotPasswordScreen.kt     # Tela de recuperação
│   │
│   ├── restaurante/
│   │   ├── RestauranteHomeScreen.kt    # Dashboard do restaurante
│   │   ├── PostVagaScreen.kt           # Publicar vaga
│   │   ├── GerenciarCandidaturasScreen.kt  # Gerenciar candidatos
│   │   └── MotoboyDetalhesScreen.kt    # Ver perfil do motoboy
│   │
│   ├── motoboy/
│   │   ├── MotoboyHomeScreen.kt        # Dashboard do motoboy
│   │   ├── VagaDetalhesScreen.kt       # Ver detalhes da vaga
│   │   └── MotoboyPerfilScreen.kt      # Editar perfil + upload docs
│   │
│   ├── common/
│   │   └── CommonComponents.kt         # Componentes reutilizáveis
│   │
│   ├── theme/
│   │   └── Theme.kt                    # Tema Material 3
│   │
│   └── MainActivity.kt                 # Activity principal + navegação
│
└── AndroidManifest.xml
```

---

## ▶️ Como Executar o Projeto

### Pré-requisitos

1. **Android Studio** Hedgehog | 2023.1.1 ou superior
2. **JDK** 17 ou superior
3. **Gradle** 8.0+
4. **Dispositivo Android** 8.0 (API 26) ou superior
5. **Conta Firebase** (configuração do `google-services.json`)

### Passos para Instalação

#### 1. Clonar o Repositório

```bash
git clone https://github.com/Willian-marques/ktlinnovscode.git
cd ktlinnovscode/TrabalhoKotlin
```

#### 2. Configurar Firebase

1. Acesse o [Firebase Console](https://console.firebase.google.com/)
2. Crie um projeto ou use o existente: `motoboy-recrutamento`
3. Adicione um app Android com o package: `com.example.motoboyrecrutamento`
4. Baixe o arquivo `google-services.json` e coloque em:
   ```
   TrabalhoKotlin/app/google-services.json
   ```

#### 3. Sincronizar Dependências

```bash
# No terminal do Android Studio ou PowerShell
cd TrabalhoKotlin
.\gradlew build
```

#### 4. Executar no Dispositivo

**Opção A: Emulador**
```bash
# Criar AVD via Android Studio
# Run > Run 'app'
```

**Opção B: Dispositivo Físico (USB)**
```bash
# Ativar modo desenvolvedor no celular
# Conectar via USB e autorizar depuração
.\gradlew installDebug
```

**Opção C: Via Android Studio**
- Clique no botão ▶️ **Run**
- Selecione o dispositivo conectado

---

## 📚 Documentação Adicional

Para uma visão mais detalhada do projeto, consulte:

- 📋 **[Diagrama de Navegação](./docs/DIAGRAMA_NAVEGACAO.md)** - Fluxo completo entre telas e rotas
- 🗄️ **[Diagrama de Banco de Dados](./docs/DIAGRAMA_BANCO_DADOS.md)** - Estrutura de entidades e relacionamentos

---

## 🗄️ Estrutura do Banco de Dados

### Diagrama ER (Entity Relationship)

```
┌─────────────────┐
│    Usuario      │
├─────────────────┤
│ PK id           │
│    firebaseUid  │◄───────────┐
│    nome         │            │
│    email        │            │ Foreign Key
│    tipo         │            │
│    dataCriacao  │            │
└─────────────────┘            │
         ▲                     │
         │                     │
    ┌────┴────┐                │
    │         │                │
    │         │                │
┌───┴──────┐  │    ┌───────────┴──────┐
│Restaurante│  │    │    Motoboy       │
├───────────┤  │    ├──────────────────┤
│ PK id     │  │    │ PK id            │
│ FK usuarioId│ │   │ FK usuarioId     │
│    cnpj   │  │    │    cnh           │
│    endereco│ │    │    veiculo       │
│    telefone│ │    │    experienciaAnos│
└───────────┘  │    │    raioAtuacao   │
         │     │    │    telefone      │
         │     │    └──────────────────┘
         │     │              │
         │     │              │
         ▼     │              ▼
    ┌─────────┴┐         ┌─────────────────┐
    │   Vaga   │         │  Candidatura    │
    ├──────────┤         ├─────────────────┤
    │ PK id    │◄────────│ PK (vagaId,     │
    │ firestoreId│       │     motoboyId)  │
    │ titulo   │         │ FK vagaId       │
    │ descricao│         │ FK motoboyId    │
    │ salario  │         │    dataCandidatura│
    │ status   │         │    status       │
    │ requisitos│        │    firestoreId  │
    │ dataPublicacao│    │    motoboyNome  │
    │ FK restauranteId│  │    motoboyEmail │
    └──────────┘         │    motoboyTelefone│
                         └─────────────────┘
```

### Descrição das Entidades

#### **Usuario**
- **Propósito**: Entidade base para todos os usuários do sistema
- **Campos**:
  - `id`: Identificador único local (Room)
  - `firebaseUid`: UID do Firebase Authentication
  - `nome`: Nome completo do usuário
  - `email`: Email de autenticação
  - `tipo`: "motoboy" ou "restaurante"
  - `dataCriacao`: Data de registro no formato ISO

#### **Restaurante**
- **Propósito**: Dados específicos do perfil Restaurante
- **Relacionamento**: 1:1 com Usuario
- **Campos**:
  - `id`: Identificador único
  - `usuarioId`: FK para Usuario
  - `cnpj`: CNPJ da empresa
  - `endereco`: Localização física
  - `telefone`: Contato comercial

#### **Motoboy**
- **Propósito**: Dados específicos do perfil Motoboy
- **Relacionamento**: 1:1 com Usuario
- **Campos**:
  - `id`: Identificador único
  - `usuarioId`: FK para Usuario
  - `cnh`: Número da Carteira Nacional de Habilitação
  - `veiculo`: Tipo de veículo (moto/bicicleta)
  - `experienciaAnos`: Anos de experiência
  - `raioAtuacao`: Distância máxima (km)
  - `telefone`: Contato pessoal

#### **Vaga**
- **Propósito**: Oportunidades de trabalho publicadas por restaurantes
- **Relacionamento**: N:1 com Restaurante
- **Campos**:
  - `id`: Identificador único local
  - `firestoreId`: ID do documento no Firestore
  - `titulo`: Título da vaga
  - `descricao`: Detalhes da oportunidade
  - `salario`: Valor oferecido (R$)
  - `status`: "aberta" ou "fechada"
  - `requisitos`: Lista em JSON
  - `dataPublicacao`: Data de criação
  - `restauranteId`: FK para Usuario (restaurante)

#### **Candidatura**
- **Propósito**: Registro de candidaturas de motoboys em vagas
- **Relacionamento**: N:M entre Vaga e Motoboy
- **Chave Primária Composta**: (vagaId, motoboyId) - previne duplicatas
- **Campos**:
  - `vagaId`: FK para Vaga
  - `motoboyId`: FK para Motoboy
  - `dataCandidatura`: Data/hora da candidatura
  - `status`: "pendente", "aceita" ou "rejeitada"
  - `firestoreId`: ID no Firestore
  - `motoboyNome`, `motoboyEmail`, `motoboyTelefone`: Dados desnormalizados para performance

### Sincronização Firestore ↔ Room

O sistema implementa uma arquitetura **Offline-First**:

1. **Escrita**: Dados são salvos primeiro no Firestore, depois sincronizados para Room
2. **Leitura**: Dados são buscados primeiro do Room (cache), depois do Firestore se necessário
3. **Conflitos**: Firestore é a fonte da verdade (Source of Truth)
4. **Deduplicação**: Validação de chaves compostas antes de inserção

---

## 🗺️ Diagrama de Navegação

### Mapa de Fluxo das Telas

```
                    ┌──────────────────┐
                    │  LoginScreen     │
                    │  (Autenticação)  │
                    └────────┬─────────┘
                             │
                ┌────────────┼────────────┐
                │            │            │
                ▼            ▼            ▼
         ┌──────────┐ ┌─────────────┐ ┌────────────────┐
         │ Register │ │ ForgotPass  │ │  (Login bem    │
         │ Screen   │ │  Screen     │ │   sucedido)    │
         └──────────┘ └─────────────┘ └────────┬───────┘
                                                │
                                     ┌──────────┴──────────┐
                                     │  Roteamento por     │
                                     │  tipo de usuário    │
                                     └──────────┬──────────┘
                                                │
                              ┌─────────────────┴─────────────────┐
                              │                                   │
                              ▼                                   ▼
                  ┌───────────────────────┐         ┌───────────────────────┐
                  │ RestauranteHomeScreen │         │  MotoboyHomeScreen    │
                  │  (Dashboard)          │         │  (Vagas disponíveis)  │
                  └───────────┬───────────┘         └───────────┬───────────┘
                              │                                   │
                ┌─────────────┼─────────────┐                    │
                │             │             │                    │
                ▼             ▼             ▼                    ▼
        ┌──────────┐  ┌─────────────┐  ┌──────────────┐  ┌────────────────┐
        │PostVaga  │  │ Gerenciar   │  │  Motoboy     │  │ VagaDetalhes   │
        │Screen    │  │Candidaturas │  │  Detalhes    │  │ Screen         │
        │(Criar    │  │Screen       │  │  Screen      │  │ (Ver vaga +    │
        │vaga)     │  │(Lista       │  │(Ver perfil   │  │  candidatar)   │
        └──────────┘  │candidatos)  │  │do candidato) │  └────────────────┘
                      └─────────────┘  └──────────────┘           │
                                                                    │
                                                                    ▼
                                                          ┌────────────────┐
                                                          │MotoboyPerfil   │
                                                          │Screen          │
                                                          │(Editar perfil +│
                                                          │ upload docs)   │
                                                          └────────────────┘
```

### Rotas de Navegação

| Rota | Parâmetros | Acesso | Descrição |
|------|-----------|--------|-----------|
| `login` | - | Público | Tela inicial de autenticação |
| `register` | - | Público | Cadastro de novo usuário |
| `forgot_password` | - | Público | Recuperação de senha |
| `motoboy_home` | - | Motoboy | Dashboard com vagas disponíveis |
| `vaga_detalhes/{vagaId}` | vagaId: Long | Motoboy | Detalhes da vaga + candidatura |
| `motoboy_perfil` | - | Motoboy | Editar perfil e upload de docs |
| `restaurante_home` | - | Restaurante | Dashboard com estatísticas |
| `post_vaga` | - | Restaurante | Publicar nova vaga |
| `gerenciar_candidaturas/{vagaId}` | vagaId: Long | Restaurante | Ver candidatos de uma vaga |
| `motoboy_detalhes/{motoboyId}` | motoboyId: Long | Restaurante | Ver perfil completo do motoboy |

---

## 🔌 Endpoints Firebase (Backend as a Service)

O projeto utiliza Firebase como backend, sem necessidade de API REST customizada.

### Firebase Authentication

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `createUserWithEmailAndPassword` | Firebase Auth | RF01 - Cadastro |
| `signInWithEmailAndPassword` | Firebase Auth | RF02 - Login |
| `sendPasswordResetEmail` | Firebase Auth | RF03 - Recuperação |

### Firebase Firestore

| Coleção | Documento | Operações | Descrição |
|---------|-----------|-----------|-----------|
| `/vagas` | `{vagaId}` | CREATE, READ, UPDATE | RF04 - Vagas publicadas |
| `/candidaturas` | `{candidaturaId}` | CREATE, READ, UPDATE | RF05 - Candidaturas enviadas |
| `/motoboys` | `{firebaseUid}` | CREATE, READ, UPDATE | RF07 - Perfis motoboy |

### Firebase Storage

| Path | Operação | Descrição |
|------|----------|-----------|
| `/documentos/{userId}/{filename}` | UPLOAD | RF07 - Upload de CNH/docs |

---


## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos na disciplina de **Desenvolvimento Mobile**.

---

## 📞 Contato

Para dúvidas ou sugestões, entre em contato com a equipe através do repositório no GitHub.

**Repositório**: [https://github.com/Willian-marques/ktlinnovscode](https://github.com/Willian-marques/ktlinnovscode)
