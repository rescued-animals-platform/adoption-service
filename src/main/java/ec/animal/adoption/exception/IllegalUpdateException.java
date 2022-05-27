package ec.animal.adoption.exception;

import java.util.UUID;

public class IllegalUpdateException extends RuntimeException {

    private static final transient long serialVersionUID = -367826159819421923L;

    private static final String MESSAGE_TEMPLATE = "Can't update animal with clinical record: %s. " +
                                                   "An animal with id: %s already has this clinical record";

    public IllegalUpdateException(final UUID existingAnimalId, final String clinicalRecord) {
        super(String.format(MESSAGE_TEMPLATE, clinicalRecord, existingAnimalId));
    }
}
