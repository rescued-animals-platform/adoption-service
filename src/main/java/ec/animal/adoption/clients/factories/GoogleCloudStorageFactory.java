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

package ec.animal.adoption.clients.factories;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;
import org.springframework.stereotype.Component;

@Component
public class GoogleCloudStorageFactory {

    private static final String DEV_ENVIRONMENT = "dev";

    public Storage get(final String environment) {
        if (DEV_ENVIRONMENT.equals(environment)) {
            return LocalStorageHelper.getOptions().getService();
        }

        return StorageOptions.getDefaultInstance().getService();
    }
}