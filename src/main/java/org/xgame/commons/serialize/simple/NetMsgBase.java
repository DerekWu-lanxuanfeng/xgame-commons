package org.xgame.commons.serialize.simple;

/**
 * @Name: NetMsgBase.class
 * @Description: // NetMsgBase
 * @Create: DerekWu on 2018/3/19 0:28
 * @Version: V1.0
 */
public class NetMsgBase implements IBaseObject {

    private int cmd; // 网络消息指令号（1-65535)

    /**
     * 网络消息基类构造函数
     *
     * @param cmd 消息指令号
     */
    public NetMsgBase(int cmd) {
        this.cmd = cmd;
    }

    /**
     * 序列化
     * @param s
     */
    public void serialize(ObjectSerializer s) {
        this.cmd = s.sUnsignedShort(this.cmd);
    }

    public int getCmd() {
        return cmd;
    }
}
