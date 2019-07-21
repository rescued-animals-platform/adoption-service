package ec.animal.adoption.logger;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.concurrent.atomic.AtomicLong;

public class Sequence extends ClassicConverter {

    private AtomicLong sequenceNumber;

    public Sequence() {
        sequenceNumber = new AtomicLong(0L);
    }

    @Override
    public String convert(ILoggingEvent event) {
        return Long.toString(this.sequenceNumber.getAndIncrement());
    }
}