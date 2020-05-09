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

package ec.animal.adoption.api.validator;

import ec.animal.adoption.api.model.characteristics.temperaments.TemperamentsRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TemperamentsRequestValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private TemperamentsRequestValidator temperamentsRequestValidator;

    @BeforeEach
    public void setUp() {
        temperamentsRequestValidator = new TemperamentsRequestValidator();
    }

    @Test
    public void shouldBeAnInstanceOfConstraintValidator() {
        assertThat(temperamentsRequestValidator, is(instanceOf(ConstraintValidator.class)));
    }

    @Test
    public void shouldBeValid() {
        TemperamentsRequest temperamentsRequest = mock(TemperamentsRequest.class);
        when(temperamentsRequest.isEmpty()).thenReturn(false);

        assertThat(temperamentsRequestValidator.isValid(temperamentsRequest, context), is(true));
    }

    @Test
    public void shouldBeInvalidIfTemperamentsIsEmpty() {
        TemperamentsRequest temperamentsRequest = mock(TemperamentsRequest.class);
        when(temperamentsRequest.isEmpty()).thenReturn(true);

        assertThat(temperamentsRequestValidator.isValid(temperamentsRequest, context), is(false));
    }
}