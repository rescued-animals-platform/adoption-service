package ec.animal.adoption.domain.characteristics;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class CharacteristicsTest {

    private static final Random random = new Random();
    private Size size;
    private PhysicalActivity physicalActivity;
    private Characteristics characteristics;

    @Before
    public void setUp() {
        size = getRandomSize(random);
        physicalActivity = getRandomPhysicalActivity(random);
        characteristics = new Characteristics(size, physicalActivity);
    }

    @Test
    public void shouldHaveASize() {
        characteristics = new Characteristics(size, physicalActivity);

        assertThat(characteristics.getSize(), is(size));
    }

    @Test
    public void shouldHaveAPhysicalActivity() {
        characteristics = new Characteristics(size, physicalActivity);

        assertThat(characteristics.getPhysicalActivity(), is(physicalActivity));
    }

    @Test
    public void shouldCreateCharacteristicsWithOneFriendlyWith() {
        FriendlyWith friendlyWith = getRandomFriendlyWith(random);
        characteristics = new Characteristics(size, physicalActivity, friendlyWith);

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