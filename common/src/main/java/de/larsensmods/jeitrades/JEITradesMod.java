package de.larsensmods.jeitrades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JEITradesMod {

    public static final String MOD_ID = "jeitrades";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        LOG.info("Initializing JEITradesMod");
    }
}