# Gestión de Clientes (JavaFX)

Aplicación de escritorio en JavaFX para el registro, búsqueda y consulta de clientes, desarrollada como proyecto del curso de Programación Orientada a Objetos I (UAM).

## Arquitectura

MVC sencillo sobre JavaFX + FXML:

- **`models`**: `Client`, `Request`, `User`, `Role` — POJOs con Lombok (`@Data`, `@Builder`).
- **`dao`**: `Dao<T, ID>` (contrato) y sus implementaciones en memoria (`ClientDaoImpl`, `UserDaoImpl`), ambas expuestas como **singleton** (`getInstance()`) para que el mismo repositorio sea visible desde cualquier pantalla.
- **`controllers`**: un controller por vista FXML.
- **`util`**:
  - `SceneManager` — único punto de navegación entre ventanas. `sceneChange(fxml)` cambia de escena; `changeSceneWithData(fxml, client)` además entrega un `Client` al controller destino si este implementa `ReceivesClientData`.
  - `ReceivesClientData` — contrato que implementa cualquier controller que necesite recibir un `Client` al navegar (usado por la vista de Detalle).
  - `AlertHelper` — diálogos de información, advertencia, error, confirmación e input.
  - `FileChooserHelper` — selección de imagen (`FileChooser`) y de carpeta (`DirectoryChooser`).

## Flujo de pantallas

```
Login ──(credenciales válidas)──► Menú Principal ──► Registro de Cliente ──► (guarda en ClientDaoImpl) ──► Menú Principal
                                        │
                                        └──► Búsqueda de Clientes (TableView) ──(doble clic en fila)──► Detalle de Cliente ──► Menú Principal
```

El estado de los clientes vive únicamente en memoria (`ClientDaoImpl`, una `ObservableList`), tal como pide el enunciado del curso: no hay persistencia en disco ni base de datos.

## Usuario de acceso (demo)

No hay pantalla de alta de usuarios (fuera del alcance del enunciado). `UserDaoImpl` siembra un usuario de prueba:

- **Usuario:** `admin`
- **Contraseña:** `admin123`

## Cómo ejecutar

```bash
./mvnw clean javafx:run
```

## Notas de estado / decisiones

- El `main-view.fxml` / `MainController` originales (plantilla por defecto de JavaFX) se eliminaron: no formaban parte del flujo real de la aplicación.
- `SceneManager` existía en una rama (`feature/Francisco/utils-SceneManager`) que nunca se integró a `dev` y no compilaba (dependía de una interfaz `RecClientData` que no llegó a crearse). Se retomó, se corrigió y se integró como `ReceivesClientData`.
- `ClientDaoImpl` se convirtió en singleton: los controllers se recrean en cada `FXMLLoader.load()`, así que instanciar el DAO por controller rompía el requisito de "paso de datos en memoria entre controllers" (el registro y la búsqueda verían listas distintas).
