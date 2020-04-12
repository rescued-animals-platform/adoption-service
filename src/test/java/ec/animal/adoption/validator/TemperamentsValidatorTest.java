/*
    Copyright © 2018 Luisa Emme

    This file is part of Adoption Service in the Rescued Animals Platform.

    Adoption Service is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Adoption Service is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with Adoption Service.  If not, see <https://www.gnu.org/licenses/>.
 */

package ec.animal.adoption.validator;

import ec.animal.adoption.domain.characteristics.temperaments.Temperaments;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TemperamentsValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private TemperamentsValidator temperamentsValidator;

    @Before
    public void setUp() {
        temperamentsValidator = new TemperamentsValidator();
    }

    @Test
    public void shouldBeAnInstanceOfConstraintValidator() {
        assertThat(temperamentsValidator, is(instanceOf(ConstraintValidator.class)));
    }

    @Test
    public void shouldBeValid() {
        Temperaments temperaments = mock(Temperaments.class);
        when(temperaments.isEmpty()).thenReturn(false);

        assertThat(temperamentsValidator.isValid(temperaments, context), is(true));
    }

    @Test
    public void shouldBeInvalidIfTemperamentsIsNull() {

        assertThat(temperamentsValidator.isValid(null, context), is(false));
    }

    @Test
    public void shouldBeInvalidIfTemperamentsIsEmpty() {
        Temperaments temperaments = mock(Temperaments.class);
        when(temperaments.isEmpty()).thenReturn(true);

        assertThat(temperamentsValidator.isValid(temperaments, context), is(false));
    }
}