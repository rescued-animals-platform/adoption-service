package ec.animal.adoption.domain.characteristics;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacteristicsTest {

    private Size size;
    private PhysicalActivity physicalActivity;
    private Characteristics characteristics;

    @Before
    public void setUp() {
        size = getRandomSize();
        physicalActivity = getRandomPhysicalActivity();
        characteristics = new Characteristics(size, physicalActivity);
    }

    @Test
    public void shouldHaveASize() {
        assertThat(characteristics.getSize(), is(size));
    }

    @Test
    public void shouldHaveAPhysicalActivity() {
        assertThat(characteristics.getPhysicalActivity(), is(physicalActivity));
    }

    private static Size getRandomSize() {
        final List<Size> sizes = Arrays.asList(Size.values());
        int randomSizeIndex = new Random().nextInt(sizes.size());
        return sizes.get(randomSizeIndex);
    }

    private static PhysicalActivity getRandomPhysicalActivity() {
        final List<PhysicalActivity> physicalActivities = Arrays.asList(PhysicalActivity.values());
        int randomPhysicalActivityIndex = new Random().nextInt(physicalActivities.size());
        return physicalActivities.get(randomPhysicalActivityIndex);
    }
}