# Neww Clientt

A dark, animated JavaFX Minecraft launcher UI with a moving starfield.

## Requirements

- Java 21+
- Maven 3.9+

## Run

```bash
mvn javafx:run
```

The interface uses JavaFX `AnimationTimer` for a smooth, continuously animated starfield. The UI targets a 120 FPS presentation where the display/runtime supports it; actual frame rate is ultimately limited by the JavaFX pulse and display refresh rate.

## Project

- `src/main/java/com/neww/clientt/LauncherApp.java` — launcher UI and animated starfield
- `pom.xml` — Java 21 + JavaFX build configuration
- `.github/workflows/build.yml` — CI build
