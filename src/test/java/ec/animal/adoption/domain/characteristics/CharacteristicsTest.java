package ec.animal.adoption.domain.characteristics;

import ec.animal.adoption.domain.characteristics.temperament.Temperament;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CharacteristicsTest {

    @Mock
    private Temperament temperament;

    private Size size;
    private PhysicalActivity physicalActivity;
    private List<Temperament> temperaments;

    @Before
    public void setUp() {
        size = getRandomSize();
        physicalActivity = getRandomPhysicalActivity();
        temperaments = newArrayList(temperament);
    }

    @Test
    public void shouldEliminateDuplicatesOnFriendlyWith() {
        Characteristics expectedCharacteristics = new Characteristics(
                size, physicalActivity, temperaments, FriendlyWith.CATS, FriendlyWith.CHILDREN
        );

        Characteristics characteristics = new Characteristics(
                size, physicalActivity, temperaments, FriendlyWith.CATS, FriendlyWith.CATS, FriendlyWith.CHILDREN
        );

        assertThat(characteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldEliminateDuplicatesOnTemperaments() {
        Temperament anotherTemperament = mock(Temperament.class);
        List<Temperament> temperaments = newArrayList(temperament, anotherTemperament, anotherTemperament);
        Characteristics expectedCharacteristics = new Characteristics(
                size, physicalActivity, newArrayList(temperament, anotherTemperament)
        );

        Characteristics characteristics = new Characteristics(size, physicalActivity, temperaments);

        assertThat(characteristics, is(expectedCharacteristics));
    }

    @Test
    public void shouldVerifyEqualsAndHashCodeMethods() {
        EqualsVerifier.forClass(Characteristics.class).usingGetClass().verify();
    }

    private static Size getRandomSize() {
        Random random = new Random();
        List<Size> sizes = Arrays.asList(Size.values());
        int randomSizeIndex = random.nextInt(sizes.size());
        return sizes.get(randomSizeIndex);
    }

    private static PhysicalActivity getRandomPhysicalActivity() {
        Random random = new Random();
        List<PhysicalActivity> physicalActivities = Arrays.asList(PhysicalActivity.values());
        int randomPhysicalActivityIndex = random.nextInt(physicalActivities.size());
        return physicalActivities.get(randomPhysicalActivityIndex);
    }
}