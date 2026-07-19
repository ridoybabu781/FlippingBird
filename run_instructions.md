# How to Run FlippingBird

This document provides step-by-step instructions on compiling and running the game on different environments.

## Prerequisites

- **Java Development Kit (JDK)**: Make sure you have JDK 8 or higher installed on your system. You can verify this by running:
  ```bash
  java -version
  ```
  and
  ```bash
  javac -version
  ```

---

## 1. Running from the Command Line (Terminal)

This is the quickest way to compile and run the game directly from your terminal.

### Step 1: Navigate to the Project Root Directory
Ensure you are in the `/Volumes/2BT/Ridoy/Project/FlippingBird` directory.

### Step 2: Compile the Java Source Files
Run the following command to compile all `.java` files and output the compiled classes to the `bin/` directory:
```bash
javac -d bin src/main/java/com/flippingbird/*.java
```

### Step 3: Run the Game
Execute the entry-point class `com.flippingbird.Main` with the classpath (`-cp`) pointing to the `bin/` directory:
```bash
java -cp bin com.flippingbird.Main
```

---

## 2. Running in Visual Studio Code (VS Code)

If you are using VS Code, follow these steps:

1. **Install Java Extension Pack**: Ensure you have the "Extension Pack for Java" installed from the VS Code Marketplace.
2. **Open the Project Folder**: Open the `/Volumes/2BT/Ridoy/Project/FlippingBird` directory in VS Code.
3. **Run Main.java**:
   - Open [Main.java](file:///Volumes/2BT/Ridoy/Project/FlippingBird/src/main/java/com/flippingbird/Main.java) in the editor.
   - Click the **Run** button that appears above the `main` method (or press `F5` / `Ctrl+F5`).

---

## 3. Running in IntelliJ IDEA

If you are using IntelliJ IDEA:

1. **Open Project**: Select **Open** and select the `/Volumes/2BT/Ridoy/Project/FlippingBird` directory.
2. **Set JDK**: Make sure a valid JDK is selected in **File > Project Structure > Project**.
3. **Set Sources Root**: Ensure the directory `src/main/java` is marked as a **Sources Root** (Right-click `java` -> **Mark Directory as** -> **Sources Root**).
4. **Run**: Open [Main.java](file:///Volumes/2BT/Ridoy/Project/FlippingBird/src/main/java/com/flippingbird/Main.java), right-click inside the file, and select **Run 'Main.main()'**.

---

## Controls

Once the game is running, you can use the following keyboard controls:

| Key | Action |
| --- | --- |
| **SPACE** | Jump / Start Game |
| **P** | Pause / Resume |
| **R** | Restart (after Game Over) |
| **ESC** | Exit Game |

---

## Asset Generation
If the `assets/` folder is empty or missing, the game will automatically generate all necessary sprites (`bird.png`, `pipe.png`, `background.png`) and sounds (`jump.wav`, `score.wav`, `hit.wav`) upon startup.
