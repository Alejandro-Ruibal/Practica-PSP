import java.io.File;

/**
 * ACTIVIDAD 3: Simulación intencionada de Interbloqueo (Deadlock).
 * 
 * OBSERVACIONES DE LA EXPERIMENTACIÓN:
 * - Qué se observa: El programa se queda congelado indefinidamente en la línea del waitFor().
 * - Tiempo de espera: Tras esperar varios minutos, el proceso no avanza ni termina.
 * - Explicación teórica: Al lanzar un comando que genera mucha salida de texto sin redirigir
 *   su flujo a un fichero ni leerlo mediante un búfer en Java, el búfer intermedio del 
 *   sistema operativo se llena por completo. El proceso hijo se bloquea esperando a que 
 *   el padre libere espacio, y el padre se bloquea esperando al hijo con waitFor(),
 *   provocando un interbloqueo clásico.
 */
public class Colgado {
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando simulación de bloqueo... (Búfer sin vaciar)");
            
            // Ejecutamos Calculo con un rango amplio para que genere salida masiva,
            // pero INTENCIONADAMENTE no leemos su getInputStream() ni redirigimos a archivo.
            ProcessBuilder pb = new ProcessBuilder("java", "Calculo", "2", "5000000");
            Process hijo = pb.start();
            
            System.out.println("Esperando al proceso hijo con waitFor(). Se congelará aquí...");
            int codigoSalida = hijo.waitFor(); // Aquí ocurre el interbloqueo eterno
            
            System.out.println("Este mensaje nunca se llegará a imprimir: " + codigoSalida);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
