package jonathan.modern_design._shared.tags.models;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModel {
// Es un DTO específico para la capa de presentacion, los cuales la composicion es en runtime (no hay tablas ni nada fisico de por medio),
// es decir, desde la propia query, generalmente mediante joins a no ser que sea algo muy concreto.
//
//La clave aquí es que se suele hacer composicion de varias fuentes de datos (tablas, etc) desde la query.
}
