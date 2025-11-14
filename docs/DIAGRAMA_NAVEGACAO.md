# 🗺️ Diagrama de Navegação do Aplicativo

## Mapa Completo de Fluxo de Telas

```
                           ┌──────────────────────────┐
                           │    LoginScreen           │
                           │  (Tela Inicial)          │
                           │                          │
                           │  • Email/Senha           │
                           │  • Botão Login           │
                           │  • Link Cadastro         │
                           │  • Link Esqueci Senha    │
                           └────────────┬─────────────┘
                                        │
                           ┌────────────┼────────────┐
                           │            │            │
                           ▼            ▼            ▼
                  ┌────────────┐ ┌─────────────┐ ┌──────────┐
                  │ Register   │ │ ForgotPass  │ │ (Login   │
                  │ Screen     │ │  Screen     │ │ Sucesso) │
                  │            │ │             │ │          │
                  │ • Nome     │ │ • Email     │ └────┬─────┘
                  │ • Email    │ │ • Enviar    │      │
                  │ • Senha    │ │ • Voltar    │      │
                  │ • Tipo     │ └─────────────┘      │
                  │ • Registrar│                      │
                  └────────────┘           ┌──────────┴──────────┐
                                           │  Firebase Auth      │
                                           │  Verifica tipo:     │
                                           │  motoboy/restaurante│
                                           └──────────┬──────────┘
                                                      │
                                   ┌──────────────────┴──────────────────┐
                                   │                                     │
                                   ▼                                     ▼
                      ┌────────────────────────┐          ┌────────────────────────┐
                      │ RestauranteHomeScreen  │          │  MotoboyHomeScreen     │
                      │  (Dashboard Empresa)   │          │  (Dashboard Motoboy)   │
                      │                        │          │                        │
                      │  📊 Estatísticas:      │          │  📋 Vagas Disponíveis: │
                      │    • Total Vagas       │          │    • Card Vaga 1       │
                      │    • Vagas Abertas     │          │    • Card Vaga 2       │
                      │    • Total Candidatos  │          │    • Card Vaga 3       │
                      │                        │          │                        │
                      │  🏠 Menu:              │          │  🏠 Menu:              │
                      │    • Nova Vaga         │          │    • Ver Vagas         │
                      │    • Minhas Vagas      │          │    • Meu Perfil        │
                      │    • Sair              │          │    • Sair              │
                      └────────────┬───────────┘          └────────────┬───────────┘
                                   │                                   │
                      ┌────────────┼────────────┐                     │
                      │            │            │                     │
                      ▼            ▼            ▼                     ▼
            ┌─────────────┐ ┌──────────────┐ ┌──────────────┐ ┌────────────────┐
            │ PostVaga    │ │  Gerenciar   │ │  Motoboy     │ │ VagaDetalhes   │
            │ Screen      │ │Candidaturas  │ │  Detalhes    │ │ Screen         │
            │             │ │  Screen      │ │  Screen      │ │                │
            │ • Título    │ │              │ │              │ │ • Título       │
            │ • Descrição │ │ Lista de     │ │ 👤 Perfil:   │ │ • Descrição    │
            │ • Salário   │ │ Candidatos:  │ │  • Nome      │ │ • Salário      │
            │ • Requisitos│ │              │ │  • Email     │ │ • Requisitos   │
            │             │ │ • Card 1 📝  │ │  • Telefone  │ │ • Status       │
            │ 📤 Publicar │ │ • Card 2 📝  │ │  • CNH       │ │                │
            └─────────────┘ │ • Card 3 📝  │ │  • Veículo   │ │ 📤 Candidatar  │
                            │              │ │  • Experiência│ └────────┬───────┘
                            │ Ações:       │ │              │          │
                            │ • Ver Perfil ├─┤ 📄 Docs:     │          │
                            │ • Aceitar    │ │  • CNH PDF   │          │
                            │ • Rejeitar   │ └──────────────┘          │
                            └──────────────┘                           │
                                                                        │
                                                                        ▼
                                                              ┌────────────────┐
                                                              │MotoboyPerfil   │
                                                              │Screen          │
                                                              │                │
                                                              │ 📝 Editar:     │
                                                              │  • Nome        │
                                                              │  • CNH         │
                                                              │  • Veículo     │
                                                              │  • Experiência │
                                                              │  • Raio (km)   │
                                                              │  • Telefone    │
                                                              │                │
                                                              │ 📤 Upload:     │
                                                              │  • Escolher CNH│
                                                              │  • Enviar Doc  │
                                                              │                │
                                                              │ 💾 Salvar      │
                                                              └────────────────┘
```

---

## 📋 Tabela de Rotas

### Rotas Públicas (Sem Autenticação)

| Rota | Arquivo | Descrição | Ações Disponíveis |
|------|---------|-----------|-------------------|
| `login` | `LoginScreen.kt` | Tela inicial de autenticação | • Login<br>• Ir para Registro<br>• Ir para Esqueci Senha |
| `register` | `RegisterScreen.kt` | Cadastro de novo usuário | • Escolher tipo (Motoboy/Restaurante)<br>• Preencher dados<br>• Registrar<br>• Voltar |
| `forgot_password` | `ForgotPasswordScreen.kt` | Recuperação de senha | • Inserir email<br>• Enviar email<br>• Voltar |

### Rotas Privadas - Motoboy

| Rota | Arquivo | Parâmetros | Descrição | Ações Disponíveis |
|------|---------|-----------|-----------|-------------------|
| `motoboy_home` | `MotoboyHomeScreen.kt` | - | Dashboard com vagas disponíveis | • Ver vagas<br>• Clicar em vaga<br>• Ir para perfil<br>• Sair |
| `vaga_detalhes/{vagaId}` | `VagaDetalhesScreen.kt` | `vagaId: Long` | Detalhes completos da vaga | • Ver informações<br>• Candidatar-se<br>• Voltar |
| `motoboy_perfil` | `MotoboyPerfilScreen.kt` | - | Edição de perfil + upload documentos | • Editar dados<br>• Upload CNH<br>• Salvar<br>• Voltar |

