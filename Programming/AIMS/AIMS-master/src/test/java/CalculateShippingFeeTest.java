import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import isd.aims.main.controller.PlaceOrderController;
import isd.aims.main.entity.order.Order;
import isd.aims.main.entity.order.OrderMedia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.List;

class CalculateShippingFeeTest {

    private PlaceOrderController placeOrderController;
    private Order order;

    @BeforeEach
    void setUp() {
        placeOrderController = new PlaceOrderController();
        order = mock(Order.class);  // Giả lập đối tượng Order
    }

    @Test
    void testCalculateShippingFee_withPositiveAmount() {
        // Thiết lập Order có amount là 1000
        when(order.getAmount()).thenReturn(1000);

        // Gọi phương thức tính phí vận chuyển
        int shippingFee = placeOrderController.calculateShippingFee(order);

        // Kiểm tra shippingFee nằm trong phạm vi 0 đến 10% của amount (1000 * 0.1 = 100)
        assertTrue(shippingFee >= 0 && shippingFee <= 100, "Shipping fee should be between 0 and 10% of the order amount");
    }

    @Test
    void testCalculateShippingFee_withZeroAmount() {
        // Thiết lập Order có amount là 0
        when(order.getAmount()).thenReturn(0);

        // Gọi phương thức tính phí vận chuyển
        int shippingFee = placeOrderController.calculateShippingFee(order);

        // Kiểm tra rằng shippingFee phải là 0 khi order amount là 0
        assertEquals(0, shippingFee, "Shipping fee should be 0 when the order amount is 0");
    }

    @Test
    void testCalculateShippingFee_withLargeAmount() {
        // Thiết lập Order có amount là một giá trị lớn
        when(order.getAmount()).thenReturn(1000000);

        // Gọi phương thức tính phí vận chuyển
        int shippingFee = placeOrderController.calculateShippingFee(order);

        // Kiểm tra rằng shippingFee nằm trong phạm vi 0 đến 10% của amount (1000000 * 0.1 = 100000)
        assertTrue(shippingFee >= 0 && shippingFee <= 100000, "Shipping fee should be between 0 and 10% of the order amount");
    }

    @Test
    void testCalculateShippingFee_withRandomness() {
        // Thiết lập Order có amount là 5000
        when(order.getAmount()).thenReturn(5000);

        // Gọi phương thức tính phí vận chuyển nhiều lần để kiểm tra tính ngẫu nhiên
        int firstFee = placeOrderController.calculateShippingFee(order);
        int secondFee = placeOrderController.calculateShippingFee(order);

        // Kiểm tra tính ngẫu nhiên, phí vận chuyển không nên giống nhau giữa các lần gọi
        assertNotEquals(firstFee, secondFee, "Shipping fee should be random and different between calls");

        // Kiểm tra rằng phí vận chuyển nằm trong phạm vi hợp lệ
        assertTrue(firstFee >= 0 && firstFee <= 500, "First shipping fee should be between 0 and 10% of the order amount");
        assertTrue(secondFee >= 0 && secondFee <= 500, "Second shipping fee should be between 0 and 10% of the order amount");
    }
}
