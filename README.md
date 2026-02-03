# Checkout System

Este proyecto es una implementación de un sistema de checkout robusto diseñado para cumplir con los requerimientos técnicos de la evaluación para **Walmart**.

El sistema calcula descuentos configurables, costos de envío dinámicos y valida métodos de pago, siguiendo patrones de diseño como **Strategy** y **Factory**, asegurando un código limpio y extensible.

## Características

- **Descuentos por producto**: Configurables como porcentaje en base de datos
- **Descuentos promocionales**: Sistema extensible usando Strategy Pattern
- **Descuentos por método de pago**: Configurables por tipo de pago (efectivo, débito, crédito)
- **Cálculo de envío**: Basado en zona geográfica
- **Validación de métodos de pago**: Enum type-safe con validación en runtime

## Stack Tecnológico

- **Java 17**
- **Spring Boot 3.4.2**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Lombok**
- **Maven**

## Requisitos Previos

- **Java SDK**: Versión 17 o superior instalada.
- **Variables de Entorno**: Asegúrese de tener configurado `JAVA_HOME`.
- **Puerto 8080**: La aplicación utiliza el puerto 8080 por defecto. Asegúrese de que esté disponible.

## Instalación y Ejecución Local

Siga estos pasos para levantar el proyecto sin necesidad de configuraciones adicionales:

### Ejecución con Docker (Recomendado)

Si tiene Docker instalado, puede levantar el proyecto completo (backend + frontend) con un solo comando:

```powershell
docker-compose up --build
```

Esto descargará las imágenes necesarias, compilará el proyecto y lo dejará listo en el puerto **8080**.

### 1. Ejecución con Maven Wrapper (Manual)

No es necesario tener Maven instalado de forma global, el proyecto incluye un "wrapper".

**En Windows (PowerShell/CMD):**
```powershell
./mvnw.cmd spring-boot:run
```

**En Linux/macOS:**
```bash
./mvnw spring-boot:run
```

### 2. Acceso a la Interfaz de Usuario (Frontend)

Una vez que la consola muestre `Started CheckoutApplication`, abra su navegador y visite:

