package isd.aims.main.InterbankSubsystem.vnPay;

import isd.aims.main.listener.TransactionResultListener;
import isd.aims.main.entity.payment.PaymentTransaction;
import isd.aims.main.entity.request.Request;
import isd.aims.main.entity.response.Response;
import isd.aims.main.utils.Configs;
import isd.aims.main.views.payment.VNPay;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VnPaySubsystemController {

    private static final String PAY_COMMAND = "pay";
    private static final String VERSION = "2.1.0";
    private TransactionResultListener listener;

    public VnPaySubsystemController() {
    }

    public VnPaySubsystemController(TransactionResultListener listener) {
        this.listener = listener;
    }

    public PaymentTransaction refund(int amount, String contents) {
        return null;
    }

    public String generatePayOrderUrl(int money, String contents) throws IOException {

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = money * 100L * 1000;


        String vnp_TxnRef = VnPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VnPayConfig.getIpAddress();

        String vnp_TmnCode = VnPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_BankCode", "");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", contents);
        vnp_Params.put("vnp_OrderType", orderType);


        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VnPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    public void payOrder(int amount, String orderInfo) throws IOException {
        var req = new Request(amount, orderInfo);
        String paymentURL = req.buildQueryURL();
        Stage stage = new Stage();
        var vnPayScreen = new VNPay(stage, Configs.PAYMENT_SCREEN_PATH, paymentURL, this.listener);
        vnPayScreen.show();
    }

    public static PaymentTransaction processResponse(String vnpReturnURL) throws URISyntaxException, ParseException {
        URI uri = new URI(vnpReturnURL);
        String query = uri.getQuery();
        Response response = new Response(query);

        if (response == null) return null;

        // Create Payment transaction
        String errorCode = response.getVnp_TransactionStatus();
        String transactionId = response.getVnp_TransactionNo();
        String transactionContent = response.getVnp_OrderInfo();
        int amount = Integer.parseInt(response.getVnp_Amount()) / 100;
        String createdAt = response.getVnp_PayDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        Date date = dateFormat.parse(createdAt);

        return new PaymentTransaction(errorCode, transactionId, transactionContent, amount, date);
    }

}
