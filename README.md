# ✝️ Ebenezer Missions — Backend API

<div align="center">

![Java](https://img.shields.io/badge/Java%2017-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot%203.2-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL%208.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

![Status](https://img.shields.io/badge/Status-🚀%20Em%20Desenvolvimento-0F6E56?style=for-the-badge)
![License](https://img.shields.io/badge/License-Proprietária-red?style=for-the-badge)

**API REST para conectar missionários cristãos com intercessores e apoiadores financeiros.**

*"Até aqui nos ajudou o Senhor"* — 1 Samuel 7:12

</div>

---

## 📖 Sobre o Projeto

O **Ebenezer Missions** é um portal full stack desenvolvido para resolver um problema real: missionários que partem para campos difíceis frequentemente enfrentam isolamento espiritual, falta de suporte financeiro e distância da comunidade.

Este repositório é o **backend** da plataforma — uma API REST construída com Java 17 e Spring Boot 3, com autenticação JWT stateless, refresh token server-side, controle de acesso por roles e notificações por e-mail.

> **Fase Atual:** Módulo de auth completo (registro, login, refresh, logout) e módulo de pedidos de oração com sistema de orações e encorajamentos implementados.

---

## ✨ Funcionalidades

### 🔐 Autenticação & Segurança
- ✅ Registro de usuários com validação e criptografia BCrypt
- ✅ Login com geração de **JWT Access Token** (1 hora)
- ✅ **Refresh Token** persistido no banco para renovação segura
- ✅ Logout com invalidação server-side do refresh token
- ✅ Controle de acesso por Roles (MISSIONARY, INTERCESSOR, SUPPORTER, ADMIN)
- ✅ Filtro JWT em todas as requisições protegidas

### 🙏 Pedidos de Oração
- ✅ CRUD completo de pedidos de oração (missionário)
- ✅ Listagem pública para intercessores
- ✅ Contador de orações por pedido
- ✅ Encorajamentos (mensagens de apoio)
- ✅ Controle de status (ACTIVE, ANSWERED, CLOSED)

### 👤 Perfis Públicos
- ✅ Listagem de missionários cadastrados
- ✅ Perfil público individual do missionário

### 🧠 Psicólogos Cristãos
- ✅ Listagem de psicólogos parceiros
- ✅ Busca por ID

### 📧 Notificações
- ✅ Envio de e-mail via Spring Mail (SMTP Gmail)

---

## 🏗️ Arquitetura e Estrutura

```
ebenezer-back/
└── src/main/java/com/ebenezer/
    ├── config/
    │   ├── SecurityConfig.java          🔒 Configuração Spring Security
    │   └── JwtProperties.java           ⚙️ @ConfigurationProperties JWT
    │
    ├── modules/
    │   ├── auth/                        🔐 Autenticação
    │   │   ├── model/RefreshToken.java
    │   │   ├── repository/
    │   │   ├── AuthController.java
    │   │   ├── AuthService.java
    │   │   ├── JwtUtil.java
    │   │   └── JwtAuthenticationFilter.java
    │   │
    │   ├── user/                        👤 Perfis de usuário
    │   │   ├── model/UserEntity.java
    │   │   ├── repository/
    │   │   ├── service/UserService.java
    │   │   └── MissionaryController.java
    │   │
    │   ├── prayer/                      🙏 Pedidos de oração
    │   │   ├── model/                   (PrayerRequest, Prayer, Encouragement)
    │   │   ├── repository/
    │   │   ├── service/
    │   │   └── PrayerRequestController.java
    │   │
    │   └── psychology/                  🧠 Psicólogos cristãos
    │       ├── PsychologistController.java
    │       └── PsychologyService.java
    │
    └── shared/
        ├── enums/                       (Role, PrayerRequestStatus)
        ├── exception/                   (GlobalExceptionHandler + tipos)
        └── email/EmailService.java
```

### 📊 Fluxo de Dados

```
HTTP Request
     ↓
JwtAuthenticationFilter (valida token, define SecurityContext)
     ↓
Controller (valida entrada — Bean Validation)
     ↓
Service (lógica de negócio)
     ↓
Repository (Spring Data JPA)
     ↓
MySQL Database
     ↓
Response JSON 200 / 400 / 401 / 403 / 404 / 422
```

---

## 🔌 Endpoints da API

### 🔑 Autenticação (`/auth`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/auth/register` | Registrar novo usuário | ❌ |
| POST | `/auth/login` | Login (retorna access + refresh token) | ❌ |
| POST | `/auth/refresh` | Renovar access token | ❌ |
| POST | `/auth/logout` | Invalidar refresh token | ✅ |

### 🙏 Pedidos de Oração (`/prayer-requests`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/prayer-requests` | Criar pedido | ✅ |
| GET | `/prayer-requests` | Listar todos (feed público) | ✅ |
| GET | `/prayer-requests/my` | Meus pedidos | ✅ |
| GET | `/prayer-requests/{id}` | Buscar por ID | ✅ |
| PUT | `/prayer-requests/{id}` | Atualizar pedido | ✅ |
| PATCH | `/prayer-requests/{id}/status` | Atualizar status | ✅ |
| DELETE | `/prayer-requests/{id}` | Remover pedido | ✅ |
| POST | `/prayer-requests/{id}/pray` | Registrar oração | ✅ |
| DELETE | `/prayer-requests/{id}/pray` | Remover oração | ✅ |
| POST | `/prayer-requests/{id}/encouragements` | Enviar encorajamento | ✅ |
| GET | `/prayer-requests/{id}/encouragements` | Listar encorajamentos | ✅ |

### 👤 Missionários (`/missionaries`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/missionaries` | Listar missionários | ❌ |
| GET | `/missionaries/{id}` | Perfil público | ❌ |

### 🧠 Psicólogos (`/psychologists`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/psychologists` | Listar psicólogos | ❌ |
| GET | `/psychologists/{id}` | Buscar por ID | ❌ |

---

## 🚀 Tecnologias

<div align="center">

| Tecnologia | Versão | Função |
|---|---|---|
| **Java** | 17 | Linguagem principal |
| **Spring Boot** | 3.2.5 | Framework Web/REST |
| **Spring Security** | 6.x | Autenticação e autorização |
| **Spring Data JPA** | 3.2 | ORM com Hibernate |
| **JWT (JJWT)** | 0.11.5 | Tokens stateless |
| **MySQL** | 8.0+ | Banco de dados relacional |
| **Spring Mail** | 3.2 | Envio de e-mails SMTP |
| **Lombok** | 1.18 | Redução de boilerplate |
| **Bean Validation** | 3.x | Validação de entrada |
| **Maven** | 3.9+ | Build e dependências |

</div>

---

## 📋 Checklist de Desenvolvimento

### ✅ Fase 1 — Auth & Base
- ✅ Setup do projeto (Spring Initializr)
- ✅ Configuração do banco MySQL
- ✅ Entidade `UserEntity` com roles
- ✅ Spring Security + JWT filter
- ✅ `AuthController` (register, login, refresh, logout)
- ✅ Refresh token server-side
- ✅ Tratamento global de exceções

### ✅ Fase 2 — Módulo de Oração
- ✅ CRUD de pedidos de oração
- ✅ Sistema de orações (pray/unpray)
- ✅ Encorajamentos
- ✅ Perfil público de missionários
- ✅ Módulo de psicólogos cristãos

### ⏳ Fase 3 — Módulo Financeiro
- ⏳ Integração Stripe
- ⏳ Campanhas de doação
- ⏳ Metas financeiras
- ⏳ Transparência de repasse

### ⏳ Fase 4 — Deploy
- ⏳ Deploy no Railway
- ⏳ Configuração de variáveis de ambiente
- ⏳ Testes em produção

---

## 💻 Como rodar localmente

### Pré-requisitos
- Java 17+
- MySQL 8.0 rodando em `localhost:3306`
- Maven 3.9+

### Configuração

1. **Clone o repositório:**
```bash
git clone https://github.com/claudiondev/ebenezer-missions.git
cd ebenezer-missions
```

2. **Crie o banco de dados:**
```sql
CREATE DATABASE ebenezer_db;
```

3. **Configure as variáveis de ambiente:**
```bash
export DB_USERNAME=root
export DB_PASSWORD=sua_senha
export JWT_SECRET=seu_secret_minimo_32_caracteres
```

4. **Rode a aplicação:**
```bash
./mvnw spring-boot:run
```

API disponível em `http://localhost:8080`

---

## 🔗 Frontend

O frontend do Ebenezer Missions é desenvolvido em **React 18 + Vite + Tailwind CSS** com globo 3D interativo e identidade visual própria.

📦 Repositório: [ebenezer-missions-front](https://github.com/claudiondev/ebenezer-missions-front)

---

## 👨‍💻 Autor

**Claudio Nascimento**

- 🔗 GitHub: [@claudiondev](https://github.com/claudiondev)
- 💼 LinkedIn: [Claudio Nascimento](https://linkedin.com/in/claudiondev)
- 📧 Email: claudioncruz152@gmail.com

---

## 📄 Licença

**LICENÇA PROPRIETÁRIA - VISUALIZAÇÃO APENAS**

Este código é um **projeto de portfólio** protegido por direitos autorais.

### ✅ Permitido:
- 👀 Visualizar e estudar o código
- 💼 Usar como referência em entrevistas
- 📚 Aprender com as implementações

### ❌ Proibido:
- 🚫 Copiar ou usar comercialmente sem autorização
- 🚫 Distribuir sem permissão do autor

```
Copyright © 2026 Claudio Nascimento. Todos os direitos reservados.
```

---

<div align="center">

### ⭐ Se este projeto te inspirou, deixe uma star! ⭐

**Desenvolvido com fé e código por Claudio Nascimento**

*"Ide por todo o mundo" — Mateus 28:19*

</div>
