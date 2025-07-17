Prática DevOps: Implantação de Aplicação Java com MySQL no Kubernetes (Minikube)
Este projeto demonstra a implantação de uma aplicação Java (dividida em dois microsserviços: app e logger) e um banco de dados MySQL em um cluster Kubernetes local utilizando Minikube e Helm Charts.

🚀 Visão Geral da Aplicação
A aplicação consiste em dois componentes principais, cada um conteinerizado e implantado como um microsserviço:

Aplicação Principal (app-service):

Responsável pela lógica de negócio principal (ex: gerenciamento de pessoas).

Interage com o banco de dados MySQL para persistir e recuperar dados.

Exposta na raiz (/) da URL pública.

Serviço de Logs (logger-service):

Responsável por registrar e listar eventos (logs).

Também interage com o banco de dados MySQL para persistir os logs.

Exposto no caminho /logs da URL pública.

Banco de Dados MySQL (mysql-service):

Armazena os dados da aplicação principal (tabela pessoa) e os logs (tabela logs).

Configurado com persistência de dados.

⚙️ Componentes e Artefatos Kubernetes
A implantação da aplicação no Kubernetes é gerenciada por diversos artefatos, organizados em um Helm Chart para automação.

1. Deployments
Definem como os pods de cada componente devem ser executados no cluster.

app-deployment:

Imagem Docker: cassax/praticadevops-app:latest (deve ser carregada localmente no Minikube).

Porta Exposta: 8080 (para o Tomcat/aplicação Java).

Variáveis de Ambiente: Configurações de conexão com o MySQL (host, porta, usuário, senha, nome do banco) são injetadas via variáveis de ambiente, obtidas de um Kubernetes Secret.

logger-service-deployment:

Imagem Docker: cassax/praticadevops-logger:latest (deve ser carregada localmente no Minikube).

Porta Exposta: 8080 (para o Tomcat/serviço de logs).

Variáveis de Ambiente: Similar ao app-deployment, obtém as credenciais do MySQL via Secret.

mysql-db-deployment:

Imagem Docker: cassax/mysql-db:latest (customizada, contendo o script init.sql).

Porta Exposta: 3306 (porta padrão do MySQL).

Variáveis de Ambiente: MYSQL_ROOT_PASSWORD e MYSQL_DATABASE são injetadas via Secret, configurando o usuário root com a senha root e um banco de dados chamado root.

Volume Persistente: Utiliza um PersistentVolumeClaim (mysql-pvc) para garantir que os dados do banco de dados não sejam perdidos se o pod for reiniciado ou recriado.

Probes de Saúde: Configurado com livenessProbe e readinessProbe para verificar a disponibilidade do MySQL.

2. Services
Permitem que os pods se comuniquem entre si e sejam acessíveis dentro do cluster.

app-service:

Tipo: ClusterIP (acessível apenas internamente no cluster).

Porta: 8080 (mapeia para a targetPort 8080 dos pods do app-deployment).

logger-service:

Tipo: ClusterIP (acessível apenas internamente no cluster).

Porta: 8080 (mapeia para a targetPort 8080 dos pods do logger-service-deployment).

mysql-service:

Tipo: ClusterIP (acessível apenas internamente no cluster).

Porta: 3306 (mapeia para a targetPort 3306 do pod do mysql-db-deployment). Este é o nome de host que as aplicações (app-service e logger-service) usam para se conectar ao banco de dados.

3. Ingress
Expõe os serviços da aplicação para o mundo externo através de uma URL amigável.

appservice-ingress:

Ingress Class: nginx (requer o NGINX Ingress Controller habilitado no Minikube).

Host: k8s.local (a URL pela qual a aplicação será acessada).

Regras de Roteamento:

path: /logs (com pathType: Exact): Roteia requisições para k8s.local/logs para o logger-service na porta 8080.

path: / (com pathType: Prefix): Roteia todas as outras requisições para k8s.local/ para o app-service na porta 8080.

4. PersistentVolumeClaim (PVC)
Solicita armazenamento persistente para o MySQL.

mysql-pvc:

Modo de Acesso: ReadWriteOnce (o volume pode ser montado como leitura/escrita por um único nó).

Armazenamento: 2Gi (solicita 2 Gigabytes de armazenamento).

5. Secret
Armazena informações sensíveis (como senhas de banco de dados) de forma segura.

mysql-secrets:

Contém as chaves MYSQL_ROOT_PASSWORD e MYSQL_DATABASE, ambas com o valor root (codificado em Base64). As aplicações e o próprio MySQL utilizam esses valores para configuração.

🛠️ Pré-requisitos
Antes de iniciar, certifique-se de ter as seguintes ferramentas instaladas e configuradas em seu ambiente:

Docker Desktop: Para rodar o Minikube com o driver Docker (inclui WSL 2 no Windows).

Minikube: Ferramenta para rodar um cluster Kubernetes localmente.

kubectl: Ferramenta de linha de comando para interagir com clusters Kubernetes.

Helm: Gerenciador de pacotes para Kubernetes, usado para implantar o Helm Chart da aplicação.

Chocolatey (Windows): Gerenciador de pacotes para Windows (opcional, mas recomendado para instalar minikube e kubectl).

