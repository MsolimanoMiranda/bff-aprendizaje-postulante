package pe.gob.oece.bff.shared;

public class ApiWrapper<T> {

    private String type;
    private int status;
    private String detail;
    private String instance;
    private T data;

    public ApiWrapper(String type, int status, String detail, String instance, T data) {
        this.type = type;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.data = data;
    }

    public static <T> ApiWrapper<T> success(T data,String detail, String instance) {
        return new ApiWrapper<>(
                "success",
                200,
                detail,
                instance,
                data
        );
    }
    public static <T> ApiWrapper<T> created(T data, String detail, String instance) {
        return new ApiWrapper<>(
                "created",
                201,
                detail,
                instance,
                data
        );
    }
    public static <T> ApiWrapper<T> noContent(String detail, String instance) {
        return new ApiWrapper<>(
                "success",
                204,
                detail,
                instance,
                null
        );
    }
    public String getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public String getInstance() {
        return instance;
    }

    public T getData() {
        return data;
    }
}