👉 **[http://localhost:8080](http://localhost:8080)**

Desde aquí podrá interactuar con el sistema completo (selección de zona, carrito, descuentos y simulación de pago).

### 3. Ejecución de Pruebas Unitarias

Para asegurar que toda la lógica de negocio (descuentos CLP, envío, etc.) es correcta:

```powershell
./mvnw.cmd test
```

## Consola de Base de Datos H2

El proyecto utiliza una base de datos en memoria que se pre-carga automáticamente al iniciar. Si desea inspeccionar las tablas (`product`, `payment_method`, `promotion`):

- **URL**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL**: `jdbc:h2:mem:checkoutdb`  <-- ⚠️ **IMPORTANTE**: Cambiar el valor por defecto por este.
- **Usuario**: `sa`
- **Contraseña**: (en blanco)

> [!WARNING]
> Si al entrar ves una URL como `jdbc:h2:~/test` o algo similar, **debes borrarla y escribir `jdbc:h2:mem:checkoutdb`**, de lo contrario verás el error "Database not found".

## API Endpoints

### POST /checkout

Procesa un checkout con items del carrito, dirección de envío y método de pago.

**Request Body:**
```json
{
  "cartId": "cart-1001",
  "items": [
    { "sku": "p-001", "quantity": 1 },
    { "sku": "p-010", "quantity": 2 }
  ],
  "shippingAddress": {
    "street": "Av. Falsa 123",
    "city": "Ciudad",
    "zoneId": "zone-1"
  },
  "paymentMethod": "DEBIT"
}
```

**Response (200 OK):**
```json
{
  "subtotal": 1180.00,
  "productDiscount": 200.00,
  "promotionalDiscount": 0.00,
  "shippingCost": 10.00,
  "paymentDiscount": 99.00,
  "total": 891.00
}
```

**Response (400 Bad Request):**
```json
{
  "error": "Invalid payment method: ABC"
}
```

```json
{
  "error": "Product not found: p-999"
}
```

```json
{
  "error": "Invalid shipping zone: zone-999"
}
```

## Métodos de Pago Válidos

- `CASH` - Efectivo (15% descuento)
- `DEBIT` - Débito (10% descuento)
- `CREDIT_CARD` - Tarjeta de crédito (0% descuento)

## Zonas de Envío Válidas

- `zone-1` - Zona urbana cercana ($10.00)
- `zone-2` - Zona urbana lejana ($25.00)
- `zone-3` - Zona suburbana ($50.00)
- `zone-4` - Zona rural ($100.00)
- `zone-5` - Zona remota ($150.00)

## Configuración de Base de Datos

La aplicación usa H2 en memoria. Los datos iniciales se cargan desde `src/main/resources/data.sql`:

### Productos
```sql
INSERT INTO product (id, sku, name, price, discount, category) VALUES
(1, 'p-001', 'Laptop', 1000.00, 20.00, 'Tech'),
(2, 'p-010', 'Mouse', 50.00, 0.00, 'Tech');
```

### Métodos de Pago
```sql
INSERT INTO payment_method (id, name, type, discount) VALUES
(1, 'Debit Card', 'DEBIT', 0.10),
(2, 'Credit Card', 'CREDIT_CARD', 0.05);
```

### Promociones
```sql
INSERT INTO promotion (id, name, strategy_type, config_value, active) VALUES
(1, 'Global Summer Sale', 'TEN_PERCENT_OFF', 10.00, 'S');
```

### Guía de Uso (Frontend)

Para operar la interfaz y realizar un checkout exitoso, sigue estas reglas:

1.  **Selección de Zona**: Es obligatorio seleccionar una **Zona de Envío** en el selector ubicado en el header (ícono 📍). Hasta que no se elija una zona, el costo de envío aparecerá como "$0" o "Sin costo" y el cálculo no estará completo.
2.  **Validación del Carrito**: Debes agregar al menos un producto al carrito usando el botón `[+]` de las tarjetas de productos.
3.  **Botón de Pago**: El botón **"Pagar ahora"** se habilitará automáticamente **SOLO CUANDO**:
    *   Haya al menos 1 producto en el carrito.
    *   Haya una zona de envío seleccionada.
4.  **Simulación de Pago**: Al hacer clic en "Pagar ahora", el sistema simulará un procesamiento de 2 segundos (mostrando un spinner de carga). Al finalizar, aparecerá una notificación de **"¡Pago realizado con éxito!"** y el carrito se vaciará automáticamente.
5.  **Actualización en Tiempo Real**: Cada vez que agregues/quites un producto, cambies la zona o el método de pago, el resumen se actualizará instantáneamente conectándose al backend.

---

## Arquitectura

### Patrones de Diseño

- **Strategy Pattern**: Para promociones (`PromotionStrategy`)
- **Repository Pattern**: Para acceso a datos
- **DTO Pattern**: Para request/response (`CheckoutRequest`, `CheckoutResult`)

### Principios de Clean Code

Este proyecto sigue principios de código limpio:

- **Clean API**: Endpoints REST para `/checkout`, `/products`, `/payment-methods` y `/shipping-zones`.
- **Frontend Interactivo**: Interfaz moderna inspirada en Mundo Lider con actualizaciones en tiempo real y validación de checkout.
- **Clean Code**: Sin comentarios innecesarios, código autoexplicativo y robusto.
- **Constantes extraídas**: Magic numbers reemplazados por constantes con nombres descriptivos
- **Métodos pequeños**: Cada método tiene una única responsabilidad
- **Nombres descriptivos**: Variables como `intermediateTotal`, `paymentDiscountPercent` son autoexplicativos
- **DRY (Don't Repeat Yourself)**: Lógica de cálculo de porcentajes extraída a método reutilizable
- **Validación temprana**: Excepciones lanzadas inmediatamente cuando se detectan datos inválidos

### Estructura del Proyecto

```
src/main/java/com/example/checkoutbackend/
├── controller/
│   └── CheckoutController.java
├── model/
│   ├── CartItem.java
│   ├── CheckoutRequest.java
│   ├── CheckoutResult.java
│   ├── PaymentMethod.java
│   ├── PaymentMethodType.java (Enum)
│   ├── Product.java
│   └── ShippingAddress.java
├── repository/
│   ├── PaymentMethodRepository.java
│   └── ProductRepository.java
├── service/
│   ├── CheckoutService.java
│   ├── ShippingService.java (Gestión de zonas y costos)
│   └── promotion/
│       ├── PromotionStrategy.java
│       ├── TenPercentOffStrategy.java
│       └── ProductSpecificStrategy.java
└── CheckoutApplication.java
```

## Lógica de Negocio

### Cálculo de Descuentos

1. **`productDiscount`**: Es el descuento que trae el producto "de fábrica" en el catálogo (por ejemplo, el 20% que tiene la Laptop `p-001`).
2. **`promotionalDiscount`**: Son descuentos adicionales que se aplican a nivel de carrito o por campañas específicas (cupones, "CyberDay", "2x1", etc.) usando el **Strategy Pattern**.
3. **`paymentDiscount`**: Es el descuento que obtienes exclusivamente por elegir un medio de pago (como el 10% de Débito). Se aplica sobre el neto (Subtotal - Descuentos + Envío).

### Fórmula del Total

```
Total = subtotal 
        - productDiscount 
        - promotionalDiscount 
        + shippingCost 
        - paymentDiscount
```

## Validaciones

- **SKU**: Debe existir en la base de datos
- **Método de Pago**: Debe ser uno de los valores del enum `PaymentMethodType`
- **Zona de Envío**: Debe ser una zona válida configurada

## Manejo de Errores

- **400 Bad Request**: Método de pago inválido, SKU no encontrado, zona de envío inválida
- **500 Internal Server Error**: Errores inesperados del servidor

## Desarrollo

### Agregar un nuevo método de pago

1. Agregar valor al enum `PaymentMethodType`
2. Insertar registro en `data.sql`
3. Reiniciar la aplicación

### Agregar una nueva estrategia de promoción

1. Crear clase que implemente `PromotionStrategy`
2. Inyectar en `CheckoutService`
3. Configurar lógica de aplicación

## Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.
