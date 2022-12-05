package yuqiang.rpc.common.exception;

public class SerializerException extends RuntimeException{
    public SerializerException(final Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new Serializer exception.
     *
     * @param message the message
     */
    public SerializerException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Serializer exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public SerializerException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
