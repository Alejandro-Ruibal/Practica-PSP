/**
 * ACTIVIDAD 4: Diagnóstico y Resolución de Interbloqueos (Deadlock)
 * 
 * INFORME DE DIAGNÓSTICO DEL DEADLOCK:
 * - SÍNTOMAS: Al ejecutar el código original, el programa se congelaba indefinidamente tras mostrar los primeros mensajes.
 * - HERRAMIENTA UTILIZADA: Se empleó el comando 'jcmd <PID> Thread.print' desde una segunda consola del sistema.
 * - RESULTADO DEL INFORME: La herramienta de la JDK confirmó explícitamente: "Found 1 deadlock."
 * - CAUSA RAÍZ: El 'Hilo-Transferencia-A' adquirió el candado de cuentaOrigen y se quedó esperando cuentaDestino. 
 *   Al mismo tiempo, el 'Hilo-Transferencia-B' adquirió cuentaDestino y se quedó esperando cuentaOrigen. 
 *   Se produjo una dependencia circular cíclica irreversible.
 * 
 * SOLUCIÓN APLICADA:
 * - Se ha reordenado la adquisición de monitores en el 'Hilo-Transferencia-B'. Ahora ambos hilos solicitan los 
 *   candados en el mismo orden lineal: primero 'cuentaOrigen' y luego 'cuentaDestino'. Esto elimina por completo 
 *   la posibilidad de una espera mutua, garantizando una ejecución segura y fluida.
 */

public class GestionCuentas {

    private static final Object cuentaOrigen = new Object();
    private static final Object cuentaDestino = new Object();

    public static void main(String[] args) {

        // Hilo A: Bloquea primero Origen y luego Destino
        Thread hiloA = new Thread(() -> {
            synchronized (cuentaOrigen) {
                System.out.println("[Hilo-A] He bloqueado la Cuenta Origen...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                System.out.println("[Hilo-A] Intentando bloquear la Cuenta Destino...");
                synchronized (cuentaDestino) {
                    System.out.println("[Hilo-A] ¡Transferencia A realizada con éxito!");
                }
            }
        }, "Hilo-Transferencia-A");

        // Hilo B SOLUCIONADO: Ahora TAMBIÉN bloquea primero Origen y luego Destino
        Thread hiloB = new Thread(() -> {
            synchronized (cuentaOrigen) { // <-- CAMBIADO: Antes ponía cuentaDestino
                System.out.println("[Hilo-B] He bloqueado la Cuenta Origen...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}

                System.out.println("[Hilo-B] Intentando bloquear la Cuenta Destino...");
                synchronized (cuentaDestino) { // <-- CAMBIADO: Antes ponía cuentaOrigen
                    System.out.println("[Hilo-B] ¡Transferencia B realizada con éxito!");
                }
            }
        }, "Hilo-Transferencia-B");

        System.out.println("[main] Iniciando transferencias ordenadas sin riesgo de deadlock...");
        hiloA.start();
        hiloB.start();
    }
}
