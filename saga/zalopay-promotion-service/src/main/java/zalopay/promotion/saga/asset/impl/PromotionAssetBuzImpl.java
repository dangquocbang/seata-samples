package zalopay.promotion.saga.asset.impl;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import zalopay.promotion.asset.PromotionAssetBuz;

import java.util.Date;

public class PromotionAssetBuzImpl implements PromotionAssetBuz {

    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionAssetBuzImpl.class);

    private JdbcTemplate jdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public short usePromotion(String promoSig, double amount, String promo_user_id, String transit_user_id) {
        LOGGER.info("Use Promotion Asset ... xid: " + RootContext.getXID());
        try {
            jdbcTemplate.update("update promo_voucher set `status` = ? where voucher_code = ?  " ,
                    new Object[]{0, promoSig});

            jdbcTemplate.update("insert into promo_log(`code`, amount, promo_user_id,transit_user_id, created_date) " +
                            "values(?,?,?,?,?); ",
                    new Object[]{promoSig, amount, promo_user_id,transit_user_id, new Date()});

            LOGGER.info("Use Promotion End ... ");
        }catch( Exception ex)
        {
            LOGGER.info(String.format("Use Promotion ... xid: %s %s  ",   RootContext.getXID() , ex.getMessage() ));
            return 0;
        }
        return 1;
    }

    @Override
    public short compensateUsePromotion(String promoSig) {
        LOGGER.info("Compensate Using Promotion Asset ... xid: " + RootContext.getXID());
        try {
            jdbcTemplate.update("update promo_voucher set `status` = ? where voucher_code = ?  " ,
                    new Object[]{1, promoSig});

            LOGGER.info("Compensate Using Promotion End ... ");
        }catch( Exception ex)
        {
            LOGGER.info(String.format("Compensate Use Promotion ... xid: %s %s  ",   RootContext.getXID() , ex.getMessage() ));
            return 0;
        }
        return 1;
    }
}
