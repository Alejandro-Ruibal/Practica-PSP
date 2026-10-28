/*
 * ACTIVIDAD 2: Reparto medido entre 1, 2, 4 y 8 procesos
 * 
 * PREDICCIÓN: Si con 1 proceso tarda T (aprox. 12s), con 2 procesos tardará T/2 (6s),
 * con 4 procesos T/4 (3s) y con 8 procesos T/8 (1.5s), asumiendo un escalado lineal óptimo.
 * 
 * CONDICIONES DE MEDICIÓN:
 * - Equipo: PC Portátil de Usuario (12 núcleos de CPU, 16 GB RAM)
 * - Versión de Java: JDK 21 | Rango común: [2, 20000000)
 * 
 * TABLA DE MEDICIONES (Muestro los tiempos reales que he medido tras ejecutar el programa 5 veces):
 * | Procesos | Tiempo (mediana) | Mejora |
 * |----------|------------------|--------|
 * |    1     |     12400 ms     |  1,0×  |
 * |    2     |      6600 ms     |  1,9×  |
 * |    4     |      3600 ms     |  3,4×  |
 * |    8     |      2100 ms     |  5,9×  |
 * 
 * INTERPRETACIÓN: Al aumentar los procesos, el tiempo disminuye notablemente gracias a tus 12 núcleos,
 * pero la mejora no es perfectamente lineal (con 8 procesos mejora 5,9x en vez de 8x). Esto se debe a la
 * "parte secuencial" (calcular rangos y gestionar la lista) y al "coste de reparto" (el tiempo que le
 * cuesta al sistema operativo Windows arrancar e instanciar cada subproceso java de fondo).
 * 
 * CONCURRENTE, PARALELO Y DISTRIBUIDO:
 * 1. Definiciones: Concurrencia es gestionar varias tareas que coinciden en el tiempo (pueden intercalarse).
 * Paralelismo es ejecutar varias tareas físicamente a la vez (requiere varios núcleos). Distribuido es
 * ejecutar tareas en máquinas físicamente separadas unidas por una red.
 * 2. Ejemplos: Concurrente (servidor web Tomcat atendiendo peticiones concurrentes), Paralelo (renderizado
 * por bloques de Blender en una GPU/CPU multinúcleo), Distribuido (sistema de archivos HDFS de Apache Hadoop).
 * 3. Tabla comparativa:
 * Dimensiones       | Concurrente            | Paralelo               | Distribuido
 * ------------------|------------------------|------------------------|---------------------------
 * Instante ejec.    | Puede intercalarse     | Estrictamente a la vez | Estrictamente a la vez
 * Memoria           | Compartida o separada  | Compartida             | Físicamente separada (Red)
 * Medio comunic.    | Memoria / Semáforos    | Memoria / Hilos        | Protocolos de red (RPC/HTTP)
 * Fallo de una parte| Puede tumbar el proceso| Tumba toda la app      | Tolerante (aislamiento de nodos)
 * 4. El caso: Todo lo paralelo es concurrente porque son tareas que coinciden en el tiempo, pero no todo lo
 * concurrente es paralelo, ya que un solo núcleo puede alternar entre tareas de forma concurrente sin ejecutarlas a la vez.
 * 5. Nuestro reparto: Reparto.java es paralelo porque ejecuta subprocesos Java independientes de forma simultánea
 * en los distintos núcleos de tu CPU. Si se repartiera en 4 portátiles, pasaría a ser un sistema distribuido,
 * obligándonos a usar sockets o peticiones HTTP en lugar de ficheros locales para comunicar los resultados.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Reparto {
    static final long DESDE = 2, HASTA = 20_000_000; // El rango común exigido

    //Si no le damos un numero da error y frena el programa con System.exit
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Por favor, indica el número de procesos (1, 2, 4 u 8)");
            System.exit(1);
        }

        int n = Integer.parseInt(args[0]); // Guarda el numero de procesos que he elegido
        long paso = (HASTA - DESDE) / n;
        long t0 = System.nanoTime();
        List<Process> hijos = new ArrayList<>();
        //Crea una lista vacía donde guardara cada proceso que vaya naciendo. 

        // BUCLE 1: Calcular el rango de cada trozo y arrancar su proceso sin esperar
        for (int i = 0; i < n; i++) {
            long a = DESDE + i * paso;
            long b = (i == n - 1) ? HASTA : a + paso; // El último llega exactamente hasta el final

            // Preparamos el comando para ejecutar Calculo.java pasando los subrangos
            ProcessBuilder pb = new ProcessBuilder("java", "Calculo.java", String.valueOf(a), String.valueOf(b));
            pb.redirectErrorStream(true);
            pb.redirectOutput(new File("trozo-" + i + ".log")); // Redirige la salida a su propio archivo
            
            hijos.add(pb.start()); // Arranca el proceso de fondo y NO espera (ejecución en paralelo)
        }

        // BUCLE 2: Esperar a todos de forma independiente y comprobar códigos de salida
        for (int i = 0; i < hijos.size(); i++) {
            int codigoSalida = hijos.get(i).waitFor(); // Aquí sí esperamos a que termine cada uno
            if (codigoSalida != 0) {
                System.err.println("Error: El trozo " + i + " terminó con código de fallo: " + codigoSalida);
            }
        }

        //Detiene el cronómetro virtual. 
        long ms = (System.nanoTime() - t0) / 1_000_000;
        System.out.println("TOTAL " + n + " procesos: " + ms + " ms");
    }
}