### Rotas Privadas - Restaurante

| Rota | Arquivo | Parâmetros | Descrição | Ações Disponíveis |
|------|---------|-----------|-----------|-------------------|
| `restaurante_home` | `RestauranteHomeScreen.kt` | - | Dashboard com estatísticas | • Ver estatísticas<br>• Publicar vaga<br>• Ver minhas vagas<br>• Sair |
| `post_vaga` | `PostVagaScreen.kt` | - | Publicação de nova vaga | • Preencher dados<br>• Adicionar requisitos<br>• Publicar<br>• Voltar |
| `gerenciar_candidaturas/{vagaId}` | `GerenciarCandidaturasScreen.kt` | `vagaId: Long` | Lista de candidatos por vaga | • Ver candidatos<br>• Clicar em candidato<br>• Aceitar/Rejeitar<br>• Voltar |
| `motoboy_detalhes/{motoboyId}` | `MotoboyDetalhesScreen.kt` | `motoboyId: Long` | Visualização de perfil do candidato | • Ver perfil completo<br>• Ver documentos<br>• Download docs<br>• Voltar |

---

## 🔐 Controle de Acesso

### Regras de Redirecionamento

```kotlin
// Após login bem-sucedido:
when (userType) {
    "motoboy" -> navigate("motoboy_home")
    "restaurante" -> navigate("restaurante_home")
    else -> navigate("login")
}
```

### Navegação Segura

- ✅ **BackStack Management**: Ao fazer login, as telas de autenticação são removidas do histórico
- ✅ **Deep Links**: Cada tela com parâmetros suporta navegação direta via ID
- ✅ **State Preservation**: ViewModels preservam estado durante rotação de tela
- ✅ **Logout**: Ao sair, usuário é redirecionado para `login` e stack é limpo

---

## 🎨 Transições e Animações

As transições entre telas utilizam as animações padrão do Navigation Compose:
- **Enter**: Slide da direita para esquerda
- **Exit**: Fade out
- **Pop Enter**: Slide da esquerda para direita
- **Pop Exit**: Fade out

---

## 📊 Fluxo de Dados

### Fluxo Motoboy - Candidatura

```
MotoboyHomeScreen → VagaDetalhesScreen → CandidaturaRepository
                                              ↓
                                        Firebase Firestore
                                              ↓
                                         Room Database
                                              ↓
                                    GerenciarCandidaturasScreen
                                    (Restaurante vê a candidatura)
```

### Fluxo Restaurante - Publicar Vaga

```
RestauranteHomeScreen → PostVagaScreen → VagaRepository
                                              ↓
                                        Firebase Firestore
                                              ↓
                                         Room Database
                                              ↓
                                       MotoboyHomeScreen
                                    (Motoboy vê a nova vaga)
```

---

## 📱 Implementação Técnica

### Navigation Host (MainActivity.kt)

```kotlin
NavHost(
    navController = navController,
    startDestination = getStartDestination()
) {
    // Rotas públicas
    composable("login") { LoginScreen(navController) }
    composable("register") { RegisterScreen(navController) }
    composable("forgot_password") { ForgotPasswordScreen(navController) }
    
    // Rotas Motoboy
    composable("motoboy_home") { MotoboyHomeScreen(navController) }
    composable("vaga_detalhes/{vagaId}") { backStackEntry ->
        val vagaId = backStackEntry.arguments?.getString("vagaId")?.toLongOrNull() ?: 0L
        VagaDetalhesScreen(navController, vagaId)
    }
    composable("motoboy_perfil") { MotoboyPerfilScreen(navController) }
    
    // Rotas Restaurante
    composable("restaurante_home") { RestauranteHomeScreen(navController) }
    composable("post_vaga") { PostVagaScreen(navController) }
    composable("gerenciar_candidaturas/{vagaId}") { backStackEntry ->
        val vagaId = backStackEntry.arguments?.getString("vagaId")?.toLongOrNull() ?: 0L
        GerenciarCandidaturasScreen(navController, vagaId)
    }
    composable("motoboy_detalhes/{motoboyId}") { backStackEntry ->
        val motoboyId = backStackEntry.arguments?.getString("motoboyId")?.toLongOrNull() ?: 0L
        MotoboyDetalhesScreen(navController, motoboyId)
    }
}
```

---

## 🔄 Estados de Navegação

### Loading States

Cada tela possui estados para indicar carregamento:
- **Idle**: Estado inicial
- **Loading**: Carregando dados
- **Success**: Dados carregados com sucesso
- **Error**: Erro ao carregar (com mensagem)

### Navigation Actions

```kotlin
// Navegação simples
navController.navigate("motoboy_home")

// Navegação com parâmetros
navController.navigate("vaga_detalhes/$vagaId")

// Navegação com pop
navController.navigate("login") {
    popUpTo("motoboy_home") { inclusive = true }
}

// Voltar
navController.popBackStack()
```

---

## 📌 Observações Importantes

1. **Single Activity**: Todo o app usa uma única MainActivity com Navigation Compose
2. **No Fragments**: 100% Jetpack Compose, sem XML ou Fragments
3. **Type-Safe**: Parâmetros de rota são validados e convertidos com segurança
4. **Recomposition**: Telas reagem automaticamente a mudanças de estado via StateFlow
5. **Memory Efficient**: ViewModels são scope-aware e liberados automaticamente
