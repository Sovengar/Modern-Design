package jonathan.modern_design._shared.tags.models;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ReadModel {
    //Es similar a un data model, es decir, es un mapeo 1:1 con tu sistema de persistencia, pero en este caso los datos están denormalizados.
    // Pero lo más importante, es que la composicion de la query se ha movido de runtime a design time.
    //
    //Ejemplos:
    //Tabla denormalizada (Proyeccion)
    //Columna denormalizada (JSON)
    //Vista materializada
    //Cached response
    //Data in another storage (Elastic search, Mongo)
}
