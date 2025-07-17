🚀 Prática DevOps: Implantação de Aplicação Java com MySQL no Kubernetes (Minikube)
Este projeto demonstra a implantação de uma aplicação Java (dividida em dois microsserviços: app e logger) e um banco de dados MySQL em um cluster Kubernetes local, utilizando Minikube e Helm Charts.

🌟 Visão Geral da Aplicação
A aplicação é composta por três componentes principais, cada um conteinerizado e orquestrado pelo Kubernetes:

Aplicação Principal (app-service):

Desenvolvida em Java, responsável pela lógica de negócio central (ex: gerenciamento de pessoas).

Interage com o banco de dados MySQL para persistir e recuperar dados.

Exposta na raiz (/) da URL pública.

Serviço de Logs (logger-service):

Também desenvolvido em Java, dedicado a registrar e listar eventos (logs) da aplicação.

Persiste os dados de logs no banco de dados MySQL.

Exposto no caminho /logs da URL pública.

Banco de Dados MySQL (mysql-service):

Base de dados relacional utilizada por ambos os microsserviços (app-service e logger-service).

Configurado com persistência de dados para garantir a durabilidade das informações.

⚙️ Componentes e Artefatos Kubernetes
A implantação completa da aplicação no Kubernetes é orquestrada por um Helm Chart, que agrupa e gerencia os seguintes artefatos:

1. Deployments
Definem o estado desejado para os pods de cada componente, garantindo que o número especificado de réplicas esteja sempre em execução.

app-deployment:

Imagem Docker: cassax/praticadevops-app:latest (deve ser carregada localmente no Minikube).

Porta do Contêiner: 8080 (onde o servidor Tomcat/aplicação Java escuta).

Configuração de Ambiente: As credenciais e o host do banco de dados MySQL são injetados como variáveis de ambiente, obtidas de um Kubernetes Secret.

logger-service-deployment:

Imagem Docker: cassax/praticadevops-logger:latest (deve ser carregada localmente no Minikube).

Porta do Contêiner: 8080 (onde o servidor Tomcat/serviço de logs escuta).

Configuração de Ambiente: Similar ao app-deployment, obtém as credenciais do MySQL via Secret.

mysql-db-deployment:

Imagem Docker: cassax/mysql-db:latest (imagem customizada contendo o script init.sql para inicialização do banco).

Porta do Contêiner: 3306 (porta padrão do MySQL).

Configuração de Ambiente: MYSQL_ROOT_PASSWORD e MYSQL_DATABASE são injetadas via Secret, configurando o usuário root com a senha root e um banco de dados chamado root.

Volume Persistente: Utiliza um PersistentVolumeClaim (mysql-pvc) para garantir que os dados do banco de dados sejam mantidos mesmo se o pod for reiniciado ou recriado.

Probes de Saúde (livenessProbe, readinessProbe): Monitoram a disponibilidade do MySQL para garantir que ele esteja sempre pronto para aceitar conexões.

2. Services
Abstraem os pods, fornecendo um nome de rede estável e um IP interno para que outros componentes possam se comunicar com eles.

app-service:

Tipo: ClusterIP (acessível apenas internamente no cluster).

Porta: 8080 (mapeia para a targetPort 8080 dos pods do app-deployment).

logger-service:

Tipo: ClusterIP (acessível apenas internamente no cluster).

Porta: 8080 (mapeia para a targetPort 8080 dos pods do logger-service-deployment).

mysql-service:

Tipo: ClusterIP (acessível apenas internamente no cluster).

Porta: 3306 (mapeia para a targetPort 3306 do pod do mysql-db-deployment). Este é o nome de host (mysql-service) que as aplicações (app-service e logger-service) usam para se conectar ao banco de dados.

3. Ingress
Gerencia o acesso externo aos serviços no cluster, atuando como um roteador HTTP/HTTPS.

appservice-ingress:

Ingress Class: nginx (requer o NGINX Ingress Controller habilitado no Minikube).

Host: k8s.local (a URL pela qual a aplicação será acessada externamente).

