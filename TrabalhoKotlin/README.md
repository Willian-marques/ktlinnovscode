# Projeto Final: Plataforma de Recrutamento de Motoboys

## Visão Geral

Este é o projeto final de Android Studio para uma **Plataforma de Recrutamento de Motoboys para Restaurantes**, desenvolvido em equipe de 3 membros.

### Tecnologias Utilizadas

- **Linguagem:** Kotlin
- **UI:** Jetpack Compose
- **Arquitetura:** MVVM (Model-View-ViewModel)
- **Banco de Dados Local:** Room
- **Comunicação com API:** Retrofit
- **Autenticação:** Firebase Authentication
- **Armazenamento de Arquivos:** Firebase Storage
- **Assincronismo:** Coroutines

### Versão do Android Studio

**Android Studio Giraffe | 2022.3.1** ou superior

- Android Gradle Plugin (AGP): 8.1.4
- Kotlin: 1.9.0
- Compile SDK: 34
- Min SDK: 24

## Estrutura do Projeto

```
MotoboyRecrutamento/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/motoboyrecrutamento/
│   │   │   ├── data/
│   │   │   │   ├── local/          # Room (Entities, DAOs, Database)
│   │   │   │   ├── remote/         # Retrofit (API Services)
│   │   │   │   └── repository/     # Repositories (Sincronização)
│   │   │   ├── domain/             # Modelos de domínio
│   │   │   ├── ui/
│   │   │   │   ├── login/          # Telas de autenticação (Membro 1)
│   │   │   │   ├── restaurante/    # Telas do Restaurante (Membro 2)
│   │   │   │   ├── motoboy/        # Telas do Motoboy (Membro 3)
│   │   │   │   ├── theme/          # Tema do app
│   │   │   │   └── MainActivity.kt # Activity principal
│   │   │   ├── viewmodel/          # ViewModels
│   │   │   └── utils/              # Utilitários
│   │   ├── res/                    # Recursos (strings, layouts, etc.)
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── GUIA_MEMBRO_1.md                # Guia detalhado para Membro 1
├── GUIA_MEMBRO_2.md                # Guia detalhado para Membro 2
├── GUIA_MEMBRO_3.md                # Guia detalhado para Membro 3
└── README.md                       # Este arquivo
```

## Divisão de Tarefas por Membro

### Membro 1: Infraestrutura e Autenticação

**Responsabilidades:**
- Configuração inicial do projeto
- Configuração do Firebase (Authentication e Storage)
- Implementação das telas de Login, Cadastro e Recuperação de Senha
- Configuração da navegação principal

**Requisitos Funcionais:**
- RF01: Cadastrar Usuário
- RF02: Logar no Sistema
- RF03: Recuperar Senha

**Guia:** [GUIA_MEMBRO_1.md](./GUIA_MEMBRO_1.md)

### Membro 2: Perfil Restaurante

**Responsabilidades:**
- Entidades e DAOs do Restaurante e Vagas (Room)
- Telas do perfil Restaurante
- Funcionalidade de Publicar Vaga
- Funcionalidade de Gerenciar Candidaturas

**Requisitos Funcionais:**
- RF04: Postar Vaga de Trabalho
- RF06: Gerenciar Candidaturas

**Guia:** [GUIA_MEMBRO_2.md](./GUIA_MEMBRO_2.md)

### Membro 3: Perfil Motoboy

**Responsabilidades:**
- Entidades e DAOs do Motoboy e Candidaturas (Room)
- Telas do perfil Motoboy
- Funcionalidade de Enviar Candidatura
- Funcionalidade de Anexar Documentos (Firebase Storage)

**Requisitos Funcionais:**
- RF05: Enviar Candidatura
- RF07: Anexar Documentos

**Guia:** [GUIA_MEMBRO_3.md](./GUIA_MEMBRO_3.md)

## Como Começar

### Pré-requisitos

1. **Android Studio Giraffe (2022.3.1)** ou superior instalado
2. **JDK 8** ou superior
3. **Conta no Firebase** (gratuita)
4. **Git** instalado para controle de versão

### Passo a Passo para Configuração Inicial

1. **Clone ou baixe este projeto:**
   ```bash
   git clone <URL_DO_REPOSITORIO>
   cd MotoboyRecrutamento
   ```

2. **Abra o projeto no Android Studio:**
   - File → Open
   - Selecione a pasta `MotoboyRecrutamento`
   - Aguarde a sincronização do Gradle

3. **Configure o Firebase:**
   - Siga as instruções no [GUIA_MEMBRO_1.md](./GUIA_MEMBRO_1.md) seção 1.2
   - Baixe o arquivo `google-services.json` e coloque em `app/`

4. **Sincronize o Gradle:**
   - Clique em "Sync Now" quando solicitado

5. **Execute o projeto:**
   - Conecte um dispositivo Android ou inicie um emulador
   - Clique em "Run" (▶️)

## Estratégia de Git e Commits

### Estrutura de Branches

