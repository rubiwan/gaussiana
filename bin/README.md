# Implementación del método de eliminación gaussiana (pivoteo parcial escalado)

Esta aplicación implementa el método de eliminación Gaussiana 
con pivoteo parcial escalado para resolver sistemas de ecuaciones lineales. 

El pivoteo parcial escalado mejora la estabilidad numérica del algoritmo al seleccionar el pivote más adecuado 
en cada paso de la eliminación.

## Estructura del proyecto
Implementa el patrón Modelo-Vista-Controlador (MVC).

- `src/`: Código fuente de la aplicación
    - `AppGaussiana.java`: Clase principal que ejecuta la aplicación
    - `controller/`
        - `GaussianaController.java`: Controlador principal
    - `logic/`
        - `Sistema.java`: Implementación del algoritmo de eliminación Gaussiana
    - `view/`
        - `VentanaPrincipal.java`: Interfaz de usuario

## Requisitos
- Java Development Kit (JDK) 8 o superior.
- Un entorno de desarrollo integrado (IDE) como IntelliJ IDEA, Eclipse o NetBeans.

## Cómo ejecutar la aplicación
1. Descarga el código fuente.
2. Abre el proyecto en tu IDE favorito.
3. Importa el proyecto.
4. Ejecuta la clase `AppGaussiana.java`.
5. Sigue las instrucciones en la interfaz de usuario para ingresar el sistema de ecuaciones y obtener la solución.

## Autor
Anabel Díaz

## Asignatura
Álgebra y Matemática Discreta - Universidad Internacional de La Rioja (UNIR)
Noviembre de 2025