Regras de Roteamento:

path: /logs (pathType: Exact): Roteia requisições exatas para http://k8s.local/logs para o logger-service na porta 8080.

path: / (pathType: Prefix): Roteia todas as outras requisições que não correspondem a /logs em http://k8s.local/ para o app-service na porta 8080.

4. PersistentVolumeClaim (PVC)
Solicita e provisiona armazenamento persistente para o banco de dados.

mysql-pvc:

Modo de Acesso: ReadWriteOnce (o volume pode ser montado como leitura/escrita por um único nó).

Armazenamento: 2Gi (solicita 2 Gigabytes de armazenamento).

5. Secret
Armazena informações sensíveis (como senhas de banco de dados) de forma segura, evitando que elas fiquem expostas diretamente nos arquivos de Deployment.

mysql-secrets:

Contém as chaves MYSQL_ROOT_PASSWORD e MYSQL_DATABASE, ambas com o valor root (codificado em Base64). As aplicações e o próprio MySQL utilizam esses valores para configuração.

🛠️ Pré-requisitos
Certifique-se de ter as seguintes ferramentas instaladas e configuradas em seu ambiente local:

Docker Desktop: Essencial para rodar o Minikube com o driver Docker (inclui WSL 2 no Windows).

Minikube: Ferramenta para criar e gerenciar um cluster Kubernetes local.

kubectl: Ferramenta de linha de comando para interagir com clusters Kubernetes.

Helm: Gerenciador de pacotes para Kubernetes, utilizado para implantar o Helm Chart da aplicação.

Chocolatey (Windows): Gerenciador de pacotes para Windows (opcional, mas recomendado para instalar minikube e kubectl).

🚀 Como Executar o Projeto
Siga os passos abaixo para implantar a aplicação no seu ambiente Minikube.

1. Clone o Repositório
Comece clonando o projeto para sua máquina local e navegando até o diretório principal:

git clone https://github.com/CassaX/PraticaDevops.git
cd PraticaDevops

2. Iniciar o Minikube e Configurar o Ambiente
Certifique-se de que o Docker Desktop está em execução antes de iniciar o Minikube.

# Inicia o Minikube utilizando o driver Docker
minikube start --driver=docker

# Habilita o addon Ingress, que é necessário para o roteamento de tráfego externo
minikube addons enable ingress

# Configura o ambiente Docker do seu terminal para o Minikube.
# Isso é essencial para que as imagens Docker construídas localmente sejam reconhecidas pelo Minikube.
eval $(minikube docker-env) # Para Linux/macOS
# No PowerShell (Windows): & minikube docker-env | Invoke-Expression

3. Construir e Carregar Imagens Docker
Como as imagens Docker da sua aplicação e do MySQL utilizam imagePullPolicy: Never, elas devem ser construídas localmente e carregadas no ambiente Docker do Minikube.

# Navegue até o diretório da sua aplicação principal (onde está o Dockerfile)
cd ./caminho/para/sua/aplicacao/app # Exemplo: ./app
docker build -t cassax/praticadevops-app:latest .
cd - # Volta para o diretório anterior (raiz do projeto)

# Navegue até o diretório do seu serviço de logs
cd ./caminho/para/seu/servico/logger # Exemplo: ./logger
docker build -t cassax/praticadevops-logger:latest .
cd - # Volta para o diretório anterior (raiz do projeto)

# Navegue até o diretório do seu Dockerfile do MySQL
cd ./caminho/para/seu/mysql/dockerfile # Exemplo: ./mysql-docker
docker build -t cassax/mysql-db:latest .
cd - # Volta para o diretório anterior (raiz do projeto)

Importante: Substitua caminho/para/sua/aplicacao/app, caminho/para/seu/servico/logger e caminho/para/seu/mysql/dockerfile pelos caminhos reais dos diretórios que contêm os Dockerfiles em seu repositório.

4. Implantar a Aplicação com Helm
Navegue até a raiz do seu projeto (onde está a pasta helm-chart) e implante a aplicação.

