package org.xgame.commons.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @Name: ByteBufTest.class
 * @Description: //
 * @Create: DerekWu on 2018/3/15 0:31
 * @Version: V1.0
 */
public class ByteBufTest {

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(4);
        byteBuf.writeShort(65535);
        byteBuf.writeShort(-2365);

        System.out.println(byteBuf.readUnsignedShort());
        System.out.println(byteBuf.readShort());

    }
}
