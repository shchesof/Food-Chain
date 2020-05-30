package foodchain.parties;

import java.util.Iterator;

public class PartiesIterator implements Iterator {

    private AbstractParty current;

    /**
     *
     * @param current
     */
    public PartiesIterator(AbstractParty current) {
        this.current = current;
    }

    @Override
    public boolean hasNext() {
        return current.getNextParty() != null;
    }

    @Override
    public AbstractParty next() {
        if (hasNext()) {
            current = current.getNextParty();
            return current;
        }
        return null;
    }
}