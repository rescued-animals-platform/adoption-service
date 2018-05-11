package ec.animal.adoption.domain.characteristics;

import ec.animal.adoption.domain.characteristics.temperament.Temperament;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CharacteristicsTest {

    @Mock
    private Temperament temperament;

    private Random random;
    private Size size;
    private PhysicalActivity physicalActivity;
    private List<Temperament> temperaments;
    private Characteristics characteristics;

    @Before
    public void setUp() {
        random = new Random();
        size = getRandomSize(random);
        physicalActivity = getRandomPhysicalActivity(random);
        temperaments = newArrayList(temperament);
        characteristics = new Characteristics(size, physicalActivity, temperaments);
    }

    @Test
    public void shouldHaveASize() {
        assertThat(characteristics.getSize(), is(size));
    }

    @Test
    public void shouldHaveAPhysicalActivity() {
        assertThat(characteristics.getPhysicalActivity(), is(physicalActivity));
    }

    @Test
    public void shouldCreateCharacteristicsWithOneFriendlyWith() {
        FriendlyWith friendlyWith = getRandomFriendlyWith(random);
        characteristics = new Characteristics(size, physicalActivity, temperaments, friendlyWith);

        assertThat(characteristics.getFriendlyWith().size(), is(1));
        assertThat(characteristics.getFriendlyWith().get(0), is(friendlyWith));
    }

    @Test
    public void shouldCreateCharacteristicsWithMultipleFriendlyWith() {
        FriendlyWith friendlyWithDogs = FriendlyWith.DOGS;
        FriendlyWith friendlyWithAdults = FriendlyWith.ADULTS;
        FriendlyWith friendlyWithOtherAnimals = FriendlyWith.OTHER_ANIMALS;
        FriendlyWith friendlyWithChildren = FriendlyWith.CHILDREN;
        characteristics = new Characteristics(
                size,
                physicalActivity,
                temperaments,
                friendlyWithDogs,
                friendlyWithAdults,
                friendlyWithOtherAnimals,
                friendlyWithChildren
        );

        assertThat(characteristics.getFriendlyWith().size(), is(4));
        assertTrue(characteristics.getFriendlyWith().contains(friendlyWithDogs));
        assertTrue(characteristics.getFriendlyWith().contains(friendlyWithAdults));
        assertTrue(characteristics.getFriendlyWith().contains(friendlyWithOtherAnimals));
        assertTrue(characteristics.getFriendlyWith().contains(friendlyWithChildren));
    }

    @Test
    public void shouldEliminateDuplicatesOnFriendlyWith() {
        characteristics = new Characteristics(
                size, physicalActivity, temperaments, FriendlyWith.CATS, FriendlyWith.CATS, FriendlyWith.CHILDREN
        );

        assertThat(characteristics.getFriendlyWith().size(), is(2));
        assertTrue(characteristics.getFriendlyWith().contains(FriendlyWith.CATS));
        assertTrue(characteristics.getFriendlyWith().contains(FriendlyWith.CHILDREN));
    }

    @Test
    public void shouldCreateCharacteristicsWithOneTemperament() {
        characteristics = new Characteristics(size, physicalActivity, temperaments);

        assertThat(characteristics.getTemperaments().size(), is(1));
        assertThat(characteristics.getTemperaments().get(0), is(temperament));
    }

    @Test
    public void shouldCreateCharacteristicsWithMultipleTemperaments() {
        Temperament anotherTemperament = mock(Temperament.class);
        Temperament oneMoreTemperament = mock(Temperament.class);
        List<Temperament> temperaments = newArrayList(temperament, anotherTemperament, oneMoreTemperament);

        characteristics = new Characteristics(size, physicalActivity, temperaments);

        assertThat(characteristics.getTemperaments().size(), is(3));
        assertTrue(characteristics.getTemperaments().contains(temperament));
        assertTrue(characteristics.getTemperaments().contains(anotherTemperament));
        assertTrue(characteristics.getTemperaments().contains(oneMoreTemperament));
    }

    @Test
    public void shouldEliminateDuplicatesOnTemperaments() {
        Temperament duplicateTemperament = mock(Temperament.class);
        List<Temperament> temperaments = newArrayList(temperament, duplicateTemperament, duplicateTemperament);

        characteristics = new Characteristics(size, physicalActivity, temperaments);

        assertThat(characteristics.getTemperaments().size(), is(2));
        assertTrue(characteristics.getTemperaments().contains(temperament));
        assertTrue(characteristics.getTemperaments().contains(duplicateTemperament));
    }

    private static Size getRandomSize(Random random) {
        List<Size> sizes = Arrays.asList(Size.values());
        int randomSizeIndex = random.nextInt(sizes.size());
        return sizes.get(randomSizeIndex);
    }

    private static PhysicalActivity getRandomPhysicalActivity(Random random) {
        List<PhysicalActivity> physicalActivities = Arrays.asList(PhysicalActivity.values());
        int randomPhysicalActivityIndex = random.nextInt(physicalActivities.size());
        return physicalActivities.get(randomPhysicalActivityIndex);
    }

    private static FriendlyWith getRandomFriendlyWith(Random random) {
        List<FriendlyWith> friendlyWith = Arrays.asList(FriendlyWith.values());
        int randomFriendlyWithIndex = random.nextInt(friendlyWith.size());
        return friendlyWith.get(randomFriendlyWithIndex);
    }
}