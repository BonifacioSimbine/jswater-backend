# JsWater

Sistema de gestão e facturação de água, desenvolvido em Java 17 com Spring Boot.

Inclui:
- Autenticação JWT com controlo de acesso por roles (ADMIN/USER)
- Gestão de clientes, contadores (meters) e leituras
- Emissão de facturas com tarifas por período e controlo de estado (OPEN/PAID)
- Regras de negócio para activação/desactivação de clientes
- Relatórios de facturação mensal, top devedores e dívida por zona
- Endpoint de **dashboard** com KPIs gerais para backoffice

## Stack backend

- **Linguagem:** Java 17
- **Framework:** Spring Boot (Web, Security, Data JPA)
- **Base de dados:** PostgreSQL
- **Build:** Maven (`mvnw` / `mvnw.cmd`)
- **Segurança:** JWT, blacklist de tokens, rate limiting de login
- **Documentação:** OpenAPI/Swagger via anotações `@Operation`, `@ApiResponses`, `@Tag`

## Requisitos

- Java 17 instalado
- PostgreSQL
- Maven Wrapper incluído no projecto (não precisa de Maven global)

## Configuração

1. Crie uma base de dados PostgreSQL, por exemplo `jswater`.
2. Defina as credenciais por variáveis de ambiente. Os arquivos YAML versionados usam apenas placeholders e não devem conter segredos reais.
3. Crie um arquivo `.env.example` como referência e use um arquivo local `.env` apenas na sua máquina. O `.env` e variantes como `.env.local` estão ignorados pelo Git.
4. Exporte as variáveis no terminal antes de arrancar a aplicação, ou configure-as na sua IDE.

Exemplo no PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5433/jswater"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="coloque-aqui-a-sua-password"
$env:JWT_SECRET="coloque-aqui-um-segredo-com-pelo-menos-32-caracteres"
```

Exemplo no CMD:

```bat
set DB_URL=jdbc:postgresql://localhost:5433/jswater
set DB_USERNAME=postgres
set DB_PASSWORD=coloque-aqui-a-sua-password
set JWT_SECRET=coloque-aqui-um-segredo-com-pelo-menos-32-caracteres
```

5. Confirme a porta HTTP em cada arquivo YAML conforme o ambiente.

## Executar em desenvolvimento

Na raiz do projecto:

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

Se quiser activar explicitamente o perfil de desenvolvimento:

```bash
# Windows
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# Linux/macOS
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

A API ficará disponível em `http://localhost:8080`.

Durante o desenvolvimento, existe CORS configurado para um frontend em `http://localhost:4300`.

## Testes

Para correr os testes automatizados:

```bash
# Windows
mvnw.cmd test

# Linux/macOS
./mvnw test
```

## Principais módulos funcionais

- **Auth / Utilizadores**
  - Login com JWT
  - Registo de utilizadores (role USER por omissão)
  - Logout com revogação de token
  - Reset de password (pelo próprio ou por ADMIN)
  - Activar/desactivar utilizadores

- **Clientes**
  - CRUD de clientes
  - Activar/desactivar cliente (representa corte/reativação de água)
  - Clientes inactivos não podem receber novas leituras nem novas facturas

- **Contadores e Leituras**
  - Associação de contador ao cliente
  - Registo de leituras
  - Bloqueio de leituras para clientes inactivos

- **Tarifas**
  - Tarifas por período
  - Histórico de tarifas (nova tarifa encerra a anterior)

- **Facturas**
  - Geração de facturas com base em leituras e tarifas
  - Estados: OPEN, PAID
  - Pagamento de factura

- **Relatórios e Dashboard**
  - Relatório mensal de facturação (emitido, pago, em aberto)
  - Top devedores (lista de clientes com maior dívida)
  - Dívida por zona/bairro (com contagem de clientes por zona)
  - Dashboard geral com KPIs: totais de clientes, facturas, dívida aberta, etc.

## Endpoints de alto nível

Alguns endpoints principais (detalhe completo via Swagger/OpenAPI):

- `/api/auth/login` – autenticação e obtenção de JWT
- `/api/auth/register` – registo de utilizador
- `/api/auth/logout` – logout e revogação de token
- `/api/auth/reset-password` – alterar a própria password
- `/api/auth/users` – listar utilizadores (ADMIN)
- `/api/clients` – gestão de clientes
- `/api/clients/{id}/status` – activar/desactivar cliente (ADMIN)
- `/api/invoices` – gestão de facturas
- `/api/invoices/{id}/pay` – pagar factura (ADMIN)
- `/api/invoices/reports/monthly` – relatório mensal de facturação (ADMIN)
- `/api/invoices/reports/top-debtors` – top devedores (ADMIN)
- `/api/invoices/reports/zone-debt` – dívida por zona (ADMIN)
- `/api/invoices/reports/zone-debt/monthly` – dívida por zona num mês (ADMIN)
- `/api/dashboard` – dashboard geral (ADMIN)

## Próximos passos

- Implementar frontend (por exemplo, Angular) para backoffice/admin
- Afinar textos e labels conforme feedback dos utilizadores
- Adicionar mais testes de integração para cenários críticos de negócio
