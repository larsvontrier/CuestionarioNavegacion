## Cuestionarios con Navegación entre Actividades

[//]: # (https://github.com/larsvontrier/CuestionarioNavegacion)

Este proyecto es un ejemplo de navegación dentro de una actividad para trabajar con cuestionarios e invoca a a una segunda actividad para mostrar el resultado.

Emplea las siguientes tecnologías:

- viewBinding
- Layouts: `ConstraintLayout`, `LinearLayout`, `ScrollView`
- Views: `TextView`, `ImageView`, `Button`, `EditText`, `RadioButton`, `RadioGroup`, `ListView` (Legacy/Desaconsejado), `FloatingActionButton`
- Se han añadido imágenes a botones: `drawableLeft`, `drawableRight`, `drawableTop`, `drawableBottom`
- Recursos de cadenas (`strings.xml`)
- Modelo de datos de preguntas: se ha añadido `Resultado` para mantener las respuestas del usuario: `ResultadoRepository`
- **Repositorio de preguntas** (a mano), `PreguntaRepository`, que debe sustituirse por un repositorio de preguntas de una base de datos o una API. Se implanta como `object` para mantener las preguntas, pero debería ser un repositorio de preguntas real.
- **Repostorio de respuestas**, `ResultadoRepository`, para mantener las respuestas del usuario. `ResultadoRepository` se implante como object, para mantener las respuestas del usuario. Un `object` es el **modo más sencillo de implementar un patrón Singleton en Kotlin**.
- **ListView** para mostrar las repuestas (**sustituiremos por un RecyclerView** para mejorar la eficiencia).
- **Mantener el estado** al girar la pantalla.
- **Navegación entre actividades**:
  - `MainActivity` para mostrar las preguntas invoca a `ResultadosActivity` para mostrar el resultado cuando se pulsa el botón de "Enviar". Añade un extra con los intentos (los resultados, la actividad invocada, los recoge de `ResultadoRepository`).
  - `ResultadosActivity` para mostrar los resultados. Dispone de un botón para volver a la actividad principal y reiniciar el cuestionario, pasandole el número de intentos (incrementado en 1).

```kotlin
        binding.btEnviar.setOnClickListener {
            val intent = Intent(this, ResultadosActivity::class.java)
            intent.putExtra(ResultadosActivity.INTENTOS_KEY, numeroIntentos)
            startActivity(intent)
        }
```
En el que INTENTOS_KEY es una constante que se emplea para pasar el número de intentos entre actividades.

```kotlin
companion object {
  const val INTENTOS_KEY = "intentos"
}
```

Se recupera el número de intentos en `ResultadosActivity`:

```kotlin
        intento = intent.getIntExtra(INTENTOS_KEY, 0)
```


Mejoras pendientes:

- Un ViewModel para manejar las preguntas (pendiente)
- Mejorar la presentación de las preguntas.
- Crear un repositorio para cuestionarios.
- Sustituir el repositorio de preguntas por un repositorio de cuestionarios y leer de un archivo JSON (remoto o local).
- Emplear Retrofit para leer de un servicio web.
- Emplear Room para leer de una base de datos local.
- Emplear Firebase para leer de una base de datos remota.