package foodchain.states;

import foodchain.products.Product;

public class SoldState extends State {

    public SoldState() {
        stateName = "Sold";
    }

    /**
     * Only prints warning what product is already prepared.
     * @param productContext
     */
    @Override
    public void prepare(Product productContext) {
        System.out.println("Product is already prepared!\n");  
    }
}