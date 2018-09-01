package org.xgame.commons.serialize.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Name: ObjectSerializer.class
 * @Description: // 对象序列化
 * @Create: DerekWu on 2018/3/14 23:52
 * @Version: V1.0
 */
public class ObjectSerializer {

    private static final Logger LOG = LogManager.getLogger(ObjectSerializer.class);

    private static final Charset CHARSET = Charset.forName("UTF-8");

    /** true读取，false写入 */
    private final boolean readMode;
    /** 读取的时候是否还能读取 */
    private boolean _canRead = true;


    /** 二进制 */
    public final ByteBuf srcBytes;

    public ObjectSerializer(boolean readMode, ByteBuf srcBytes) {
        this.readMode = readMode;
        this.srcBytes = srcBytes;
    }

    /**
     * 序列化一个boolean
     */
    public boolean sBoolean(boolean value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 1) {
                value = this.srcBytes.readBoolean();
            } else {
                this._canRead = false;
                LOG.debug("sBoolean xx");
            }
        } else {
            if (value)
                this.srcBytes.writeBoolean(value);
            else
                this.srcBytes.writeBoolean(false);
        }
        return value;
    }

    /**
     * 序列化一个byte
     */
    public byte sByte(byte value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 1) {
                value = this.srcBytes.readByte();
            } else {
                this._canRead = false;
                LOG.debug("sByte xx");
            }
        } else {
            this.srcBytes.writeByte(value);
        }
        return value;
    }

    /**
     * byte 数组
     */
    public List<Byte> sByteArray(List<Byte> array){
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 2) {
                int array_length = this.srcBytes.readUnsignedShort();
                if (array_length > 0) {
                    if (array == null || array.size() > 0) {
                        array = new ArrayList<>();
                    }
                    for (int i = 0; i < array_length; i++) {
                        byte vInt = this.srcBytes.readByte();
                        array.add(vInt);
                    }
                }
            } else {
                this._canRead = false;
                LOG.debug("sByteArray xx");
            }
        } else {
            if (array != null) {
                int array_length = array.size();
                this.srcBytes.writeShort(array_length);
                if (array_length > 0) {
                    for (int i = 0; i < array_length; i++) {
                        this.srcBytes.writeByte(array.get(i));
                    }
                }
            } else {
                this.srcBytes.writeShort(0);
            }
        }
        return array;
    }

    /**
     * 序列化一个short
     */
    public short sShort(short value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 2) {
                value = this.srcBytes.readShort();
            } else {
                this._canRead = false;
                LOG.debug("sShort xx");
            }
        } else {
            this.srcBytes.writeShort(value);
        }
        return value;
    }

    /**
     * 序列化一个无符号short
     */
    public int sUnsignedShort(int value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 2) {
                value = this.srcBytes.readUnsignedShort();
            } else {
                this._canRead = false;
                LOG.debug("sShort xx");
            }
        } else {
//            ByteBufUtil.setShortBE()
            this.srcBytes.writeShort(value);
        }
        return value;
    }

    /**
     * short 数组
     */
    public List<Short> sShortArray(List<Short> array) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 2) {
                int array_length = this.srcBytes.readUnsignedShort();
                if (array_length > 0) {
                    if (array == null || array.size() > 0) {
                        array = new ArrayList<>();
                    }
                    for (int i = 0; i < array_length; i++) {
                        short vInt = this.srcBytes.readShort();
                        array.add(vInt);
                    }
                }
            } else {
                this._canRead = false;
                LOG.debug("sShortArray xx");
            }
        } else {
            if (array != null) {
                int array_length = array.size();
                this.srcBytes.writeShort(array_length);
                if (array_length > 0) {
                    for (int i = 0; i < array_length; i++) {
                        this.srcBytes.writeShort(array.get(i));
                    }
                }
            } else {
                this.srcBytes.writeShort(0);
            }
        }
        return array;
    }

    /**
     * 序列化一个int
     */
    public int sInt(int value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 4) {
                value = this.srcBytes.readInt();
            } else {
                this._canRead = false;
                LOG.debug("sInt xx");
            }
        } else {
            this.srcBytes.writeInt(value);
        }
        return value;
    }

    /**
     * int 数组
     */
    public List<Integer> sIntArray(List<Integer> array) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 2) {
                int array_length = this.srcBytes.readUnsignedShort();
                if (array_length > 0) {
                    if (array == null || array.size() > 0) {
                        array = new ArrayList<>();
                    }
                    for (int i = 0; i < array_length; i++) {
                        int vInt = this.srcBytes.readInt();
                        array.add(vInt);
                    }
                }
            } else {
                this._canRead = false;
                LOG.debug("sIntArray xx");
            }
        } else {
            if (array != null) {
                int array_length = array.size();
                this.srcBytes.writeShort(array_length);
                if (array_length > 0) {
                    for (int i = 0; i < array_length; i++) {
                        this.srcBytes.writeInt(array.get(i));
                    }
                }
            } else {
                this.srcBytes.writeShort(0);
            }
        }
        return array;
    }

    /**
     * 序列化一个Long   注意value 千万不能传null 进来
     */
    public long sLong(long value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 8) {
                value = this.srcBytes.readLong();
            } else {
                this._canRead = false;
                LOG.debug("sLong xx");
            }
        } else {
            this.srcBytes.writeLong(value);
        }
        return value;
    }

    /**
     * 序列化一个double变量
     */
    public float sFloat(float value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 4) {
                value = this.srcBytes.readFloat();
            } else {
                this._canRead = false;
                LOG.debug("sFloat xx");
            }
        } else {
            this.srcBytes.writeFloat(value);
        }
        return value;
    }

    /**
     * 序列化一个double变量
     */
    public double sDouble(double value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 8) {
                value = this.srcBytes.readDouble();
            } else {
                this._canRead = false;
                LOG.debug("sDouble xx");
            }
        } else {
            this.srcBytes.writeDouble(value);
        }
        return value;
    }

    /**
     * 序列化一个字符串
     */
    public String sString(String value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 2) {
                value = readUTF();
            } else {
                this._canRead = false;
                LOG.debug("sString xx");
            }
        } else {
            writeUTF(value);
        }
        return value;
    }

    /**
     * string 数组
     */
    public List<String> sStringArray(List<String> array) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 2) {
                int array_length = this.srcBytes.readUnsignedShort();
                if (array_length > 0) {
                    if (array == null || array.size() > 0) {
                        array = new ArrayList<>();
                    }
                    for (int i = 0; i < array_length; i++) {
                        array.add(readUTF());
                    }
                }
            } else {
                this._canRead = false;
                LOG.debug("sStringArray xx");
            }
        } else {
            if (array != null) {
                int array_length = array.size();
                this.srcBytes.writeShort(array_length);
                if (array_length > 0) {
                    for (int i = 0; i < array_length; i++) {
                        writeUTF(array.get(i));
                    }
                }
            } else {
                this.srcBytes.writeShort(0);
            }
        }
        return array;
    }

    /**
     * 读取String
     * @return
     */
    private String readUTF() {
        int vStrLength = this.srcBytes.readUnsignedShort();
        if (vStrLength == 65535) { // 特定的长度就是null
            return null;
        } else if (vStrLength == 0 ){
            return "";
        } else {
            String value = this.srcBytes.toString(this.srcBytes.readerIndex(), vStrLength, ObjectSerializer.CHARSET);
            this.srcBytes.readerIndex(this.srcBytes.readerIndex()+vStrLength);
            return value;
        }
    }

    /**
     * 写String
     * @param str
     */
    private void writeUTF(String str) {
        if (str == null) {
            this.srcBytes.writeShort(65535); // null 标识
        } else if (str == "") {
            this.srcBytes.writeShort(0);
        } else {
            byte[] bytes = str.getBytes(ObjectSerializer.CHARSET);
            this.srcBytes.writeShort(bytes.length);
            this.srcBytes.writeBytes(bytes);
        }
    }

    /**
     * 序列化一个从baseobject继承下来的简单数据对象
     */
    public <T extends IBaseObject> T sObject(T value, Class<T> clazz) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 1) {
                if (this.srcBytes.readBoolean()) {
                    // 假如有值则读取
                    T vBaseObject = BaseObjectManager.instance(clazz);
                    vBaseObject.serialize(this);
                }
            } else {
                this._canRead = false;
                LOG.debug("sObject xx");
            }
        } else {
            if (value != null) {
                this.srcBytes.writeBoolean(true); // 表示非null
                value.serialize(this);
            } else {
                this.srcBytes.writeBoolean(false); // 表示 null
            }
        }
        return value;
    }

    /**
     * 序列化一个数组对象
     */
    public <T extends IBaseObject> List<T> sObjArray(List<T> array, Class<T> clazz) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 2) {
                int array_length = this.srcBytes.readUnsignedShort();
                if (array_length > 0) {
                    if (array == null || array.size() > 0) {
                        array = new ArrayList<>();
                    }
                    for (int i = 0; i < array_length; i++) {
                        if (this.srcBytes.readBoolean()) {
                            // 假如有值则读取
                            T vBaseObject = BaseObjectManager.instance(clazz);
                            vBaseObject.serialize(this);
                            array.add(vBaseObject);
                        } else {
                            array.add(null);
                        }
                    }
                }
            } else {
                this._canRead = false;
                LOG.debug("sObjArray xx");
            }
        } else {
            if (array != null) {
                int array_length = array.size();
                this.srcBytes.writeShort(array_length);
                if (array_length > 0) {
                    for (int i = 0; i < array_length; i++) {
                        T vBaseObject = array.get(i);
                        if (vBaseObject != null) {
                            this.srcBytes.writeBoolean(true); // 表示非null
                            vBaseObject.serialize(this);
                        } else {
                            this.srcBytes.writeBoolean(false); // 表示 null
                        }
                    }
                }
            } else {
                this.srcBytes.writeShort(0);
            }
        }
        return array;
    }

    /**
     * 序列化一个Bytes
     */
    public byte[] sBytes(byte[] value) {
        if (this.readMode) {
            if (this._canRead && this.srcBytes.readableBytes() >= 2) {
                int vByteSize = this.srcBytes.readUnsignedShort();
                if (vByteSize > 0) {
                    value = new byte[vByteSize];
                    this.srcBytes.readBytes(value);
                }
            } else {
                this._canRead = false;
                LOG.debug("sBytes xx");
            }
        } else {
            if (value != null) {
                this.srcBytes.writeShort(value.length);
                this.srcBytes.writeBytes(value);
            } else {
                this.srcBytes.writeShort(0);
            }
        }
        return value;
    }

}
