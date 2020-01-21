package zalopay.promotion.asset;

public interface PromotionAssetBuz {
    short usePromotion(String promoSig, double amount, String promo_user_id, String transit_user_id);
    short compensateUsePromotion(String promoSig);
}
