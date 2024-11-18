import java.io.File
import kotlin.random.Random

fun main() {
    val longitudNumero = 4
    val digitosPermitidos = (1..6).toList()
    val intentosMaximos = 10
    val archivoTraza = "traza.txt"

    // Generar número secreto
    val numeroSecreto = generarNumeroSecreto(longitudNumero, digitosPermitidos)
    println("¡Bienvenido a Adivina un Número!")
    println("Debes adivinar un número de $longitudNumero cifras. No se repiten cifras, y solo puedes usar los dígitos del 1 al 6.")
    println("Tienes un máximo de $intentosMaximos intentos.")

    val intentos = mutableListOf<String>()
    var aciertos = 0
    var intentosRestantes = intentosMaximos

    // Bucle principal del juego
    while (intentosRestantes > 0 && aciertos < longitudNumero) {
        print("Introduce un número: ")
        val entrada = readlnOrNull()

        if (entrada != null && entrada.length == longitudNumero && entrada.all { it.isDigit() }) {
            //Utilizamos un pair para gestionar aciertos y coincidencias
            val (aciertosIntento, coincidenciasIntento) = evaluarIntento(entrada, numeroSecreto)
            //Guardamos en la lista intentos el resultado del intento actual, como un registro interno
            intentos.add("Intento ${intentos.size + 1}: $entrada, Aciertos: $aciertosIntento, Coincidencias: $coincidenciasIntento")
            //Mostramos por pantalla el resultado del intento actual
            println("\u001B[32mAciertos: $aciertosIntento\u001B[0m, \u001B[33mCoincidencias: $coincidenciasIntento\u001B[0m")
            aciertos = aciertosIntento

            if (aciertos == longitudNumero) {
                println("\u001B[32m¡Felicidades! Adivinaste el número secreto $numeroSecreto en ${intentos.size} intentos.\u001B[0m")
                break
            }

            intentosRestantes--
        } else {
            println("\u001B[31mEntrada inválida. Asegúrate de introducir un número de $longitudNumero cifras.\u001B[0m")
        }
    }

    if (aciertos < longitudNumero) {
        println("\u001B[31mLo siento, no adivinaste el número secreto $numeroSecreto en $intentosMaximos intentos.\u001B[0m")
    }

    guardarTraza(intentos, numeroSecreto, archivoTraza)
    println("Traza guardada en $archivoTraza.")
    println("¡Gracias por jugar!")
}

// Generar número secreto aleatorio
fun generarNumeroSecreto(longitud: Int, digitos: List<Int>): String {
    return digitos.shuffled().take(longitud).joinToString("")
}

// Evaluar intento del jugador
fun evaluarIntento(intentado: String, secreto: String): Pair<Int, Int> {
    var aciertos = 0
    var coincidencias = 0
//Compara cada caracter con la posicion correspondiente
    intentado.forEachIndexed { index, char ->
        when {
            char == secreto[index] -> aciertos++  //si está en la posicion correcta
            char in secreto -> coincidencias++    // si está pero en otra posición
        }
    }
    return aciertos to coincidencias
}

// Guardar un registro de la última partida  en un archivo
fun guardarTraza(intentos: List<String>, numeroSecreto: String, archivo: String) {
    val contenido = buildString {
        append("Número secreto: $numeroSecreto\n")
        intentos.forEach { append("$it\n") }
    }
    File(archivo).writeText(contenido)
}
