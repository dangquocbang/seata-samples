package zalopay.zas.saga.asset.impl;

import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import zalopay.zas.asset.AssetExchange;

import java.util.Date;
import java.util.Map;

public class AssetExchangeImpl implements AssetExchange {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetExchangeImpl.class);

    private JdbcTemplate jdbcTemplate;
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public short moveMoney(String outAccountID, double amount, String inAccountID) {


        LOGGER.info("Move Asset ... xid: " + RootContext.getXID());
        try {

            jdbcTemplate.update("update zas_account set amount = amount - ? where user_id = ? ; " +
                            "update zas_account set amount = amount + ? where user_id = ? ; ",
                    new Object[]{amount, outAccountID, inAccountID, amount});

            jdbcTemplate.update("insert into saga_zas.zas_account_log(user_id, credit, debit, created_date) " +
                            "values(?,?,?,?); ",
                    new Object[]{outAccountID, 0, amount, new Date()});
            jdbcTemplate.update("insert into saga_zas.zas_account_log(user_id, credit, debit, created_date) " +
                            "values(?,?,?,?); ",
                    new Object[]{inAccountID, amount, 0, new Date()});

            LOGGER.info("Move Asset End ... ");
        }catch( Exception ex)
        {
            LOGGER.info(String.format("Move Asset ... xid: %s %s  ",   RootContext.getXID() , ex.getMessage() ));
            return 0;
        }
        return 1;
    }

    @Override
    public short compensateMoveMoney(String outAccountID, double amount, String inAccountID) {
        try {
            LOGGER.info("Compensate move Asset ... xid: " + RootContext.getXID());

            jdbcTemplate.update("update zas_account set amount = amount - ? where user_id = ? ; " +
                            "update zas_account set amount = amount + ? where user_id = ? ; ",
                    new Object[]{amount, inAccountID, amount, outAccountID});

            jdbcTemplate.update("insert into saga_zas.zas_account_log(user_id, credit, debit, created_date) " +
                            "values(?,?,?,?); ",
                    new Object[]{outAccountID, 0, amount, new Date()});
            jdbcTemplate.update("insert into saga_zas.zas_account_log(user_id, credit, debit, created_date) " +
                            "values(?,?,?,?); ",
                    new Object[]{inAccountID, amount, 0, new Date()});

            LOGGER.info("Compensate move Asset End ... ");
        }catch( Exception ex)
        {
            LOGGER.info(String.format("Compensate move Asset ... xid: %s %s  ",   RootContext.getXID() , ex.getMessage() ));
            return 0;
        }
        return 1;
    }
}
