# 🏦 DevBank - Backend

![License](https://img.shields.io/static/v1?label=license&message=MIT&color=orange) &nbsp;
![Cargo version](https://img.shields.io/static/v1?label=cargo&message=v1.1.0&color=yellow) &nbsp;
![Pull request](https://img.shields.io/static/v1?label=PR&message=welcome&color=green)

## Indices

- [`Sobre o Projeto`](#sobre-o-projeto)
- [`Tecnologias Utilizadas`](#tecnologias-utilizadas)
- [`Estrutura do Projeto`](#estrutura-projeto)
- [`Configuração é Execução`](#configuracao-execucao)
- [`Principais Funcionalidades`](#principais-func)
- [`EndPoints`](#endpoints)
- [`Autenticação`](#autenticação)
- [`Diagrama do Banco de Dados`](#diagrama)
- [`Proposito`](#proposito)
- [`Contribuições`](#contribuicoes)
- [`Licença`](#license)

<span id="sobre-o-projeto"></span>

## 📌 Sobre o Projeto

**DEVBANK** é uma aplicação bancária fictícia que simula um banco digital moderno, com funcionalidades reais de contas, transferências, segurança e organização financeira. O backend é construído em Java com Spring Boot, seguindo as melhores práticas de arquitetura MVC, DTOs, segurança com JWT, validação e serviços desacoplados.

## 🚀 Tecnologias Utilizadas

<div align="center" id="tecnologias-utilizadas">
  <img  height='49' width='49' title='Dotenv' alt='dotenv' src='https://github.com/bush1D3v/navarro_blog_api/assets/133554156/de030e87-8f12-4b6b-8c75-071bab8526a5' /> &nbsp;
   <img  height='50' width='50' title='Cors' alt='cors' src='https://github.com/bush1D3v/navarro_blog_api/assets/133554156/5dcd815b-e815-453b-9f3f-71e7dbcdf71d' />
   <img  height='48' width='48' title='Bcrypt' alt='bcrypt' src='https://github.com/bush1D3v/navarro_blog_api/assets/133554156/8d9137f8-cd85-4629-be08-c639db52088d' /> &nbsp;
    <img  height='48' width='48'  title='Postman' alt='Postman' src='https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg' /> &nbsp;
    <imgheight='48' width='48'  title='Junit' alt='Junit' src='https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/junit/junit-original-wordmark.svg' /> &nbsp;
    <img  height='48' width='48'  title='Oauth' alt='Oauth' src='https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/oauth/oauth-original.svg' /> &nbsp;
   <img height='48' width='48'  title='Maven' alt='mave' src='https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/maven/maven-original.svg' /> &nbsp;
    <img height="49" width="49" title="Java" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" /> &nbsp; 
    <img height="49" width="49" title="Spring Boot" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" /> &nbsp; <img height="49" width="49" title="PostgreSQL" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" /> &nbsp; 
    <img height="49" width="49" title="Docker" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" /> &nbsp;
    <img height="49" width="49" title="JWT" src="https://img.icons8.com/?size=512&id=rHpveptSuwDz&format=png" /> &nbsp; 
    <img  height="49" width="49" title="Swagger"src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/swagger/swagger-original.svg" />&nbsp;
</div>

<span id="estrutura-projeto"></span>

## 📂 Estrutura do Projeto

```
backend-devbank/
│-- src/
│   ├── main/java/br/com/projeto/
│   │   ├── config/
│   │   ├── controle/
│   │   ├── dto/
│   │   ├── infra/
│   │   ├── middleware/
│   │   ├── models/
│   │   ├── repositorio/
│   │   ├── service/
│   │   ├── ultils/
│   ├── main/resources/
│   │   ├── application.properties
│-- Dockerfile
│-- .env
│-- README.md
```

<span id="configuracao-execucao"></span>

## 🛠️ Configuração e Execução

### 1️⃣ Clonar o Repositório

```bash
git clone https://github.com/wallacemt/devbank-backend
cd devbank-frontend
```

### 2️⃣ Instalar Dependências

```bash
./mvnw spring-boot:run
```

Ou, se estiver usando Java diretamente:

```bash
java -jar target/backend-devbank.jar
```

<span id="principais-func"></span>`

# 📌 Principais Funcionalidades

- `🔐 Autenticação segura com verificação em duas etapas (2FA) e sistema anti-brute-force.`

- `🧾 Sistema completo de transferências via Pix, incluindo visualização de histórico, comprovantes e logs.`

- `🖥️ TransferShell — um terminal interativo inspirado no Linux, onde o usuário envia comandos para fazer transações, visualizar logs e interagir com o banco como se estivesse no shell.`

- `💼 Caixinhas (Stash) — funcionalidade para criar containers de saldo, permitindo guardar valores com organização e metas financeiras.`

- `🧩 Fluxo completo de onboarding com stepper para registro e complemento de perfil.`

- `🧠 Interface responsiva e inteligente, construída com React, TailwindCSS, TypeScript e componentes ShadCN UI.`

- `📬 Sistema de email transacional com templates personalizados para validação de conta, alertas e confirmações.`

<span id="endpoints"></span>

## 📌 Endpoints Principais

A API está documentada no **Swagger** é **Postman**. Você pode importar a coleção utilizando o arquivo `critix-api.json` incluído no projeto ou acessar.

```bash
http://localhost:8081/swagger-ui/index.html
```

### 🔑 Autenticação

- `POST /auth/sign-in` - Realiza o login e retorna um token JWT
- `POST /auth/register` - Cria um novo usuário

<span id="autenticacao"></span>

## 🔒 Autenticação e Segurança

A API utiliza **JWT** para autenticação. Para acessar rotas protegidas, adicione o token no cabeçalho:

```json
Authorization: Bearer <seu_token>
```

<span id="proposito"></span>

<span id="database"></span>

## 🗂️ Banco de Dados

### Diagrama Do Banco:

<div align='center'>
   <img align='center' height='750'  style="border-radius:1.5rem"  title='DB Diagrama' alt='Diagrama' src='https://res.cloudinary.com/dg9hqvlas/image/upload/v1749687344/Untitled-cy_xomtjh.png' /> &nbsp;
</div>

## 💡 Propósito

O objetivo do **DEVBANK** é explorar um modelo de banco digital que não apenas simule operações reais, mas também engaje desenvolvedores de forma criativa, permitindo que realizem ações bancárias como se estivessem usando um terminal de código. Ao unir design moderno, segurança, interatividade e linguagem do universo tech, o projeto oferece uma proposta de valor diferenciada para usuários com perfil técnico.

<span id="contribuicoes"></span>

## 🛠 Contribuição

Ficou interessado em contribuir? Faça um **fork** do repositório, crie uma **branch**, implemente a melhoria e envie um **pull request**. Toda ajuda é bem-vinda!

1. **Fork the repository.**
2. **Clone your forked repository to your local machine.**
3. **Create a branch for your feature or fix:**

   ```bash
   git checkout -b my-new-feature
   ```

4. **Commit your changes:**

   ```bash
   git commit -m 'Add new feature'
   ```

5. **Push your changes to your fork:**

   ```bash
   git push origin my-new-feature
   ```

6. **Create a Pull Request.**

<span id="license"></span>

# 📜 Licença

`Este projeto está sob a licença MIT.`
