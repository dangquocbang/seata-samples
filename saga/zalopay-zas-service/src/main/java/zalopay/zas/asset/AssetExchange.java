package zalopay.zas.asset;

import java.math.BigDecimal;
import java.util.Map;

public interface AssetExchange {

    short moveMoney(String outAccountID, double amount, String inAccountID);

    short compensateMoveMoney(String outAccountID, double amount, String inAccountID);

}
