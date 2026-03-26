# 👾 SamiBox TV Launcher

**SamiBox TV** es un Launcher (Pantalla de Inicio) ultraligero y avanzado para Android TV y TV Boxes genéricas, construido 100% con **Jetpack Compose for TV**. Cuenta con una estética única inspirada en las consolas retro de 8-bits de los años 80s y 90s, combinada con herramientas de diagnóstico de hardware en tiempo real.

Este proyecto fue diseñado teniendo en mente el rendimiento fluido en hardware con recursos limitados (como las TV Boxes Rockchip y Amlogic), utilizando carga asíncrona de imágenes (Coil), caché en memoria y optimizaciones de hilos para evitar el lag.

## ✨ Características Principales

* **🎨 Tema Retro Arcade:** Tipografía genuina "Press Start 2P", colores de acento neón (Cyan, Verde y Naranja) sobre un fondo azul marino profundo (`#0B1426`), y cursores pixelados.
* **🎮 Navegación 100% D-Pad:** Toda la grilla y paginación (`HorizontalPager`) fueron construidas pensando en el control remoto nativo, limitando a 10 aplicaciones por página para mantener un diseño limpio.
* **🛠 Personalización y Gestión de Apps:** Manteniendo presionado el botón "OK" / "Select" sobre cualquier app se abre un menú retro. Puedes **Mover** la app en la cuadrícula u **Ocultar** aplicaciones basura preinstaladas de fábrica (Bloatware).
* **♻️ Añadir Aplicaciones:** Un botón dedicado que abre una pantalla flotante para seleccionar y restaurar aplicaciones instaladas a tu pantalla de inicio principal.
* **⚡ Monitor de Sistema Global (Overlay):** Un panel de diagnóstico al estilo "Developer Mode" que flota **encima de cualquier aplicación** (Netflix, YouTube, etc.). Muestra en tiempo real:
  - **FPS Reales** (Choreographer frame drop)
  - **Uso de CPU diferencial** (`/proc/stat`)
  - **Uso de RAM** con alertas de color
  - **Temperatura de CPU**
  - **Velocidad de Red** (Subida/Bajada real)
  - **Uptime y Almacenamiento (Interno/Externo)**
  > *Se despliega instantáneamente presionando la tecla **MENU** (`≡`) en el control remoto gracias al Servicio de Accesibilidad (SamiBoxAccessibilityService).*

## 📸 Capturas de Pantalla
*(Considera subir algunas fotos directamente a tu repositorio para que la gente vea cómo luce la interfaz y el overlay)*

## 🛠 Instalación y Configuración

**Requisitos de compilación:**
* Android Studio Iguana / Jellyfish (o más nuevo)
* Kotlin `1.9.0+` y Jetpack Compose (`TV Material 3`)
* Android API 21+ (Recomendado API 26+ para TV Boxes)

```bash
./gradlew assembleDebug
```

**Permisos Especiales en el TV Box:**
Para que el **Monitor de Sistema (System Info Overlay)** funcione globalmente y la tecla MENU sea interceptada en cualquier app, debes:
1. Ir a `Ajustes > Accesibilidad` en tu TV Box y activar **SamiBox TV**.
2. Opcionalmente, otorgar permiso de "Mostrar sobre otras aplicaciones" (`SYSTEM_ALERT_WINDOW`) si el sistema lo requiere, aunque la App lo solicita al arrancar.

## 🤝 Contribuir
¡SamiBox TV es un proyecto Open Source (Código Abierto) y toda ayuda de la comunidad retro es bienvenida!
Por favor lee el archivo `CONTRIBUTING.md` para conocer nuestras reglas de convivencia y estilo de código antes de abrir un *Pull Request* (PR).

Siéntete libre de navegar por la carpeta `/documentacion/rotmap.md` (roadmap) para ver qué ideas estamos planeando integrar en el futuro, como mapeo agresivo de teclados o dashboards en la nube.

## 📄 Licencia
Este proyecto está bajo la Licencia **MIT**. Consulta el archivo `LICENSE` para más detalles.
