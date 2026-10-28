/*
 * ACTIVIDAD 1: Inventario de procesos y estados
 * RECUENTO: 12 núcleos de CPU y 153 procesos vivos en tasklist.
 * CLASIFICACIÓN DE PROCESOS (Windows tasklist):
 * - explorer.exe: Aplicación de usuario. Interfaz gráfica y navegación de archivos del usuario.
 * - svchost.exe: Servicio del sistema. Aloja procesos de servicios críticos de Windows en segundo plano.
 * - msedge.exe: Aplicación de usuario. Navegador web interactivo iniciado directamente por mí.
 * - conhost.exe: Proceso auxiliar. Consola de comandos auxiliar que da soporte a las terminales.
 * - spoolsv.exe: Servicio del sistema. Gestiona de forma autónoma la cola de impresión del equipo.
 * PASO 5 (Consumo CPU): Es prácticamente cero (0%) porque el proceso entra en estado BLOQUEADO
 * al invocar System.in.read(). El sistema operativo lo saca de la CPU hasta que reciba un evento.
 * PASO 6 (Lectura de fichero): Al leer un flujo grande, el proceso pasa alternativamente por los
 * estados LISTO y EN EJECUCIÓN, compitiendo por la CPU, lo que eleva el uso del procesador notablemente.
 * PASO 7 (Finalizar tarea): Al matarlo con "Finalizar tarea" desde Windows, el proceso termina de forma
 * abrupta y devuelve un código de salida distinto de cero (habitualmente el código de terminación 1).
 * PASO 8 (DILEMA HILO VS PROCESO - Tarea de liberación de reservas caducadas):
 * Decisión: Se debe implementar como un HILO dentro del propio servidor de la aplicación.
 * - ¿Comparte estado?: Sí, necesita acceder a la lista de reservas activas en memoria constantemente.
 * - ¿Puede tumbar al servidor?: Sí, si el hilo tiene una excepción no capturada podría comprometerlo.
 * - ¿Tiene que correr si el servidor para?: No, si el servidor está caído no hay reservas que liberar.
 * - ¿Cuántas veces se lanza?: Muchas (cada minuto). Crear un proceso nuevo cada vez sería costoso.
 * PASO 9 (LÍNEAS DE COSTE):
 * Con la opción elegida (hilo) perdemos aislamiento total de fallos: un error crítico no
 * controlado en la tarea de limpieza podría desestabilizar o tumbar por completo el servidor
 * principal, afectando a todos los usuarios conectados en ese momento.
 */

public class MiProceso {
    public static void main(String[] args) throws Exception {
        System.out.println("PID: " + ProcessHandle.current().pid());
        int n = Runtime.getRuntime().availableProcessors();
        System.out.println("Núcleos: " + n);
        System.out.println("Pulsa Intro para continuar...");
        System.in.read();
    }
}
