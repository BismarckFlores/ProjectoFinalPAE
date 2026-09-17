# Gestión de Clientes (JavaFX)

Aplicación de escritorio en JavaFX para el registro, búsqueda y consulta de clientes, desarrollada como caso práctico ("Eventos, navegación y paso de datos") del curso **Programación de Aplicaciones de Escritorio** (UAM).

## Arquitectura

MVC sencillo sobre JavaFX + FXML, diseñado en Scene Builder:

- **`models`**: `Client`, `Request`, `User`, `Role` — POJOs con Lombok (`@Data`, `@Builder`).
- **`dao`**: `Dao<T, ID>` (contrato) y sus implementaciones en memoria (`ClientDaoImpl`, `UserDaoImpl`), ambas expuestas como **singleton** (`getInstance()`) para que el mismo repositorio sea visible desde cualquier pantalla.
- **`controllers`**: un controller por vista FXML.
- **`util`**:
  - `SceneManager` — único punto de navegación. `sceneChange(fxml)` reemplaza toda la ventana (solo se usa para entrar/salir del shell: Login ↔ `app-shell-view`); `setContent(fxml)` / `setContentWithData(fxml, client)` cargan una pantalla **dentro** del shell sin tocar `MenuBar`/`ToolBar` ni el tamaño de la ventana; `showHome()` vuelve al panel de bienvenida. Si una vista falla al cargar, se muestra un `Alert` de error en vez de fallar en silencio. El tamaño de ventana es fijo (`WINDOW_WIDTH`/`WINDOW_HEIGHT`, 960×640) para toda la app.
  - `AppShell` — conecta `SceneManager` con el `StackPane` central del shell (`AppShellController`), para que "navegar" sea intercambiar el contenido de ese panel y no reemplazar toda la escena.
  - `ReceivesClientData` — contrato que implementa cualquier controller que necesite recibir un `Client` al navegar (usado por la vista de Detalle).
  - `Session` — guarda el `User` autenticado durante la ejecución y expone `isAdmin()` para controlar el acceso según el rol.
  - `AlertHelper` — diálogos de información, advertencia, error, confirmación (`Alert`) e input (`TextInputDialog`, usado en la búsqueda de clientes).
  - `FileChooserHelper` — selección de imagen (`FileChooser`) y de carpeta (`DirectoryChooser`).

### Shell persistente (MenuBar/ToolBar fijos)

`app-shell-view.fxml` + `AppShellController` son la única pantalla con `MenuBar`, `ToolBar` y `ContextMenu`. Tras el login se carga una sola vez; el resto de pantallas (Registro, Consulta, Detalle, Panel de Administración, Nueva Cuenta) se cargan **dentro** de su `StackPane` central (`contentArea`) vía `SceneManager.setContent(...)`, así que la barra de menú y la barra de herramientas nunca desaparecen ni la ventana cambia de tamaño al navegar. Solo Login y el propio shell reemplazan la ventana completa (`sceneChange`), porque Login no tiene sesión todavía y por lo tanto no debe mostrar `MenuBar`/`ToolBar`.

## Roles y permisos

Hay dos roles (`Role`): **Administrador** y **Usuario Común**.

| Acción                              | Administrador | Usuario Común |
|--------------------------------------|:---:|:---:|
| Consultar / buscar clientes           | ✅ | ✅ |
| Ver el detalle de un cliente          | ✅ | ✅ |
| Registrar un cliente nuevo            | ✅ | ❌ (solo lectura) |
| Panel de Administración               | ✅ | ❌ |
| Crear / eliminar cuentas de usuario   | ✅ | ❌ |

Un usuario con rol Común inicia sesión con normalidad, pero el shell oculta "Registrar Cliente" y "Panel de Administración"; además, `AppShellController` valida el rol antes de navegar (`Session.isAdmin()`), así que aunque se intente disparar la acción por otro medio (menú, barra de herramientas, menú contextual), el sistema la rechaza con un `Alert` de advertencia en vez de solo ocultar el botón.

