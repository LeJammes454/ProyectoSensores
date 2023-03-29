# Proyecto Sensores
Proyecto Sensores es una aplicación de demostración que utiliza el acelerómetro del dispositivo para mover una imagen de un balón por la pantalla. La aplicación está escrita en Kotlin y utiliza Android Studio como entorno de desarrollo integrado.

# Cómo funciona
La aplicación registra un `SensorEventListener` para el acelerómetro del dispositivo. Cada vez que se recibe una actualización de los valores del acelerómetro, se calcula la nueva posición del balón en función de los valores `x` e `y` del acelerómetro. Si la nueva posición del balón está dentro de los límites de la pantalla, se actualiza la posición de la imagen del balón para reflejar la nueva posición. En caso contrario, se ajusta la posición del balón para que se encuentre dentro de los límites de la pantalla.

Para mejorar la fluidez de la animación, la lógica de actualización de la posición del balón se ejecuta en un hilo secundario utilizando un `Handler`. Esto evita que la actualización de la posición del balón se bloquee en el hilo principal y permite que la animación se ejecute sin problemas.

Además, he agregado una funcionalidad adicional para asegurarnos de que el balón nunca salga de los límites de la pantalla. Si la nueva posición del balón está fuera de los límites de la pantalla, ajustamos la posición del balón para que se encuentre dentro de los límites de la pantalla.

##  Cómo utilizar la aplicación

Para utilizar la aplicación, simplemente iníciala en tu dispositivo. Verás una imagen de un balón en el centro de la pantalla. Mueve tu dispositivo para mover el balón por la pantalla. La posición del balón se actualizará automáticamente en función de los valores del acelerómetro.

## Cómo ejecutar la aplicación

Para ejecutar la aplicación, sigue los siguientes pasos:

1.  Descarga el repositorio de GitHub o clónalo en tu máquina local.
2.  Abre el proyecto en Android Studio.
3.  Conecta tu dispositivo Android a tu máquina local utilizando un cable USB.
4.  Haz clic en el botón "Run" en Android Studio y selecciona tu dispositivo Android como el dispositivo de destino.
5.  La aplicación se instalará y ejecutará automáticamente en tu dispositivo Android.


## Créditos

La aplicación fue desarrollada por Jaime Leon Angeles.

## Licencia

Esta aplicación está bajo la Licencia MIT. Puede ver los detalles de la licencia en el archivo LICENSE.
