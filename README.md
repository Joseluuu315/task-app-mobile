# TrassTarea - Sistema de Gestión de Tareas

**TrassTarea** es una aplicación nativa para Android desarrollada en Java, diseñada para facilitar la organización personal mediante un sistema de gestión de tareas (CRUD). El proyecto implementa una arquitectura robusta basada en la comunicación eficiente entre actividades y la gestión centralizada de datos.

## 🛠 Especificaciones Técnicas

- **Lenguaje**: Java 17
- **SDK Objetivo**: API 34+
- **Arquitectura**: Singleton para Persistencia en Memoria y Patrón Adaptador para UI.
- **Dependencias Clave**:
    - `androidx.appcompat:appcompat:1.7.1`
    - `com.google.android.material:material:1.13.0`
    - `androidx.recyclerview:recyclerview:1.4.0`

## 🏗 Arquitectura y Estructura del Código

El proyecto se divide en componentes especializados para garantizar la escalabilidad y el mantenimiento:

### 1. Actividad Principal: `ListadoTareasActivity`
Es el núcleo de la aplicación. Gestiona el ciclo de vida de la lista y la interacción con el usuario:
- **`ActivityResultLauncher`**: Implementa la API moderna de Android para recibir datos de `CrearTareaActivity` y `EditarTareaActivity` de forma segura, evitando el uso de métodos obsoletos.
- **Gestión de Menús**: Controla el filtrado dinámico, el acceso a creación y el cierre de la aplicación.
- **Lógica de Filtrado**: Implementa un sistema de conmutación de prioridad mediante una `copiaCompleta` del `ArrayList`, permitiendo filtrar tareas importantes sin destruir la colección de datos original.

### 2. Capa de Datos: `ManagerMethods` (Singleton)
Ubicada en el paquete `.manager`, esta clase garantiza una **única instancia** de los datos durante toda la sesión.
- Centraliza el `ArrayList<Tarea>`.
- Evita la duplicación de datos al navegar entre pantallas.

### 3. Modelo de Datos: `Tarea`
- Implementa la interfaz **`Parcelable`**. Esto es fundamental para el rendimiento del sistema, permitiendo que los objetos `Tarea` se envíen rápidamente a través de `Intents` entre actividades sin la carga de procesamiento de la serialización estándar de Java.

### 4. Interfaz de Usuario: `TareaAdapter`
- Extiende de `RecyclerView.Adapter`.
- Utiliza el patrón **ViewHolder** para optimizar el scroll y el uso de memoria.
- Define listeners personalizados (`OnEditListener`, `OnDeleteListener`) para comunicar eventos desde los elementos individuales de la lista hacia la actividad principal.

## 🚀 Flujos de Trabajo Implementados

1.  **Inserción**: Al crear una tarea, se recibe el objeto `Parcelable`, se añade al Singleton y se utiliza `notifyItemInserted(0)` para una actualización visual inmediata.
2.  **Edición**: Se rastrea la posición mediante `posicionEditando`, se actualiza el objeto en el índice correspondiente y se refresca la vista con `notifyItemChanged`.
3.  **Borrado**: Eliminación directa de la lista y sincronización del adaptador mediante `notifyItemRemoved`.
4.  **Estado Vacío**: Método `actualizerVisibilities()` que alterna entre el `RecyclerView` y un `TextView` informativo cuando no hay datos.

## ✒️ Autor

* **Jose Luis Fuentes Parra** - *Desarrollo Integral* - [Joseluu](https://github.com/tu-usuario)* **Centro**: IES Trassierra
* **Año**: 2025

---
*Proyecto desarrollado para el módulo de Programación Multimedia y Dispositivos Móviles.*
