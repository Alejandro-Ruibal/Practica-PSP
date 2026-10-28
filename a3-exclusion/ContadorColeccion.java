import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ACTIVIDAD 3 - SOLUCIÓN 3: Sincronización con Colecciones Concurrentes
 * 
 * EXPLICACIÓN TÉCNICA DE LA SOLUCIÓN:
 * (Rellenaremos esta sección en el siguiente paso cuando verifiquemos el resultado)
 */

public class ContadorColeccion {

    // Usamos una cola concurrente y segura para hilos en lugar de una variable directa
    private static final ConcurrentLinkedQueue<Integer> colaElementos = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        
        Runnable tareaIncrementar = () -> {
            for (int i = 0; i < 10000; i++) {
                // Cada incremento añade un elemento a la cola de forma segura
                colaElementos.add(1); 
            }
        };

        Thread hiloA = new Thread(tareaIncrementar, "Hilo-A");
        Thread hiloB = new Thread(tareaIncrementar, "Hilo-B");

        System.out.println("[main] Arrancando hilos protegidos con 'ConcurrentLinkedQueue'...");
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
        // El tamaño final de la cola nos dirá cuántos incrementos reales se han guardado sin pérdidas
        System.out.println("RESULTADO REAL OBTENIDO: " + colaElementos.size()); 
        System.out.println("--------------------------------------------------");
    }
}
