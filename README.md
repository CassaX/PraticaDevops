# PraticaDevops
Projeto utilizando docker com 3 conteiners

Pré-Requisitos:
Docker
Docker Compose

Para rodar:
Baixar repositório git
Entra no diretório e executar: Docker compose up --build

Acesso a aplicação:
http://localhost:8080/

Aplicação Principal:

Cadastro de pessoas com: nome, idade, CPF, telefone e email
Listagem e exclusão de pessoas
Cada ação registrada também é enviada ao serviço de logs

Serviço de Logs:
Registro de ações feitas no sistema
Listagem de logs com data/hora e descrição

Para finalizar:
Docker compose down