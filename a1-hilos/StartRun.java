/**
 * ACTIVIDAD 1 - CASO 1: Start vs Run
 * 
 * EXPLICACIÓN TEÓRICA DE LAS DIFERENCIAS:
 * - El método start() solicita a la Máquina Virtual de Java (JVM) la creación 
 *   de un nuevo hilo de ejecución en el sistema operativo. Como hemos visto en la 
 *   primera traza, esto permite que las tareas de 'main' y de 'hilo-trabajador' 
 *   se ejecuten de forma concurrente y asíncrona, entrelazando sus salidas.
 * 
 * - El método run() no crea ningún hilo nuevo. Simplemente realiza una llamada 
 *   a un método ordinario sobre el mismo hilo que realiza la invocación (en este caso, 'main'). 
 *   Por tanto, la ejecución se vuelve síncrona y secuencial: el hilo principal se 
 *   bloquea ejecutando todo el bucle de la tarea antes de continuar con su propio bucle, 
 *   motivo por el cual en la segunda traza solo vemos al hilo 'main' trabajar de forma consecutiva.
 */


public class StartRun {
    public static void main(String[] args) {
        
        // Definimos la tarea que va a ejecutar el hilo
        Runnable tarea = () -> {
            String yo = Thread.currentThread().getName(); // Consigue el nombre del hilo
            for (int i = 1; i <= 3; i++) {
                System.out.println(yo + " va por " + i);
                try { 
                    Thread.sleep(200); // Pausa de 200 milisegundos
                } catch (InterruptedException e) { 
                    Thread.currentThread().interrupt(); 
                    return; 
                }
            }
        };

        // Creamos el hilo trabajador con un nombre asignado
        Thread hilo = new Thread(tarea, "hilo-trabajador");

        // --- PRUEBA 1: Ejecución usando .start() ---
        System.out.println("--- EJECUCIÓN CON START ---");
        hilo.run(); 

        // El hilo principal (main) también hace su propio bucle
        for (int i = 1; i <= 3; i++) {
            System.out.println("main va por " + i);
            try { 
                Thread.sleep(200); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
