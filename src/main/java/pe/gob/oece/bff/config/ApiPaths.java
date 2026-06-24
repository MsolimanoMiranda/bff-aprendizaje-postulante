package pe.gob.oece.bff.config;

public final class ApiPaths {

    private ApiPaths() {
    }

    public static final String API_V1 = "/api/v1";

    public static final String CONNECTIVITY = API_V1 + "/connectivity";

    public static final String APRENDIZAJE_POSTULACION_BASE = API_V1 + "/aprendizaje-postulacion";

    public static final String POSTULACIONES = APRENDIZAJE_POSTULACION_BASE + "/postulaciones";

    public static final String DETALLE_POSTULACION = POSTULACIONES;

    public static final String POSTULANTE = APRENDIZAJE_POSTULACION_BASE + "/postulante";

    public static final String PARAMETROS = APRENDIZAJE_POSTULACION_BASE + "/parametros";

    public static final String PUBLICO = APRENDIZAJE_POSTULACION_BASE + "/publico";
    public static final String CERTIFICADO = APRENDIZAJE_POSTULACION_BASE + "/certificado";
    public static final String CURSO_ASIGNADO = APRENDIZAJE_POSTULACION_BASE + "/curso-asignado";

    public static final String EJECUCION_EXAMEN = APRENDIZAJE_POSTULACION_BASE + "/ejecucion-examen";

    public static final String REPROGRAMACION_EXAMENES = APRENDIZAJE_POSTULACION_BASE + "/reprogramacion-examenes";

    public static final String ENCUESTA_SATISFACCION = APRENDIZAJE_POSTULACION_BASE + "/encuesta-satisfaccion";

    public static final String NIUBIZ_WEBHOOK = APRENDIZAJE_POSTULACION_BASE + "/niubiz/webhook/callback";
    
    public static final String PAGOS = APRENDIZAJE_POSTULACION_BASE + "/pagos";
}
