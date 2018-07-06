package com.suidifu.watchman.message.amqp.codec;


/**
 * CodecType
 *
 * @author lisf
 */
public enum CodecType {
    KYRO("kyro"), JSON("json");
    private String key;

    private CodecType(String key) {
        this.key = key;
    }

    public static RpcCodec getCodec(int ordinal) {
        switch (ordinal) {
            case 0:
                return new KryoRpcCodec();
            case 1:
                return new JsonRpcCodec();
        }
        return null;
    }

    public static CodecType fromOridinal(int oridinal) {

        for (CodecType codecType : CodecType.values()) {
            if (oridinal == codecType.ordinal()) {
                return codecType;
            }
        }
        return null;
    }

    public String get() {
        return key;
    }

    public int getOrdinal() {
        return this.ordinal();
    }

    public RpcCodec getCodec() {
        return getCodec(getOrdinal());
    }
}
