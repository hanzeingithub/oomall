package xmu.oomall.util;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import xmu.oomall.dao.ProductsDao;

/**
 * @author hanzelegend
 * 将redis中的数据定期更新到数据库中的实现类
 */
public class ProductTask extends QuartzJobBean {
    @Autowired
    ProductsDao productsDao;
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        productsDao.transProductFromRedis();
    }
}