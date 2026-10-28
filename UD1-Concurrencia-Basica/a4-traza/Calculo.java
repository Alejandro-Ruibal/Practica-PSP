// Calculo.java — el trabajo que se reparte (PSP-UD1, actividad 2: reparto medido entre 1, 2, 4 y 8 procesos).
// Recibe un rango [desde, hasta) y cuenta cuántos números primos hay en él.
//     java Calculo.java 2 300000
// Es deliberadamente lento (prueba por división hasta la raíz) para que el reparto en procesos se note.
public class Calculo {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Uso: java Calculo.java <desde> <hasta>   (cuenta los primos del rango [desde, hasta))");
            System.exit(2);
        }
        long desde = Long.parseLong(args[0]), hasta = Long.parseLong(args[1]);
        long n = 0;
        for (long x = desde; x < hasta; x++) if (esPrimo(x)) n++;
        System.out.println("primos=" + n + " rango=[" + desde + "," + hasta + ")");
    }

    static boolean esPrimo(long x) {
        if (x < 2) return false;
        for (long d = 2; d * d <= x; d++) if (x % d == 0) return false;
        return true;
    }
}
