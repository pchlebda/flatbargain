package com.flat.bargain.runner;

import com.flat.bargain.model.FlatStatistics;
import com.flat.bargain.parser.PageNumberParser;

public class BasicOfferRunner implements OfferRunner {


    @Override
    public FlatStatistics execute(RunConfiguration runConfiguration) {
        Website website = runConfiguration.getWebsite();
        if (website == Website.GRATKA) {
            OfferRunner offerRunner = new GratkaRunner();
            return offerRunner.execute(runConfiguration);
        }

        return null;
    }
}
