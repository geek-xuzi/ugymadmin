package com.ugym.admin.common;

import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zheng.xu
 * @since 2017-05-11
 */
public abstract class ResultTemplate<E> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<E> executor() {
        long start = System.currentTimeMillis();
        try {
            return getDataByCache();
        } catch (ExecutionException e) {
            return getDataByDao();
        } finally {
            logger.info(getTag() + "###" + (System.currentTimeMillis() - start));
        }
    }

    protected abstract String getTag();

    protected abstract List getDataByDao();

    protected abstract List getDataByCache() throws ExecutionException;



}
