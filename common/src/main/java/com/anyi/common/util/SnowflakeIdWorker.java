package com.anyi.common.util;


import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.Getter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

@Getter
public class SnowflakeIdWorker {

    // ==============================Fields===========================================
    /***/
    /**
     * 随机种子
     */
    private final static Random RANDOM = new Random();

    /**
     * 开始时间截 (2015-01-01)
     */
    private final long twepoch = 1420041600000L;

    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;

    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId;

    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 雪花单例默认
     */
//    private volatile static SnowflakeIdWorker defaultWorker;

//    private static SnowflakeIdWorker getInstance() {
//        if (defaultWorker == null) {
//            synchronized (SnowflakeIdWorker.class) {
//                if (defaultWorker == null) {
//                    defaultWorker = new SnowflakeIdWorker();
//                }
//            }
//        }
//        return defaultWorker;
//    }

//    public static long nextID() {
//        return SnowflakeIdWorker.getInstance().nextId();
//    }




    /**
     * 初始化workId和dataCenterId
     * 雪花算法类的使用注意：
     * 1.同一个jvm进程下，只能保证同一个SnowFlakeWorker实例生成的id不会重复，如果是多个对象的话是可能重复的
     * 2.同第一点，如果是不同的服务，在同一台机器下，则完全可能生成的ID重复
     * 3.同一微服务不同实例，需要部署在不同ip的机器上，才能保证centerDataId和workId段不重复，保证ID不重复
     * <p>
     * 以下优化思路：
     * 因为dataCenterId+workerId最大限制是2^10，我们生成的dataCenterId+workerId只要满足小于1024即可。
     * 1.针对IPV4:
     * IP最大 255.255.255.255。而（255+255+255+255) < 1024。
     * 因此采用IP段数值相加即可生成唯一的workerId，不受IP位限制。
     * 2.针对IPV6:
     * IP最大ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
     * 为了保证相加生成出的dataCenter+workerId < 1024,思路是将每个bit位的后6位相加。这样在一定程度上也可以满足workerId不重复的问题。
     * <p>
     * 注意:!!! 使用这种IP生成workerId的方法,必须保证IP段相加不能重复,否则可能workId重复，此方法可以在一定程度少减少workId的重复
     * <p>
     * 优化workId和dataCenterId生成
     * xionglin
     * 2018-06-07
     */
    public void initWorkAndCenterIdByIpSection() {

        InetAddress address;
        try {
            address = InetAddress.getLocalHost();//获取local ip地址
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        byte[] ipAddressByteArray = address.getAddress();
        long workerId = 0L;
        switch (ipAddressByteArray.length) {
            case 4:
                for (byte byteNum : ipAddressByteArray) {
                    workerId += byteNum & 0xFF;
                }
                break;
            case 16:
                for (byte byteNum : ipAddressByteArray) {
                    workerId += byteNum & 0B111111;
                }
                break;
            default:
                throw new IllegalStateException("Bad LocalHost InetAddress, please check your network!");
        }

        long dataCenterId = (workerId & 0B1111100000) >>> 5; //取高5位作为dataCenterID
        long workerSubId = workerId & 0B0000011111; //取低5位作为workerId

        //初始化当前对象
        this.datacenterId = dataCenterId;
        this.workerId = workerSubId;

    }

    /**
     * 根据机器IP获取工作进程Id,如果线上机器的IP二进制表示的最后10位不重复,建议使用此种方式
     * 列如机器的IP为192.168.1.108,二进制表示:11000000 10101000 00000001 01101100
     * 截取最后10位 01 01101100,转为十进制364,设置dataCenter+workerId为364.
     */
    public void initWorkAndCenterIdByIp() {

        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        byte[] ipAddressByteArray = address.getAddress();

        long workerId = (long) (((ipAddressByteArray[ipAddressByteArray.length - 2] & 0B11) << Byte.SIZE) + (ipAddressByteArray[ipAddressByteArray.length - 1] & 0xFF));
        long dataCenterId = (workerId & 0B1111100000) >>> 5; //取高5位作为dataCenterID
        long workerSubId = workerId & 0B0000011111; //取低5位作为workerId

        //初始化当前对象
        this.datacenterId = dataCenterId;
        this.workerId = workerSubId;

    }

    /**
     * 根据机器名最后的数字编号获取工作进程Id.如果线上机器命名有统一规范,建议使用此种方式.
     * 列如机器的HostName为:dangdang-db-sharding-dev-01(公司名-部门名-服务名-环境名-编号)
     * ,会截取HostName最后的编号01作为dataCenterId+workerId.
     */
    public void initWorkAndCenterIdByHostName() {
        InetAddress address;
        Long workerId;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        String hostName = address.getHostName();
        try {
            workerId = Long.valueOf(hostName.replace(hostName.replaceAll("\\d+$", ""), ""));
            long dataCenterId = (workerId & 0B1111100000) >>> 5; //取高5位作为dataCenterID
            long workerSubId = workerId & 0B0000011111; //取低5位作为workerId

            //初始化当前对象
            this.datacenterId = dataCenterId;
            this.workerId = workerSubId;
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Wrong hostname:%s, hostname must be end with number!", hostName));
        }
    }

    /**还可以采取在配置文件中设置dataCenterId和workerId的方式,这种就一定可以避免重复，但是在打包的时候会比较麻烦，还要取维护配置*/


    //==============================Constructors=====================================

    /**
     * 构造函数
     */
//    private SnowflakeIdWorker() {
////        if (workerId > maxWorkerId || workerId < 0) {
////            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
////        }
////        if (datacenterId > maxDatacenterId || datacenterId < 0) {
////            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
////        }
//        this.workerId = 1L;
//        this.datacenterId = 1L;
//        initWorkAndCenterIdByIpSection();//初始化,根据ip段相加的方式初始化dataCenterId和workerId,一定程度避免重复
//    }

    public SnowflakeIdWorker(long workerId, long datacenterId) {
        // sanity check for workerId
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;

    }


    // ==============================Methods==========================================

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        //时间戳改变，毫秒内序列重置
        else {
            sequence = betweenLong(0, 1, true);//解决雪花算法在1ms内没有并发始终是偶数问题，但是生成速度会比传统的雪花减少50%，1ms内2048次并发支持
//            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) //
                | (datacenterId << datacenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }


    /**
     * 获取两个整数Long之间的随机数
     *
     * @param min     最小值
     * @param max     最大值
     * @param include 是否包含边界值
     * @return
     */
    public static long betweenLong(long min, long max, boolean include) {
        // 参数检查
        if (min > max) {
            throw new IllegalArgumentException("最小值[" + min + "]不能大于最大值[" + max + "]");
        } else if (!include && min == max) {
            throw new IllegalArgumentException("不包括边界值时最小值[" + min + "]不能等于最大值[" + max + "]");
        }
        // 修正边界值
        if (include) {
            max++;
        } else {
            min++;
        }
        return (long) (min + Math.random() * (max - min));
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }

//    public static void main(String[] args) throws InterruptedException {
//
//        System.out.println(System.currentTimeMillis());
//
//        for(int i=0;i<4096;i++){
//            betweenLong( 0,  1, true);//用随机算法来生成sequnce之后雪花算法的毫秒内并发速度会降低一半2进制12位=4096次运算需要消耗2ms
////            long a = System.currentTimeMillis()%2;//去当前时间取模的方式也和随机算法速度差不多需要2ms
//
//        }
//
//        System.out.println(System.currentTimeMillis());
//
//    }

//    public static void main(String[] args) {
//
//        new SnowflakeIdWorker();
//        new SnowflakeIdWorker();
//        new SnowflakeIdWorker();
//        new SnowflakeIdWorker();
//        for (int i = 0 ; i< 10;i++){
//            SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker();
//            System.out.println("workerId:" + snowflakeIdWorker.getWorkerId()  + "   datacenterID:" +   snowflakeIdWorker.getDatacenterId() + " sequence : " + snowflakeIdWorker.getSequence() );
//        }
//    }

}