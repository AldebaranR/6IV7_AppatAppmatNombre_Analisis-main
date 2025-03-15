document.addEventListener("DOMContentLoaded", function () {
    // Asignar eventos a los botones cuando la página esté completamente cargada
    document.getElementById("btnCifrar").addEventListener("click", codificar);
    document.getElementById("btnDescifrar").addEventListener("click", decodificar);
    document.getElementById("btnReiniciar").addEventListener("click", reiniciar);
    document.getElementById("btnCopiar").addEventListener("click", copiar);
});

var vigenere = (function(){

    var proceso = function(txt, clave, action) {
        var abc = 'abcdefghijklmnopqrstuvwxyzñ';  // Alfabeto con "ñ" incluida
        var longitud = abc.length;
        var resultado = "";
        var indiceClave = 0;

        for (var i = 0; i < txt.length; i++) {
            var letra = txt[i].toLowerCase();
            var indiceTexto = abc.indexOf(letra);
            
            if (indiceTexto !== -1) {
                var indiceClaveActual = abc.indexOf(clave[indiceClave % clave.length].toLowerCase());
                var nuevoIndice;

                if (action) {
                    // Cifrar
                    nuevoIndice = (indiceTexto + indiceClaveActual) % longitud;
                } else {
                    // Descifrar
                    nuevoIndice = (indiceTexto - indiceClaveActual + longitud) % longitud;
                }

                resultado += abc[nuevoIndice];

                // Mover el índice de la clave
                indiceClave++;
            } else {
                resultado += letra; // Mantener caracteres no alfabéticos sin cambios
            }
        }

        return resultado;
    };

    return {
        encode: function(txt, clave) {
            return proceso(txt, clave, true);
        },
        decode: function(txt, clave) {
            return proceso(txt, clave, false);
        }
    };
})();

// Función para cifrar el texto
function codificar() {
    var texto = document.getElementById("txt").value.trim();
    var clave = document.getElementById("txtclave").value.trim();

    if (texto === "" || clave === "") {
        alert("Por favor, completa todos los campos.");
        return;
    }

    if (clave.length > texto.length) {
        alert("La clave no puede ser más larga que el texto.");
        return;
    }

    var resultado = vigenere.encode(texto, clave);
    document.getElementById("respuesta").value = resultado;
}

// Función para descifrar el texto
function decodificar() {
    var textoCifrado = document.getElementById("respuesta").value.trim();  // Corregimos el origen del texto cifrado
    var clave = document.getElementById("txtclave").value.trim();

    if (textoCifrado === "" || clave === "") {
        alert("Por favor, completa todos los campos.");
        return;
    }

    if (clave.length > textoCifrado.length) {
        alert("La clave no puede ser más larga que el texto.");
        return;
    }

    var resultado = vigenere.decode(textoCifrado, clave);
    document.getElementById("respuesta").value = resultado;  // Mostramos el texto descifrado en el input original
}

// Función para reiniciar los campos
function reiniciar() {
    document.getElementById("txt").value = "";
    document.getElementById("txtclave").value = "";
    document.getElementById("respuesta").value = "";
}

// Función para copiar el resultado al portapapeles
function copiar() {
    var resultado = document.getElementById("respuesta");
    
    if (resultado.value === "") {
        alert("No hay nada para copiar.");
        return;
    }

    resultado.select();
    document.execCommand("copy");
    alert("Texto copiado al portapapeles.");
}
