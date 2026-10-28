/**
 * ACTIVIDAD 1 - CASO 2: Esperas con join()
 * 
 * RESPUESTAS A LAS PREGUNTAS DEL ENUNCIADO:
 * 
 * 1. ¿Qué ocurre si no se usa join() antes de consultar el resultado?
 *    Como el hilo principal 'main' se ejecuta de forma asíncrona y en paralelo al hilo de cálculo,
 *    avanza a toda velocidad sin esperar. Al consultar la variable 'totalPrimos' inmediatamente, 
 *    el hilo secundario aún no ha tenido tiempo de calcular nada, obteniendo siempre un resultado incorrecto de 0 primos.
 * 
 * 2. ¿Cómo soluciona join() este problema de sincronización?
 *    El método join() bloquea el progreso del hilo invocador (en este caso, 'main') y lo obliga a quedarse 
 *    en estado de espera hasta que el hilo sobre el que se invoca ('hiloTrabajador') finalice por completo su ejecución.
 *    Esto garantiza de forma segura que el hilo principal solo leerá el resultado una vez que el cálculo ha terminado,
 *    obteniendo el valor exacto de 1.270.607 números primos.
 */


public class PrimosJoin {

    // Variable global para guardar el resultado del conteo
    private static int totalPrimos = 0;

    public static void main(String[] args) {
        
        // Definimos la tarea pesada: contar números primos
        Runnable tareaCalculo = () -> {
            System.out.println("[Hilo-Primos] Iniciando cálculo de primos...");
            int contador = 0;
            
            // Bucle que recorre desde 2 hasta 20 millones
            for (int i = 2; i <= 20000000; i++) {
                if (esPrimo(i)) {
                    contador++;
                }
            }
            totalPrimos = contador; // Guardamos el resultado final
            System.out.println("[Hilo-Primos] ¡Cálculo finalizado!");
        };

        // Creamos el hilo trabajador
        Thread hiloTrabajador = new Thread(tareaCalculo);

        System.out.println("[main] Lanzando el hilo de cálculo...");
        hiloTrabajador.start(); // Arrancamos el hilo en paralelo

        // =====================================================================
        // PRUEBA A: Descomenta la línea de abajo para probar SIN usar join()
        // PRUEBA B: Comenta la línea de abajo y descomenta el bloque try-catch para probar CON join()
        // =====================================================================
        
        // --- De momento dejamos esta línea activa para ver qué pasa SIN join ---
       // obtenerResultadoSinEspera();

        
        try {
            System.out.println("[main] Esperando a que el hilo termine con join()...");
            hiloTrabajador.join(); // El hilo main se para aquí a esperar
            System.out.println("[main] Hilo trabajador finalizado de forma segura.");
            System.out.println("RESULTADO FINAL: Se han encontrado " + totalPrimos + " números primos.");
        } catch (InterruptedException e) {
            System.out.println("[main] La espera ha sido interrumpida.");
        }
        
    }

    // Método auxiliar para imprimir el resultado de golpe sin esperar
    private static void obtenerResultadoSinEspera() {
        System.out.println("RESULTADO INMEDIATO: Se han encontrado " + totalPrimos + " números primos.");
    }

    // Algoritmo eficiente para comprobar si un número es primo
    private static boolean esPrimo(int numero) {
        if (numero <= 1) return false;
        if (numero == 2) return true;
        if (numero % 2 == 0) return false;
        for (int i = 3; i * i <= numero; i += 2) {
            if (numero % i == 0) return false;
        }
        return true;
    }
}
