/**
 * ACTIVIDAD 2: Condición de Carrera (Race Condition)
 * 
 * EXPLICACIÓN DEL PROBLEMA DE CONCURRENCIA DETECTADO:
 * 
 * - Se ha detectado una Condición de Carrera (Race Condition) debido a que dos hilos 
 *   ('Hilo-A' e 'Hilo-B') acceden y modifican de forma simultánea y desprotegida 
 *   la misma variable compartida en memoria ('contador').
 * 
 * - La operación de incremento 'contador++' NO es atómica. En realidad, se divide en 3 pasos a nivel de CPU:
 *   1. Leer el valor actual de la variable en memoria.
 *   2. Incrementar ese valor en 1 en un registro temporal.
 *   3. Escribir el nuevo valor de vuelta en la memoria.
 * 
 * - Al no haber sincronización, los hilos se intercalan constantemente de forma caótica. Por ejemplo, 
 *   si el contador vale 10, ambos hilos pueden leer el valor 10 a la vez. Ambos le suman 1 (dando 11) 
 *   y ambos escriben el valor 11. Tras dos incrementos reales, el contador solo ha subido a 11 en lugar de a 12, 
 *   perdiéndose una actualización. Esta superposición masiva explica por qué el resultado real es notablemente 
 *   inferior a los 20.000 esperados.
 * 
 * NOTA DE DISEÑO: De acuerdo con los requisitos de la Actividad 2, este código se deja roto intencionadamente 
 * para demostrar el fallo de concurrencia. La solución se implementará en la Actividad 3.
 */


public class ContadorCarrera {

    // Variable compartida por ambos hilos
    private static int contador = 0;

    public static void main(String[] args) {
        
        // Definimos la tarea que realizarán ambos hilos: incrementar 10.000 veces
        Runnable tareaIncrementar = () -> {
            for (int i = 0; i < 10000; i++) {
                // Operación no atómica (Lectura -> Modificación -> Escritura)
                contador++; 
            }
        };

        // Creamos dos hilos independientes que ejecutan la misma tarea sobre la misma variable
        Thread hiloA = new Thread(tareaIncrementar, "Hilo-A");
        Thread hiloB = new Thread(tareaIncrementar, "Hilo-B");

        System.out.println("[main] Arrancando Hilo-A e Hilo-B simultáneamente...");
        
        // Los lanzamos a la vez en paralelo
        hiloA.start();
        hiloB.start();

        // Esperamos obligatoriamente a que ambos terminen para poder ver el resultado final
        try {
            hiloA.join();
            hiloB.join();
        } catch (InterruptedException e) {
            System.out.println("[main] Interrumpido.");
        }

        // Imprimimos el resultado obtenido
        System.out.println("--------------------------------------------------");
        System.out.println("RESULTADO ESPERADO: 20000");
        System.out.println("RESULTADO REAL OBTENIDO: " + contador);
        System.out.println("--------------------------------------------------");
    }
}
