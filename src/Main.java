public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    public static int[][][] profitData = new int[MONTHS][DAYS][COMMS];

    private static int getCommodityIndex(String name) {
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    public static String mostProfitableCommodityInMonth(int month) {
        if (month < 0 || month >= MONTHS) return "INVALID_MONTH";
        int maxProfit = Integer.MIN_VALUE;
        int bestCommodityIndex = -1;
        for (int c = 0; c < COMMS; c++) {
            int currentTotal = 0;
            for (int d = 0; d < DAYS; d++) {
                currentTotal += profitData[month][d][c];
            }
            if (currentTotal > maxProfit) {
                maxProfit = currentTotal;
                bestCommodityIndex = c;
            }
        }
        if (bestCommodityIndex == -1) return "No Data 0";
        return commodities[bestCommodityIndex] + " " + maxProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) return -99999;
        int dayIndex = day - 1;
        int total = 0;
        for (int c = 0; c < COMMS; c++) {
            total += profitData[month][dayIndex][c];
        }
        return total;
    }
}
