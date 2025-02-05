package com.hd.daq.plugins.shike.iot.hex.ie.socket;

import com.hd.daq.plugins.shike.exception.EncodeException;
import com.hd.daq.plugins.shike.iot.hex.DeviceCmdType;
import com.hd.daq.plugins.shike.iot.hex.IInformationElement;
import com.hd.daq.plugins.shike.util.ByteArrayUtil;
import lombok.Builder;
import lombok.Data;

/**
 * 插座普通事件信息体元素（设备上行）
 *
 * @author ymm
 */
@Builder
@Data
public class IeSocketCommonEvent implements IInformationElement {
    /**
     * 设备类型 1字节
     */
    private int deviceTypeCode;
    /**
     * 随机数 1字节
     */
    private int eventId;
    /**
     * 电压（2字节单位V）
     */
    private int voltage;
    /**
     * 电流（2字节，单位mA）
     */
    private int electricCurrent;
    /**
     * 功率因数（1字节，0-100）
     */
    private int powerFactor;
    /**
     * 累计功耗（4字节，单位Wh）
     */
    private long accumulatedPower;
    /**
     * 温度（1字节，单位℃，0xFF无效）
     */
    private int temperature;
    /**
     * 湿度（1字节，单位%RH （0-100）0xFF无效）
     */
    private int humidity;
    /**
     * 开关状态（1字节，本地开，0x02，开0x01,关0x00））
     */
    private int switchStatus;
    /**
     * 过压保护 5字节
     */
    private String overVoltageProtection;
    /**
     * 欠压保护 5字节
     */
    private String underVoltageProtection;
    /**
     * 功率过载保护 5字节
     */
    private String overloadProtection;
    /**
     * 超温保护 3字节
     */
    private String overTempProtection;
    /**
     * 充电超时 1字节
     */
    private String chargingTimeout;
    /**
     * 功能控制项 1字节
     */
    private String functionControlItems;
    /**
     * 低功率保护 2字节
     */
    private String underPowerProtection;

    /**
     * 解码成实体对象
     *
     * @param buf 缓冲区
     * @return 实体对象
     */
    public static IeSocketCommonEvent decode(byte[] buf) {
        if (buf.length < 36) {
            return null;
        }
        IeSocketCommonEventBuilder builder = IeSocketCommonEvent.builder()
                .deviceTypeCode(Byte.toUnsignedInt(buf[0]))
                .eventId(Byte.toUnsignedInt(buf[2]))
                .voltage((Byte.toUnsignedInt(buf[3]) << 8) | Byte.toUnsignedInt(buf[4]))
                .electricCurrent((Byte.toUnsignedInt(buf[5]) << 8) | Byte.toUnsignedInt(buf[6]))
                .powerFactor(Byte.toUnsignedInt(buf[7]))
                .accumulatedPower(((long) Byte.toUnsignedInt(buf[8]) << 24) | (Byte.toUnsignedInt(buf[9]) << 16) | (Byte.toUnsignedInt(buf[10]) << 8) | Byte.toUnsignedInt(buf[11]))
                .temperature(buf[12])
                .humidity(buf[13])
                .switchStatus(buf[14])
                .overVoltageProtection(ByteArrayUtil.toHexString(buf, 15, 5))
                .underVoltageProtection(ByteArrayUtil.toHexString(buf, 20, 5))
                .overloadProtection(ByteArrayUtil.toHexString(buf, 25, 5))
                .overTempProtection(ByteArrayUtil.toHexString(buf, 30, 3))
                .chargingTimeout(ByteArrayUtil.toHexString(buf, 34, 1))
                .functionControlItems(ByteArrayUtil.toHexString(buf, 35, 1));
        if (buf.length > 37) {
            builder.underPowerProtection(ByteArrayUtil.toHexString(buf, 36, 2));
        }
        return builder.build();
    }

    /**
     * 编码信息体元素(设备上行，不需要编码)
     *
     * @return 字符串数组
     */
    @Override
    public byte[] encode() throws EncodeException {
        throw new EncodeException();
    }

    @Override
    public int getCmdCode() {
        return DeviceCmdType.CMD_10.getId();
    }

    /**
     * 获取RfData中cmd值
     *
     * @return 命令编码
     */
    @Override
    public int getRfDataCmdCode() {
        return DeviceCmdType.CMD_B0.getId();
    }

    /**
     * 获取消息存储类型
     *
     * @return 多个存储类型的并集
     */
    @Override
    public int getMessageStorageType() {
        return DEVICE_MSG;
    }

    /**
     * 获取设备类型码
     *
     * @return 有支持时返回整数，不支持时返回null
     */
    @Override
    public Integer getDeviceTypeCode() {
        return deviceTypeCode;
    }
}