# Instala o Helm Chart, que irá provisionar todos os artefatos Kubernetes da aplicação
helm install praticadevops ./helm-chart # Assumindo que seu Helm Chart está na pasta 'helm-chart'

5. Configurar Acesso Externo (k8s.local)
Para acessar a aplicação via k8s.local no seu navegador, você precisa configurar o arquivo de hosts do seu sistema operacional.

Obtenha o IP do Minikube:

minikube ip

Anote o endereço IP retornado (ex: 192.168.49.2).

Edite o arquivo de hosts:

Windows: Abra o Bloco de Notas como Administrador. No Bloco de Notas, vá em Arquivo > Abrir..., navegue até C:\Windows\System32\drivers\etc\, mude o filtro para "Todos os Arquivos (.)" e abra o arquivo hosts.

Linux/macOS: Edite o arquivo /etc/hosts utilizando um editor de texto com permissões de superusuário (ex: sudo nano /etc/hosts).

Adicione a seguinte linha ao final do arquivo hosts:

<IP_DO_MINIKUBE> k8s.local

Substitua <IP_DO_MINIKUBE> pelo IP que você obteve no passo anterior.

Salve o arquivo. No Windows, certifique-se de que ele foi salvo sem a extensão .txt.

Limpe o cache DNS (apenas Windows):
Abra o Prompt de Comando como Administrador e execute:

ipconfig /flushdns

6. Verificar a Implantação
Utilize os seguintes comandos para monitorar o status dos componentes da sua aplicação no cluster:

# Verificar o status de todos os pods no namespace 'default'
kubectl get pods

# Verificar o status dos serviços
kubectl get svc

# Verificar o status do Ingress
kubectl get ingress

# Visualizar os logs da sua aplicação principal (substitua <NOME_DO_POD_DA_APLICACAO_PRINCIPAL>)
kubectl logs -f <NOME_DO_POD_DA_APLICACAO_PRINCIPAL>

# Visualizar os logs do seu serviço de logs (substitua <NOME_DO_POD_DO_LOGGER_SERVICE>)
kubectl logs -f <NOME_DO_POD_DO_LOGGER_SERVICE>

# Visualizar os logs do seu banco de dados MySQL (substitua <NOME_DO_POD_DO_MYSQL>)
kubectl logs -f <NOME_DO_POD_DO_MYSQL>

7. Acessar a Aplicação
Com tudo configurado e rodando, abra seu navegador e acesse as seguintes URLs:

Aplicação Principal: http://k8s.local/

Serviço de Logs: http://k8s.local/logs

🧹 Limpeza
Para remover a aplicação e liberar os recursos do cluster Minikube:

# Desinstala o Helm Chart, removendo todos os artefatos da aplicação
helm uninstall praticadevops

# Para e deleta o cluster Minikube, liberando os recursos da máquina virtual/contêiner
minikube stop
minikube delete

⚠️ Observações Importantes
imagePullPolicy: Never: As imagens Docker (cassax/praticadevops-app, cassax/praticadevops-logger, cassax/mysql-db) são configuradas com imagePullPolicy: Never. Isso significa que elas devem ser construídas localmente e carregadas no ambiente Docker do Minikube antes da implantação.

Credenciais MySQL: As credenciais do MySQL (root para usuário e senha, root para nome do banco de dados) são definidas no Secret mysql-secrets e devem ser consistentes com a configuração na sua aplicação Java.

Recursos do WSL 2 (Windows): Se você estiver usando Docker Desktop com backend WSL 2, certifique-se de que seu arquivo C:\Users\<SeuNomeDeUsuario>\.wslconfig está configurado com memória e CPU suficientes para o WSL 2 (e, consequentemente, para o Minikube). Exemplo:

[wsl2]
memory=4GB    # Ajuste conforme sua RAM total
processors=2  # Ajuste conforme seus núcleos de CPU
swap=2GB

Lembre-se de executar wsl --shutdown no PowerShell após qualquer alteração no .wslconfig para que as mudanças tenham efeito.