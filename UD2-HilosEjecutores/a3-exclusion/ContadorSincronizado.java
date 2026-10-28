/**
 * ACTIVIDAD 3 - SOLUCIÓN 1: Exclusión Mutua con Bloque Synchronized
 * 
 * EXPLICACIÓN TÉCNICA DE LA SOLUCIÓN:
 * 
 * - Esta solución implementa la exclusión mutua mediante un bloque 'synchronized' en Java.
 *   Utiliza una referencia común e inmutable ('candado') que actúa como monitor o cerradura.
 * 
 * - Cuando el 'Hilo-A' entra en el bloque synchronised, adquiere el monitor de dicho objeto. 
 *   Si el 'Hilo-B' intenta acceder a la misma sección crítica mientras el monitor está ocupado, 
 *   la Máquina Virtual de Java (JVM) lo suspende y lo pone en estado de bloqueo (Blocked).
 * 
 * - Esto garantiza que la operación de lectura-modificación-escritura del contador se ejecute de 
 *   forma estrictamente secuencial y aislada por un único hilo a la vez. Cuando el hilo poseedor 
 *   sale del bloque, libera el monitor, permitiendo al otro hilo proceder de forma segura. 
 *   Por esta razón, el resultado es siempre robusto y alcanza exactamente los 20.000 incrementos.
 */


public class ContadorSincronizado {

    private static int contador = 0;
    
    // Objeto testigo (candado) sobre el cual sincronizaremos los hilos
    private static final Object candado = new Object();

    public static void main(String[] args) {
        
        Runnable tareaIncrementar = () -> {
            for (int i = 0; i < 10000; i++) {
                // Sincronizamos el acceso utilizando el bloque con el objeto candado
                synchronized (candado) {
                    contador++; 
                }
            }
        };

        Thread hiloA = new Thread(tareaIncrementar, "Hilo-A");
        Thread hiloB = new Thread(tareaIncrementar, "Hilo-B");

        System.out.println("[main] Arrancando hilos protegidos con 'synchronized'...");
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
        System.out.println("RESULTADO REAL OBTENIDO: " + contador);
        System.out.println("--------------------------------------------------");
    }
}
