# 👾 SamiBox TV Launcher

**SamiBox TV** es un Launcher (Pantalla de Inicio) para Android TV y TV Boxes genéricas, construido 100% con **Jetpack Compose for TV**. Cuenta con una estética única inspirada en las consolas retro de 8-bits de los años 80s y 90s.

Este proyecto fue diseñado teniendo en mente el rendimiento fluido en hardware con recursos limitados (como las TV Boxes asiáticas), utilizando carga asíncrona de imágenes (Coil) y caché en memoria para evitar el "lag" común en Android TV al consultar el `PackageManager`.

## ✨ Características Principales
* **🎨 Tema Retro Arcade:** Tipografía genuina "Press Start 2P", colores de acento neón (Cyan y Naranja) sobre un fondo azul marino profundo (`#0B1426`), y cursores pixelados.
* **🎮 Navegación 100% D-Pad:** Toda la grilla y paginación (`HorizontalPager`) fueron construidas pensando en el control remoto nativo, limitando a 10 aplicaciones por página para mantener el diseño limpio en resolución `1080p`.
* **🛠 Personalización Completa:** Mantenimiento presionado el botón "OK" / "Select" sobre cualquier app se abre un menú contextual retro. Puedes **Mover** la app cruzando en cualquier dirección con las flechas, u **Ocultar** aplicaciones basura preinstaladas de fábrica (Bloatware).
* **♻️ Restauración y Adición de Apps:** Un botón con un "Androide Retro +" al final de tu cuadrícula abre una pantalla flotante donde descansan tus aplicaciones ocultas para restaurarlas con un clic.
* **🚀 Hyper-Optimizado:** Uso de `Dispatchers.IO` para la lectura segura de intents, emparejado con una capa de caché de `AppInfo` en el ViewModel que elimina cualquier caída de fotogramas (lag) al manejar el inventario de apps instaladas.

## 📸 Capturas de Pantalla
*(Considera subir algunas fotos directamente a tu repositorio para que la gente vea cómo luce)*

## 🛠 Requisitos y Construcción
* Android Studio Iguana / Jellyfish (o más nuevo)
* Kotlin `1.9.0+`
* Jetpack Compose (BOM `2024.02.00+` y Compose TV Material 3 `1.0.0-alpha10+`)
* Android API 21+ (Recomendado API 26+ para TV Boxes)

Para compilar, simplemente clona este repositorio, ábrelo en Android Studio y ejecuta la tarea de Gradle:
```bash
./gradlew assembleDebug
```

## 🤝 Contribuir
¡SamiBox TV es un proyecto Open Source (Código Abierto) y toda ayuda de la comunidad retro es bienvenida!
Por favor lee el archivo `CONTRIBUTING.md` para conocer nuestras reglas de convivencia y estilo de código antes de abrir un *Pull Request* (PR).

Siéntete libre de navegar por la carpeta `/documentacion/rotmap.md` (roadmap) para ver qué ideas estamos planeando integrar en el futuro.

## 📄 Licencia
Este proyecto está bajo la Licencia **MIT**. Consulta el archivo `LICENSE` para más detalles.
