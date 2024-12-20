package com.ruoyi.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Describe: Sequence 工具类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 */
public class SequenceUtil {

    /*** 机 器 Id */
    private static final long WORKER_ID = 0;
    /*** 数 据 中 心 */
    private static final long CENTER_ID = 0;
    /** 机 器 编 号 所 占 位 数 */
    private static final long WORKER_ID_BITS = 5L;
    /** 数 据 标 识 所 占 位 数 */
    private static final long CENTER_ID_BITS = 5L;
    /** 开 始 时 间 戳 */
    private static final long POC = 1288834974657L;
    /** 序 列 在 Id 中 所 占 的 位 数 */
    private static final long SEQUENCE_BITS = 12L;
    /** 为 算 法 提 供 可 用 配 置 */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private static final long MAX_CENTER_ID = ~(-1L << CENTER_ID_BITS);
    private static final long CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + CENTER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);
    /*** 毫 秒 内 序 列 */
    private static long sequence = 0L;
    /*** 上 次 Id 生 成 的 时 间 戳 */
    private static long lastTimestamp = -1L;

    public static synchronized long makeSequence() throws Exception {
        long timestamp = timeGen();
        // 当 前 时 间 小 于 上 次 Id 生 成 时 间 ,说 明 系 统 时 钟 回 退, 应 会 抛 出 异 常
        if (timestamp < lastTimestamp) {
            // 服 务 器 时 钟 被 调 整 了, Sequence 生 成 器 停 止 服 务
            throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        // 如 果 是 同 一 时 间 生 成,则 进 行 毫 秒 内 序 列
        if (lastTimestamp == timestamp) {
            // 每 次 加 +
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 毫 秒 内 序 列 溢 出
            if (sequence == 0) {
                // 阻塞到下一个毫秒,获取新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        // 暂 存 当 前 时 间 戳 , 为 下 次 使 用  提 供 依 据
        lastTimestamp = timestamp;
        // 雪 花  算 法 核 心
        return ((timestamp - POC) << TIMESTAMP_LEFT_SHIFT) | (CENTER_ID << CENTER_ID_SHIFT) | (WORKER_ID << WORKER_ID_SHIFT) | sequence;
    }

    /**
     * 获 取 下 一 个 Id
     */
    public static long makeId() {
        try {
            return makeSequence();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获 取 下 一 个 Id ( String 类型 )
     */
    public static String makeStringId() {
        return "" + makeId();
    }

    public static List<String> makeStringIds(int size) {

        List<String> ids = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            ids.add(makeStringId());
        }
        return ids;
    }

    /**
     * 时 间 戳 比 对
     */
    protected static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 当 前 系 统 时 间 毫 秒
     */
    protected static long timeGen() {

        return System.currentTimeMillis();
    }

    /**
     * 根 据 一 定 数 量 的 Id
     */
    public Set<Long> makeId(int initSize) throws Exception {
        Set<Long> ids = new HashSet<Long>(initSize);
        for (long current = 0; current < initSize; current++) {
            ids.add(makeId());
        }
        return ids;
    }
}
