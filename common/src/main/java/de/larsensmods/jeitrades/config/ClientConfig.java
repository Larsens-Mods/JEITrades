package de.larsensmods.jeitrades.config;

public class ClientConfig {

    private boolean cacheLastDataset = true;

    public void setCacheLastDataset(boolean cacheLastDataset) {
        this.cacheLastDataset = cacheLastDataset;
    }

    public boolean isCacheLastDataset(){
        return this.cacheLastDataset;
    }

}
