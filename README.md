# MetaMapa

### Sistema de Mapeo Colaborativo

**Trabajo Práctico Anual – Diseño de Sistemas de Información**

---

## 📌 Descripción del proyecto

**MetaMapa** es un sistema de mapeo colaborativo desarrollado como **Trabajo Práctico Anual (TPA)** para la materia **Diseño de Sistemas de Información**.

El objetivo del sistema es permitir la **recolección, validación, agregación y visualización de hechos** provenientes de distintas fuentes, utilizando una **arquitectura distribuida basada en servicios**.

El proyecto busca simular un entorno real, incorporando conceptos de **autenticación, seguridad, API Gateway, Service Discovery y comunicación entre servicios**.

---

## 🧱 Arquitectura general

El sistema está compuesto por múltiples **servicios independientes**, cada uno con una responsabilidad bien definida:

* **Fuente Estática**
  Expone hechos predefinidos y datos persistentes.

* **Fuente Dinámica**
  Permite la carga, modificación y validación de hechos ingresados por los usuarios.

* **Fuente Proxy**
  Actúa como intermediario para el consumo de fuentes externas.

* **Servicio de Agregación**
  Consolida y unifica hechos provenientes de distintas fuentes.

* **Servicio de Autenticación**
  Gestiona usuarios, roles y permisos del sistema.

* **Servicio Gateway**
  Funciona como punto de entrada único al sistema, centralizando las peticiones.

* **Servicio Discovery**
  Permite el registro y descubrimiento dinámico de los servicios.

* **Servicio de Estadísticas**
  Genera métricas e indicadores a partir de los hechos recolectados.

* **Interfaz Web**
  Frontend que permite la interacción de los usuarios con el sistema.

---

## 🛠️ Tecnologías utilizadas

### Backend

* **Java**
* **Spring Boot**
* **Spring Security**
* **Spring Cloud** (Gateway / Discovery)
* **REST APIs**

### Frontend

* **HTML / CSS / JavaScript**

### Arquitectura e infraestructura

* **Arquitectura basada en servicios**
* **Service Discovery**
* **API Gateway**
* **Autenticación centralizada**
* **Control de accesos y roles**
