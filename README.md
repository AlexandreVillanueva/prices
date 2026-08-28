# Prices

Servicio para consultar el precio aplicable a un producto y una cadena de un grupo en un instante dado. En caso de que varias tarifas se solapen se desempata por el campo prioridad. Si empatan en prioridad se indica el error.

## Requisitos

JDK 25.

## Arranque

Para arrancar el proyecto nos situamos en la raíz del proyecto y lanzamos por consola:

```bash
./mvnw spring-boot:run
```

Si el puerto 8080 por defecto está ocupado configuramos el que queremos de esta manera:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port={puertoDeseado}
```

## El endpoint

Para hacer una petición de prueba acordarse de que el puerto coincida con el configurado en el arranque.

```bash
curl "http://localhost:8080/api/v1/prices/applicable?brandId=1&productId=35455&applicationDate=2020-06-14T16:00:00"
```

A la petición anterior con los datos de prueba tendríamos la siguiente respuesta:

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

Los distintos códigos de respuesta son:

| Código | Cuándo |
|---|---|
| `200`  | Hay un precio aplicable |
| `404`  | No hay ninguna tarifa vigente para esa cadena, producto e instante |
| `400`  | Falta un parámetro, la fecha no es ISO-8601, o un identificador no es positivo |
| `409`  | Dos tarifas vigentes empatan en la prioridad más alta |

Los errores se devuelven en formato *problem detail* (RFC 9457).

## Datos de ejemplo

Tenemos una base H2 en memoria que se inicializa con `schema.sql` y `data.sql`.

Se puede consultar vía navegador en `http://localhost:8080/h2-console`, es importante poner el mismo puerto que usaste en el arranque.

Para conectarte los datos son:
- URL: `jdbc:h2:mem:prices`
- Usuario: `sa`
- Contraseña vacía.

## Tests

Para lanzar los test:

```bash
./mvnw test
```

Los cinco casos del enunciado están en `FindApplicablePriceEndpointTest`. El resto de test son a mayores para validar el comportamiento esperado.

## Estructura

Arquitectura hexagonal en cuatro capas.

```
com.classora.prices
├── presentation      controlador REST, DTO de respuesta y manejo de errores
├── application       casos de uso, y los puertos de entrada y de salida
├── domain            el modelo y la regla de negocio. Sin Spring y sin JPA
└── infrastructure    entidad JPA, repositorio y mapeo a dominio
```

De cada capa solo es público lo que tiene que serlo: la implementación del caso de uso, la entidad y el
adaptador de persistencia son *package-private*, así que no se puede saltar un puerto.
