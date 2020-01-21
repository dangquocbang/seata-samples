package zalopay.promotion.saga.starter;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PromotionProviderStarter {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"spring/seata-dubbo-provider.xml"});

        new ApplicationKeeper(applicationContext).keep();
    }
}
