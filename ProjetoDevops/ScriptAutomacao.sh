#!/bin/bash

echo "--- Configurando ambiente Docker do Minikube ---"
eval $(minikube docker-env) || { echo "ERRO: Minikube não está rodando"; exit 1; }

echo "--- Verificando e ativando o Ingress no Minikube ---"
if ! minikube addons list | grep -q "ingress.*enabled"; then
    echo "Ativando o Ingress..."
    minikube addons enable ingress
    sleep 10 
else
    echo "Ingress já está ativado."
fi

echo "--- Construindo imagens no Minikube ---"
docker build -t cassax/mysql-db:latest ./mysql || { echo "ERRO: Build do MySQL falhou"; exit 1; }
docker build -t cassax/praticadevops-app:latest ./app || { echo "ERRO: Build do App falhou"; exit 1; }
docker build -t cassax/praticadevops-logger:latest ./logger || { echo "ERRO: Build do Logger falhou"; exit 1; }

echo "--- Imagens disponíveis no Minikube ---"
minikube image ls | grep cassax

echo "✅ Processo concluído! Use as imagens nos seus deployments Kubernetes."