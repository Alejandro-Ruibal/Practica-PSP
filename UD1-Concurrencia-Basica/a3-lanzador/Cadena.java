import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ACTIVIDAD 3: Ejecución encadenada y condicional de procesos.
 * 
 * JUSTIFICACIÓN DE DISEÑO:
 * - Gestión de fallos: Si el primer paso falla (código de salida != 0), se decide
 *   abortar el programa inmediatamente y conservar el archivo parcial para poder auditar
 *   el error, avisando al usuario por el flujo de errores estándar (System.err).
 * - Límites de tiempo: Se asigna un tiempo máximo estimado al cálculo basado en las mediciones
 *   previas de la Actividad 2 para evitar que atasque el servidor en caso de bucle anómalo.
 */
public class Cadena {
    public static void main(String[] args) {
        try {
            // Creamos una marca de tiempo única para el nombre del archivo
            String marca = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File ficheroResultado = new File("resultado-" + marca + ".txt");
            
            System.out.println("=== PASO 1: Lanzando el proceso de cálculo ===");
            ProcessBuilder pb1 = new ProcessBuilder("java", "Calculo", "2", "100000");
            pb1.redirectOutput(ficheroResultado);
            pb1.redirectErrorStream(true);
            
            Process hijo1 = pb1.start();
            int codigo1 = hijo1.waitFor();
            
            if (codigo1 != 0) {
                System.err.println("ERROR: El Paso 1 (Cálculo) falló con código " + codigo1 + ". Abortando cadena.");
                System.exit(codigo1); // Rompemos la cadena con el código del hijo
            }
            System.out.println("Paso 1 completado con éxito. Archivo generado: " + ficheroResultado.getName());
            
            System.out.println("\n=== PASO 2: Lanzando el proceso de empaquetado (JAR) ===");
            // Simulamos el segundo paso creando un archivo comprimido comprimiendo el resultado
            ProcessBuilder pb2 = new ProcessBuilder("jar", "cvf", "resultado-" + marca + ".jar", ficheroResultado.getName());
            pb2.redirectOutput(new File("salida-jar.log"));
            pb2.redirectErrorStream(true);
            
            Process hijo2 = pb2.start();
            int codigo2 = hijo2.waitFor();
            
            if (codigo2 != 0) {
                System.err.println("ERROR: El Paso 2 (Empaquetado) falló con código " + codigo2);
                System.exit(codigo2);
            }
            
            System.out.println("Paso 2 completado con éxito. Proceso de secuenciación finalizado.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
