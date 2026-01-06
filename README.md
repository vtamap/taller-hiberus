# Payment Initiation Service

## Contexto
Este servicio es parte del proceso de migración de servicios legados SOAP a REST, alineado con el estándar BIAN (Service Domain: Payment Initiation, BQ: PaymentOrder).
El objetivo es modernizar la arquitectura utilizando Spring Boot 3, Java 17+ (se usó 21), y Arquitectura Hexagonal.

## Decisiones de Diseño
- **Arquitectura Hexagonal**: Se separó el dominio, aplicación e infraestructura para desacoplar la lógica de negocio de los detalles técnicos.
  - **Dominio**: Entidades puras (`PaymentOrder`, `PaymentStatus`).
  - **Aplicación**: Puertos (`application/port`) y Casos de Uso (`application/usecase`).
  - **Infraestructura**: Adaptadores REST y de Persistencia.
- **Contract-First**: Se definió primero el contrato OpenAPI 3.0 (`src/main/resources/openapi.yaml`) y se generaron las interfaces.
- **Mappers**: Se usó MapStruct para el mapeo eficiente entre DTOs, Dominio y Entidades.
- **Base de Datos**: Se configuró H2 para desarrollo local y pruebas, y PostgreSQL para despliegue con Docker.
- **Java 21**: Se utilizó Java 21 para aprovechar las últimas características y rendimiento, compatible con el entorno.

## Ejecución Local
1. Asegurarse de tener Java 21 y Maven instalados.
2. Ejecutar:
   ```bash
   mvn spring-boot:run
   ```
3. Acceder a Swagger UI: `http://localhost:9999/swagger-ui.html`

## Ejecución con Docker
1. Construir y levantar los contenedores:
   ```bash
   docker-compose up --build
   ```
2. El servicio estará disponible en el puerto 9999.

## Pruebas
Ejecutar todas las pruebas (Unitarias e Integración):
```bash
mvn clean verify
```

## Endpoints Principales
- `POST /payment-initiation/payment-orders`: Iniciar una orden de pago.
- `GET /payment-initiation/payment-orders/{id}`: Consultar una orden de pago.
- `GET /payment-initiation/payment-orders/{id}/status`: Consultar estado.

## Evidencias de IA
Se utilizó IA para:
- Analizar el WSDL y proponer el mapeo a BIAN.
- Generar el contrato OpenAPI.
- Prompts

Actúa como un arquitecto de integración bancaria experto en BIAN (Banking Industry Architecture Network).

Analiza el siguiente archivo WSDL y realiza las siguientes tareas:

1. Identifica las operaciones, mensajes, tipos complejos y endpoints expuestos.
2. Determina a qué Service Domain(s) de BIAN corresponde cada operación.
3. Propón el mapeo entre:
    - Operaciones WSDL → BIAN Service Operations
    - Mensajes de entrada/salida → BIAN Request/Response Models
4. Indica si el servicio corresponde a:
    - Control Record
    - Behavior Qualifier
5. Señala brechas de alineación con BIAN (nombres, granularidad, responsabilidades).
6. Propón ajustes para cumplir con las mejores prácticas BIAN (naming, responsabilidades, separación de dominios).

Entrega el resultado en formato estructurado (tabla o JSON), con una breve justificación por cada mapeo.
