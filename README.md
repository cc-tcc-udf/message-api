# Message - API

## Descrição

API RESTful corporativa de mensageria e notificações push que atende ao ecossistema do **Campus Connect**:
- 💻 **Frontend Web:** [Campus Connect Web App (Angular/TypeScript)](https://github.com/cc-tcc-udf/message-app)
- 📱 **Aplicativo Móvel:** [Campus Connect Mobile (Flutter/Dart)](https://github.com/cc-tcc-udf/message-mobile)

## Requisitos

> - [Git](https://git-scm.com)
> - [JAVA](https://www.oracle.com/br/java/technologies/downloads/)
> - [Docker](https://www.docker.com/products/docker-desktop)

## Optional

> - [Github Desktop](https://desktop.github.com/)
> - [Postman](https://www.postman.com/) cliente de testes dos endpoint
> - Além disto é bom ter um editor para trabalhar com o código como [IntelliJ](https://www.jetbrains.com/pt-br/idea/)

## 🛠 Tecnologias

As seguintes ferramentas foram usadas na construção do projeto:

![SpringBoot](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Intellij](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Git](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)

### ● [JAVA 21](https://www.java.com) ●  [SPRING BOOT](https://spring.io/projects/spring-boot) ● [MAVEN](https://maven.apache.org/) ● [DOCKER](https://www.docker.com/) ● [GIT](https://git-scm.com/)  ● [POSTGRES](https://www.postgresql.org/)

## 🎲 Rodando a aplicação

```bash
# Clone este repositório
$ git clone <url> 

# Acesse a pasta do projeto no terminal/cmd
$ cd message-api

# Construa a imagem Docker
$ docker compose up -d

#Inicie a aplicação
$ mvn spring-boot:run ou Run pela IDE
# O servidor iniciará na porta:8180 - acesse <http://localhost:8180>
```

## 🗯️ informações auxiliares

SWAGGER
> http://localhost:8180/swagger-ui/index.html


POSTMAN
> [COMO TESTAR A API COM O POSTMAN](https://medium.com/trainingcenter/testando-sua-api-com-o-postman-9a927ddc2f93)