package foodchain.states;

import foodchain.products.Product;

/**
 * Abstract class representing product's state in each party of
 * food chain.
 */
public abstract class State {

    /**
     * Name of state.
     */
    protected String stateName;

    public String getStateName() {
        return stateName;
    }

    /**
     * Change product's state to next in its processing.
     * @param product
     */
    public abstract void prepare(Product product);
}