🚀 Como Executar o Projeto
Siga os passos abaixo para implantar a aplicação no seu Minikube.

1. Clone o Repositório
git clone https://github.com/CassaX/PraticaDevops.git
cd PraticaDevops

2. Iniciar o Minikube e Configurar o Ambiente
Certifique-se de que o Docker Desktop está rodando.

# Inicia o Minikube com o driver Docker
minikube start --driver=docker

# Habilita o addon Ingress (necessário para o roteamento externo)
minikube addons enable ingress

# Configura o ambiente Docker para o Minikube (para carregar imagens locais)
eval $(minikube docker-env) # Para Linux/macOS
# No PowerShell (Windows): & minikube docker-env | Invoke-Expression

3. Construir e Carregar Imagens Docker
As imagens da sua aplicação e do MySQL são customizadas e têm imagePullPolicy: Never, o que significa que elas precisam ser construídas localmente e carregadas no ambiente Docker do Minikube.

# Navegue até o diretório da sua aplicação principal (onde está o Dockerfile)
cd ./caminho/para/sua/aplicacao/app # Ex: ./app
docker build -t cassax/praticadevops-app:latest .
cd .. # Volte para a raiz do projeto

# Navegue até o diretório do seu serviço de logs
cd ./caminho/para/seu/servico/logger # Ex: ./logger
docker build -t cassax/praticadevops-logger:latest .
cd ..

# Navegue até o diretório do seu Dockerfile do MySQL
cd ./caminho/para/seu/mysql/dockerfile # Ex: ./mysql-docker
docker build -t cassax/mysql-db:latest .
cd ..

Substitua caminho/para/sua/aplicacao/app, caminho/para/seu/servico/logger e caminho/para/seu/mysql/dockerfile pelos caminhos reais no seu repositório.

4. Implantar a Aplicação com Helm
Navegue até a raiz do seu projeto (onde está a pasta helm-chart).

# Instale o Helm Chart
helm install praticadevops ./helm-chart # Assumindo que seu Helm Chart está na pasta 'helm-chart'

5. Configurar Acesso Externo (k8s.local)
Para acessar a aplicação via k8s.local, você precisa configurar seu arquivo de hosts.

Obtenha o IP do Minikube:

minikube ip

Anote o IP retornado (ex: 192.168.49.2).

Edite o arquivo de hosts:

Windows: Abra o Bloco de Notas como Administrador. Vá em Arquivo > Abrir..., navegue até C:\Windows\System32\drivers\etc\, mude o filtro para "Todos os Arquivos (.)" e abra o arquivo hosts.

Linux/macOS: Edite o arquivo /etc/hosts com permissões de superusuário (ex: sudo nano /etc/hosts).

Adicione a seguinte linha ao final do arquivo hosts:

<IP_DO_MINIKUBE> k8s.local

Substitua <IP_DO_MINIKUBE> pelo IP que você obteve no passo anterior.

Salve o arquivo. No Windows, certifique-se de que ele foi salvo sem extensão .txt.

Limpe o cache DNS (Windows):
Abra o Prompt de Comando como Administrador e execute:

ipconfig /flushdns

6. Verificar a Implantação
Use os seguintes comandos para verificar o status dos seus componentes:

# Verificar todos os pods no namespace default
kubectl get pods

# Verificar os serviços
kubectl get svc

# Verificar o Ingress
kubectl get ingress

# Verificar os logs da sua aplicação principal (substitua o nome do pod)
kubectl logs -f <NOME_DO_POD_DA_APLICACAO_PRINCIPAL>

# Verificar os logs do seu serviço de logs (substitua o nome do pod)
kubectl logs -f <NOME_DO_POD_DO_LOGGER_SERVICE>

# Verificar os logs do seu banco de dados MySQL (substitua o nome do pod)
kubectl logs -f <NOME_DO_POD_DO_MYSQL>

7. Acessar a Aplicação
Abra seu navegador e acesse as seguintes URLs:

Aplicação Principal: http://k8s.local/

Serviço de Logs: http://k8s.local/logs

🧹 Limpeza
Para remover a aplicação e o cluster Minikube:

# Desinstalar o Helm Chart
helm uninstall praticadevops

# Parar e deletar o cluster Minikube
minikube stop
minikube delete

⚠️ Observações Importantes
imagePullPolicy: Never: As imagens Docker (cassax/praticadevops-app, cassax/praticadevops-logger, cassax/mysql-db) devem ser construídas localmente e carregadas no ambiente Docker do Minikube antes da implantação.

Credenciais MySQL: As credenciais do MySQL (root para usuário e senha, root para nome do banco) são definidas no mysql-secrets e devem ser consistentes com a configuração na sua aplicação Java.

Recursos do WSL 2 (Windows): Se você estiver usando Docker Desktop com backend WSL 2, certifique-se de que seu arquivo C:\Users\<SeuNomeDeUsuario>\.wslconfig está configurado com memória e CPU suficientes para o WSL 2 (e, consequentemente, para o Minikube). Exemplo:

[wsl2]
memory=4GB    # Ajuste conforme sua RAM total
processors=2  # Ajuste conforme seus núcleos de CPU
swap=2GB

Lembre-se de executar wsl --shutdown no PowerShell após qualquer alteração no .wslconfig.