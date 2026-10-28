# Auditoría de IA - Actividad 2

## Prompt enviado
"Haz una comparación detallada entre programación concurrente, paralela y distribuida indicando definición, ejemplos reales y dimensiones como memoria y fallos."

## Fallos detectados en el modelo de IA
1. **Imprecisión en memoria distribuida**: El modelo afirmó originalmente que los sistemas distribuidos podían compartir memoria RAM de forma directa mediante hilos. Esto es incorrecto; los sistemas distribuidos están en nodos separados por red y se comunican obligatoriamente mediante paso de mensajes (red) como sockets o RPC, no compartiendo RAM.
2. **Confusión en tolerancia a fallos**: La IA sugirió que un fallo en un hilo paralelo no afectaba al resto de la aplicación local. Al contrario, en un entorno paralelo tradicional, un pánico o error no controlado en un subproceso o hilo compartido suele tumbar la aplicación completa si el proceso padre no lo gestiona de forma explícita.
