import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        ValidateAddressTest.class,
        ValidateNameTest.class,
        ValidatePhoneNumberTest.class,
        CalculateShippingFeeTest.class  // Thêm CalculateShippingFeeTest vào
})
public class AllTests {
}