```
main (ou master)
  └── develop
        ├── feature/membro1-autenticacao
        ├── feature/membro2-restaurante
        └── feature/membro3-motoboy
```

### Padrão de Mensagens de Commit

Use o formato: `<tipo>: <descrição concisa>`

**Tipos:**
- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Documentação
- `style`: Formatação de código
- `refactor`: Refatoração
- `chore`: Tarefas de manutenção (configuração, dependências)

**Exemplos:**
```bash
feat: Implementa tela de login com Firebase Auth
fix: Corrige erro de sincronização do Room
docs: Adiciona comentários na classe VagaRepository
chore: Atualiza versão do Retrofit para 2.9.0
```

### Workflow de Integração

1. Crie sua branch de feature a partir de `develop`
2. Faça commits frequentes e atômicos
3. Quando concluir uma tarefa, abra um Pull Request para `develop`
4. Outro membro revisa o código
5. Após aprovação, faça merge em `develop`
6. Apenas quando um marco for atingido, faça merge em `main`

## Requisitos Funcionais (RFs)

| ID | Perfil | Descrição | Tecnologia | Membro |
|---|---|---|---|---|
| RF01 | Geral | Cadastrar Usuário | Firebase Auth | 1 |
| RF02 | Geral | Logar no Sistema | Firebase Auth | 1 |
| RF03 | Geral | Recuperar Senha | Firebase Auth | 1 |
| RF04 | Restaurante | Postar Vaga de Trabalho | Retrofit (API) | 2 |
| RF05 | Motoboy | Enviar Candidatura | Retrofit (API) | 3 |
| RF06 | Restaurante | Gerenciar Candidaturas | Retrofit (API) | 2 |
| RF07 | Motoboy | Anexar Documentos | Firebase Storage | 3 |

## Fases de Desenvolvimento

### Fase 1: Configuração e Autenticação (Membro 1)
- Configuração do projeto
- Firebase Authentication
- Telas de Login, Cadastro e Recuperação
- Navegação principal

### Fase 2: Persistência Local e Rede (Todos)
- Configuração do Room (Entities, DAOs, Database)
- Configuração do Retrofit (API Services)
- Implementação dos Repositories

### Fase 3: Funcionalidades do Restaurante (Membro 2)
- Tela principal do Restaurante
- Publicar Vaga (RF04)
- Gerenciar Candidaturas (RF06)

### Fase 4: Funcionalidades do Motoboy (Membro 3)
- Tela principal do Motoboy
- Detalhes da Vaga e Enviar Candidatura (RF05)
- Perfil e Anexar Documentos (RF07)

## Observações Importantes

### API REST

O projeto está configurado para se comunicar com uma API REST. **A API ainda não existe**, então você tem três opções:

1. **Criar uma API mock local** usando JSON Server ou similar
2. **Usar dados mockados nos Repositories** temporariamente
3. **Desenvolver a API em paralelo** (se houver tempo e recursos)

**Base URL da API:**
- Definida em `data/remote/RetrofitClient.kt`
- Padrão: `https://api.exemplo.com/`
- Para emulador Android: `http://10.0.2.2:3000/` (localhost)

### Firebase

**Serviços utilizados:**
- **Authentication:** Login, cadastro e recuperação de senha
- **Storage:** Upload de documentos do Motoboy

**Configuração:**
- Arquivo `google-services.json` deve estar em `app/`
- Não faça commit deste arquivo (já está no `.gitignore`)

### Testes

Para testar sem a API real:

1. **Use dados mockados:**
   ```kotlin
   // Exemplo em VagaRepository.kt
   suspend fun syncVagasFromApi(): Result<Unit> {
       val vagasMock = listOf(/* ... */)
       vagaDao.insertAll(vagasMock)
       return Result.success(Unit)
   }
   ```

2. **Teste localmente:**
   - Cadastre usuários no Firebase
   - Teste a navegação entre telas
   - Teste o upload de documentos

## Checklist Final do Projeto

- [ ] Todas as 4 fases concluídas
- [ ] Todos os 7 Requisitos Funcionais implementados
- [ ] Firebase configurado e funcional
- [ ] Room configurado e funcional
- [ ] Retrofit configurado (mesmo sem API real)
- [ ] Navegação entre telas funcionando
- [ ] Commits seguindo o padrão
- [ ] Código revisado e testado
- [ ] Documentação atualizada

## Contato e Suporte

Se tiver dúvidas:
1. Consulte o guia específico do seu membro
2. Revise o código fornecido (há comentários explicativos)
3. Consulte a documentação oficial:
   - [Jetpack Compose](https://developer.android.com/jetpack/compose)
   - [Room](https://developer.android.com/training/data-storage/room)
   - [Retrofit](https://square.github.io/retrofit/)
   - [Firebase](https://firebase.google.com/docs/android/setup)

## Licença

Este é um projeto acadêmico para fins educacionais.

---

**Boa sorte no desenvolvimento! 🚀**
