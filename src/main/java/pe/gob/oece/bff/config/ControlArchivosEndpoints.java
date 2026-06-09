package pe.gob.oece.bff.config;

public class ControlArchivosEndpoints {
    private ControlArchivosEndpoints() {
    }

    public static final String LISTAR_ESTRUCTURA = "/api/v1/configuracion/estructura";

    public static final String VALIDAR_UUID = "/api/v1/archivo/validar-uuid";

    public static final String ASIGNAR_PERMISO = "/api/v1/documento/permiso/asignar";
    public static final String QUITAR_PERMISO = "/api/v1/documento/permiso/quitar";

    public static final String CREAR_EXPEDIENTE = "/api/v1/expediente/crear-expediente";
    public static final String LEER_EXPEDIENTE = "/api/v1/expediente/leer-expediente";

    public static final String INSERTAR_ARCHIVO = "/api/v1/documentos/insertar";
    public static final String INSERTAR_ARCHIVO_TOKEN = "/api/v1/documento/insertar-archivo";
    public static final String ACTUALIZAR_ARCHIVO = "/api/v1/documento/actualizar";

    public static final String DESCARGAR_ARCHIVO = "/api/v1/archivo/descargar";
    public static final String DESCARGAR_ARCHIVO_TOKEN = "/api/v1/archivo/descargar-archivo";

    public static final String CREAR_CARPETA_V2 = "/api/v2/carpeta";
    public static final String VALIDAR_CARPETA_V2 = "/api/v2/carpeta/valida";
}