Desde el **Panel de Administración** (solo accesible para el rol Admin) se pueden crear cuentas nuevas indicando usuario, contraseña y rol, y eliminar cuentas existentes (no se puede eliminar la cuenta con la que se inició sesión).

## Flujo de pantallas

```
Login ──(credenciales válidas)──► [shell: MenuBar+ToolBar fijos] ──► Registro de Cliente ──► (guarda en ClientDaoImpl) ──► Inicio del shell
                                        │
                                        ├──► Búsqueda de Clientes (TableView) ──(doble clic en fila)──► Detalle de Cliente ──► Inicio del shell
                                        │
                                        └──► Panel de Administración (solo Admin) ──► Nueva Cuenta ──► Panel de Administración
```

Todo lo que va después del login ocurre dentro del mismo shell (mismo `MenuBar`/`ToolBar`, mismo tamaño de ventana); solo "Cerrar Sesión" saca de vuelta al Login.

El estado (clientes y usuarios) vive únicamente en memoria (`ClientDaoImpl`, `UserDaoImpl`, ambas `ObservableList`), tal como pide el enunciado del curso: no hay persistencia en disco ni base de datos.

## Usuario de acceso (demo)

`UserDaoImpl` siembra una cuenta administradora al arrancar; desde ahí se pueden crear el resto de cuentas (de cualquier rol) desde el Panel de Administración:

- **Usuario:** `admin`
- **Contraseña:** `admin123`
- **Rol:** Administrador

## Cómo ejecutar

```bash
./mvnw clean javafx:run
```

## Entregables (sección 10 del caso práctico)

