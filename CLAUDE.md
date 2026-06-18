# CLAUDE.md

Este arquivo fornece orientações ao Claude Code (claude.ai/code) ao trabalhar com o código deste repositório.

## Projeto

Ebenezer Missions — portal web para conectar missionários cristãos com intercessores (oração) e apoiadores financeiros. Inspirado em 1 Samuel 7:12.

## Comandos

```bash
# Rodar a aplicação
./mvnw spring-boot:run

# Build
./mvnw clean package

# Rodar todos os testes
./mvnw test

# Rodar uma classe de teste específica
./mvnw test -Dtest=NomeDaClasse

# Rodar um método de teste específico
./mvnw test -Dtest=NomeDaClasse#nomeDoMetodo
```

## Variáveis de Ambiente

A aplicação roda sem nenhuma variável de ambiente configurada (todas têm valores padrão), exceto pela necessidade de um MySQL disponível. Os padrões assumem:
- MySQL em `localhost:3306`, banco `ebenezer_db`, usuário `root`, sem senha
- Secret JWT já definido com valor de desenvolvimento no `application.yml`
- E-mail desabilitado por padrão (sem username/password)

Para ambiente real, configure: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION`, `JWT_REFRESH_EXPIRATION`, `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `FRONTEND_URL`.

## Arquitetura

### Estrutura de pacotes

Estrutura híbrida: organizada por módulo no nível superior e por camada dentro de cada módulo.

```
com.ebenezer/
├── config/                  # SecurityConfig, JwtProperties (@ConfigurationProperties)
├── modules/
│   ├── auth/
│   │   ├── model/           # RefreshToken
│   │   ├── repository/      # RefreshTokenRepository
│   │   ├── JwtUtil.java
│   │   └── JwtAuthenticationFilter.java
│   ├── user/
│   │   ├── model/           # UserEntity
│   │   ├── repository/      # UserRepository
│   │   └── service/         # UserService (implementa UserDetailsService)
│   └── prayer/
│       ├── model/           # PrayerRequest, Prayer, Encouragement
│       └── repository/      # PrayerRequestRepository, PrayerRepository, EncouragementRepository
└── shared/
    ├── enums/               # Role (MISSIONARY, INTERCESSOR, SUPPORTER, ADMIN), PrayerRequestStatus
    └── exception/           # GlobalExceptionHandler, ApiError e exceções tipadas
```

Novos módulos (controller, dto, service) seguem o mesmo padrão de subpacotes dentro do módulo correspondente.

### Fluxo de autenticação

JWT stateless. O `JwtAuthenticationFilter` lê o header `Authorization: Bearer <token>` a cada requisição, valida via `JwtUtil` e define o `SecurityContext`. Sem sessões.

O `SecurityConfig` libera `/auth/**` e `GET /missionaries/**` publicamente. Tudo mais exige autenticação. Segurança em nível de método habilitada via `@EnableMethodSecurity`.

O `RefreshToken` é persistido no banco (one-to-one com `UserEntity`) para permitir invalidação server-side.

As configs do JWT (`secret`, `expiration`, `refresh-expiration`) são injetadas via `JwtProperties` com `@ConfigurationProperties(prefix = "application.jwt")`.

### Tratamento de exceções

O `GlobalExceptionHandler` (`@RestControllerAdvice`) mapeia exceções para status HTTP:
- `BusinessException` → 400
- `ResourceNotFoundException` → 404
- `UnauthorizedException` → 401
- `AccessDeniedException` → 403
- `MethodArgumentNotValidException` → 422 (une todos os erros de campo em uma mensagem)
- `Exception` → 500

Sempre lance uma dessas exceções tipadas nos services — nunca retorne strings de erro diretamente.

### Banco de dados

JPA com `ddl-auto: update` (somente dev). O Hibernate Auditing (`@EntityListeners(AuditingEntityListener.class)`) preenche os campos `createdAt`/`updatedAt` automaticamente via `@CreatedDate` e `@LastModifiedDate`. Não atribua esses campos manualmente.

O `UserEntity` usa `passwordHash` como nome de coluna — o `UserService.loadUserByUsername` faz o mapeamento para o `UserDetails` do Spring Security.

### Contexto do roadmap

- **v1 (atual):** Módulo de auth (JWT + roles), CRUD de pedidos de oração, perfil público do missionário, notificações por e-mail, contador de orações
- **v2:** Módulo financeiro (Stripe, campanhas, metas de doação)
- **v3:** Módulo comunidade (testemunhos, mapa de missionários, mensagens, devocional)
