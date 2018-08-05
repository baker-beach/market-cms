package com.bakerbeach.market.cms.view.velocity;

import org.apache.velocity.app.VelocityEngine;

public class MarketVelocityEngineHolder {

    private static VelocityEngine velocityEngine;

    public static VelocityEngine getInstance() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        MarketVelocityEngineHolder.velocityEngine = velocityEngine;
    }

}
