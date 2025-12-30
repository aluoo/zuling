package com.anyi.common.util;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.anyi.common.advice.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/27
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class DistributionLockUtil {
    @Autowired
    private RedissonClient redissonClient;
    /**
     * 获取锁的超时时间(单位: 秒)
     */
    public static final Integer LOCK_WAIT_SECOND = 10;

    public static final String DEFAULT_TRY_LOCK_FAILED_MESSAGE = "获取锁超时";
    public static final String DEFAULT_LOCK_FAILED_MESSAGE = "获取锁失败";

    @FunctionalInterface
    public interface LockTemplate {
        /**
         * 执行业务逻辑 无返回值
         */
        void execute();
    }

    @FunctionalInterface
    public interface LockResultTemplate<T> {
        /**
         * 执行业务代码 有返回值
         * @return 业务代码的返回值
         */
        T execute();
    }

    /**
     * 用于包装{@link LockTemplate} 和 {@link LockResultTemplate}
     * @param <T>
     */
    @FunctionalInterface
    private interface LockOperation<T> {
        /**
         * 执行业务代码
         *
         * @return 有返回值的情况下返回操作的结果，无返回值的情况下返回null
         */
        T execute();
    }

    /**
     * 获取分布式锁并执行无返回值的操作
     *
     * @param lockTemplate 无返回值的操作代码函数
     * @param waitTime 获取锁的超时时间(单位: 秒)
     * @param lockName 锁名称函数，用于获取锁的名称
     */
    public void lock(LockTemplate lockTemplate, Integer waitTime, Supplier<String> lockName) {
        // 将LockTemplate转换为LockOperation，执行操作
        LockOperation<Void> lockOperation = () -> {
            lockTemplate.execute();
            return null;
        };

        // 调用通用的execute方法获取锁并执行操作
        execute(lockOperation, waitTime, lockName.get(), null, null);
    }

    public void lock(LockTemplate lockTemplate, Integer waitTime, Supplier<String> lockName, String tryLockFailedMsg, String lockFailedMsg) {
        // 将LockTemplate转换为LockOperation，执行操作
        LockOperation<Void> lockOperation = () -> {
            lockTemplate.execute();
            return null;
        };

        // 调用通用的execute方法获取锁并执行操作
        execute(lockOperation, waitTime, lockName.get(), tryLockFailedMsg, lockFailedMsg);
    }

    /**
     * 获取分布式锁并执行无返回值的操作
     * <p>
     * 默认获取锁的超时时间为10秒
     *
     * @param lockTemplate 无返回值的操作代码函数
     * @param lockName 锁名称函数，用于获取锁的名称
     */
    public void lock(LockTemplate lockTemplate, Supplier<String> lockName) {
        // 将LockTemplate转换为LockOperation，执行操作
        LockOperation<Void> lockOperation = () -> {
            lockTemplate.execute();
            return null;
        };

        // 调用通用的execute方法获取锁并执行操作
        execute(lockOperation, LOCK_WAIT_SECOND, lockName.get(), null, null);
    }

    public void lock(LockTemplate lockTemplate, Supplier<String> lockName,  String tryLockFailedMsg, String lockFailedMsg) {
        // 将LockTemplate转换为LockOperation，执行操作
        LockOperation<Void> lockOperation = () -> {
            lockTemplate.execute();
            return null;
        };

        // 调用通用的execute方法获取锁并执行操作
        execute(lockOperation, LOCK_WAIT_SECOND, lockName.get(), tryLockFailedMsg, lockFailedMsg);
    }

    /**
     * 获取分布式锁并执行带有返回值的操作
     *
     * @param lockResultTemplate 带有返回值的操作代码函数
     * @param waitTime 获取锁的超时时间(单位: 秒)
     * @param lockName 锁名称函数，用于获取锁的名称
     * @param <T> 返回值的类型
     * @return 带有返回值的操作结果
     */
    public <T> T lock(LockResultTemplate<T> lockResultTemplate, Integer waitTime, Supplier<String> lockName) {
        // 将LockResultTemplate转换为LockOperation，执行操作
        LockOperation<T> lockOperation = lockResultTemplate::execute;

        // 调用通用的execute方法获取锁并直选操作，返回结果
        return execute(lockOperation, waitTime, lockName.get(), null, null);
    }

    public <T> T lock(LockResultTemplate<T> lockResultTemplate, Integer waitTime, Supplier<String> lockName, String tryLockFailedMsg, String lockFailedMsg) {
        // 将LockResultTemplate转换为LockOperation，执行操作
        LockOperation<T> lockOperation = lockResultTemplate::execute;

        // 调用通用的execute方法获取锁并直选操作，返回结果
        return execute(lockOperation, waitTime, lockName.get(), tryLockFailedMsg, lockFailedMsg);
    }

    /**
     * 获取分布式锁并执行带有返回值的操作
     * <p>
     * 默认获取锁的超时时间为10秒
     *
     * @param lockResultTemplate 带有返回值的操作代码函数
     * @param lockName 锁名称函数，用于获取锁的名称
     * @param <S> 返回值的类型
     * @return 带有返回值的操作结果
     */
    public <S> S lock(LockResultTemplate<S> lockResultTemplate, Supplier<String> lockName) {
        // 将LockResultTemplate转换为LockOperation，执行操作
        LockOperation<S> lockOperation = lockResultTemplate::execute;

        // 调用通用的execute方法获取锁并直选操作，返回结果
        return execute(lockOperation, LOCK_WAIT_SECOND, lockName.get(), null, null);
    }

    public <S> S lock(LockResultTemplate<S> lockResultTemplate, Supplier<String> lockName, String tryLockFailedMsg, String lockFailedMsg) {
        // 将LockResultTemplate转换为LockOperation，执行操作
        LockOperation<S> lockOperation = lockResultTemplate::execute;

        // 调用通用的execute方法获取锁并直选操作，返回结果
        return execute(lockOperation, LOCK_WAIT_SECOND, lockName.get(), tryLockFailedMsg, lockFailedMsg);
    }

    /**
     * 通用执行业务代码
     *
     * @param lockOperation 业务代码具体实现逻辑
     * @param time 获取锁的超时时间 (单位: 毫秒)，不传为0
     * @param lockName 锁名称
     */
    private <T> T execute(LockOperation<T> lockOperation, Integer time, String lockName, String tryLockFailedMsg, String lockFailedMsg) {
        RLock rlock = null;
        if (StrUtil.isBlank(tryLockFailedMsg)) {
            tryLockFailedMsg = DEFAULT_TRY_LOCK_FAILED_MESSAGE;
        }
        if (StrUtil.isBlank(lockFailedMsg)) {
            lockFailedMsg = DEFAULT_LOCK_FAILED_MESSAGE;
        }
        if (null == time) {
            time = 0;
        }
        try {
            rlock = redissonClient.getLock(lockName);
            try {
                boolean tryLock = rlock.tryLock(time, TimeUnit.MILLISECONDS);
                if (!tryLock) {
                    log.error("execute.error: try lock failed, [lockName-{}]", lockName);
                    throw new BaseException(-1, tryLockFailedMsg);
                }
                log.info("execute.info: get lock success, [lockName-{}]", lockName);
                // 执行业务代码
                return lockOperation.execute();
            } catch (InterruptedException e) {
                log.error("execute.error: lock failed, [lockName-{}], {}", lockName, ExceptionUtil.getMessage(e));
                throw new BaseException(-1, lockFailedMsg);
            }
        } finally {
            if (rlock != null && rlock.isHeldByCurrentThread()) {
                rlock.unlock();
            }
        }
    }
}