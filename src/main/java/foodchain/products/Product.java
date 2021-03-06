package foodchain.products;

import foodchain.states.State;

import java.util.ArrayList;
import java.util.Map;
import com.google.common.collect.ImmutableMap; 
import foodchain.parties.Party;
import java.util.List;

/**
 * Abstract class representing product processed in a food chain.
 */
public abstract class Product {
    
    /**
     * State of product in different parties.
     */
    protected State state;

    protected Integer price;
    protected String name;

    /**
     * True if some party already has this product and it is not necessary
     * to make it in food factory.
     */
    protected boolean isReadyToTransmit;

    protected boolean isCurrentlyProcessed;
    
    /**
     * List of parties in a food chain which are currently involved
     * in processing of this product.
     */
    protected List<Party> currentlyProcessingParties;
    
    /**
     * List of previous states names.
     */
    protected List<String> statesHistory;
    
    /* Demos are necessary to modify and then convert to immutable maps */

    /**
     * Map of product's storing characteristic to modify.
     */
    protected Map<String, Integer> demoStorageParametres;

    /**
     * Immutable map of product's storing characteristics.
     */
    protected ImmutableMap<String, Integer> storageParametres;
    
    /**
     * Map of product's processing characteristic to modify.
     */
    protected Map<String, Integer> demoProcessorParametres;

    /**
     * Immutable map of product's processing characteristics.
     */
    protected ImmutableMap<String, Integer> processorParametres;
    
    /**
     * Map of product's selling characteristic to modify.
     */
    protected Map<String, String> demoSellerParametres;

    /**
     * Immutable map of product's selling characteristics.
     */
    protected ImmutableMap<String, String> sellerParametres;

    /**
     * Sets state for product.
     * @param state the state to set
     */
    public void setState(State state) {
        this.state = state;
        statesHistory.add(state.getStateName());
    }
    
     /**
     * Get history of previous states of product.
     * @return list with state history
     */
    public List<String> getStatesHistory() {
        return statesHistory;
    }
    
    /**
     * Gets current state of product.
     * @return product's current state
     */
    public State getState() {
        return state;
    }
    
    /**
     * Gets price of product.
     * @return product's price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * Gets name of product.
     * @return product's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets list of parties in a food chain which are currently involved
     * in processing of this product.
     * @return list of parties involved
     */
    public List<Party> getCurrentlyProcessingParties() {
        return currentlyProcessingParties;
    }

    /**
     * Gets current status of being processed or not.
     * @return boolean value of being processed or not
     */
    public boolean isIsCurrentlyProcessed() {
        return isCurrentlyProcessed;
    }
    
    /**
     * Clears list of currently processing parties of product.
     */
    public void clearPartyList() {
        currentlyProcessingParties.clear();
    }

    /**
     * Adds party in list of currently processing parties of product.
     * @param party party to process
     */
    public void addCurrentlyProcessingParties(Party party) {
        currentlyProcessingParties.add(party);
    }

    /**
     * Method for switching product to be currently processed.
     * @param isCurrentlyProcessed status of being processed
     */
    public void setIsCurrentlyProcessed(boolean isCurrentlyProcessed) {
        this.isCurrentlyProcessed = isCurrentlyProcessed;
    }

    /**
     * Sets product's status of being ready to be transmitted.
     * @return status of being ready to be transmitted
     */
    public boolean isIsReadyToTransmit() {
        return isReadyToTransmit;
    }

    /**
     * Sets product's status of being ready to be transmitted.
     * @param isReadyToTransmit status of being ready to be transmitted
     */
    public void setIsReadyToTransmit(boolean isReadyToTransmit) {
        this.isReadyToTransmit = isReadyToTransmit;
    }
    
    /**
     * Sets product's storing characteristics.
     * @param storageTime the storage time (product characteristic)
     * @param storageTemperature the storage temperature (product characteristic)
     * @param storageHumidity the storage humidity (product characteristic)
     */
    public void setStorageParametres(Integer storageTime, Integer storageTemperature,
            Integer storageHumidity) {
        demoStorageParametres.put("storage_time", storageTime);
        demoStorageParametres.put("storage_temperature", storageTemperature);
        demoStorageParametres.put("storage_humidity", storageHumidity);
        storageParametres = ImmutableMap.copyOf(demoStorageParametres);
        demoStorageParametres.clear();
    }

    /**
     * Gets product's storing parameters.
     * @return map of product's storing characteristics
     */
    public ImmutableMap<String, Integer> getStorageParametres() {
        return storageParametres;
    }
    
    /**
     * Sets product's processing characteristics.
     * @param processorTemperature the processing temperature (product characteristic)
     * @param chemicalDegree the chemical degree (product characteristic)
     */
    public void setProcessorParametres(Integer processorTemperature, Integer
            chemicalDegree) {
        demoProcessorParametres.put("processing_temperature", processorTemperature);
        demoProcessorParametres.put("chemical_processing_degree", chemicalDegree);
        processorParametres = ImmutableMap.copyOf(demoProcessorParametres);
        demoProcessorParametres.clear();
    }

    /**
     * Gets product's processing parameters.
     * @return map of product's processing characteristics
     */
    public ImmutableMap<String, Integer> getProcessorParametres() {
        return processorParametres;
    }
    
    /**
     * Sets product's selling characteristics.
     * @param packaging the type of packaging (product characteristic)
     * @param sellingPlace the selling place (product characteristic)
     */
    public void setSellerParametres(String packaging, String sellingPlace) {
        demoSellerParametres.put("type_of_packaging", packaging);
        demoSellerParametres.put("selling_place", sellingPlace);
        sellerParametres = ImmutableMap.copyOf(demoSellerParametres);
        demoSellerParametres.clear();
    }

    /**
     * Gets map of product's selling characteristics.
     * @return map of product's selling characteristics
     */
    public Map<String, String> getSellerParametres() {
        return sellerParametres;
    }

    /**
     * Gets list of all parameters of a product.
     * @return list of all parameters
     */
    public List<Map> getAllParameters() {
        List<Map> allProductParameters = new ArrayList<>();
        if (processorParametres != null) allProductParameters.add(processorParametres);
        if (sellerParametres != null) allProductParameters.add(sellerParametres);
        if (storageParametres != null) allProductParameters.add(storageParametres);

        return allProductParameters;
    }
}