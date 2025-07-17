# Prática DevOps: Implantação de Aplicação Java com MySQL no Kubernetes (Minikube)
Este projeto demonstra a implantação de uma aplicação Java (dividida em dois microsserviços: app e logger) e um banco de dados MySQL em um cluster Kubernetes local, utilizando Minikube e Helm Charts.

# Visão Geral da Aplicação
A aplicação é composta por três componentes principais, cada um conteinerizado e orquestrado pelo Kubernetes:

## Aplicação Principal (app-service):

Desenvolvida em Java, responsável pela lógica de gerenciamento de pessoas.

Interage com o banco de dados MySQL para persistir e recuperar dados.

Exposta na raiz (/).

## Serviço de Logs (logger-service):

Também desenvolvido em Java, dedicado a registrar e listar eventos (logs) da aplicação.

Persiste os dados de logs no banco de dados MySQL.

Exposto no caminho /logs da URL pública.

## Banco de Dados MySQL (mysql-service):

Base de dados relacional utilizada por ambos os microsserviços (app-service e logger-service).

Configurado com persistência de dados para garantir a durabilidade das informações.

# Componentes e Artefatos Kubernetes
A implantação completa da aplicação no Kubernetes é orquestrada por um Helm Chart, que agrupa e gerencia os seguintes artefatos:

1. Deployments
Definem o estado desejado para os pods de cada componente, garantindo que o número especificado de réplicas esteja sempre em execução.

## app-deployment:

Imagem Docker: cassax/praticadevops-app:latest (deve ser carregada localmente no Minikube).

Porta do Contêiner: 8080 (onde o servidor Tomcat/aplicação Java escuta).

Configuração de Ambiente: As credenciais e o host do banco de dados MySQL são injetados como variáveis de ambiente, obtidas de um Kubernetes Secret.

## logger-service-deployment:

Imagem Docker: cassax/praticadevops-logger:latest (deve ser carregada localmente no Minikube).

Porta do Contêiner: 8080 (onde o servidor Tomcat/serviço de logs escuta).

Configuração de Ambiente: Similar ao app-deployment, obtém as credenciais do MySQL via Secret.

## mysql-db-deployment:

Imagem Docker: cassax/mysql-db:latest (imagem customizada contendo o script init.sql para inicialização do banco).

Porta do Contêiner: 3306 (porta padrão do MySQL).

Configuração de Ambiente: MYSQL_ROOT_PASSWORD e MYSQL_DATABASE são injetadas via Secret, configurando o usuário root com a senha root e um banco de dados chamado root.

Volume Persistente: Utiliza um PersistentVolumeClaim (mysql-pvc) para garantir que os dados do banco de dados sejam mantidos mesmo se o pod for reiniciado ou recriado.

Probes de Saúde (livenessProbe, readinessProbe): Monitoram a disponibilidade do MySQL para garantir que ele esteja sempre pronto para aceitar conexões.

2. Services
Abstraem os pods, fornecendo um nome de rede estável e um IP interno para que outros componentes possam se comunicar com eles.

## app-service:

Tipo: ClusterIP (acessível apenas internamente no cluster).

Porta: 8080 (mapeia para a targetPort 8080 dos pods do app-deployment).

## logger-service:

Tipo: ClusterIP (acessível apenas internamente no cluster).

Porta: 8080 (mapeia para a targetPort 8080 dos pods do logger-service-deployment).

## mysql-service:

Tipo: ClusterIP (acessível apenas internamente no cluster).

Porta: 3306 (mapeia para a targetPort 3306 do pod do mysql-db-deployment). Este é o nome de host (mysql-service) que as aplicações (app-service e logger-service) usam para se conectar ao banco de dados.

3. Ingress
Gerencia o acesso externo aos serviços no cluster, atuando como um roteador HTTP/HTTPS.

## appservice-ingress:

Ingress Class: nginx (requer o NGINX Ingress Controller habilitado no Minikube).

Host: k8s.local (a URL pela qual a aplicação será acessada externamente).

## Regras de Roteamento:

path: /logs (pathType: Exact): Roteia requisições exatas para http://k8s.local/logs para o logger-service na porta 8080.

path: / (pathType: Prefix): Roteia todas as outras requisições que não correspondem a /logs em http://k8s.local/ para o app-service na porta 8080.

4. PersistentVolumeClaim (PVC)
Solicita e provisiona armazenamento persistente para o banco de dados.

## mysql-pvc:

Modo de Acesso: ReadWriteOnce (o volume pode ser montado como leitura/escrita por um único nó).

Armazenamento: 2Gi (solicita 2 Gigabytes de armazenamento).

5. Secret
Armazena informações sensíveis (como senhas de banco de dados) de forma segura, evitando que elas fiquem expostas diretamente nos arquivos de Deployment.

## mysql-secrets:

Contém as chaves MYSQL_ROOT_PASSWORD e MYSQL_DATABASE, ambas com o valor root (codificado em Base64). As aplicações e o próprio MySQL utilizam esses valores para configuração.

# Pré-requisitos
Certifique-se de ter as seguintes ferramentas instaladas e configuradas em seu ambiente local:

Docker : Essencial para rodar o Minikube com o driver Docker (inclui WSL 2 + Docker Desktop no Windows).

Minikube: Ferramenta para criar e gerenciar um cluster Kubernetes local.

kubectl: Ferramenta de linha de comando para interagir com clusters Kubernetes.

Helm: Gerenciador de pacotes para Kubernetes, utilizado para implantar o Helm Chart da aplicação.


# Como Executar o Projeto
Siga os passos abaixo para implantar a aplicação no seu ambiente Minikube.

1. Clone o Repositório
Comece clonando o projeto para sua máquina local e navegando até o diretório principal:

git clone https://github.com/CassaX/PraticaDevops.git
cd PraticaDevops

2. Inicia o Minikube utilizando o driver Docker
minikube start --driver=docker

3. Utilizar o script de automação
Executar ./ScriptAutomacao

4. Configurar Acesso Externo (k8s.local)
Para acessar a aplicação via k8s.local no seu navegador, você precisa configurar o arquivo de hosts do seu sistema operacional.

5. Acessar aplicação via k8s.local

