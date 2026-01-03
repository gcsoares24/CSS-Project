# CSS-Project: Engenharia de Sistemas de Software

Nota rápida em inglês: You can check this same README in English by visiting [here](README.md).

## Descrição

**CSS-Project** é um projeto académico desenvolvido no âmbito da unidade curricular de *Construção de Sistemas de Software*. Este projeto destaca-se pelo desenvolvimento de um sistema de software modular, com foco na qualidade, eficiência e integração. Inclui dois módulos principais—**UrbanWheels**, um sistema de partilha de bicicletas, e **WeatherWise**, um serviço de dados meteorológicos—integrados para melhorar a funcionalidade.

Este projeto utiliza tecnologias como Java, JavaFX, Spring Boot e REST APIs, cumprindo as melhores práticas de engenharia de software, como modularidade, arquitetura em camadas e contentorização com Docker.

---

## Visão Geral dos Módulos

### 1. **UrbanWheels**
**UrbanWheels** é um sistema de partilha de bicicletas concebido para gerir estações, bicicletas e operações dos utilizadores. Implementa interfaces gráficas para administradores e utilizadores finais, com integração ao **WeatherWise** para fornecer informações meteorológicas.

#### Funcionalidades:
- **Interface para Administradores** (Web via Thymeleaf):
  - Registar e listar estações e bicicletas.
  - Gerir manutenção e disponibilidade de bicicletas.
  - Registar e gerir utilizadores.

- **Interface para Utilizadores** (Desktop via JavaFX):
  - Pesquisar e reservar bicicletas.
  - Gerir reservas e histórico de utilização.

- **Integração com WeatherWise**:
  - Recuperar dados meteorológicos em tempo real durante reservas de bicicletas para uma decisão mais informada.

#### Casos de Uso:
- Reservas são geridas de forma eficiente através da atualização do estado das bicicletas (`Reservada`, `Disponível`, etc.).
- Dados meteorológicos melhoram a tomada de decisões operacionais.

---

### 2. **WeatherWise**
**WeatherWise** é um sistema independente de previsão meteorológica. Inicialmente desenvolvido para operar de forma autónoma, foi mais tarde integrado no sistema **UrbanWheels** para fornecer informações baseadas em dados meteorológicos.

#### Funcionalidades:
- Previsão meteorológica em tempo real com base na localização.
- Armazenamento de dados meteorológicos históricos para análises e previsões.
- REST API para integrações externas.

#### Casos de Uso:
- Utilizado pelo UrbanWheels para obter dinamicamente dados meteorológicos para rotas e reservas.
- Permite estimar condições meteorológicas futuras com base em dados históricos.

---

## Tecnologias Utilizadas
1. **Java**:
   - Linguagem principal utilizada para a lógica back-end.
   - Foco no design em camadas e estrutura de código manutenível.

2. **JavaFX e Thymeleaf**:
   - JavaFX: GUI de desktop para melhorar a experiência do utilizador.
   - Thymeleaf: Interface web para tarefas administrativas.

3. **Spring Boot**:
   - Implementação da REST API em UrbanWheels e integração com WeatherWise.
   - Garante escalabilidade e robustez.

4. **Docker**:
   - Simplifica o deployment em vários ambientes.
   - Garante consistência através de contentorização.

5. **PostgreSQL**:
   - Base de dados utilizada para armazenar e consultar dados no WeatherWise.

---

## Arquitetura do Sistema
O sistema foi desenhado utilizando uma abordagem em camadas:
1. **Camada de Controlador**: Gere interações com o utilizador e pedidos API.
2. **Camada de Serviço**: Contém a lógica principal de negócio.
3. **Camada de Repositório**: Gere a persistência de dados.

A arquitetura baseada em microserviços facilita a escalabilidade, com pontos de integração através de REST APIs entre os módulos.

---

## Como Configurar e Executar

### Pré-requisitos:
1. **Java Development Kit (JDK)** versão 17 ou superior.
2. **Docker** e **Docker Compose** instalados localmente.
3. Base de dados **PostgreSQL** configurada para WeatherWise, se for executado sem Docker.

---

### Instalação:
1. **Clonar o repositório**:
   ```bash
   git clone https://github.com/guimbreon/CSS-Project.git
   cd CSS-Project
   ```

2. **Executar com Docker**:
   - Navegue para o diretório `weatherwise`:
     ```bash
     cd weatherwise
     docker-compose up --build
     ```
   - Depois, navegue para o diretório `urbanwheels`:
     ```bash
     cd ../urbanwheels
     docker-compose up --build
     ```

3. **Execução JavaFX** (Opcional, após o Passo 2):
   - Executar o módulo javafx:
     ```bash
     cd javafx
     mvn javafx:run
     ```

4. **Acessar o sistema**:
   - Interface web para administradores: `http://localhost:8080`.
   - Aplicação desktop para utilizadores.

---

#### Integração da REST API:
Para documentação detalhada da API:

- **UrbanWheels**: Visite o Swagger em [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **WeatherWise**: Visite o Swagger em [http://localhost:8081/swagger-ui/index.html#/](http://localhost:8081/swagger-ui/index.html#/)

##### Exemplo: Obter dados meteorológicos para uma localização
Pedido:
```bash
GET /weatherwise/api/weather?location=Lisbon
```
Resposta:
```json
{
  "location": "Lisbon",
  "temperature": 22,
  "forecast": "Sunny"
}
```

---

## Documentação
Este repositório inclui:
- `phase3.pdf` e `relatorio.pdf`: Relatórios detalhados sobre as fases do projeto e decisões técnicas.
- Ficheiros Docker para contentorização do sistema.
- Código-fonte completo do UrbanWheels e WeatherWise.

---

## Vídeos
Estes podem ser encontrados no diretório `videos/`, com demonstrações abrangentes para todos os casos de uso implementados.

---

## Contribuições
Contribuições são bem-vindas! Para contribuir:
1. Efetue um fork deste repositório.
2. Crie um novo branch com a sua funcionalidade:
   ```bash
   git checkout -b feature-name
   ```
3. Submeta um pull request para revisão.

---

## Licença
Este projeto é exclusivamente para fins educativos e segue as diretrizes da unidade curricular Engenharia de Sistemas de Software.

---

## Agradecimentos
- **Docente** pela orientação fornecida ao longo do desenvolvimento.
- **Membros do grupo** pela colaboração e dedicação exemplares.
- Comunidades e bibliotecas open-source.

---

Visite o repositório [aqui](https://github.com/guimbreon/CSS-Project) para mais detalhes.
