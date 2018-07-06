package testppp;

import com.zufangbao.sun.utils.DateUtils;

import java.util.Date;

public class DateTest {

    public static void main(String[] args) {
        String firstPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 1));
        String secondPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 2));
        String thirdPlanDate = DateUtils.format(DateUtils.addMonths(new Date(), 3));
        Date first = DateUtils.parseDate(firstPlanDate);
        String deductDate = DateUtils.format(DateUtils.addDays(first, -1));
        System.out.println(firstPlanDate);
        System.out.println(secondPlanDate);
        System.out.println(thirdPlanDate);
        System.out.println(deductDate);

        String total = "1500";
        int totalAmount = Integer.parseInt(total);
        String a = totalAmount * 3 + "";
        System.out.println(a);
        String repaymentInterest = "20";
        String loanServiceFee = "20";
        String techMaintenanceFee = "20";
        String otheFee = "20";
        String assetInterest = Integer.parseInt(repaymentInterest) * 3 + "";
        String serviceCharge = Integer.parseInt(loanServiceFee) * 3 + "";
        String maintenanceCharge = Integer.parseInt(techMaintenanceFee) * 3 + "";
        String otherCharge = Integer.parseInt(otheFee) * 3 + "";
        String assetInitialValue = Integer.parseInt(assetInterest) + Integer.parseInt(serviceCharge) + Integer.parseInt(maintenanceCharge) + Integer.parseInt(otherCharge) + "";
        System.out.println(assetInitialValue);
    }

}
