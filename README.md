Projeto de Bloco Engenharia Disciplinada de Software(TP5)


-Status e Monitoramento

[![Pipeline Status](https://github.com/AndersonDominguesRS/Projeto_Bloco_Endenharia_Disciplinada_TP5/actions/workflows/gradle.yml/badge.svg)](https://github.com/AndersonDominguesRS/Projeto_Bloco_Endenharia_Disciplinada_TP5/actions)
![Java Version](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![Security Scan](https://img.shields.io/badge/SAST-Checkstyle%20Passed-blue)
![Coverage](https://img.shields.io/badge/Coverage-90%25-green)



Sobre Polimorfismo e Classes Abstratas;

A classe base Produto foi refatorada para ser abstrata. Implementado o polimorfismo através de duas especializações:
- ProdutoPadrao: Implementa o preço de venda sem descontos.
- ProdutoPromocional: Implementa o cálculo de preço com 10% de desconto dinâmico via o método gtPrecoVenda().

Sobre Pipeline CI/CD (GitHub Actions)

O fluxo de automação foi configurado para garantir a entrega contínua:
- Análise Estática (SAST); Uso do plugin Checkstyle para garantir padrões de codificação e segurança.
- Testes & Cobertura: Configuração do Jacoco com trava de segurança de 90% de cobertura mínima.
- Gestão de Artefatos: Upload automático do arquivo .jar gerado em cada build bem-sucedido.


Sobre Deploy Seguro;

- Aprovação Manual: O job de Deploy para o ambiente de Production exige uma revisão manual (Environment Protection).
- Segurança:Uso de GitHub Secrets e simulação de OIDC para proteger tokens de acesso.


Como rodar a aplicação;

Pré-requisitos;

- Java 21
- Gradle 8.5

Rodar Localmente
bash
- Navegue até a pasta do projeto: cd Sprint-RestAPI-TP5
- Execute o build e testes: ./gradlew build
- Inicie a aplicação: ./gradlew bootRun