- **Proyecto completo de IntelliJ IDEA**: este repositorio (`gestion-clientes/`) es el proyecto Maven/IntelliJ completo; basta con abrir la carpeta raíz (contiene `pom.xml` y `.idea/`) directamente en IntelliJ.
- **Archivos `.java`**: `src/main/java/ni/edu/uam/gestionclientes/` — modelos, DAOs, controllers y utilidades (ver [Arquitectura](#arquitectura)).
- **Archivos `.fxml`**: `src/main/resources/ni/edu/uam/gestionclientes/views/` — una vista por pantalla (`login-view`, `app-shell-view`, `client-registration-view`, `client-list-view`, `client-detail-view`, `admin-panel-view`, `account-create-view`), editables desde Scene Builder.
- **Imágenes utilizadas por la aplicación**: la aplicación **no embebe** fotos de clientes en el proyecto; la foto se selecciona en tiempo de ejecución desde el equipo del usuario mediante `FileChooser` (`ClientRegisterController#onSelectPhotoAction`) y se referencia por su ruta (`Client.imagePath`), mostrándose en el `ImageView` de Registro y de Detalle. `src/main/resources/.../images/` queda como carpeta reservada por si el equipo decide agregar íconos o imágenes propias del proyecto.
- **`pom.xml`**: en la raíz del proyecto; declara las dependencias de JavaFX 21, Lombok y JUnit, y el plugin `javafx-maven-plugin` para ejecutar con `mvn javafx:run`.
- **Evidencias de funcionamiento**: no se incluyen capturas/video en el repositorio (el equipo debe generarlas antes de la entrega). Guion sugerido para grabarlas/capturarlas, cubriendo cada punto de la sección 8 del enunciado:
  1. Ejecutar `./mvnw clean javafx:run` y mostrar el Login vacío → intentar ingresar sin datos → aparece el `Alert` de advertencia.
  2. Iniciar sesión con `admin` / `admin123` (con el mouse) y luego repetir el login presionando `ENTER` en el campo de contraseña.
  3. En la pantalla principal, mostrar `MenuBar`, `ToolBar` (íconos) y clic derecho sobre el área central para el `ContextMenu`; navegar a Registro/Consulta y mostrar que la barra de menú y de herramientas no desaparecen ni la ventana cambia de tamaño.
  4. Ir a **Registrar Cliente**: llenar nombre/apellido/tipo/ciudad/fecha, elegir un `RadioButton` de tipo de solicitud, marcar algún `CheckBox`, seleccionar una foto con `FileChooser` y ver la vista previa en el `ImageView`. Guardar y mostrar el `Alert` de éxito.
  5. Ir a **Consultar Clientes**: mostrar el cliente recién creado en el `TableView`, usar **Buscar Cliente** (`TextInputDialog`) para ubicarlo, y **Exportar a Carpeta** (`DirectoryChooser`).
  6. Doble clic (`MouseEvent`) sobre la fila del cliente → se abre el **Detalle** con todos sus datos, incluida la foto.
  7. Volver al menú, entrar a **Panel de Administración** (solo con `admin`), crear una cuenta con rol "Usuario Común".
  8. Cerrar sesión y volver a entrar con la cuenta recién creada: mostrar que "Registrar Cliente" y "Panel de Administración" ya no están disponibles.
  9. Salir de la aplicación y mostrar el `Alert` de confirmación de cierre.
- **Documento breve de integrantes**: borrador a partir del historial real de commits/ramas del repositorio (`git log --all`); el equipo debe confirmar/ajustar nombres y roles reales antes de entregar:

  | Integrante | Rama(s) / aporte principal |
  |---|---|
  | Bismarck Flores | `util/AlertHelper`, `util/FileChooserHelper`; integración final: `Login`, `SceneManager`, roles/`Session`, `Panel de Administración`, navegación completa entre pantallas y corrección de bugs de integración. |
  | Reynolds Rodríguez | `models/Client`, `dao/ClientDaoImpl` (versión inicial), `controllers/ClientRegisterController` (versión inicial). |
  | Francisco Castro Cuadra | `dao/Dao` (contrato genérico), `models/Request`, `util/SceneManager` (versión inicial, retomada y corregida en la integración final). |
  | Donald (JayWFx) | `models/User`, `models/Role`, `controllers/AppShellController` (versión inicial, como `MainMenuController`). |

## Notas de estado / decisiones

- El `main-view.fxml` / `MainController` originales (plantilla por defecto de JavaFX) se eliminaron: no formaban parte del flujo real de la aplicación.
- `SceneManager` existía en una rama (`feature/Francisco/utils-SceneManager`) que nunca se integró a `dev` y no compilaba (dependía de una interfaz `RecClientData` que no llegó a crearse). Se retomó, se corrigió y se integró como `ReceivesClientData`.
- `ClientDaoImpl` se convirtió en singleton: los controllers se recrean en cada `FXMLLoader.load()`, así que instanciar el DAO por controller rompía el requisito de "paso de datos en memoria entre controllers" (el registro y la búsqueda verían listas distintas).
- El `ContextMenu` del shell no se puede declarar como propiedad de un `VBox`/`StackPane` (esa propiedad solo existe en `Control`); se define con `<fx:define>` y se activa manualmente sobre el evento `onContextMenuRequested`.
- La navegación pasó de "reemplazar toda la ventana por cada pantalla" (`MainMenuController` + `main-menu-view.fxml`) a un shell persistente (`AppShellController` + `app-shell-view.fxml` + `AppShell`): la primera versión hacía que `MenuBar`/`ToolBar` desaparecieran fuera del menú principal y que cada pantalla tuviera su propio tamaño de ventana (`prefWidth`/`prefHeight` distintos por FXML), dando una sensación inconsistente. Ahora todas las pantallas post-login viven dentro del mismo `StackPane`, que las estira a un tamaño de ventana único (960×640); los formularios angostos (Registro, Nueva Cuenta, Detalle) fijan un `maxWidth` y se centran (`alignment="TOP_CENTER"`) en vez de estirarse pegados a la izquierda.
- `ClientRegisterController#onSaveAction` ahora valida también el tipo de cliente, la ciudad, que la fecha de nacimiento no sea futura y que se haya elegido un tipo de solicitud (antes solo validaba nombre/apellido/fecha). `AccountCreateController` valida además un largo mínimo de contraseña.
