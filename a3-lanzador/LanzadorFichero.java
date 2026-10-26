import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * ACTIVIDAD 3: Lanzador seguro con redirección a fichero y límite de tiempo.
 * 
 * JUSTIFICACIÓN DE DISEÑO:
 * - Se utiliza redirectOutput(File) para delegar la escritura en el sistema operativo,
 *   lo que elimina por completo el riesgo de interbloqueo por búfer lleno.
 * - Se impone un control de tiempo límite (Timeout) de 3 segundos mediante waitFor(long, TimeUnit).
 * - Si el tiempo expira, se inicia un protocolo de cierre seguro llamando a destroy() 
 *   y, si no responde, a destroyForcibly() para liberar recursos.
 */
public class LanzadorFichero {

    public static void main(String[] args) {
        try {
            // Simulamos un comando (puedes cambiarlo por el que quieras probar)
            // Para la práctica, ejecutamos Calculo con un rango enorme para probar el timeout
            ProcessBuilder pb = new ProcessBuilder("java", "Calculo", "2", "200000000");
            
            // Redirigimos la salida a un fichero físico para evitar bloqueos de búfer
            File ficheroSalida = new File("salida-lanzador.txt");
            pb.redirectOutput(ficheroSalida);
            pb.redirectErrorStream(true);
            
            System.out.println("Iniciando proceso con límite de tiempo de 3 segundos...");
            Process hijo = pb.start();
            
            // Esperamos un máximo de 3 segundos a que termine
            boolean terminadoA_Tiempo = hijo.waitFor(3, TimeUnit.SECONDS);
            
            if (!terminadoA_Tiempo) {
                System.err.println("¡ALERTA! El proceso ha superado el tiempo límite de 3 segundos.");
                System.err.println("Procediendo a la terminación controlada del subproceso...");
                
                // Intentamos un cierre ordenado
                hijo.destroy();
                
                // Esperamos un instante corto para ver si se cierra bien
                if (!hijo.waitFor(1, TimeUnit.SECONDS)) {
                    System.err.println("El proceso no responde al cierre ordenado. Forzando matado (kill -9)...");
                    hijo.destroyForcibly(); // Cierre forzoso
                }
                
                System.err.println("Subproceso abortado con éxito. Recursos liberados.");
            } else {
                int codigoSalida = hijo.exitValue();
                System.out.println("El proceso terminó a tiempo con código de salida: " + codigoSalida);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
