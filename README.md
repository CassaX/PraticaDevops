# PraticaDevops
Aplicação básica de cadastro de pessoas que utiliza Docker com 4 conteiners.

1 Conteiner. Aplicação principal

2 Conteiner. Aplicação secundaria que controla os logs do sistema

3 Conteiner. Banco de dados Mysql

4 Conteiner. Nginx para controle do fluxo entre os conteiners no frontend

### Aplicação Principal CRUD de Pessoas:

Cadastro de pessoas com: nome, idade, CPF, telefone e email

Listagem, adição, edição e exclusão de pessoas com acesso ao banco

Cada ação é enviada uma mensagem para o contêiner do logger

### Aplicação Secundaria - Serviço de Logs:

Recebe as mensagens da aplicação principal e registra as ações do sistema no banco de dados.

Listagem de logs com data/hora e descrição

### Banco de dados Mysql
Conteiner para o banco de dados que possue o registro de pessoas e os logs do sistema.

### Nginx

Recebe as chamadas na porta 80 do frontend e redireciona caso necessário para outro conteiner.

## Pré-Requisitos:
Docker

Docker Compose

## Para rodar:
Baixar repositório git

Entra no diretório e executar: 
    
    Docker compose up --build

## Acesso a aplicação:

http://localhost

## Para finalizar:

Docker compose down
