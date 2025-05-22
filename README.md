# PraticaDevops
Aplicação básica de cadastro de pessoas que utiliza Docker com 4 conteiners.

1 Conteiner. Aplicação principal

2 Conteiner. Aplicação secundaria que controla os logs do sistema

3 Conteiner. Banco de dados Mysql

4 Conteiner. Nginx para controle do fluxo entre os conteiners no frontend

Pré-Requisitos:
Docker
Docker Compose

Para rodar:
Baixar repositório git
Entra no diretório e executar: Docker compose up --build

Acesso a aplicação:
http://localhost

Aplicação Principal:

Cadastro de pessoas com: nome, idade, CPF, telefone e email
Listagem, adição e exclusão de pessoas
Cada ação é enviada uma mensagem para o contêiner do logger

Serviço de Logs:
Recebe as mensagens da aplicação principal e registra as ações feitas no sistema
Listagem de logs com data/hora e descrição

Para finalizar:
Docker compose down
