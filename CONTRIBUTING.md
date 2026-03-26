# Guía de Contribución para SamiBox TV

¡Gracias por mostrar interés en colaborar con **SamiBox TV**! Nos encanta recibir ayuda de desarrolladores Android, diseñadores y apasionados por lo retro.

Para mantener el proyecto ordenado y funcionando rápido en cualquier TV Box, te pedimos seguir unas pautas muy sencillas:

## 1. 👾 Filosofía de Diseño
SamiBox TV es un launcher **retro**. Si vas a sugerir cambios en la Interfaz de Usuario (UI):
* Asegúrate de que los colores armonicen con nuestro fondo Azul Marino (`#0B1426`) preferiblemente usando los acentos Naranja (`#FFFF8C00`) y Cyan (`#00E5FF`).
* Todo texto nuevo debe usar de forma estricta la fuente 8-bits (`Press Start 2P`) para no romper la inmersión del usuario.
* El diseño debe obligatoriamente usar `TvMaterial3` de Compose y no los componentes normales para teléfonos, además de garantizar que todos los *focus states* (estados al pasar encima con el control remoto) sean visibles y amigables.

## 2. ⚡ Filosofía de Rendimiento
Las TV Boxes para las que fue diseñado este Launcher tienen recursos de RAM o procesadores algo antiguos (ej. procesadores como Rockchip, Amlogic genéricos).
* **NUNCA** solicites listas repetitivas de `PackageManager` en el hilo principal o en redibujados (Recompositions).
* Usa `rememberAsyncImagePainter` (Coil) para cualquier recurso gráfico que no sea un Vector XML nativo chiquito.
* Extracción de Drawables pesados debe hacerse siempre asíncrona (como lo hace nuestra solución actual con caché).

## 3. 👨‍💻 Cómo Enviar tus Cambios (Pull Requests)
1. Has un *Fork* (bifurcación) de este repositorio a tu cuenta personal de GitHub.
2. Crea una rama (`branch`) con el nombre de tu característica. Ejemplo: `git checkout -b feature/nuevos-wallpapers-animados`.
3. Haz tus *commits* con mensajes descriptivos.
4. Empuja (*push*) tus cambios a tu *Fork*.
5. Abre un **Pull Request (PR)** hacia la rama principal (`main`) de este repositorio.

Revisaremos tu PR lo antes posible. ¡Una vez más, gracias por ayudar a revivir los 8-bits en nuestros televisores!
