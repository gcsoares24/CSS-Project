
---

# CSS Phase 3 – UrbanWheels

**Curso:** Construção de Sistemas de Software – Tecnologias de Informação
**Semestre:** 2025/2026
**Entrega:** 15/12/2025, 23:59

# EXTRAS, PARA DIZER NO RELATÓRIO:
## 1.
- **docker-compose.yml:**

  - **Antes:**

      ```yaml
      services:
        pgserver:
          image: postgres:latest
          container_name: pgserver
          volumes:
            - postgres-data:/var/lib/postgresql
      ```
   - **Depois:**

      ```yaml
      pgserver:
        build:
          context: .
          dockerfile: postgres.dockerfile
        container_name: pgserver
        volumes:
          - postgres-data:/var/lib/postgresql/data
      ```
  - **Motivo:** Para garantir consistencia de dados.

- **application.proprerties:**

  - **Alteração:** `spring.jpa.hibernate.ddl-auto=validate` para garantir a consistência dos dados.



## 1. Contexto

A **ConcreteCast Solutions (CCS)** pretende implementar o **UrbanWheels**, um sistema de bicicletas partilhadas. O objetivo desta fase é construir interfaces gráficas (web e desktop) e integrar o sistema com **WeatherWise** para previsões meteorológicas.

* Fases 1 e 2: backend funcional com API REST e persistência de dados.
* Fase 3: interfaces gráficas + integração com WeatherWise + vídeo de demonstração.

---

## 2. Casos de Uso

### 2.1 Interface Web (Thymeleaf)

Operações típicas de **administrador**:

1. Login (mock: qualquer password válida)
2. Registar nova estação
3. Listar todas as estações
4. Detalhes de uma estação (lista de bicicletas)
5. Adicionar nova bicicleta à frota
6. Listar todas as bicicletas (filtro por estado: DISPONÍVEL, EM USO, etc.)
7. Registar manutenção de bicicleta
8. Registar novo utilizador
9. Listar todos os utilizadores

### 2.2 Interface Desktop (JavaFX)

Operações típicas de **clientes**:

1. Login (mock)
2. Listar estações
3. Detalhes de uma estação
4. Reservar bicicleta → estado = RESERVADA
5. Cancelar reserva → estado = DISPONÍVEL
6. Levantar bicicleta
7. Entregar bicicleta
8. Detalhes do utilizador (histórico de viagens)

### 2.3 Integração de Sistemas (REST API)

* Atualizar WeatherWise: criar endpoint REST que fornece previsão baseada em **localização** (lat/long) e **data**.
* UrbanWheels: ao reservar bicicleta, chamar API do WeatherWise e exibir previsão ao utilizador.

---

## 3. Requisitos Não Funcionais

* Interfaces comunicam via API REST do UrbanWheels.
* WeatherWise REST API implementada em Spring Boot.
* Projeto **contentorizado com Docker**.
* Vídeos de demonstração obrigatórios para cada caso de uso implementado.
* Arquitetura em camadas: Controller, Service, Repository.
* Controle de qualidade: pre-commit, testes.
* Participação avaliada via commits e qualidade do código.
* Decisões técnicas justificadas em relatório técnico.

---

## 4. Tarefas da Fase 3

* Implementar interface web com Thymeleaf
* Implementar interface desktop com JavaFX
* Atualizar WeatherWise e criar endpoint REST
* Integrar UrbanWheels com WeatherWise (caso de uso de reserva)
* Produzir vídeo(s) demonstrando todos os casos de uso

---

## 5. Entrega

1. Criar tag **fase3** no Git:

```bash
git tag fase3
git push origin fase3
```

2. Incluir:

    * Código-fonte de **UrbanWheels** e **WeatherWise**
    * Ficheiros Docker
    * README.md atualizado com instruções de execução + link do vídeo
3. Garantir que todos os componentes podem ser executados pela equipa docente.

---

## 6. Critérios de Avaliação (40% da nota final)

### 6.1 Processo de Desenvolvimento

* Frequência e granularidade dos commits
* Qualidade das mensagens de commit
* Funcionalidade do código

### 6.2 Interfaces Gráficas

* Implementação correta dos casos de uso
* Qualidade da interface e UX
* Comunicação correta com API REST

### 6.3 Integração de Sistemas

* REST API funcional no WeatherWise
* Integração completa no UrbanWheels

### 6.4 Vídeo de Demonstração

* Clareza e objetividade
* Demonstração completa dos casos de uso
* Qualidade geral do vídeo

### 6.5 Relatório Técnico

* Decisões técnicas claras
* Máximo de 2 páginas, conciso e estruturado

## 7. Divisões

### Guilherme Soares (fc62372)
- E. Adicionar uma nova bicicleta a frota (TL)
- F. Listar todas as bicicletas em todas as estações (TL)
- G. Registar uma nova operação de manutenção para uma bicicleta (TL)
- M. Realizar uma reserva de bicicleta (JJX)
- N. Cancelar uma reserva de bicicleta (JJX)
- P. Entregar uma bicicleta (JJX)

### Vitória Correia (fc62211)
- A. Realizar login (TL)
- H. Registar um novo utilizador (TL)
- I. Listar todos os utilizadores (TL)
- O. Levantar uma bicicleta (JJX)
- Q. Obter os detalhes do utilizador (JJX)
- J. Realizar login (JJX)

### Duarte Soares (fc62371)
- B. Registar uma nova estação (TL)
- C. Listar todas as estações (TL)
- D. Obter os detalhes de uma estação específica (TL)
- K. Listar todas as estações (JJX)
- L. Obter os detalhes de uma estação específica (JJX)


---