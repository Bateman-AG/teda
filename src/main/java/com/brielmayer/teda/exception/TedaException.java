package com.brielmayer.teda.exception;

public class TedaException extends RuntimeException {

    private String message;
    private Exception cause;

    public TedaException(final String message, final Exception cause) {
        this.message = message;
        this.cause = cause;
    }

    public static TedaExceptionBuilder builder() {
        return new TedaExceptionBuilder();
    }

    public static class TedaExceptionBuilder {

        private final StringBuilder message = new StringBuilder();
        private Exception cause;

        public TedaExceptionBuilder appendMessage() {
            this.message
                    .append(System.lineSeparator());
            return this;
        }

        public TedaExceptionBuilder appendMessage(final String message, final Object... params) {
            this.message
                    .append(String.format(message, params))
                    .append(System.lineSeparator());
            return this;
        }

        public TedaExceptionBuilder cause(final Exception cause) {
            this.cause = cause;
            return this;
        }

        public TedaException build() {
            return new TedaException(message.toString(), cause);
        }
    }
}
