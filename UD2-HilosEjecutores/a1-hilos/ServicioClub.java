/**
 * ACTIVIDAD 1 - CASO 3: Hilo Limpiador (Daemon)
 * 
 * EXPLICACIÓN TEÓRICA SOBRE LOS HILOS DAEMON:
 * 
 * - Un hilo demonio (Daemon Thread) es un hilo de baja prioridad que se ejecuta en segundo plano 
 *   para prestar servicios de soporte a los hilos de usuario (como el recolector de basura de Java 
 *   o, en este caso, nuestro limpiador de reservas caducadas).
 * 
 * - La característica fundamental de un hilo marcado con setDaemon(true) es que su existencia 
 *   depende del ciclo de vida de los hilos de usuario. La Máquina Virtual de Java (JVM) finalizará 
 *   y destruirá de forma abrupta todos los hilos demonio en cuanto el último hilo de usuario activo 
 *   (en este caso, el hilo 'main') termine su ejecución.
 * 
 * - Si no hubiéramos configurado el hilo como demonio mediante setDaemon(true), al contener un 
 *   bucle infinito 'while(true)', la JVM se vería obligada a mantener la aplicación colgada indefinidamente, 
 *   impedida de cerrarse a pesar de haber finalizado el hilo principal 'main'.
 */


public class ServicioClub {
    public static void main(String[] args) {
        
        // 1. Definimos la tarea del hilo limpiador (un bucle infinito)
        Runnable tareaLimpiadora = () -> {
            while (true) {
                System.out.println("[Limpiador] Revisando y eliminando reservas caducadas...");
                try {
                    Thread.sleep(100); // Revisa cada 100 milisegundos
                } catch (InterruptedException e) {
                    System.out.println("[Limpiador] Hilo de limpieza interrumpido.");
                    return;
                }
            }
        };

        // 2. Creamos el hilo trabajador
        Thread hiloLimpiador = new Thread(tareaLimpiadora);

        // =====================================================================
        // MARCAR COMO DAEMON (MÁRCALO O DESMÁRCALO PARA CONFIGURAR LA PRUEBA)
        // =====================================================================
        hiloLimpiador.setDaemon(true); // <--- Lo dejamos en 'true' de momento

        // 3. Arrancamos el hilo limpiador
        System.out.println("[Club] Arrancando el servicio de limpieza en segundo plano...");
        hiloLimpiador.start();

        // 4. Simulación del hilo principal (El club funciona durante 3 segundos)
        System.out.println("[Club] El club de vela está abierto y funcionando.");
        try {
            Thread.sleep(3000); // Espera 3000 milisegundos (3 segundos)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 5. El club cierra sus puertas
        System.out.println("[Club] El tiempo ha terminado. Cerrando el club de vela de forma segura.");
        System.out.println("[main] Hilo principal finalizado.");
    }
}
