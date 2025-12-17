MetaMapa es un sistema de mapeo colaborativo desarrollado como Trabajo Práctico Anual (TPA) para la materia Diseño de Sistemas de Información.
El objetivo del sistema es permitir la recolección, validación, agregación y visualización de hechos provenientes de distintas fuentes, utilizando una arquitectura distribuida basada en servicios.

El proyecto está diseñado para simular un entorno real, incorporando autenticación, seguridad, gateway, discovery y comunicación entre servicios.

🧱 Arquitectura general

El sistema está compuesto por múltiples servicios independientes que se comunican entre sí:

-Fuente Estática: expone hechos predefinidos.

-Fuente Dinámica: permite la carga, modificación y validación de hechos.

-Fuente Proxy: actúa como intermediario hacia fuentes externas.

-Servicio de Agregación: consolida hechos provenientes de distintas fuentes.

-Servicio de Autenticación: gestiona usuarios y permisos.

-Servicio Gateway: punto de entrada único al sistema.

-Servicio Discovery: registro y descubrimiento de servicios.

-Servicio de Estadísticas: generación de métricas e indicadores.

-Interfaz Web: frontend para interacción con el sistema.

🛠️ Tecnologías utilizadas

Java

Spring Boot

Spring Security

Spring Cloud (Gateway / Discovery)

HTML/CSS/JS

REST APIs

Service Discovery

API Gateway

Autenticación centralizada

Control de accesos y roles
