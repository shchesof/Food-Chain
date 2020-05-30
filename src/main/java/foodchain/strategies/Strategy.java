package foodchain.strategies;

import foodchain.products.Product;

/**
 * Common strategy class that defines execution method providing the
 * strategy implementation.
 */
abstract public class Strategy {
    
    /**
     * Product to change characteristics of.
     */
    protected Product product;

    public Strategy(Product product) {
        this.product = product;
    }

    public abstract void initStorageParametres();
    public abstract void initSellerParametres();
    public abstract void initProcessorParametres();
}