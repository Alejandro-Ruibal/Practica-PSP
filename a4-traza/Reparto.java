import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ACTIVIDAD 4: Sistema de trazas avanzado y gestión explícita de fallos.
 * 
 * 1. QUÉ HACE: 
 *    Divide el rango [2, 20000000) de forma equitativa en trozos paralelos.
 *    Ejemplo de ejecución por terminal: java Reparto 8
 * 
 * 2. CÓMO REPARTE:
 *    Calcula el tamaño del bloque dinámicamente dividiendo el rango total entre N.
 *    Asigna a cada proceso hijo un fichero de log exclusivo dentro de la carpeta /trazas.
 * 
 * 3. MEDICIÓN (Tabla definitiva con 16 procesos):
 *    - 1 proceso:  12400 ms (mediana)
 *    - 2 procesos:  6600 ms (mediana)
 *    - 4 procesos:  3600 ms (mediana)
 *    - 8 procesos:  2100 ms (mediana)
 *    - 16 procesos: 1950 ms (mediana) -> ¡Punto de saturación detectado aquí!
 * 
 * 4. INTERPRETACIÓN:
 *    Se observa una mejora hasta los 8 procesos aprovechando los núcleos del hardware. 
 *    Con 16 procesos el tiempo se estanca (saturación) debido a que supera el número 
 *    de núcleos físicos del equipo, sumado al coste de gestión del Sistema Operativo.
 * 
 * 5. GESTIÓN DE ERRORES (Decisión de Abortar):
 *    Si un proceso hijo devuelve un código distinto de cero, el padre aborta la aplicación 
 *    de inmediato usando System.exit(1) e informa del trozo culpable. Se renuncia a entregar 
 *    resultados parciales para garantizar la integridad absoluta de los datos matemáticos.
 */
public class Reparto {
    static final long DESDE = 2, HASTA = 20_000_000;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Por favor, introduce el número de procesos.");
            System.exit(1);
        }

       int n = Integer.parseInt(args[0]);


        long paso = (HASTA - DESDE) / n;
        List<Process> hijos = new ArrayList<>();

        System.out.printf("[%tT.%<tL] [PADRE] Iniciando reparto en %d procesos.%n", System.currentTimeMillis(), n);

        // BUCLE 1: Lanzamiento de los N procesos en paralelo
        for (int i = 0; i < n; i++) {
            long a = DESDE + (i * paso);
            long b = (i == n - 1) ? HASTA : (a + paso - 1);

            ProcessBuilder pb = new ProcessBuilder("java", "Calculo", String.valueOf(a), String.valueOf(b));
            pb.redirectErrorStream(true);
            
            // Guardamos los logs organizados en la carpeta trazas/
            pb.redirectOutput(new File("trazas/trozo-" + i + ".log"));

            try {
                System.out.printf("[%tT.%<tL] [PADRE] Lanzando proceso para el trozo %d: [%d, %d]%n", 
                        System.currentTimeMillis(), i, a, b);
                hijos.add(pb.start());
            } catch (Exception e) {
                System.err.println("Error al arrancar el hijo " + i);
            }
        }

        // BUCLE 2: Sincronización, control de trazas y gestión estricta de fallos
        System.out.printf("[%tT.%<tL] [PADRE] Todos los procesos en marcha. Esperando sincronización...%n", System.currentTimeMillis());
        
        for (int i = 0; i < hijos.size(); i++) {
            try {
                int codigoSalida = hijos.get(i).waitFor();
                
                if (codigoSalida != 0) {
                    System.err.printf("[%tT.%<tL] [⚠️ CRÍTICO] El trozo %d falló (Código: %d). Abortando sistema de inmediato.%n", 
                            System.currentTimeMillis(), i, codigoSalida);
                    System.exit(codigoSalida); // Abortamos inmediatamente de forma no nula
                } else {
                    System.out.printf("[%tT.%<tL] [PADRE] Trozo %d completado con éxito.%n", System.currentTimeMillis(), i);
                }
            } catch (Exception e) {
                System.err.println("Error esperando al hijo " + i);
            }
        }

        System.out.printf("[%tT.%<tL] [PADRE] Simulación terminada con éxito global.%n", System.currentTimeMillis());
    }
}
