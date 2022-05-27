package ec.animal.adoption;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "ec.animal.adoption", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    @SuppressWarnings("unused")
    static final ArchRule onionArchitectureIsRespected = onionArchitecture()
            .domainModels("..domain.model..")
            .domainServices("..domain.service..")
            .applicationServices("..application..")
            .adapter("cloudinary", "..adapter.cloudinary..")
            .adapter("jpa", "..adapter.jpa..")
            .adapter("rest", "..adapter.rest..");
}
