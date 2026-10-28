/**
 * ACTIVIDAD 3 - SOLUCIÓN 2: Exclusión Mutua con Variables Atómicas
 * 
 * EXPLICACIÓN TÉCNICA DE LA SOLUCIÓN:
 * 
 * - Esta solución utiliza la clase 'AtomicInteger' del paquete 'java.util.concurrent.atomic'. 
 *   A diferencia de 'synchronized', esta técnica no utiliza bloqueos tradicionales (es "lock-free").
 * 
 * - En su lugar, se apoya en instrucciones atómicas del hardware (a nivel de CPU) conocidas como 
 *   CAS (Compare-And-Swap / Comparar y Intercambiar). El método 'incrementAndGet()' lee el valor, 
 *   intenta cambiarlo y, si detecta que otro hilo lo ha cambiado en ese instante, reintenta la 
 *   operación automáticamente en un bucle interno ultrarrápido.
 * 
 * - La operación se realiza de forma atómica e indivisible para el sistema operativo, impidiendo 
 *   que un hilo pise los datos del otro a mitad de la lectura y escritura. Esto evita el coste 
 *   de rendimiento que supone pausar y reactivar hilos, ofreciendo un resultado de 20.000 perfecto.
 */

import java.util.concurrent.atomic.AtomicInteger;

public class ContadorAtomico {

    // En lugar de un int normal, usamos una clase atómica optimizada para hilos
    private static final AtomicInteger contador = new AtomicInteger(0);

    public static void main(String[] args) {
        
        Runnable tareaIncrementar = () -> {
            for (int i = 0; i < 10000; i++) {
                // El método incrementAndGet() realiza la operación completa en un solo paso indivisible a nivel de hardware
                contador.incrementAndGet(); 
            }
        };

        Thread hiloA = new Thread(tareaIncrementar, "Hilo-A");
        Thread hiloB = new Thread(tareaIncrementar, "Hilo-B");

        System.out.println("[main] Arrancando hilos protegidos con 'AtomicInteger'...");
        hiloA.start();
        hiloB.start();

        try {
            hiloA.join();
            hiloB.join();
        } catch (InterruptedException e) {
            System.out.println("[main] Interrumpido.");
        }

        System.out.println("--------------------------------------------------");
        System.out.println("RESULTADO ESPERADO: 20000");
        System.out.println("RESULTADO REAL OBTENIDO: " + contador.get()); // Leemos el valor final con .get()
        System.out.println("--------------------------------------------------");
    }
}
