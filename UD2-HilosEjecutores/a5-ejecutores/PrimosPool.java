import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ACTIVIDAD 5: Pools de Ejecutores e Hilos Virtuales (Java 21)
 * 
 * COMPARATIVA DE TIEMPOS Y CONCLUSIONES:
 * 
 * - PRUEBA A (FixedThreadPool - 12 hilos de plataforma): 1369 ms
 * - PRUEBA B (VirtualThreadPerTaskExecutor - Hilos virtuales): 1322 ms
 * 
 * CONCLUSIÓN TÉCNICA:
 * - El uso de 'ExecutorService' moderniza la gestión de concurrencia al desacoplar 
 *   la creación de tareas ('Runnable') de los hilos físicos mediante un patrón de 
 *   piscina de trabajo (Worker Thread), evitando crear y destruir hilos a mano.
 * 
 * - En la Prueba A, al ajustar el pool al número exacto de núcleos lógicos de la CPU (12 hilos), 
 *   el rendimiento se maximiza de forma espectacular con respecto a un único hilo asíncrono, 
 *   repartiendo los 20 millones de cómputos en bloques paralelos balanceados.
 * 
 * - En la Prueba B, los Hilos Virtuales (Virtual Threads) introducidos en Java 21 demuestran 
 *   un rendimiento excelente (1322 ms). Al estar gestionados directamente por la Máquina 
 *   Virtual (JVM) en lugar de mapearse de forma costosa en hilos del Sistema Operativo, 
 *   eliminan la sobrecarga de memoria y contexto, permitiendo una escalabilidad teóricamente 
 *   infinita de tareas simultáneas de forma ultra ligera y eficiente.
 */


public class PrimosPool {

    // Variable atómica para acumular los primos encontrados de forma segura
    private static final AtomicInteger totalPrimos = new AtomicInteger(0);

    public static void main(String[] args) {
        
        // Medimos el tiempo exacto en el que empieza a ejecutarse el programa
        long tiempoInicio = System.currentTimeMillis();

        // =====================================================================
        // CONFIGURACIÓN DEL POOL (COMENTA O DESCOMENTA SEGÚN LA PRUEBA)
        // =====================================================================
        
        // --- PRUEBA A: Pool de hilos fijo (usa tantos hilos como núcleos tenga tu procesador) ---
       // int nucleos = Runtime.getRuntime().availableProcessors();
        //System.out.println("[main] Creando un FixedThreadPool con " + nucleos + " hilos de plataforma...");
        // ExecutorService ejecutor = Executors.newFixedThreadPool(nucleos);

        // --- PRUEBA B: Hilos Virtuales de Java 21 (Descoméntalo para la segunda prueba) ---
         System.out.println("[main] Creando un VirtualThreadPerTaskExecutor con hilos virtuales ligeros...");
         ExecutorService ejecutor = Executors.newVirtualThreadPerTaskExecutor();

        // =====================================================================

        System.out.println("[main] Repartiendo el cálculo en 20 bloques de tareas...");

        // Dividimos los 20 millones en 20 bloques de 1 millón cada uno
        int tamañoBloque = 1000000;
        for (int i = 0; i < 20; i++) {
            final int inicio = (i * tamañoBloque) + 1;
            final int fin = (i + 1) * tamañoBloque;

            // Enviamos la tarea directamente al ejecutor en lugar de crear un hilo a mano
            ejecutor.submit(() -> {
                int contadorBloque = 0;
                for (int n = inicio; n <= fin; n++) {
                    if (esPrimo(n)) {
                        contadorBloque++;
                    }
                }
                // Añadimos el resultado parcial al contador total acumulado de forma segura
                totalPrimos.addAndGet(contadorBloque);
            });
        }

        // Le ordenamos al ejecutor que apague las máquinas una vez terminen sus tareas
        ejecutor.shutdown();

        try {
            // El hilo main se para a esperar un máximo de 10 minutos a que terminen todas las tareas del pool
            if (ejecutor.awaitTermination(10, TimeUnit.MINUTES)) {
                long tiempoFin = System.currentTimeMillis();
                long tiempoTotal = tiempoFin - tiempoInicio;

                System.out.println("--------------------------------------------------");
                System.out.println("RESULTADO OBTENIDO: " + totalPrimos.get() + " primos.");
                System.out.println("TIEMPO TOTAL DE EJECUCIÓN: " + tiempoTotal + " milisegundos.");
                System.out.println("--------------------------------------------------");
            }
        } catch (InterruptedException e) {
            System.out.println("[main] La espera en el pool ha sido interrumpida.");
        }
    }

    // Algoritmo eficiente para comprobar si un número es primo
    private static boolean esPrimo(int numero) {
        if (numero <= 1) return false;
        if (numero == 2) return true;
        if (numero % 2 == 0) return false;
        for (int i = 3; i * i <= numero; i += 2) {
            if (numero % i == 0) return false;
        }
        return true;
    }
}
